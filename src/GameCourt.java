/**
 * Corsairs
 * @author Andrew Butt
 * @date 12/8/2017
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another.
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    //Toolkit for determining screen resolution
    private Toolkit toolkit =  Toolkit.getDefaultToolkit ();
    private Dimension dim = toolkit.getScreenSize();

    // Game constants
    private final int COURT_SIZE = (int) (dim.getHeight()/1.5);
    private final int CENTER_X = COURT_SIZE/2;
    private final int CENTER_Y = COURT_SIZE/2;
    private final int ORBIT_RADIUS = COURT_SIZE/2 - COURT_SIZE/8;
    private static final int INTERVAL = 1;
    private static final int CANNON_TIME_DECREASE = 100;
    private static final int NEW_COIN_TIME_DECREASE = 100;
    private static final int MIN_NEW_COIN_INTERVAL = 150;
    private static final int MIN_CANNON_INTERVAL = 200;
    private static final int SPEED_INCREASE_INTERVAL = 10000;
    private static final int INIT_NEW_COIN_INTERVAL = 750;
    private static final int POWER_UP_LENGTH = 5000;
    private static final int INIT_CANNON_INTERVAL = 1000;
    private static final double INIT_SHIP_SPEED = 0.001;
    private final double INIT_CANNONBALL_SPEED = COURT_SIZE/2500.0;
    private static final double SHIP_SPEED_INCREASE = 0.00025;
    private final double CANNONBALL_SPEED_INCREASE = COURT_SIZE/25000.0;

    //Objects passed in from Game
    private JLabel scoreText;
    private JLabel invincibilityText;
    private JTextField highScoreNameInput;
    private JLabel[] highScoreLabels;

    //Name game objects
    private Ship ship;
    private Cannon cannon;
    private Coins coins;
    private HighScores highScores;

    // the state of the game logic
    private boolean playing = false;
    private int score;
    private int powerUpTimeLeft;
    private int cannonInterval;
    private int newCoinInterval;
    private double shipSpeed;
    private double cannonBallSpeed;
    private boolean isInvincible;
    private boolean isDoubleCoins;
    private boolean canStart;
    private boolean enteredHighScore;
    private boolean reset;
    private String playerName;
    private OrbitDirection direction;

    //Timing Counters
    private int cannonTime = 0;
    private int speedIncreaseTime = 0;
    private int newCoinTime = 0;

    public GameCourt(JLabel scoreText, JLabel invincibilityText, JTextField highScoreNameInput,
                     JLabel[] highScoreLabels) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        Timer gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate((new TimerTask() {
            @Override
            public void run() {
                tick();
            }
        }), 0, INTERVAL);

        canStart = false;
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            private boolean spaceIsPressed;

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE  && !spaceIsPressed) {
                    if(!canStart) {
                        canStart = true;
                    } else {
                        direction = (direction == OrbitDirection.CW) ?
                                OrbitDirection.CCW : OrbitDirection.CW;
                    }
                    spaceIsPressed = true;
                }
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    spaceIsPressed = false;
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            private boolean mouseIsPressed;

            @Override
            public void mousePressed(MouseEvent e) {
                if (!mouseIsPressed) {
                    if(!canStart) {
                        canStart = true;
                    } else {
                        direction = (direction == OrbitDirection.CW) ?
                                OrbitDirection.CCW : OrbitDirection.CW;
                        mouseIsPressed = true;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseIsPressed = false;
            }
        });

        highScoreNameInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && highScoreNameInput.getText() != null &&
                        !highScoreNameInput.getText().trim().equals("")) {
                    enteredHighScore = true;
                    playerName = highScoreNameInput.getText();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                enteredHighScore = false;
            }
        });

        this.highScoreNameInput = highScoreNameInput;
        this.invincibilityText = invincibilityText;
        this.scoreText = scoreText;
        this.highScoreLabels = highScoreLabels;

        try {
            highScores = new HighScores();
        } catch (IOException e) {
            playing = false;
            this.scoreText.setText("High Score File Not Found");
        }
    }

    public Dimension getDim() {
        return new Dimension(COURT_SIZE, COURT_SIZE);
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        //Break high score loop
        reset = true;

        //Reset game objects
        cannon = new Cannon(COURT_SIZE, COURT_SIZE/16, COURT_SIZE/2,
                COURT_SIZE/2, ORBIT_RADIUS);
        ship = Ship.makeShip(COURT_SIZE, CENTER_X, CENTER_Y);
        coins = new Coins(COURT_SIZE, CENTER_X, CENTER_Y, ORBIT_RADIUS);

        //Reset variables
        enteredHighScore = false;
        playing = false;
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
        canStart = false;
        newCoinInterval = INIT_NEW_COIN_INTERVAL;

        //Reset Text
        scoreText.setText("Press space or click to begin");
        invincibilityText.setText("You are not invincible");

        //Reset TextBox
        highScoreNameInput.setVisible(false);
        highScoreNameInput.setText("");

        //Wait until playing
        Thread startThread = new Thread((() -> {
            while(!canStart) {
                repaint();
            }
            playing = true;
        }));
        startThread.start();

        //Add HighScores to screen
        String[] scores = new String[1];

        if (highScores != null)
            scores = highScores.getScores();

        for (int i = 1; i < scores.length + 1; i++)
            highScoreLabels[i].setText(scores[i - 1]);

        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    private void tick() {
        if (playing) {
            reset = false;

            cannon.moveCannonBalls(cannonBallSpeed);
            ship.moveInCircle(direction, shipSpeed, ORBIT_RADIUS);
            coins.collectCoins(ship, this);

            //Set text for user
            scoreText.setText("Score: " + score);
            if (isInvincible) {
                invincibilityText.setText("Invincibility left: " + (powerUpTimeLeft /1000 + 1) + " seconds");
            } else {
                invincibilityText.setText("You are not invincible");
            }

            //Check exit conditions
            for (CannonBall c : cannon) {
                if (c.intersects(ship) && !isInvincible) {
                    playing = false;
                    boolean isHighScore = highScores.isHighScore(score);

                    if (!isHighScore) {
                        scoreText.setText("You lose! Score: " + score);
                    } else {
                        scoreText.setText("High Score of " + score + ", Please enter name:");
                        try {
                            highScoreNameInput.setVisible(true);

                            javax.swing.Timer t = new javax.swing.Timer(100, e -> repaint());
                            t.setRepeats(false);

                            while (!enteredHighScore) {
                                t.start();

                                if(reset)
                                    break;
                            }

                            t.stop();
                            if (playerName != null && playerName.length() <= 15) {
                                highScores.addScore(playerName, score);
                            } else if (playerName != null){
                                highScores.addScore(playerName.substring(0, 16), score);
                            }

                            reset();
                        } catch (IOException e) {
                            scoreText.setText("High Score File Not Found");
                        }
                    }
                }
            }

            //Timer for cannon
            if(cannonTime >= cannonInterval) {
                cannon.fireCannonBall(playing, ship.getAngleInDegrees(), shipSpeed, cannonBallSpeed, direction);
                cannonTime = 0;
            } else {
                cannonTime++;
            }

            //Timer for speed increase
            if(speedIncreaseTime >= SPEED_INCREASE_INTERVAL) {
                speedIncreaseTick();
                speedIncreaseTime = 0;
            } else {
                speedIncreaseTime++;
            }

            //Timer for new coin
            if(newCoinTime >= newCoinInterval) {
                coins.addRandomCoin();
                newCoinTime = 0;
            } else {
                newCoinTime++;
            }

            //Timer for power ups
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

    private void speedIncreaseTick() {
        if (playing) {
            if (cannonInterval > MIN_CANNON_INTERVAL) {
                cannonInterval -= CANNON_TIME_DECREASE;
            } else {
                cannonInterval = MIN_CANNON_INTERVAL;
            }

            if (newCoinInterval > MIN_NEW_COIN_INTERVAL) {
                newCoinInterval -= NEW_COIN_TIME_DECREASE;
            } else {
                newCoinInterval = MIN_NEW_COIN_INTERVAL;
            }

            shipSpeed += SHIP_SPEED_INCREASE;
            cannonBallSpeed += CANNONBALL_SPEED_INCREASE;
        }
    }

    public void incrementScore() {
        if (score < Integer.MAX_VALUE - 10) {
            if (isDoubleCoins) {
                score += 2;
            } else {
                score++;
            }
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
        coins.draw(g);
        cannon.draw(g);
    }


    @Override
    public Dimension getPreferredSize() {
            return new Dimension(COURT_SIZE, COURT_SIZE);
    }
}