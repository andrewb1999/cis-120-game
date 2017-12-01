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
    private final int CENTER_X = COURT_WIDTH/2;
    private final int CENTER_Y = COURT_HEIGHT/2;
    private final int ORBIT_RADIUS = COURT_HEIGHT/2 - COURT_HEIGHT/8;
    private static final int INTERVAL = 1;
    private static final int CANNON_TIME_DECREASE = 5;
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
    private CircleObj centerTest;
    private List<CannonBall> cannonBalls;
    private List<CollectibleCircleObject> collectibles;
    private Timer cannonTimer;
    private int cannonInterval;
    private double shipSpeed;
    private double cannonballSpeed;
    private int score;


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


        cannon = new Cannon(COURT_WIDTH, COURT_HEIGHT, COURT_WIDTH/8, COURT_WIDTH/2, COURT_HEIGHT/2);
        ship = new Ship(COURT_WIDTH, COURT_HEIGHT, CENTER_X, CENTER_Y, Color.BLUE);
        centerTest = new CircleObj(CENTER_X - 10, CENTER_Y - 10, 10, COURT_WIDTH, COURT_HEIGHT) {
            @Override
            public void draw(Graphics g) {
                g.setColor(Color.BLACK);
                g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
            }
        };
        cannonBalls = new LinkedList<>();
        collectibles = new LinkedList<>();
        for (int i = 0; i < 360; i += 10) {
            if (i != 90)
                collectibles.add(Coin.createCoin(ORBIT_RADIUS, i, COURT_WIDTH, COURT_HEIGHT, CENTER_X, CENTER_Y));
        }
        score = 0;
        cannonInterval = INIT_CANNON_INTERVAL;
        shipSpeed = INIT_SHIP_SPEED;
        cannonballSpeed = INIT_CANNONBALL_SPEED;
        cannonInterval = INIT_CANNON_INTERVAL;
        direction = OrbitDirection.CW;
        playing = true;
        status.setText("Score: " + score);
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
                    status.setText("You lose! Score: " + score);
                }
            }

            List<CollectibleCircleObject> toRemoveCollectible = new LinkedList<CollectibleCircleObject>();
            for (CollectibleCircleObject c : collectibles) {
                if (c.intersects(ship)) {
                    c.modifyState(this);
                    toRemoveCollectible.add(c);
                }
            }

            collectibles.removeAll(toRemoveCollectible);
            cannonBalls.removeAll(toRemove);

            status.setText("Score: " + score);

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

    public void incrementScore() {
        score++;
    }

//    public int getScore() {
//        return score;
//    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (CollectibleCircleObject c : collectibles)
            c.draw(g);
        for (CannonBall c : cannonBalls)
            c.draw(g);
//        cannon.draw(g);
        centerTest.draw(g);
        ship.draw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}