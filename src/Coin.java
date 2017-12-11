import java.util.Objects;

public abstract class Coin extends CircleObj implements CollectibleObject {
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

    public int getCircleRadius() {
        return CIRCLE_RADIUS;
    }

    public int getCenterX() {
        return CENTER_X;
    }

    public int getCenterY() {
        return CENTER_Y;
    }
}