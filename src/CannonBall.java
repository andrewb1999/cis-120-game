
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

    public CannonBall(int courtWidth, int courtHeight, double angleInDegrees) {
        super(courtWidth/2 - courtWidth/RADIUS_FACTOR, courtHeight/2 - courtWidth/RADIUS_FACTOR,
                courtWidth/RADIUS_FACTOR, courtWidth, courtHeight);

        this.angleInDegrees = angleInDegrees;
        this.initPx = getPx();
        this.initPy = getPy();
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
