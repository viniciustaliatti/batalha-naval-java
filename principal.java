import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class principal {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> escolherDificuldade());
    }

    public static void escolherDificuldade() {
        JFrame menu = new JFrame("Batalha Naval");
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel fundo = new FundoMar();
        fundo.setLayout(new BoxLayout(fundo, BoxLayout.Y_AXIS));
        fundo.setBorder(BorderFactory.createEmptyBorder(30, 40, 50, 40));

        JLabel titulo = new JLabel("BATALHA NAVAL");
        titulo.setFont(new Font("Arial", Font.BOLD, 35));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Escolha a dificuldade");
        subtitulo.setFont(new Font("Arial", Font.BOLD, 20));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        BotaoMenu facil = new BotaoMenu("Fácil", "Tabuleiro 6x6  •  30 tiros", new Color(45, 160, 90));
        BotaoMenu dificil = new BotaoMenu("Difícil", "Tabuleiro 8x8  •  40 tiros", new Color(200, 70, 60));
        BotaoMenu sair = new BotaoMenu("Sair", "Fechar o jogo", new Color(90, 105, 125));

        facil.addActionListener(e -> {
            menu.dispose();
            new BatalhaNaval(false).setVisible(true);
        });
        dificil.addActionListener(e -> {
            menu.dispose();
            new BatalhaNaval(true).setVisible(true);
        });
        sair.addActionListener(e -> System.exit(0));

        fundo.add(titulo);
        fundo.add(Box.createVerticalStrut(5));
        fundo.add(subtitulo);
        fundo.add(Box.createVerticalStrut(30));
        fundo.add(facil);
        fundo.add(Box.createVerticalStrut(15));
        fundo.add(dificil);
        fundo.add(Box.createVerticalStrut(15));
        fundo.add(sair);

        menu.setContentPane(fundo);
        menu.pack();
        menu.setMinimumSize(new Dimension(380, 440));
        menu.setResizable(false);
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
    }

    private static class FundoMar extends JPanel {
        private static final String[] PASTAS = {
            "batalha-naval-java/images/",
            "images/",
            "../batalha-naval-java/images/",
            "../images/"
        };

        private BufferedImage imagem = carregarImagem("mar.jpg");

        private static BufferedImage carregarImagem(String arquivo) {
            for (String pasta : PASTAS) {
                File f = new File(pasta + arquivo);
                if (f.exists()) {
                    try {
                        return ImageIO.read(f);
                    } catch (Exception e) {
                        return null;
                    }
                }
            }
            System.out.println("Não consegui carregar: " + arquivo);
            return null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            if (imagem != null) {
                g2.drawImage(imagem, 0, 0, getWidth(), getHeight(), null);
            } else {
                g2.setColor(new Color(45, 125, 195));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
            g2.dispose();
        }
    }

    private static class BotaoMenu extends JButton {
        private final Color cor;

        BotaoMenu(String texto, String detalhe, Color cor) {
            super("<html><div style='text-align:center'><b style='font-size:16px'>" + texto
                + "</b><br><span style='font-size:10px'>" + detalhe + "</span></div></html>");
            this.cor = cor;
            setFont(new Font("Arial", Font.PLAIN, 12));
            setForeground(Color.WHITE);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setRolloverEnabled(true);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setAlignmentX(CENTER_ALIGNMENT);
            Dimension tamanho = new Dimension(260, 60);
            setPreferredSize(tamanho);
            setMaximumSize(tamanho);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color atual = cor;
            if (getModel().isPressed()) {
                atual = cor.darker();
            } else if (getModel().isRollover()) {
                atual = cor.brighter();
            }
            g2.setColor(atual);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            g2.dispose();

            super.paintComponent(g);
        }
    }
}