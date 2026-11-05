import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class BatalhaNaval extends JFrame {

    private static final int TAMANHO = 6;
    private static final int TOTAL_TIROS = 30;
    private static final int CELULA = 80; // tamanho de cada casa em pixels

    private static final String[] NOMES = {"Porta-aviões", "Encouraçado", "Cruzador", "Submarino", "Destroyer"};
    private static final int[] TAMANHOS = {5, 4, 3, 3, 2};
    private static final String[] IMAGENS = {
        "Aircraft-Carrier.png",
        "battleship.png",
        "cruiser.png",
        "submarino.png",
        "destroyer.png"
    };

    // Pastas onde o programa tenta achar as imagens
    private static final String[] PASTAS = {
        "images/",
        "batalha-naval-java-main/images/",
        "../batalha-naval-java-main/images/",
        "../images/"
    };

    // 0 = água, 1..5 = navio (índice do navio + 1)
    private int[][] tabuleiro = new int[TAMANHO][TAMANHO];
    private boolean[][] jaAtirado = new boolean[TAMANHO][TAMANHO];
    private ImageIcon[][] fatias = new ImageIcon[TAMANHO][TAMANHO]; // pedaço da imagem de cada casa
    private JButton[][] botoes = new JButton[TAMANHO][TAMANHO];

    private int[] acertos = new int[NOMES.length];
    private int naviosAfundados = 0;

    private int tirosRestantes = TOTAL_TIROS;
    private JLabel lblTiros;

    public BatalhaNaval() {
        setTitle("Batalha Naval - Fácil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        lblTiros = new JLabel("Tiros restantes: " + tirosRestantes, SwingConstants.CENTER);
        add(lblTiros, BorderLayout.NORTH);

        posicionarNavios();
        add(criarPainelTabuleiro(), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }
    // Sorteia a posição de cada navio (só na horizontal, sem sobrepor)
    private void posicionarNavios() {
        Random rnd = new Random();
        for (int i = 0; i < NOMES.length; i++) {
            int tam = TAMANHOS[i];
            ImageIcon[] pedacos = cortarImagem(IMAGENS[i], tam);

            boolean colocado = false;
            while (!colocado) {
                int linha = rnd.nextInt(TAMANHO);
                int coluna = rnd.nextInt(TAMANHO - tam + 1);

                boolean livre = true;
                for (int j = 0; j < tam; j++) {
                    if (tabuleiro[linha][coluna + j] != 0) {
                        livre = false;
                    }
                }
                if (livre) {
                    for (int k = 0; k < tam; k++) {
                        tabuleiro[linha][coluna + k] = i + 1;
                        if (pedacos != null) {
                            fatias[linha][coluna + k] = pedacos[k];
                        }
                    }
                    colocado = true;
                }
            }
        }
    }
    // Carrega a imagem do navio e corta em 'tam' pedaços, um para cada casa
    private ImageIcon[] cortarImagem(String arquivo, int tam) {
        BufferedImage original = null;
        for (String pasta : PASTAS) {
            File f = new File(pasta + arquivo);
            if (f.exists()) {
                try {
                    original = ImageIO.read(f);
                } catch (Exception e) {
                    original = null;
                }
                break;
            }
        }
        if (original == null) {
            System.out.println("Não consegui carregar: " + arquivo);
            return null;
        }

        // Redimensiona o navio inteiro para (tam casas) x (1 casa)
        BufferedImage ajustada = new BufferedImage(tam * CELULA, CELULA, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = ajustada.createGraphics();
        g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
                java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, tam * CELULA, CELULA, null);
        g.dispose();

        ImageIcon[] pedacos = new ImageIcon[tam];
        for (int k = 0; k < tam; k++) {
            pedacos[k] = new ImageIcon(ajustada.getSubimage(k * CELULA, 0, CELULA, CELULA));
        }
        return pedacos;
    }
    private JPanel criarPainelTabuleiro() {
        JPanel painel = new JPanel(new GridLayout(TAMANHO, TAMANHO, 2, 2));
        painel.setPreferredSize(new Dimension(TAMANHO * CELULA + 10, TAMANHO * CELULA + 10));

        for (int linha = 0; linha < TAMANHO; linha++) {
            for (int coluna = 0; coluna < TAMANHO; coluna++) {
                JButton botao = new JButton();
                botao.setOpaque(true);
                botao.setBorderPainted(false);
                botao.setMargin(new Insets(0, 0, 0, 0));
                botao.setBackground(new Color(100, 170, 230));
                botoes[linha][coluna] = botao;
                final int l = linha;
                final int c = coluna;
                botao.addActionListener(e -> atirar(l, c));
                painel.add(botao);
            }
        }
        return painel;
    }
    // Mostra a casa como acerto: imagem do navio
    private void marcarAcerto(int linha, int coluna) {
        JButton botao = botoes[linha][coluna];
        if (fatias[linha][coluna] != null) {
            botao.setIcon(fatias[linha][coluna]);
            botao.setBackground(new Color(60, 90, 130));
        }
    }
    private void atirar(int linha, int coluna) {
        if (jaAtirado[linha][coluna]) {
            return;
        }
        jaAtirado[linha][coluna] = true;

        JButton botao = botoes[linha][coluna];
        int id = tabuleiro[linha][coluna];
        String resultado;

        if (id == 0) {
            botao.setText("X");
            botao.setBackground(Color.LIGHT_GRAY);
            resultado = "Água";
        } else {
            marcarAcerto(linha, coluna);
            resultado = "Acertou!";

            acertos[id - 1]++;
            if (acertos[id - 1] == TAMANHOS[id - 1]) {
                naviosAfundados++;
                resultado = "Afundou o " + NOMES[id - 1] + "!";
            }
        }

        tirosRestantes--;
        String posicao = "" + (char) ('A' + linha) + (coluna + 1);
        lblTiros.setText("Tiros restantes: " + tirosRestantes + "  |  Último tiro: " + posicao + " (" + resultado + ")");

        if (naviosAfundados == NOMES.length) {
            desativarTabuleiro();
            JOptionPane.showMessageDialog(this, "Você venceu! Todos os navios afundaram.");
        } else if (tirosRestantes == 0) {
            desativarTabuleiro();
            revelarNavios();
            JOptionPane.showMessageDialog(this, "Acabaram os tiros! Você perdeu.");
        }
    }
    // Impede novos cliques (marcando tudo como já atirado)
    private void desativarTabuleiro() {
        for (int linha = 0; linha < TAMANHO; linha++) {
            for (int coluna = 0; coluna < TAMANHO; coluna++) {
                jaAtirado[linha][coluna] = true;
            }
        }
    }
    // Quando o jogador perde, mostra os navios que sobraram
    private void revelarNavios() {
        for (int linha = 0; linha < TAMANHO; linha++) {
            for (int coluna = 0; coluna < TAMANHO; coluna++) {
                boolean naoAtingido = botoes[linha][coluna].getIcon() == null
                        && botoes[linha][coluna].getText().isEmpty();
                if (tabuleiro[linha][coluna] != 0 && naoAtingido) {
                    marcarAcerto(linha, coluna);
                    botoes[linha][coluna].setBackground(Color.ORANGE);
                }
            }
        }
    }
}