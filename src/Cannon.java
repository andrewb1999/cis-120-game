import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class Cannon implements Drawable, Iterable<CannonBall>{

    private final int ORBIT_RADIUS;
    private final int COURT_SIZE;
    private final int CANNON_PX;
    private final int CANNON_PY;
    private final int CANNON_DIAMETER;
    private final Color COLOR = new Color(150, 150, 150);

    private List<CannonBall> cannonBalls;

    Cannon(int courtSize, int cannonDiameter, int initPosX, int initPosY, int orbitRadius) {
        COURT_SIZE = courtSize;
        ORBIT_RADIUS = orbitRadius;
        CANNON_PX = initPosX - cannonDiameter/2;
        CANNON_PY = initPosY - cannonDiameter/2;
        CANNON_DIAMETER = cannonDiameter;
        cannonBalls = new LinkedList<>();
    }

    public synchronized void fireCannonBall(boolean playing, double currentShipAngle, double shipSpeed,
                               double cannonBallSpeed, OrbitDirection direction) {
        if (playing) {
            double expectedAdditionalShipAngle = (ORBIT_RADIUS/ cannonBallSpeed) * Math.toDegrees(shipSpeed);
            double launchAngle;
            if (Math.random() > 0.5) {
                int i = 30;
                if(Math.random() > 0.75) {
                    i = 60;
                }
                launchAngle = (Math.random() - 0.5) * i + currentShipAngle
                        + direction.getDirection() * expectedAdditionalShipAngle;
            } else {
                launchAngle = (Math.random() - 0.5) * 45 + currentShipAngle;
            }

            cannonBalls.add(CannonBall.makeCannonBall(COURT_SIZE, launchAngle));
        }
    }

    public synchronized void moveCannonBalls(double cannonBallSpeed) {
        List<CannonBall> toRemove = new LinkedList<>();
        for (CannonBall c : cannonBalls) {
            c.moveAtAngle(cannonBallSpeed);
            if (c.isTouchingBorder()) {
                toRemove.add(c);
            }
        }

        cannonBalls.removeAll(toRemove);
    }

    @Override
    public synchronized void draw(Graphics g) {
        for (CannonBall c : cannonBalls) {
            c.draw(g);
        }

        g.setColor(this.COLOR);
        g.fillOval(CANNON_PX, CANNON_PY, CANNON_DIAMETER, CANNON_DIAMETER);
    }

    @Override
    public Iterator<CannonBall> iterator() {
        List<CannonBall> copy = new LinkedList<>(cannonBalls);

        return copy.iterator();
    }

    @Override
    public void forEach(Consumer<? super CannonBall> action) {
        for (CannonBall c : cannonBalls) {
            action.accept(c);
        }
    }
}
