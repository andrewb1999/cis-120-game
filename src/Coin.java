import java.util.Objects;

public abstract class Coin extends CircleObj implements CollectibleObject, Comparable {
    private double angle;

    private final int CIRCLE_RADIUS;
    private final int CENTER_X;
    private final int CENTER_Y;

    /**
     * Constructor
     *
     * @param px
     * @param py
     * @param radius
     * @param courtSize
     */
    public Coin(int px, int py, int radius, int courtSize, double angleInDegrees, int circleRadius, int centerX,
                int centerY) {
        super(px, py, radius, courtSize);

        CENTER_X = centerX;
        CENTER_Y = centerY;
        angle = angleInDegrees;
        CIRCLE_RADIUS = circleRadius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coin that = (Coin) o;
        return Double.compare(that.angle, angle) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(angle);
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angleInDegrees) {
        angle = angleInDegrees;
        int cx = (int) (CIRCLE_RADIUS * Math.cos(Math.toRadians(angle)));
        int cy = (int) (CIRCLE_RADIUS * Math.sin(Math.toRadians(angle)));
        int px = CENTER_X + cx - getRadius();
        int py = CENTER_Y - cy - getRadius();

        setPx(px);
        setPy(py);
    }

    @Override
    public int compareTo(Object o) {
        Double d1 = ((Coin) o).getAngle();
        Double d2 = this.getAngle();
        return d1.compareTo(d2);
    }
}