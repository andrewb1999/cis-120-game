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
     * @param courtWidth
     * @param courtHeight
     * @param angleInDegrees
     */
    private InvincibilityCoin(int px, int py, int radius, int courtWidth, int courtHeight, double angleInDegrees) {
        super(px, py, radius, courtWidth, courtHeight, angleInDegrees);

        if (img == null) {
            img = new ImageIcon(IMG_FILE).getImage();
        }
    }

    public static InvincibilityCoin createICoin(int circleRadius, double angleInDegrees, int courtWidth,
                                  int courtHeight, int centerX, int centerY) {
        int cx = (int) (circleRadius * Math.cos(Math.toRadians(angleInDegrees)));
        int cy = (int) (circleRadius * Math.sin(Math.toRadians(angleInDegrees)));
        int px = centerX + cx - courtWidth/COIN_RADIUS_FACTOR;
        int py = centerY - cy - courtWidth/COIN_RADIUS_FACTOR;
        int radius = courtHeight/COIN_RADIUS_FACTOR;

        return new InvincibilityCoin(px, py, radius, courtWidth, courtHeight, angleInDegrees);
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
