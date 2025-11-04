package collisions;

import java.awt.*;

/**
 * La bounding box est le plus petit rectangle aux côtés parallèles aux axes qui englobe une forme géométrique 2D.
 *
 * @param left   coordonnée x du côté gauche du rectangle
 * @param right  coordonnée x du côté droit du rectangle
 * @param top    coordonnée y du côté haut du rectangle
 * @param bottom coordonnée y du côté bas du rectangle
 */
public record BoundingBox(double left, double right, double top, double bottom) {
    public BoundingBox {
        if (left >= right || top <= bottom) {
            throw new IllegalArgumentException(String.format("Illegal BoundingBox: (%f, %f, %f, %f)", left, right, bottom, top));
        }
    }

    /**
     * @return largeur du rectangle (right - left)
     */
    public double width() {
        return right - left;
    }

    /**
     * @return hauteur du rectangle (top - bottom)
     */
    public double height() {
        return top - bottom;
    }

    /**
     * @param that une autre bounding box
     * @return <code>true</code> si et seulement si l'intersection entre les rectangles this et that n'est pas vide
     */
    public boolean intersects(BoundingBox that) {
        return this.left < that.right && that.left < this.right && this.bottom < that.top && that.bottom < this.top;
    }

    /**
     * @return les coordonnées du rectangle du quart en haut à gauche du rectangle <code>this</code>
     */
    public BoundingBox topLeftQuarter() {
        return new BoundingBox(left, (left + right) / 2, top, (top + bottom) / 2);
    }

    /**
     * @return les coordonnées du rectangle du quart en haut à droite du rectangle <code>this</code>
     */
    public BoundingBox topRightQuarter() {
        return new BoundingBox((left + right) / 2, right, top, (top + bottom) / 2);
    }

    /**
     * @return les coordonnées du rectangle du quart en bas à gauche du rectangle <code>this</code>
     */
    public BoundingBox bottomLeftQuarter() {
        return new BoundingBox(left, (left + right) / 2, (top + bottom) / 2, bottom);
    }

    /**
     * @return les coordonnées du rectangle du quart en bas à droite du rectangle <code>this</code>
     */
    public BoundingBox bottomRightQuarter() {
        return new BoundingBox((left + right) / 2, right, (top + bottom) / 2, bottom);
    }

    /**
     * Dessine le rectangle de la boundingBox dans le Graphics donné.
     * @param windowHeight hauteur de la fenêtre
     */
    public void draw(Graphics graphics, double windowHeight) {
        graphics.drawRect((int) left, (int) (windowHeight - top), (int) width(), (int) height());
    }
}
