/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local variables.

//        final JFrame frame = new JFrame("Corsairs");
//        frame.setLayout(new BorderLayout());
//        frame.setLocation(300, 300);
//
//        // Status panel
//        final JPanel score_panel = new JPanel();
//        score_panel.setLayout(new GridLayout(1, 2));
//        frame.add(score_panel, BorderLayout.SOUTH);
//        final JLabel score = new JLabel("Score: " + 0);
//        final JLabel invincibility = new JLabel("You are not invincible");
//        final JPanel score_inner_panel = new JPanel();
//        final JPanel invincibility_panel = new JPanel();
//        score_inner_panel.add(score, BorderLayout.CENTER);
//        invincibility_panel.add(invincibility, BorderLayout.CENTER);
//        score_panel.add(score_inner_panel);
//        score_panel.add(invincibility_panel);
//
//
//        // Main playing area
//        //final JPanel game_panel = new JPanel();
//        //frame.add(game_panel, BorderLayout.CENTER);
//        final GameCourt court = new GameCourt(score, invincibility);
//        frame.add(court, BorderLayout.CENTER);
//
//
//        // Reset button
//        final JPanel control_panel = new JPanel();
//        frame.add(control_panel, BorderLayout.NORTH);
//
//        // Note here that when we add an action listener to the reset button, we define it as an
//        // anonymous inner class that is an instance of ActionListener with its actionPerformed()
//        // method overridden. When the button is pressed, actionPerformed() will be called.
//        final JButton reset = new JButton("Reset");
//        reset.addActionListener(e -> court.reset());
//        control_panel.add(reset);
//
//        // Put the frame on the screen
//        frame.pack();
//        frame.setResizable(true);
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.setVisible(true);

        final JFrame frame = new JFrame("TOP LEVEL FRAME");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        // Main playing area
        final GameCourt court = new GameCourt(status, status);
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we define it as an
        // anonymous inner class that is an instance of ActionListener with its actionPerformed()
        // method overridden. When the button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        control_panel.add(reset);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


        // Start game
        court.reset();
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}