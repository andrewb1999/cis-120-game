import javax.swing.*;
import java.awt.*;

public class InvincibilityCoin extends Coin {
    private static final int COIN_RADIUS_FACTOR = 50;
    private static final String IMG_FILE = "files/coin-animation-i.gif";
    private static Image img;

    /**
     * Constructor for InvincibilityCoin
     *
     * @param px
     * @param py
     * @param radius
     * @param courtSize
     * @param angleInDegrees
     */
    private InvincibilityCoin(int px, int py, int radius, int courtSize, double angleInDegrees) {
        super(px, py, radius, courtSize, angleInDegrees);

        if (img == null) {
            img = new ImageIcon(IMG_FILE).getImage();
        }
    }

    public static InvincibilityCoin createICoin(int circleRadius, double angleInDegrees, int courtSize,
                                                int centerX, int centerY) {
        int cx = (int) (circleRadius * Math.cos(Math.toRadians(angleInDegrees)));
        int cy = (int) (circleRadius * Math.sin(Math.toRadians(angleInDegrees)));
        int px = centerX + cx - courtSize/COIN_RADIUS_FACTOR;
        int py = centerY - cy - courtSize/COIN_RADIUS_FACTOR;
        int radius = courtSize/COIN_RADIUS_FACTOR;

        return new InvincibilityCoin(px, py, radius, courtSize, angleInDegrees);
    }

    @Override
    public void modifyState(GameCourt c) {
        c.makeInvincible();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }
}
