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

    public boolean playing = false; // whether the game is running 
    private JLabel status; // Current status text, i.e. "Running..."

    //Toolkit for determining screen resolution
    private Toolkit toolkit =  Toolkit.getDefaultToolkit ();
    private Dimension dim = toolkit.getScreenSize();

    // Game constants
    private final int COURT_WIDTH = (int) (dim.getHeight()/1.5);
    private final int COURT_HEIGHT = (int) (dim.getHeight()/1.5);
    private final int ORBIT_RADIUS = COURT_HEIGHT/2 - COURT_HEIGHT/8;
    private static final int INTERVAL = 1;
    private static final int CANNON_TIME_DECREASE = 50;
    private static final int MIN_CANNON_INTERVAL = 100;
    private static final int SPEED_INCREASE_INTERVAL = 5000;
    private static final int INIT_CANNON_INTERVAL = 750;
    private final double INIT_SHIP_SPEED = COURT_HEIGHT/5760.0;
    private final double INIT_CANNONBALL_SPEED = COURT_HEIGHT/1080.0;
    private final double SHIP_SPEED_INCREASE = COURT_HEIGHT/57600.0;
    private final double CANNONBALL_SPEED_INCREASE = COURT_HEIGHT/14400.0;

    // the state of the game logic
    private Ship ship;
    private Cannon cannon;
    private List<CannonBall> cannonBalls;
    private Timer cannonTimer;
    private int cannonInterval;
    private double shipSpeed;
    private double cannonballSpeed;


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
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start();

        cannonTimer = new Timer(cannonInterval, e -> cannonTick());
        cannonTimer.start();

        Timer speedIncreaseTimer = new Timer(SPEED_INCREASE_INTERVAL, e -> speedIncreaseTick());
        speedIncreaseTimer.start();

        setFocusable(true);

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
        cannonInterval = INIT_CANNON_INTERVAL;
        shipSpeed = INIT_SHIP_SPEED;
        cannonballSpeed = INIT_CANNONBALL_SPEED;
        cannonInterval = INIT_CANNON_INTERVAL;
        direction = OrbitDirection.CW;
        playing = true;
        status.setText("Running...");
        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    private void tick() {
        if (playing) {

            List<CannonBall> toRemove = new LinkedList<CannonBall>();
            ship.moveInCircle(direction, shipSpeed, ORBIT_RADIUS);
            for (CannonBall c : cannonBalls) {
                c.moveAtAngle(cannonballSpeed);

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

    private void cannonTick() {
        if (playing) {
            double currentShipAngle = ship.getAngleInDegrees();
            double expectedAdditionalShipAngle = (ORBIT_RADIUS/cannonballSpeed) * shipSpeed;
            double launchAngle;
            if (Math.random() > 0.5) {
                launchAngle = (Math.random() - 0.5) * 45 + currentShipAngle
                        + direction.getDirection() * expectedAdditionalShipAngle;
            } else {
                launchAngle = (Math.random() - 0.5) * 45 + currentShipAngle;
            }

            cannonBalls.add(new CannonBall(COURT_WIDTH, COURT_HEIGHT, launchAngle));
            if (cannonInterval > MIN_CANNON_INTERVAL) {
                cannonInterval -= CANNON_TIME_DECREASE;
                cannonTimer.stop();
                cannonTimer = new Timer(cannonInterval, e -> cannonTick());
                cannonTimer.start();
            } else {
                cannonInterval = MIN_CANNON_INTERVAL;
                cannonTimer.stop();
                cannonTimer = new Timer(cannonInterval, e -> cannonTick());
                cannonTimer.start();
            }
        }
    }

    private void speedIncreaseTick() {
        if (playing) {
            shipSpeed += SHIP_SPEED_INCREASE;
            cannonballSpeed += CANNONBALL_SPEED_INCREASE;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ship.draw(g);
        for (CannonBall c : cannonBalls)
            c.draw(g);
        cannon.draw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}