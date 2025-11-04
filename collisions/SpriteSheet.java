package collisions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Une classe pour gérer une feuille de sprites animés
 */
public class SpriteSheet {
    private final java.util.List<Image> sprites;

    public static SpriteSheet fromResource(String resource, double radius, int rows, int columns) throws IOException {
        var image = Optional.ofNullable(Board.class.getResource(resource))
                .orElseThrow(FileNotFoundException::new);
        return new SpriteSheet(image, radius, rows, columns);
    }

    public SpriteSheet(URL image, double wantedWidth, int rows, int columns) throws IOException {
        BufferedImage raw = ImageIO.read(image);
        var sheet = new BufferedImage((int) wantedWidth * columns, (int) (wantedWidth * columns * raw.getHeight() / raw.getWidth()), BufferedImage.TYPE_INT_ARGB);
        var bGr = sheet.createGraphics();
        bGr.drawImage(raw.getScaledInstance(sheet.getWidth(), sheet.getHeight(), Image.SCALE_DEFAULT), 0, 0, null);
        bGr.dispose();
        sprites = new ArrayList<>();
        double spriteWidth = (double) sheet.getWidth() / columns;
        double spriteHeight = (double) sheet.getHeight() / rows;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                sprites.add(sheet.getSubimage((int) (c * spriteWidth), (int) (r * spriteHeight), (int) (spriteWidth), (int) (spriteHeight)));
            }
        }
    }

    public Image getImage(int id) {
        return sprites.get(id % sprites.size());
    }
}
