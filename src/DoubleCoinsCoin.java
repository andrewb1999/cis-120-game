import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class DoubleCoinsCoin extends Coin {
    private static final int COIN_RADIUS_FACTOR = 50;
    private static final String IMG_FILE = "files/coin-animation-d.gif";
    private static Image img;

    /**
     * Constructor
     *
     * @param px
     * @param py
     * @param radius
     * @param courtSize
     */
    private DoubleCoinsCoin(int px, int py, int radius, int courtSize, double angleInDegrees, int circleRadius,
                      int centerX, int centerY) {
        super(px, py, radius, courtSize, angleInDegrees, circleRadius, centerX, centerY);

        if (img == null) {
            img = new ImageIcon(IMG_FILE).getImage();
        }
    }

    public static DoubleCoinsCoin createDCoin(int circleRadius, double angleInDegrees, int courtSize,
                                       int centerX, int centerY) {
        int radius = courtSize/COIN_RADIUS_FACTOR;
        DoubleCoinsCoin dc = new DoubleCoinsCoin(0, 0, radius, courtSize, angleInDegrees, circleRadius,
                centerX, centerY);
        dc.setAngle(angleInDegrees);
        return dc;
    }


    @Override
    public void modifyState(GameCourt c) {
        c.makeDoubleCoins();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }

    @Override
    public String toString() {
        return "DoubleCoinsCoin " + getAngle();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleCoinsCoin coin = (DoubleCoinsCoin) o;
        return Double.compare(coin.getAngle(), getAngle()) == 0 &&
                getCircleRadius() == coin.getCircleRadius() &&
                getCenterX() == coin.getCenterX() &&
                getCenterY() == coin.getCenterY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAngle(), getCircleRadius(), getCenterX(), getCenterY(), this.getClass());
    }
}