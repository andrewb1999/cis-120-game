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

    private Ship(int px, int py, int radius, int centerX, int centerY, int courtWidth, int courtHeight, Color color) {
        super(px, py, radius, courtWidth, courtHeight);

        this.centerX = centerX;
        this.centerY = centerY;
        this.courtWidth = courtWidth;
        this.courtHeight = courtHeight;
        theta = -Math.atan2(centerY - getPy(), centerX - getPx());
        this.color = color;
    }

    public static Ship makeShip(int courtWidth, int courtHeight, int centerX, int centerY, Color color) {
        int px = courtWidth/INIT_X_FACTOR - courtWidth/RADIUS_FACTOR;
        int py = courtHeight/INIT_Y_FACTOR - courtWidth/RADIUS_FACTOR;
        int radius = courtWidth/RADIUS_FACTOR;


        return new Ship (px, py, radius, centerX, centerY, courtWidth, courtHeight, color);
    }

    public double getAngleInDegrees() {
        return Math.toDegrees(-Math.atan2(centerY - getPy(), centerX - getPx())) + 180;
    }

    public void moveInCircle(GameCourt.OrbitDirection direction, double speedInRadians, int radius) {
        theta -= direction.getDirection() * speedInRadians;
        double x = (centerX + radius*Math.cos(theta));
        double y  = (centerY + radius*Math.sin(theta));

        this.setPx((int) x - getRadius());
        this.setPy((int) y - getRadius());
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}