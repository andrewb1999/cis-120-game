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
    private ScoreCoin(int px, int py, int radius, int courtSize, double angleInDegrees, int circleRadius,
                      int centerX, int centerY) {
        super(px, py, radius, courtSize, angleInDegrees, circleRadius, centerX, centerY);

            if (img == null) {
                img = new ImageIcon(IMG_FILE).getImage();
            }
    }

    public static ScoreCoin createCoin(int circleRadius, double angleInDegrees, int courtSize,
                                       int centerX, int centerY) {
        int radius = courtSize/COIN_RADIUS_FACTOR;
        ScoreCoin sc = new ScoreCoin(0, 0, radius, courtSize, angleInDegrees, circleRadius,
                                        centerX, centerY);
        sc.setAngle(angleInDegrees);
        return sc;
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
