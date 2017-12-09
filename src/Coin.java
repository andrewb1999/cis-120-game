import java.util.Objects;

public abstract class Coin extends CircleObj implements CollectibleObject, Comparable {
    private double angle;

    /**
     * Constructor
     *
     * @param px
     * @param py
     * @param radius
     * @param courtSize
     */
    public Coin(int px, int py, int radius, int courtSize, double angleInDegrees) {
        super(px, py, radius, courtSize);

        angle = angleInDegrees;
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

    @Override
    public int compareTo(Object o) {
        Double d1 = ((Coin) o).getAngle();
        Double d2 = this.getAngle();
        return d1.compareTo(d2);
    }
}
