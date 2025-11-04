package collisions;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

/**
 * Classe principale du simulateur.
 */
public class Board extends JPanel implements ActionListener {
    /**
     * Nombre d'astéroïdes simulés
     */
    private final static int TARGET_ASTEROIDS = 100;

    /**
     * Rayon des astéroïdes
     */
    private final static double RADIUS = 15;

    /**
     * Vitesse maximale des astéroïdes (la vitesse initiale est un nombre aléatoire entre 0 et le max, donc de moyenne
     * <code>maxSpeed/2</code>.
     */
    private final static double MAX_SPEED = 1;

    /**
     * Délai d'attente entre deux frames en millisecondes
     */
    private final static int TIMER_DELAY = 2;

    /**
     * Liste des astéroïdes actuellement simulés
     */
    private java.util.List<Asteroid> asteroids;

    /**
     * Coordonnées complètes de l'espace simulé.
     * On le fait un peu plus grand que la fenêtre afin que les astéroïdes puissent "sortir" de manière fluide
     * sans qu'on puisse les voir disparaitre.
     */
    private final BoundingBox boundingBox;

    /**
     * Générateur aléatoire. Une seed fixe permet d'avoir toujours la même simulation (permet de reproduire les bugs).
     */
    private final static Random random = new Random(0);

    /**
     * Heure de démarrage de la simulation, utilisé pour le calcul des FPS
     */
    private final long startTime;
    /**
     * Nombre de frames calculées depuis le démarrage de la simulation, utilisé pour le calcul des FPS
     */
    private long frames = 0;

    /**
     * Feuilles de sprites utilisées pour le dessin des astéroïdes
     */
    private final java.util.List<SpriteSheet> spriteSheets;
    /**
     * Image de fond
     */
    private final Image background;

    /**
     * Construit le simulateur
     *
     * @param width  résolution horizontale de l'écran
     * @param height résolution verticale de l'écran
     * @throws IOException si les images ne peuvent être chargées
     */
    public Board(int width, int height) throws IOException {
        this.boundingBox = new BoundingBox(-2 * RADIUS, width + 2 * RADIUS, height + 2 * RADIUS, -2 * RADIUS);

        var backgroundFile = Optional.ofNullable(Board.class.getResource("background.png")).orElseThrow(FileNotFoundException::new);

        this.background = ImageIO.read(backgroundFile).getScaledInstance(width, height, Image.SCALE_DEFAULT);
        this.spriteSheets = Arrays.asList(SpriteSheet.fromResource("asteroid_sheet_1.png", 3 * RADIUS, 4, 8), SpriteSheet.fromResource("asteroid_sheet_2.png", 3 * RADIUS, 4, 8));

        this.asteroids = Stream.generate(this::randomAsteroid).limit(TARGET_ASTEROIDS).toList();

        // Le timer permet de déclencher la boucle de simulation {@link Board#actionPerformed} à intervalles fixes
        Timer timer = new Timer(TIMER_DELAY, this);
        this.startTime = System.nanoTime();
        timer.start();
    }

    /**
     * Supprime les astéroïdes sortis de l'espace de simulation
     */
    private void clearAsteroids() {
        asteroids = asteroids.stream().filter(a -> boundingBox.intersects(a.boundingBox())).toList();
    }


    /**
     * @param edge indique le bord : 0 = bas, 1 = droit, 2 = haut, 3 = gauche
     * @return une coordonnée aléotoire située sur un bord de la <code>BoundingBox bb</code> donnée.
     */
    private Vector randomEdge(int edge) {
        return switch (edge) {
            // BOTTOM
            case 0 -> new Vector(random.nextDouble() * boundingBox.width() + boundingBox.left(), boundingBox.bottom());
            // RIGHT
            case 1 ->
                    new Vector(boundingBox.right(), random.nextDouble() * boundingBox.height() + boundingBox.bottom());
            // TOP
            case 2 -> new Vector(random.nextDouble() * boundingBox.width() + boundingBox.left(), boundingBox.top());
            // LEFT
            case 3 -> new Vector(boundingBox.left(), random.nextDouble() * boundingBox.height() + boundingBox.bottom());
            default -> throw new IllegalArgumentException();
        };
    }


