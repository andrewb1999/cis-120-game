/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;

/**
 * A basic game object starting in the upper left corner of the game court. It is displayed as a
 * circle of a specified color.
 */
public class Ship extends CircleObj {
    private double theta;
    private int centerX;
    private int centerY;
    private int courtWidth;
    private int courtHeight;
    private static final int RADIUS_FACTOR = 40;
    private static final int INIT_X_FACTOR = 2;
    private static final int INIT_Y_FACTOR = 8;

    private Color color;

    public Ship(int courtWidth, int courtHeight, int centerX, int centerY, Color color) {
        super(courtWidth/INIT_X_FACTOR - courtWidth/RADIUS_FACTOR,
                courtHeight/INIT_Y_FACTOR - courtWidth/RADIUS_FACTOR, courtWidth/RADIUS_FACTOR,
                courtWidth, courtHeight);

        this.courtWidth = courtWidth;
        this.courtHeight = courtHeight;
        this.centerX = centerX;
        this.centerY = centerY;
        theta = -Math.atan2(centerY - getPy(), centerX - getPx());
        this.color = color;
    }

    public double getAngleInDegrees() {
        return Math.toDegrees(-Math.atan2(centerY - getPy(), centerX - getPx())) + 180;
    }

    public void moveInCircle(GameCourt.OrbitDirection direction, double speedInDegrees, int radius) {
        theta -= direction.getDirection() * Math.toRadians(speedInDegrees);
        double x = centerX + radius*Math.cos(theta);
        double y  = centerY + radius*Math.sin(theta);

        this.setPx((int) x - courtWidth/RADIUS_FACTOR);
        this.setPy((int) y - courtWidth/RADIUS_FACTOR);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}