import javax.swing.*;
import java.awt.*;
import java.util.Objects;

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
    private InvincibilityCoin(int px, int py, int radius, int courtSize, double angleInDegrees, int circleRadius,
                      int centerX, int centerY) {
        super(px, py, radius, courtSize, angleInDegrees, circleRadius, centerX, centerY);

        if (img == null) {
            img = new ImageIcon(IMG_FILE).getImage();
        }
    }

    public static InvincibilityCoin createICoin(int circleRadius, double angleInDegrees, int courtSize,
                                       int centerX, int centerY) {
        int radius = courtSize/COIN_RADIUS_FACTOR;
        InvincibilityCoin ic = new InvincibilityCoin(0, 0, radius, courtSize, angleInDegrees, circleRadius,
                centerX, centerY);
        ic.setAngle(angleInDegrees);
        return ic;
    }

    @Override
    public void modifyState(GameCourt c) {
        c.makeInvincible();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }

    @Override
    public String toString() {
        return "InvincibilityCoin " + getAngle();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvincibilityCoin coin = (InvincibilityCoin) o;
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
