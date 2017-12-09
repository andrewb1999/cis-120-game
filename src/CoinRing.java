import java.awt.*;
import java.util.Set;
import java.util.TreeSet;

public class CoinRing implements Drawable {

    private final int COURT_SIZE;
    private final int CENTER_X;
    private final int CENTER_Y;
    private final int ORBIT_RADIUS;

    private Set<Coin> coins;

    CoinRing(int courtSize, int centerX, int centerY, int orbitRadius) {
        COURT_SIZE = courtSize;
        CENTER_X = centerX;
        CENTER_Y = centerY;
        ORBIT_RADIUS = orbitRadius;
        coins = new TreeSet<>();
        for (int i = 0; i < 360; i += 10) {
            addRandomCoin();
        }

        Set<Coin> toRemoveCoins = new TreeSet<>();
        for (Coin c : coins) {
            if (c.getAngle() == 90) {
                toRemoveCoins.add(c);
            }
        }
        coins.removeAll(toRemoveCoins);
    }

    public void addRandomCoin() {
        if (Math.random() > 0.02 ) {
            double randomAngle = Math.random() * 360;
            int roundedAngle = ((int) randomAngle/10 + 5) * 10;
            addCoin(ScoreCoin.createCoin(ORBIT_RADIUS, roundedAngle, COURT_SIZE, CENTER_X, CENTER_Y));
        } else {
            double randomAngle = Math.random() * 360;
            int roundedAngle = ((int) randomAngle/10 + 5) * 10;
            if (Math.random() > 0.5) {
                addCoin(InvincibilityCoin.createICoin(ORBIT_RADIUS, roundedAngle, COURT_SIZE,
                        CENTER_X, CENTER_Y));
            } else {
                addCoin(DoubleCoinsCoin.createDCoin(ORBIT_RADIUS, roundedAngle, COURT_SIZE,
                        CENTER_X, CENTER_Y));
            }
        }
    }

    public synchronized void addCoin(Coin c) {
        coins.add(c);
    }

    public synchronized void collectCoins(Ship ship, GameCourt court) {
        Set<Coin> toRemoveCoins = new TreeSet<>();
        for (Coin c : coins) {
            if (c.intersects(ship)) {
                c.modifyState(court);
                toRemoveCoins.add(c);
            }
        }

        coins.removeAll(toRemoveCoins);
    }

    @Override
    public synchronized void draw(Graphics g) {
        for (Coin c : coins) {
            c.draw(g);
        }
    }
}
