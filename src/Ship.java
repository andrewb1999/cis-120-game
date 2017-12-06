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
    private static final Color iColor = new Color(247, 136, 136);
    private static final Color nIColor = Color.BLUE;

    private Color color;

    private Ship(int px, int py, int radius, int centerX, int centerY, int courtWidth, int courtHeight) {
        super(px, py, radius, courtWidth, courtHeight);

        this.centerX = centerX;
        this.centerY = centerY;
        this.courtWidth = courtWidth;
        this.courtHeight = courtHeight;
        theta = -Math.atan2(centerY - getPy(), centerX - getPx());
        color = nIColor;
    }

    public static Ship makeShip(int courtWidth, int courtHeight, int centerX, int centerY) {
        int px = courtWidth/INIT_X_FACTOR - courtWidth/RADIUS_FACTOR;
        int py = courtHeight/INIT_Y_FACTOR - courtWidth/RADIUS_FACTOR;
        int radius = courtWidth/RADIUS_FACTOR;


        return new Ship (px, py, radius, centerX, centerY, courtWidth, courtHeight);
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