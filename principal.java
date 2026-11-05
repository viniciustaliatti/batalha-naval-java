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
                // FÁCIL: abre o jogo que já está pronto
                BatalhaNaval form = new BatalhaNaval();
                form.setVisible(true);
                return;
            } else if (escolha == 1) {
                // DIFÍCIL: ainda não implementada, volta para o menu
                JOptionPane.showMessageDialog(null, "A dificuldade Difícil ainda não está disponível.");
            } else {
                // Fechou a janela ou cancelou
                System.exit(0);
            }
        }
    }
}