public abstract class CollectibleCircleObject extends CircleObj implements CollectibleObject, Comparable {
    private double angle;

    /**
     * Constructor
     *
     * @param px
     * @param py
     * @param radius
     * @param courtWidth
     * @param courtHeight
     */
    public CollectibleCircleObject(int px, int py, int radius, int courtWidth,
                                   int courtHeight, double angleInDegrees) {
        super(px, py, radius, courtWidth, courtHeight);

        angle = angleInDegrees;
    }

    protected double getAngle() {
        return angle;
    }

    @Override
    public int compareTo(Object o) {
        Double d1 = ((CollectibleCircleObject) o).getAngle();
        Double d2 = this.getAngle();
        return d1.compareTo(d2);
    }
}
