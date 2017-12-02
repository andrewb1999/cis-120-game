import javax.swing.*;
import java.awt.*;

public class InvincibilityCoin extends CollectibleCircleObject{
    private static final int COIN_RADIUS_FACTOR = 50;
    private Color color = Color.GREEN;
    private static final String IMG_FILE = "files/coin-animation-i.gif";
    private static Image img;

    /**
     * Constructor
     *
     * @param px
     * @param py
     * @param radius
     * @param courtWidth
     * @param courtHeight
     */
    public InvincibilityCoin(int px, int py, int radius, int courtWidth, int courtHeight) {
        super(px, py, radius, courtWidth, courtHeight);

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

        double theta = Math.toRadians(angleInDegrees);

        return new InvincibilityCoin(px, py, radius, courtWidth, courtHeight);
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
