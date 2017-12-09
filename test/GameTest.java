import org.junit.*;

import javax.swing.*;

import static org.junit.Assert.*;

import java.io.*;
import java.util.Set;
import java.util.TreeSet;

public class GameTest {
    @Before
    private void before(){
        final JLabel score = new JLabel("Score: " + 0);
        final JLabel invincibility = new JLabel("You are not invincible");
        final JLabel[] highScoreLabels = new JLabel[6];
        highScoreLabels[0] = new JLabel("High Scores: ");
        for (int i = 1; i < 6; i++) {
            highScoreLabels[i] = new JLabel("");
        }
        final JFormattedTextField highScoreNameInput = new JFormattedTextField();

        GameCourt court = new GameCourt(score, invincibility, highScoreNameInput, highScoreLabels);
    }

    @Test
    private void test() {

    }
}
