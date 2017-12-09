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
    private int courtSize;
    private static final int RADIUS_FACTOR = 40;
    private static final int INIT_X_FACTOR = 2;
    private static final int INIT_Y_FACTOR = 8;
    private static final Color iColor = new Color(247, 136, 136);
    private static final Color nIColor = Color.BLUE;

    private Color color;

    private Ship(int px, int py, int radius, int centerX, int centerY, int courtSize) {
        super(px, py, radius, courtSize);

        this.centerX = centerX;
        this.centerY = centerY;
        this.courtSize = courtSize;
        this.courtSize = courtSize;
        theta = -Math.atan2(centerY - getPy(), centerX - getPx());
        color = nIColor;
    }

    public static Ship makeShip(int courtSize, int centerX, int centerY) {
        int px = courtSize /INIT_X_FACTOR - courtSize /RADIUS_FACTOR;
        int py = courtSize /INIT_Y_FACTOR - courtSize /RADIUS_FACTOR;
        int radius = courtSize /RADIUS_FACTOR;


        return new Ship (px, py, radius, centerX, centerY, courtSize);
    }

    public double getAngleInDegrees() {
        return Math.toDegrees(-Math.atan2(centerY - getPy(), centerX - getPx())) + 180;
    }

    public void moveInCircle(OrbitDirection direction, double speedInRadians, int radius) {
        theta -= direction.getDirection() * speedInRadians;
        double x = (centerX + radius*Math.cos(theta));
        double y  = (centerY + radius*Math.sin(theta));

        this.setPx((int) x - getRadius());
        this.setPy((int) y - getRadius());
    }

    private void setColor(Color color) {
        this.color = color;
    }

    public void setInvincibility(boolean isInvincible) {
        if(isInvincible) {
            setColor(iColor);
        } else {
            setColor(nIColor);
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}