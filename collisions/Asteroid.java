package collisions;

import collisions.quadtree.WithBB;

import java.awt.*;

/**
 * Cette classe représente un asteroïde, avec notamment son vecteur de coordonnées par rapport à l'origine (0, 0), son
 * rayon et son vecteur de déplacement.
 * Pour le simulateur, un astéroïde est un disque de rayon <code>radius</code>.
 * La <code>boundingBox</code> de l'astéroïde est le carré de côté 2×<code>radius</code> qui englobe le disque.
 * <p>
 * À chaque étape de la boucle de simulation, un appel à la méthode <code>move</code> permet de mettre à jour les coordonnées
 * de l'astéroïde, et la méthode <code>draw</code> permet de le dessiner sur l'espace à deux dimensions du simulateur.
 * <p>
 * La méthode <code>collision</code> est utilisée pour mettre à jour le vecteur mouvement de deux astéroïdes en collision.
 */
public class Asteroid implements WithBB {

    private final SpriteSheet spriteSheet;
    private int spriteId;

    private Vector coordinates;

    private Vector movement;

    private final double radius;

    public Asteroid(SpriteSheet sprites, Vector coordinates, double speed, double angle, double radius) {
        this.spriteSheet = sprites;
        this.coordinates = coordinates;
        this.movement = Vector.fromPolar(angle, speed);
        this.radius = radius;
        this.spriteId = Float.floatToIntBits((float) Math.random());
    }

    public BoundingBox boundingBox() {
        return new BoundingBox(coordinates.x() - radius, coordinates.x() + radius, coordinates.y() + radius, coordinates.y() - radius);
    }

    public Vector getCoordinates() {
        return coordinates;
    }

    /**
     * Dessine un sprite d'astéroïde dans le contexte graphique <code>g</code>. La hauteur <code>height</code> correspond
     * à la hauteur de la surface simulée (typiquement un peu plus grande que la hauteur du graphique).
     */
    public void draw(Graphics g, double height) {
        g.drawImage(spriteSheet.getImage(spriteId++ / 15), (int) (coordinates.x() - radius), (int) (height - coordinates.y() + radius), null);
    }

    /**
     * Met à jour le vecteur mouvement des astéroïdes <code>this</code> et <code>that</code> suite à une collision,
     * en accord avec les lois de conservation de l'énergie.
     */
    public void collision(Asteroid that) {
        //get the vector of the angle the balls collided and normalize it
        var dist = that.coordinates.minus(this.coordinates).length();
        var vecNorm = that.coordinates.minus(this.coordinates).multiply(1 / dist);
        //get the relative velocity between the balls
        var vecRelVelocity = this.movement.minus(that.movement);

        //calc speed after hit
        double speed = vecRelVelocity.dotProduct(vecNorm);
        if (speed >= 0) {
            // update objects positions
            this.movement = this.movement.minus(vecNorm.multiply(speed));
            that.movement = that.movement.add(vecNorm.multiply(speed));
        }
    }

    public void move() {
        coordinates = coordinates.add(movement);
    }

    public double getRadius() {
        return radius;
    }
}
