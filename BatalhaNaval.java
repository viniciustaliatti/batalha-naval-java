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

    private int TAMANHO;
    private int TOTAL_TIROS;
    private static final int CELULA = 80;

    private String[] NOMES;
    private int[] TAMANHOS;
    private String[] IMAGENS;

    private static final String[] PASTAS = {
        "batalha-naval-java/images/",
        "images/",
        "../batalha-naval-java/images/",
        "../images/"
    };

    private int[][] tabuleiro;
    private boolean[][] jaAtirado;
    private ImageIcon[][] fatias;
    private JButton[][] botoes;

    private int[] acertos;
    private int naviosAfundados = 0;

    private int tirosRestantes;
    private JLabel lblTiros;

    public BatalhaNaval() {
        this(false);
    }

    public BatalhaNaval(boolean dificil) {
        if (dificil) {
            TAMANHO = 8;
            TOTAL_TIROS = 40;

            NOMES = new String[] {
                "Porta-aviões",
                "Encouraçado", "Encouraçado",
                "Cruzador", "Cruzador",
                "Submarino", "Submarino", "Submarino", "Submarino", "Submarino", "Submarino",
                "Destroyer", "Destroyer"
            };

            TAMANHOS = new int[] {
                5, 4, 4, 3, 3, 1, 1, 1, 1, 1, 1, 2, 2
            };

            IMAGENS = new String[] {
                "Aircraft-Carrier.png",
                "battleship.png", "battleship.png",
                "cruiser.png", "cruiser.png",
                "submarino.png", "submarino.png",
                "submarino.png", "submarino.png",
                "submarino.png", "submarino.png",
                "destroyer.png", "destroyer.png"
            };

        } else {
            TAMANHO = 6;
            TOTAL_TIROS = 30;

            NOMES = new String[] {
                "Porta-aviões", "Encouraçado", "Cruzador",
                "Submarino", "Submarino", "Submarino", "Submarino",
                "Destroyer", "Destroyer"
            };

            TAMANHOS = new int[] {
                5, 4, 3, 1, 1, 1, 1, 2, 2
            };

            IMAGENS = new String[] {
                "Aircraft-Carrier.png",
                "battleship.png",
                "cruiser.png",
                "submarino.png", "submarino.png",
                "submarino.png", "submarino.png",
                "destroyer.png", "destroyer.png"
            };
        }

        tirosRestantes = TOTAL_TIROS;

        tabuleiro = new int[TAMANHO][TAMANHO];
        jaAtirado = new boolean[TAMANHO][TAMANHO];
        fatias = new ImageIcon[TAMANHO][TAMANHO];
        botoes = new JButton[TAMANHO][TAMANHO];

        acertos = new int[NOMES.length];

       if (dificil) {
    setTitle("Batalha Naval - Difícil");
        } else {
            setTitle("Batalha Naval - Fácil");
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        lblTiros = new JLabel(
            "Tiros restantes: " + tirosRestantes,
            SwingConstants.CENTER
        );
        add(lblTiros, BorderLayout.NORTH);

        posicionarNavios();
        add(criarPainelTabuleiro(), BorderLayout.CENTER);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> {
            dispose();
            principal.escolherDificuldade();
        });
        JPanel painelSul = new JPanel();
        painelSul.add(btnVoltar);
        add(painelSul, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

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
            botao.setForeground(Color.WHITE);
            botao.setBackground(Color.RED);
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

    private void desativarTabuleiro() {
        for (int linha = 0; linha < TAMANHO; linha++) {
            for (int coluna = 0; coluna < TAMANHO; coluna++) {
                jaAtirado[linha][coluna] = true;
            }
        }
    }


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