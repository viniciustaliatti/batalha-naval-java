import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class principal {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> escolherDificuldade());
    }

    public static void escolherDificuldade() {
        String[] opcoes = {"Fácil", "Difícil"};

        int escolha = JOptionPane.showOptionDialog(
            null,
            "Escolha a dificuldade:",
            "Batalha Naval",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcoes,
            opcoes[0]
        );

        if (escolha == 0) {
            new BatalhaNaval(false).setVisible(true);
        } else if (escolha == 1) {
            new BatalhaNaval(true).setVisible(true);
        } else {
            System.exit(0);
        }
    }
}