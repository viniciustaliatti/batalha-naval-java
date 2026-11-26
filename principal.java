import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class principal {

    private static final String IMAGEM_FUNDO = "fundo-menu.png";

    private static final String[] PASTAS = {
        "batalha-naval-java/images/",
        "images/",
        "../batalha-naval-java/images/",
        "../images/"
    };

    // Cores inspiradas no pôr do sol da imagem
    private static final Color COR_TITULO    = new Color(255, 238, 214); // creme
    private static final Color COR_SUBTITULO = new Color(255, 205, 160); // pêssego
    private static final Color COR_FACIL     = new Color(32, 150, 140);  // verde-água
    private static final Color COR_DIFICIL   = new Color(226, 98, 78);   // coral
    private static final Color COR_SAIR      = new Color(52, 64, 104);   // azul-noite

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> escolherDificuldade());
    }

    // Também é chamado pelo botão "Voltar" do BatalhaNaval
    public static void escolherDificuldade() {
        JFrame menu = new JFrame("Batalha Naval");
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel fundo = new FundoMenu();
        fundo.setLayout(new BoxLayout(fundo, BoxLayout.Y_AXIS));
        fundo.setBorder(BorderFactory.createEmptyBorder(30, 40, 50, 40));

        BotaoMenu facil   = new BotaoMenu("Fácil",   "Tabuleiro 6x6  •  30 tiros", COR_FACIL);
        BotaoMenu dificil = new BotaoMenu("Difícil", "Tabuleiro 8x8  •  40 tiros", COR_DIFICIL);
        BotaoMenu sair    = new BotaoMenu("Sair",    "Fechar o jogo",              COR_SAIR);

        facil.addActionListener(e -> {
            menu.dispose();
            new BatalhaNaval(false).setVisible(true);
        });
        dificil.addActionListener(e -> {
            menu.dispose();
            new BatalhaNaval(true).setVisible(true);
        });
        sair.addActionListener(e -> System.exit(0));

        fundo.add(criarTexto("BATALHA NAVAL", 35, COR_TITULO));
        fundo.add(Box.createVerticalStrut(5));
        fundo.add(criarTexto("Escolha a dificuldade", 20, COR_SUBTITULO));
        fundo.add(Box.createVerticalStrut(30));
        fundo.add(facil);
        fundo.add(Box.createVerticalStrut(15));
        fundo.add(dificil);
        fundo.add(Box.createVerticalStrut(15));
        fundo.add(sair);

        menu.setContentPane(fundo);
        menu.pack();
        menu.setResizable(false);
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
    }

    private static JLabel criarTexto(String texto, int tamanho, Color cor) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, tamanho));
        label.setForeground(cor);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private static BufferedImage carregarImagem(String arquivo) {
        for (String pasta : PASTAS) {
            File f = new File(pasta + arquivo);
            if (f.exists()) {
                try {
                    return ImageIO.read(f);
                } catch (Exception e) {
                    break;
                }
            }
        }
        System.out.println("Não consegui carregar: " + arquivo);
        return null;
    }

    // Fundo: imagem + um leve escurecido no topo para o título aparecer bem
    private static class FundoMenu extends JPanel {
        private final BufferedImage imagem = carregarImagem(IMAGEM_FUNDO);

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            if (imagem != null) {
                g2.drawImage(imagem, 0, 0, getWidth(), getHeight(), null);
            } else {
                g2.setColor(new Color(45, 125, 195)); // plano B
                g2.fillRect(0, 0, getWidth(), getHeight());
            }

            g2.setPaint(new GradientPaint(0, 0, new Color(10, 15, 40, 150),
                                          0, getHeight() * 0.45f, new Color(10, 15, 40, 0)));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    // Botão arredondado: escurece ao clicar, clareia ao passar o mouse
    private static class BotaoMenu extends JButton {
        private final Color cor;

        BotaoMenu(String texto, String detalhe, Color cor) {
            super("<html><div style='text-align:center'><b style='font-size:16px'>" + texto
                + "</b><br><span style='font-size:10px'>" + detalhe + "</span></div></html>");
            this.cor = cor;
            setForeground(Color.WHITE);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setRolloverEnabled(true);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setPreferredSize(new Dimension(260, 60));
            setMaximumSize(new Dimension(260, 60));
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