import javax.swing.*;
import java.awt.*;

public class ScoreCoin extends Coin {
    private static final int COIN_RADIUS_FACTOR = 50;
    //private Color color = Color.YELLOW;
    private static final String IMG_FILE = "files/coin-animation-s.gif";
    private static Image img;
    /**
     * Constructor
     *
     * @param px
     * @param py
     * @param radius
     * @param courtSize
     */
    private ScoreCoin(int px, int py, int radius, int courtSize, double angleInDegrees) {
        super(px, py, radius, courtSize, angleInDegrees);

            if (img == null) {
                img = new ImageIcon(IMG_FILE).getImage();
            }
    }

    public static ScoreCoin createCoin(int circleRadius, double angleInDegrees, int courtSize,
                                       int centerX, int centerY) {
        int cx = (int) (circleRadius * Math.cos(Math.toRadians(angleInDegrees)));
        int cy = (int) (circleRadius * Math.sin(Math.toRadians(angleInDegrees)));
        int px = centerX + cx - courtSize/COIN_RADIUS_FACTOR;
        int py = centerY - cy - courtSize/COIN_RADIUS_FACTOR;
        int radius = courtSize/COIN_RADIUS_FACTOR;

        return new ScoreCoin(px, py, radius, courtSize, angleInDegrees);
    }

    @Override
    public void modifyState(GameCourt c) {
        c.incrementScore();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }


}
