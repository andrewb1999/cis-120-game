import java.awt.*;

public class Coin extends CollectibleCircleObject {
    private static final int COIN_RADIUS_FACTOR = 50;
    private Color color = Color.YELLOW;
    private int centerX;
    private int centerY;

    /**
     * Constructor
     *
     * @param px
     * @param py
     * @param radius
     * @param courtWidth
     * @param courtHeight
     */
    private Coin(int px, int py, int radius, int courtWidth, int courtHeight) {
        super(px, py, radius, courtWidth, courtHeight);
    }

    public static Coin createCoin(int circleRadius, double angleInDegrees, int courtWidth,
                                  int courtHeight, int centerX, int centerY) {
        int cx = (int) (circleRadius * Math.cos(Math.toRadians(angleInDegrees)));
        int cy = (int) (circleRadius * Math.sin(Math.toRadians(angleInDegrees)));
        int px = centerX + cx - courtWidth/COIN_RADIUS_FACTOR;
        int py = centerY - cy - courtWidth/COIN_RADIUS_FACTOR;
        int radius = courtHeight/COIN_RADIUS_FACTOR;

        double theta = Math.toRadians(angleInDegrees);

        return new Coin(px, py, radius, courtWidth, courtHeight);
    }

    @Override
    public void modifyState(GameCourt c) {
        c.incrementScore();
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}
