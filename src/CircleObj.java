public abstract class CircleObj extends GameObj{
    private int radius;

    /**
     * Constructor
     *
     * @param px
     * @param py
     * @param radius
     * @param courtWidth
     * @param courtHeight
     */
    public CircleObj(int px, int py, int radius, int courtWidth, int courtHeight) {
        super(px, py, radius*2, radius*2, courtWidth, courtHeight);

        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public boolean intersects(CircleObj that) {
        int thisX = getPx() + getRadius();
        int thisY = getPy() + getRadius();
        int thatX = that.getPx() + that.getRadius();
        int thatY = that.getPy() + that.getRadius();

        double distance = Math.pow((thisX - thatX) * (thisX - thatX)
                + (thisY - thatY) * (thisY - thatY), 0.5);

        if(distance < (radius + that.getRadius())) {
            return true;
        }

        return false;
    }
}
