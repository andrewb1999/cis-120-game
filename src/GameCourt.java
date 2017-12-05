/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Timer;
import java.util.*;
import java.util.List;

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
    private static final int CANNON_TIME_DECREASE = 100;
    private static final int MIN_CANNON_INTERVAL = 200;
    private static final int SPEED_INCREASE_INTERVAL = 10000;
    private static final int NEW_COIN_INTERVAL = 500;
    private static final int POWER_UP_LENGTH = 5000;
    private static final int INIT_CANNON_INTERVAL = 1000;
    private static final double INIT_SHIP_SPEED = 0.001;
    private final double INIT_CANNONBALL_SPEED = COURT_HEIGHT/2500.0;
    private static final double SHIP_SPEED_INCREASE = 0.00025;
    private final double CANNONBALL_SPEED_INCREASE = COURT_HEIGHT/20000.0;

    // the state of the game logic
    private Ship ship;
    private Cannon cannon;
    private CannonBallList cannonBalls;
    private CollectibleSet collectibles;
    private Timer gameTimer;
    private int cannonInterval;
    private double shipSpeed;
    private double cannonBallSpeed;
    private boolean isInvincible;
    private int score;
    private int powerUpTimeLeft;
    private boolean isDoubleCoins;


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

        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate((new TimerTask() {
            @Override
            public void run() {
                tick();
            }
        }), 0, INTERVAL);

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

    private class CollectibleSet {
        private Set<CollectibleCircleObject> collectibles;

        CollectibleSet() {
            collectibles = new TreeSet<>();
        }

        synchronized Set<CollectibleCircleObject> getCollectibles() {
            Set<CollectibleCircleObject> newC = new TreeSet<>(collectibles);
            return newC;
        }

        synchronized void add(CollectibleCircleObject c) {
            collectibles.add(c);
        }

        synchronized void removeAll(Set<CollectibleCircleObject> c) {
            collectibles.removeAll(c);
        }
    }

    private class CannonBallList {
        private List<CannonBall> collectibles;

        CannonBallList() {
            collectibles = new LinkedList<>();
        }

        synchronized List<CannonBall> getCollectibles() {
            List<CannonBall> newC = new LinkedList<>();
            newC.addAll(collectibles);
            return newC;
        }

        synchronized void add(CannonBall c) {
            collectibles.add(c);
        }

        synchronized void removeAll(Collection<CannonBall> c) {
            collectibles.removeAll(c);
        }
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        cannon = new Cannon(COURT_WIDTH, COURT_HEIGHT, COURT_WIDTH/16, COURT_WIDTH/2, COURT_HEIGHT/2);
        ship = Ship.makeShip(COURT_WIDTH, COURT_HEIGHT, CENTER_X, CENTER_Y);
        cannonBalls = new CannonBallList();
        collectibles = new CollectibleSet();
        for (int i = 0; i < 360; i += 10) {
            addRandomCoin();
        }

        score = 0;
        powerUpTimeLeft = 0;
        cannonTime = 0;
        speedIncreaseTime = 0;
        newCoinTime = 0;
        cannonInterval = INIT_CANNON_INTERVAL;
        isInvincible = false;
        shipSpeed = INIT_SHIP_SPEED;
        cannonBallSpeed = INIT_CANNONBALL_SPEED;
        direction = OrbitDirection.CW;
        playing = true;
        scoreText.setText("Score: " + score);
        invincibilityText.setText("You are not invincible");
        requestFocusInWindow();
    }


    private int cannonTime = 0;
    private int speedIncreaseTime = 0;
    private int newCoinTime = 0;


    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    private void tick() {
        if (playing) {

            List<CannonBall> toRemove = new LinkedList<>();

            Set<CollectibleCircleObject> toRemoveCollectible = new TreeSet<>();
            for (CollectibleCircleObject c : collectibles.getCollectibles()) {
                if (c.intersects(ship)) {
                    c.modifyState(this);
                    toRemoveCollectible.add(c);
                }
            }

            collectibles.removeAll(toRemoveCollectible);

            scoreText.setText("Score: " + score);

            if (isInvincible) {
                invincibilityText.setText("Invincibility left: " + powerUpTimeLeft /1000 + " seconds");
            } else {
                invincibilityText.setText("You are not invincible");
            }

            //Exit Conditions
            for (CannonBall c : cannonBalls.getCollectibles()) {
                c.moveAtAngle(cannonBallSpeed);

                if (c.isTouchingBorder()) {
                    toRemove.add(c);
                }

                if (c.intersects(ship) && !isInvincible) {
                    playing = false;
                    scoreText.setText("You lose! Score: " + score);
                }
            }

            cannonBalls.removeAll(toRemove);
            ship.moveInCircle(direction, shipSpeed, ORBIT_RADIUS);


            if(cannonTime >= cannonInterval) {
                cannonTick();
                cannonTime = 0;
            } else {
                cannonTime++;
            }

            if(speedIncreaseTime >= SPEED_INCREASE_INTERVAL) {
                System.out.println(speedIncreaseTime);
                speedIncreaseTick();
                speedIncreaseTime = 0;
            } else {
                speedIncreaseTime++;
            }

            if(newCoinTime >= NEW_COIN_INTERVAL) {
                newCoinTick();
                newCoinTime = 0;
            } else {
                newCoinTime++;
            }

            if(powerUpTimeLeft > 0) {
                powerUpTimeLeft--;
            } else {
                powerUpTimeLeft = 0;
                isInvincible = false;
                isDoubleCoins = false;
                ship.setInvincibility(false);
            }

            // update the display
            repaint();
        }
    }

    private void cannonTick() {
        if (playing) {
            double currentShipAngle = ship.getAngleInDegrees();
            double expectedAdditionalShipAngle = (ORBIT_RADIUS/ cannonBallSpeed) * Math.toDegrees(shipSpeed);
            double launchAngle;
            if (Math.random() > 0.5) {
                int i = 30;
                if(Math.random() > 0.75) {
                    i = 60;
                }
                launchAngle = (Math.random() - 0.5) * i + currentShipAngle
                        + direction.getDirection() * expectedAdditionalShipAngle;
            } else {
                launchAngle = (Math.random() - 0.5) * 45 + currentShipAngle;
            }

            cannonBalls.add(CannonBall.makeCannonBall(COURT_WIDTH, COURT_HEIGHT, launchAngle));
        }
    }

    private void speedIncreaseTick() {
        if (playing) {
            System.out.println("Here");
            if (cannonInterval > MIN_CANNON_INTERVAL) {
                cannonInterval -= CANNON_TIME_DECREASE;
                System.out.println(cannonInterval);
            } else {
                cannonInterval = MIN_CANNON_INTERVAL;
            }

            shipSpeed += SHIP_SPEED_INCREASE;
            cannonBallSpeed += CANNONBALL_SPEED_INCREASE;
        }
    }

    private void addRandomCoin() {
        if (Math.random() > 0.02 ) {
            double randomAngle = Math.random() * 360;
            int roundedAngle = ((int) randomAngle/10 + 5) * 10;
            collectibles.add(Coin.createCoin(ORBIT_RADIUS, roundedAngle, COURT_WIDTH,
                        COURT_HEIGHT, CENTER_X, CENTER_Y));
        } else {
            double randomAngle = Math.random() * 360;
            int roundedAngle = ((int) randomAngle/10 + 5) * 10;
            if (Math.random() > 0.5) {
                collectibles.add(InvincibilityCoin.createICoin(ORBIT_RADIUS, roundedAngle, COURT_WIDTH,
                        COURT_HEIGHT, CENTER_X, CENTER_Y));
            } else {
                collectibles.add(DoubleCoinsCoin.createDCoin(ORBIT_RADIUS, roundedAngle, COURT_WIDTH,
                        COURT_HEIGHT, CENTER_X, CENTER_Y));
            }
        }
    }

    private void newCoinTick() {
        if (playing) {
            addRandomCoin();
        }
    }

    public void incrementScore() {
        if (isDoubleCoins) {
            score += 2;
        } else {
            score++;
        }
    }

    public void makeInvincible() {
        isInvincible = true;
        powerUpTimeLeft = POWER_UP_LENGTH;
        ship.setInvincibility(true);
    }

    public void makeDoubleCoins() {
        isDoubleCoins = true;
        powerUpTimeLeft = POWER_UP_LENGTH;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        ship.draw(g);

        for (CollectibleCircleObject c : collectibles.getCollectibles()) {
            c.draw(g);
        }

        for (CannonBall c : cannonBalls.getCollectibles())
            c.draw(g);

        cannon.draw(g);
    }


    @Override
    public Dimension getPreferredSize() {
            return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}