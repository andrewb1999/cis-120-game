/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local variables.

        final JFrame frame = new JFrame("Corsairs");
        frame.setLayout(new BorderLayout());

        final JPanel introPanel = new JPanel();
        introPanel.setLayout(new BorderLayout());

        final JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());

        JPanel overlay = new JPanel();
        overlay.setLayout( new OverlayLayout(overlay) );
        overlay.add(introPanel, BorderLayout.CENTER); // add transparent panel first
        overlay.add(gamePanel, BorderLayout.CENTER);
        frame.add(overlay);

        //Labels
        final JLabel score = new JLabel("Score: " + 0);
        final JLabel invincibility = new JLabel("You are not invincible");
        final JLabel[] highScoreLabels = new JLabel[6];
        highScoreLabels[0] = new JLabel("High Scores: ");
        highScoreLabels[0].setHorizontalAlignment(JLabel.CENTER);
        for (int i = 1; i < 6; i++) {
            highScoreLabels[i] = new JLabel("");
            highScoreLabels[i].setHorizontalAlignment(JLabel.CENTER);
        }

        // Main playing area
        final JPanel game_panel = new JPanel();
        gamePanel.add(game_panel, BorderLayout.CENTER);
        final JFormattedTextField highScoreNameInput = new JFormattedTextField();
        highScoreNameInput.addKeyListener((new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (highScoreNameInput.getText().length() > 15 || !isValidCharacter(e.getKeyChar())) {
                    e.consume();
                }
            }
        }));
        final GameCourt court = new GameCourt(score, invincibility, highScoreNameInput, highScoreLabels);
        game_panel.add(court, BorderLayout.CENTER);
        court.setBackground(new Color(204, 245, 252));

        //Size of GameCourt
        final int COURT_WIDTH = (int) court.getDim().getWidth();
        final int COURT_HEIGHT = (int) court.getDim().getHeight();

        //Instructions Labels
        final JLabel instructLabel = new JLabel("How to Play:");
        instructLabel.setHorizontalAlignment(JLabel.CENTER);
        instructLabel.setFont(new Font("Dialog", Font.BOLD, COURT_HEIGHT/20));
        final JLabel howTo = new JLabel("<html>Corsairs is an arcade style continuous play game. " +
                "<br><br>Press Play and then space bar or click to start the ship moving." +
                "<br><br>Press the space bar or click to change the direction of the ship." +
                "<br><br>Collect as many coins as possible and avoid the Cannon Balls." +
                "<br><br>Yellow coins increase your score, purple coins make you invincible" +
                "<br>for 5 seconds and green coins gives double points for 5 seconds." +
                "<br><br>Overtime the difficulty will increase." +
                "<br><br>Enter your name after earning a high score to save it." +
                "<br><br>Press Reset to try again.</html>");
        howTo.setHorizontalAlignment(JLabel.CENTER);
        howTo.setFont(new Font("Dialog", Font.BOLD, COURT_HEIGHT/40));
        introPanel.add(instructLabel, BorderLayout.NORTH);
        introPanel.add(howTo, BorderLayout.CENTER);
        // Play button
        final JPanel play_panel = new JPanel();
        introPanel.add(play_panel, BorderLayout.SOUTH);
        final JButton play = new JButton("Play");
        play.addActionListener(e -> {
            introPanel.setVisible(false);
            gamePanel.setVisible(true);
            court.reset();
        });
        play.setPreferredSize(new Dimension(COURT_WIDTH/5, COURT_HEIGHT/15));
        play.setFont(new Font("Dialog", Font.BOLD, COURT_HEIGHT/40));
        play_panel.add(play);

        //Set Label Fonts
        score.setFont(new Font("Dialog", Font.BOLD, COURT_HEIGHT/40));
        invincibility.setFont(new Font("Dialog", Font.BOLD, COURT_HEIGHT/40));

        // Status panel
        final JPanel score_panel = new JPanel();
        score_panel.setLayout(new GridLayout(2, 2));
        gamePanel.add(score_panel, BorderLayout.SOUTH);
        final JPanel score_inner_panel = new JPanel();
        score_inner_panel.setPreferredSize(new Dimension(COURT_WIDTH/3, COURT_HEIGHT/20));
        final JPanel invincibility_panel = new JPanel();
        invincibility_panel.setPreferredSize(new Dimension(COURT_WIDTH/3, COURT_HEIGHT/20));
        final JPanel text_panel = new JPanel();
        highScoreNameInput.setVisible(false);
        highScoreNameInput.setPreferredSize(new Dimension(COURT_WIDTH/3, COURT_HEIGHT/25));
        highScoreNameInput.setFont(new Font("Dialog", Font.BOLD, COURT_HEIGHT/40));
        score_inner_panel.add(score, BorderLayout.CENTER);
        invincibility_panel.add(invincibility, BorderLayout.CENTER);
        text_panel.add(highScoreNameInput, BorderLayout.CENTER);
        score_panel.add(score_inner_panel);
        score_panel.add(text_panel);
        score_panel.add(invincibility_panel);

        // Main Menu button
        final JPanel main_menu_panel = new JPanel();
        gamePanel.add(main_menu_panel, BorderLayout.NORTH);
        final JButton mainMenu = new JButton("Main Menu");
        mainMenu.addActionListener(e -> {
            introPanel.setVisible(true);
            gamePanel.setVisible(false);
            court.reset();
        });
        mainMenu.setPreferredSize(new Dimension(COURT_WIDTH/5, COURT_HEIGHT/15));
        mainMenu.setFont(new Font("Dialog", Font.BOLD, COURT_HEIGHT/40));
        main_menu_panel.add(mainMenu);

        //Right panel
        final JPanel right_panel = new JPanel();
        final JPanel left_panel = new JPanel();
        left_panel.setPreferredSize(new Dimension(COURT_HEIGHT/2, 0));
        right_panel.setLayout(new GridLayout(3, 1));
        gamePanel.add(right_panel, BorderLayout.EAST);
        final JPanel high_score_panel = new JPanel();
        high_score_panel.setLayout(new GridLayout(6, 1));
        right_panel.add(high_score_panel);
        for (JLabel j : highScoreLabels) {
            high_score_panel.add(j);
            j.setFont(new Font("Dialog", Font.BOLD, COURT_HEIGHT/40));
        }
        highScoreLabels[0].setPreferredSize( new Dimension(COURT_HEIGHT/2,
                (int) highScoreLabels[0].getPreferredSize().getHeight()));
        right_panel.add(main_menu_panel);


        // Reset button
        final JPanel control_panel = new JPanel();
        gamePanel.add(control_panel, BorderLayout.NORTH);
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> court.reset());
        reset.setPreferredSize(new Dimension(COURT_WIDTH/5, COURT_HEIGHT/15));
        reset.setFont(new Font("Dialog", Font.BOLD, COURT_HEIGHT/40));
        control_panel.add(reset);



        // Put the frame on the screen
        frame.pack();
        introPanel.setLocation(COURT_WIDTH/3, COURT_HEIGHT/8);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        gamePanel.setVisible(false);
        introPanel.setVisible(true);
        introPanel.setPreferredSize(gamePanel.getPreferredSize());

        // Start game
        court.reset();
    }

    private static boolean isValidCharacter(int c) {
        return Character.isLetter(c) || Character.isDigit(c) || c == (int) ' ';
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}