    /**
     * @return un astéroïde aléatoire dans l'espace de simulation. Les coordonnées, la direction et la vitesse
     * (entre 0 et maxSpeed) sont aléatoires.
     */
    private Asteroid randomAsteroid() {
        double x = random.nextDouble() * boundingBox.width() + boundingBox.left();
        double y = random.nextDouble() * boundingBox.height() + boundingBox.bottom();
        double angle = random.nextDouble() * 2 * Math.PI;
        SpriteSheet sheet = spriteSheets.get(random.nextInt(spriteSheets.size()));
        return new Asteroid(sheet, new Vector(x, y), random.nextDouble() * MAX_SPEED, angle, RADIUS);
    }


    /**
     * @return un astéroïde aléatoire situé sur un bord de l'espace de simulation. La direction est forcément vers
     * l'intérieur de l'espace de simulation.
     */
    private Asteroid randomEdgeAsteroid() {
        int startEdge = random.nextInt(4);
        double angle = random.nextDouble() * Math.PI + startEdge * Math.PI / 2;
        Vector position = randomEdge(startEdge);
        SpriteSheet sheet = spriteSheets.get(random.nextInt(spriteSheets.size()));
        return new Asteroid(sheet, position, random.nextDouble() * MAX_SPEED, angle, RADIUS);
    }

    /**
     * Génère des astéroïdes sur le bord de l'écran jusqu'à ce que le nombre d'astéroïdes souhaité soit atteint.
     */
    private void completeAsteroids() {
        var nbMissingAsteroids = TARGET_ASTEROIDS - asteroids.size();
        asteroids = Stream.concat(asteroids.stream(), Stream.generate(this::randomEdgeAsteroid).limit(nbMissingAsteroids)).toList();
    }

    /**
     * Dessine l'état courant du simulateur dans l'objet <code>graphics</code> donné.
     */
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        // Dessin du fond
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.drawImage(background, 0, 0, null);

        // Dessin des astéroïdes
        double screenHeight = boundingBox.top() - 2 * RADIUS;
        for (Asteroid a : asteroids) {
            a.draw(g2d, screenHeight);
        }

        // Calcul et affichage des FPS dans la console
        long now = System.nanoTime();
        frames++;
        if (frames % 100 == 0) {
            System.out.println(frames * 1e9 / (now - startTime));
        }
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * Détection des collisions : tous les astérodides distants de moins de la somme de leurs rayons sont
     * nécessairement en collision.
     */
    private void detectCollisions() {
        for (int i = 0; i < asteroids.size(); i++) {
            var a = asteroids.get(i);
            for (int j = i + 1; j < asteroids.size(); j++) {
                var n = asteroids.get(j);
                var dist = a.getCoordinates().minus(n.getCoordinates()).length();
                if (dist < a.getRadius() + n.getRadius()) {
                    // Collision !
                    a.collision(n);
                }
            }
        }
    }

    /**
     * Déplace tous les astéroïdes d'un cycle de simulation
     */
    private void moveAsteroids() {
        for (Asteroid a : asteroids) {
            a.move();
        }
    }

    /**
     * Actions à faire à chaque boucle de simulation.
     * Cette méthode est déclenchée par le Timer.
     * <ol>
     * <li> détection des collisions
     * <li> déplacement des astéroïdes
     * <li> suppression des astéroïdes hors de l'espace de simulation
     * <li> génération de nouveaux astéroïdes en remplacement
     * <li> affichage
     * </ol>
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        detectCollisions();
        moveAsteroids();
        clearAsteroids();
        completeAsteroids();
        repaint();
    }
}
