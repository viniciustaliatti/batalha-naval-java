import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class BatalhaNaval extends JFrame {

    private static final int TAMANHO = 6;
    private static final int TOTAL_TIROS = 30;

    // Matriz lógica: 0 = água (depois vamos usar outros valores para as embarcações)
    private int[][] tabuleiro = new int[TAMANHO][TAMANHO];

    // Matriz visual: um botão para cada casa
    private JButton[][] botoes = new JButton[TAMANHO][TAMANHO];

    private int tirosRestantes = TOTAL_TIROS;
    private JLabel lblTiros;

    public BatalhaNaval() {
        setTitle("Batalha Naval");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(250, 250, 250));
        setLayout(new BorderLayout());

        lblTiros = new JLabel("Tiros restantes: " + tirosRestantes, SwingConstants.CENTER);
        add(lblTiros, BorderLayout.NORTH);


        add(criarPainelTabuleiro(), BorderLayout.CENTER);
    }

    private JPanel criarPainelTabuleiro() {
        JPanel painel = new JPanel(new GridLayout(TAMANHO, TAMANHO, 2, 2));

        for (int linha = 0; linha < TAMANHO; linha++) {
            for (int coluna = 0; coluna < TAMANHO; coluna++) {
                JButton botao = new JButton();
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
     private void atirar(int linha, int coluna) {
        JButton botao = botoes[linha][coluna];

        // marca o botão escolhido
        botao.setText("X");
        botao.setBackground(Color.LIGHT_GRAY);
        botao.setEnabled(false);

        // desconta o tiro e atualiza o texto
        tirosRestantes--;
        String posicao = "" + (char) ('A' + linha) + (coluna + 1);
        lblTiros.setText("Tiros restantes: " + tirosRestantes + "  |  Último tiro: " + posicao);

        if (tirosRestantes == 0) {
            desativarTabuleiro();
            JOptionPane.showMessageDialog(this, "Acabaram os tiros!");
        }
    }

    private void desativarTabuleiro() {
        for (int linha = 0; linha < TAMANHO; linha++) {
            for (int coluna = 0; coluna < TAMANHO; coluna++) {
                botoes[linha][coluna].setEnabled(false);
            }
        }
    }
}