/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    // the state of the game logic
    private Ship ship;
    private Cannon cannon;
    private List<CannonBall> cannonBalls;
    private int time;
    private int timeBetweenShots;
    private Timer cannonTimer;

    public boolean playing = false; // whether the game is running 
    private JLabel status; // Current status text, i.e. "Running..."

    private Toolkit toolkit =  Toolkit.getDefaultToolkit ();
    private Dimension dim = toolkit.getScreenSize();

    // Game constants
    public final int COURT_WIDTH = (int) (dim.getHeight()/1.5);
    public final int COURT_HEIGHT = (int) (dim.getHeight()/1.5);
    public final int ORBIT_RADIUS = COURT_HEIGHT/2 - COURT_HEIGHT/8;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 1;
    public int cannonInterval = 750;

    public enum OrbitDirection {
        CW (-1),
        CCW (1);

        private final int direction;
        OrbitDirection(int direction) {
            this.direction = direction;
        }

        public int getDirection() {
            return direction;
        }
    }

    private OrbitDirection direction;

    public GameCourt(JLabel status) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        cannonBalls = new LinkedList<CannonBall>();

        // The timer is an object which triggers an action periodically with the given INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is called each
        // time the timer triggers. We define a helper method called tick() that actually does
        // everything that should be done in a single timestep.
        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start(); // MAKE SURE TO START THE TIMER!

        cannonTimer = new Timer(cannonInterval, e -> cannonTick());
        cannonTimer.start();

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key is pressed, by
        // changing the square's velocity accordingly. (The tick method below actually moves the
        // square.)
        addKeyListener(new KeyAdapter() {
            private boolean spaceIsPressed;

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !spaceIsPressed) {
                    direction = (direction == OrbitDirection.CW) ?
                            OrbitDirection.CCW : OrbitDirection.CW;
                    spaceIsPressed = true;
                }
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    spaceIsPressed = false;
                }
            }
        });

        time = 0;
        timeBetweenShots = 400;
        this.status = status;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        int centerX = COURT_WIDTH/2 - COURT_WIDTH/32;
        int centerY = COURT_HEIGHT/2 - COURT_WIDTH/32;

        cannon = new Cannon(COURT_WIDTH, COURT_HEIGHT, COURT_WIDTH/8, COURT_WIDTH/2, COURT_HEIGHT/2);
        ship = new Ship(COURT_WIDTH, COURT_HEIGHT, centerX, centerY, Color.BLUE);
        cannonBalls = new LinkedList<>();

        System.out.println();
        System.out.println(COURT_HEIGHT);

        cannonInterval = 750;
        direction = OrbitDirection.CW;
        playing = true;
        status.setText("Running...");
        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
        if (playing) {

            time ++;

            List<CannonBall> toRemove = new LinkedList<CannonBall>();
            ship.moveInCircle(direction, 0.25, ORBIT_RADIUS);
            for (CannonBall c : cannonBalls) {
                c.moveAtAngle(2.0);

                if (c.isTouchingBorder()) {
                    toRemove.add(c);
                }

                if (c.intersects(ship)) {
                    playing = false;
                    status.setText("You lose!");
                }
            }

            cannonBalls.removeAll(toRemove);

            // update the display
            repaint();
        }
    }

    void cannonTick() {
        if (playing) {
            double shipAngle = ship.getAngleInDegrees();
            System.out.println(shipAngle);
            double launchAngle = (Math.random() - 0.5) * 60 + shipAngle + direction.getDirection() * 30;

            cannonBalls.add(new CannonBall(COURT_WIDTH, COURT_HEIGHT, launchAngle));
            if (cannonInterval > 150) {
                cannonInterval -= 2;
                cannonTimer.stop();
                cannonTimer = new Timer(cannonInterval, e -> cannonTick());
                cannonTimer.start();
            } else {
                cannonInterval = 150;
                cannonTimer.stop();
                cannonTimer = new Timer(cannonInterval, e -> cannonTick());
                cannonTimer.start();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        cannon.draw(g);
        ship.draw(g);
        for (CannonBall c : cannonBalls)
            c.draw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}