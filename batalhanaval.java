import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BatalhaNaval extends JFrame {

    private static final int TAMANHO = 6;

    // Matriz lógica: 0 = água (depois vamos usar outros valores para as embarcações)
    private int[][] tabuleiro = new int[TAMANHO][TAMANHO];

    // Matriz visual: um botão para cada casa
    private JButton[][] botoes = new JButton[TAMANHO][TAMANHO];

    public BatalhaNaval() {
        setTitle("Batalha Naval");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(250, 250, 250));
        setLayout(new BorderLayout());

        add(criarPainelTabuleiro(), BorderLayout.CENTER);
    }

    private JPanel criarPainelTabuleiro() {
        JPanel painel = new JPanel(new GridLayout(TAMANHO, TAMANHO, 2, 2));

        for (int linha = 0; linha < TAMANHO; linha++) {
            for (int coluna = 0; coluna < TAMANHO; coluna++) {
                JButton botao = new JButton();
                botao.setBackground(new Color(100, 170, 230));
                botoes[linha][coluna] = botao;
                painel.add(botao);
            }
        }
        return painel;
    }
}