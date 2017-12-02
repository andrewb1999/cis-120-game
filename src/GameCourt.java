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
import java.util.Set;
import java.util.TreeSet;

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
    private JLabel scoreText; // Current status text, i.e. "Running..."
    private JLabel invincibilityText;

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
    private static final int CANNON_TIME_DECREASE = 7;
    private static final int MIN_CANNON_INTERVAL = 200;
    private static final int SPEED_INCREASE_INTERVAL = 5000;
    private static final int INVINCIBILITY_INTERVAL = 1000;
    private static final int NEW_COIN_INTERVAL = 500;
    private static final int INVINCIBILITY_LENGTH = 5;
    private static final int INIT_CANNON_INTERVAL = 1000;
    private final double INIT_SHIP_SPEED = 0.005;
    private final double INIT_CANNONBALL_SPEED = 3.0;
    private final double SHIP_SPEED_INCREASE = 0.0005;
    private final double CANNONBALL_SPEED_INCREASE = .1;

    // the state of the game logic
    private Ship ship;
    private Cannon cannon;
    private CircleObj centerTest;
    private List<CannonBall> cannonBalls;
    private Set<CollectibleCircleObject> collectibles;
    private Timer cannonTimer;
    private Timer invincibilityTimer;
    private Timer newCoinTimer;
    private int cannonInterval;
    private double shipSpeed;
    private double cannonballSpeed;
    private boolean isInvincible;
    private int score;
    private int invincibilityTimeLeft;


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

    public GameCourt(JLabel scoreText, JLabel invincibilityText) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start();

        cannonTimer = new Timer(cannonInterval, e -> cannonTick());
        cannonTimer.start();

        Timer speedIncreaseTimer = new Timer(SPEED_INCREASE_INTERVAL, e -> speedIncreaseTick());
        speedIncreaseTimer.start();

        invincibilityTimer = new Timer(INVINCIBILITY_INTERVAL, e -> invincibilityTick());

        newCoinTimer = new Timer(NEW_COIN_INTERVAL, e -> newCoinTick());
        newCoinTimer.start();

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

        this.invincibilityText = invincibilityText;
        this.scoreText = scoreText;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        cannon = new Cannon(COURT_WIDTH, COURT_HEIGHT, COURT_WIDTH/8, COURT_WIDTH/2, COURT_HEIGHT/2);
        ship = new Ship(COURT_WIDTH, COURT_HEIGHT, CENTER_X, CENTER_Y, Color.BLUE);
        cannonBalls = new LinkedList<>();
        collectibles = new TreeSet<>();
        for (int i = 0; i < 360; i += 10) {
            if (i != 90 && i != 0)
                collectibles.add(Coin.createCoin(ORBIT_RADIUS, i, COURT_WIDTH, COURT_HEIGHT, CENTER_X, CENTER_Y));
            if (i == 0)
                collectibles.add(InvincibilityCoin.createICoin(ORBIT_RADIUS, i, COURT_WIDTH, COURT_HEIGHT, CENTER_X, CENTER_Y));
        }

        score = 0;
        invincibilityTimeLeft = 0;
        cannonInterval = INIT_CANNON_INTERVAL;
        isInvincible = false;
        shipSpeed = INIT_SHIP_SPEED;
        cannonballSpeed = INIT_CANNONBALL_SPEED;
        cannonInterval = INIT_CANNON_INTERVAL;
        direction = OrbitDirection.CW;
        playing = true;
        scoreText.setText("Score: " + score);
        invincibilityText.setText("You are not invincible");
        requestFocusInWindow();
    }


    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    private void tick() {
        if (playing) {

            List<CannonBall> toRemove = new LinkedList<CannonBall>();
            ship.moveInCircle(direction, shipSpeed, ORBIT_RADIUS);


            List<CollectibleCircleObject> toRemoveCollectible = new LinkedList<CollectibleCircleObject>();
            for (CollectibleCircleObject c : collectibles) {
                if (c.intersects(ship)) {
                    c.modifyState(this);
                    toRemoveCollectible.add(c);
                }
            }

            collectibles.removeAll(toRemoveCollectible);

            scoreText.setText("Score: " + score);

            if (isInvincible) {
                invincibilityText.setText("Invincibility left: " + invincibilityTimeLeft + " seconds");
            } else {
                invincibilityText.setText("You are not invincible");
            }

            //Exit Conditions
            for (CannonBall c : cannonBalls) {
                c.moveAtAngle(cannonballSpeed);

                if (c.isTouchingBorder()) {
                    toRemove.add(c);
                }

                if (c.intersects(ship) && !isInvincible) {
                    playing = false;
                    scoreText.setText("You lose! Score: " + score);
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
            double expectedAdditionalShipAngle = (ORBIT_RADIUS/cannonballSpeed) * Math.toDegrees(shipSpeed);
            double launchAngle;
            if (Math.random() > 0.5) {
                launchAngle = (Math.random() - 0.5) * 30 + currentShipAngle
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

    private void invincibilityTick() {
        if (playing) {
            if (invincibilityTimeLeft > 1) {
                invincibilityTimeLeft--;
            } else {
                isInvincible = false;
                ship.setColor(Color.BLUE);
                invincibilityTimer.stop();
            }
        }
    }

    private void newCoinTick() {
        if (playing) {
            if (Math.random() > 0.025 ) {
                double randomAngle = Math.random() * 360;
                int roundedAngle = ((int) randomAngle/10 + 5) * 10;
                collectibles.add(Coin.createCoin(ORBIT_RADIUS, roundedAngle, COURT_WIDTH,
                        COURT_HEIGHT, CENTER_X, CENTER_Y));
            } else {
                double randomAngle = Math.random() * 360;
                int roundedAngle = ((int) randomAngle/10 + 5) * 10;
                collectibles.add(InvincibilityCoin.createICoin(ORBIT_RADIUS, roundedAngle, COURT_WIDTH,
                        COURT_HEIGHT, CENTER_X, CENTER_Y));
            }
        }
    }

    public void incrementScore() {
        score++;
    }

    public void makeInvincible() {
        isInvincible = true;
        invincibilityTimeLeft = INVINCIBILITY_LENGTH;
        ship.setColor(new Color(51, 204, 255));
        invincibilityTimer.start();
    }

//    public int getScore() {
//        return score;
//    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (CollectibleCircleObject c : collectibles)
            c.draw(g);

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