package collisions;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

/**
 * Représente la fenêtre et gère le démarrage et la fermeture du simulateur
 */
public class Application extends JFrame {
    private static final GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    public Application() throws IOException {
        if (false) {
            // Full screen
            this.setUndecorated(true);
            device.setFullScreenWindow(this);
        } else {
            this.setSize(1600, 900);
            this.setLocationRelativeTo(null);
        }
        Board board = new Board(getWidth(), getHeight());
        this.add(board);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Application ex;
            try {
                ex = new Application();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ex.setVisible(true);
        });
    }
}
