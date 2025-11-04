package collisions;

/**
 * Cette classe permet de représenter un vecteur géométrique et propose quelques
 * opérations de base sur ce vecteur.
 */
public record Vector(double x, double y) {
    public Vector minus(Vector that) {
        return add(that.negate());
    }

    public Vector multiply(double factor) {
        return new Vector(x * factor, y * factor);
    }

    public Vector add(Vector that) {
        return new Vector(this.x + that.x, this.y + that.y);
    }

    public double dotProduct(Vector that) {
        return x * that.x + y * that.y;
    }

    public Vector negate() {
        return new Vector(-x, -y);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Construit un vecteur de coordonnées cartésiennes à partir des coordonnées polaires <code>angle</code> et
     * <code>length</code>
     */
    public static Vector fromPolar(double angle, double length) {
        return new Vector(Math.cos(angle), Math.sin(angle)).multiply(length);
    }

}
