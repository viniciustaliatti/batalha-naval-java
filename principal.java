import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class principal {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> escolherDificuldade());
    }

    private static void escolherDificuldade() {
        String[] opcoes = {"Fácil", "Difícil"};

        while (true) {
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
                BatalhaNaval form = new BatalhaNaval(false);
                form.setVisible(true);
                return;

            } else if (escolha == 1) {
                BatalhaNaval form = new BatalhaNaval(true);
                form.setVisible(true);
                return;
            } else {
                System.exit(0);
            }
        }
    }
}