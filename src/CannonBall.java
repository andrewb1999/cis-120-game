
import java.awt.*;

/**
 * A basic game object starting in the upper left corner of the game court. It is displayed as a
 * circle of a specified color.
 */
public class CannonBall extends CircleObj {
    private double moveX;
    private double moveY;
    private int initPx;
    private int initPy;
    private double angleInDegrees;
    private static final int RADIUS_FACTOR = 64;

    private static final Color color = Color.BLACK;

    private CannonBall(int px, int py, int radius, int courtSize, double angleInDegrees) {
        super(px, py, radius, courtSize);

        this.angleInDegrees = angleInDegrees;
        this.initPx = getPx();
        this.initPy = getPy();
    }

    public static CannonBall makeCannonBall(int courtSize, double angleInDegrees) {
        int px = courtSize/2 - courtSize/RADIUS_FACTOR;
        int py = courtSize/2 - courtSize/RADIUS_FACTOR;
        int radius = courtSize/RADIUS_FACTOR;

        return new CannonBall(px, py, radius, courtSize, angleInDegrees);
    }

    public void moveAtAngle(double speed) {
        double theta = Math.toRadians(angleInDegrees);
        moveX += (speed * Math.cos(theta));
        moveY += (speed * Math.sin(theta));
        setPx(initPx + (int) moveX);
        setPy(initPy - (int) moveY);
    }

    @Override
    public void draw(Graphics g){
        g.setColor(this.color);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}
