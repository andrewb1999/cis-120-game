import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Coins implements Drawable {

    private final int COURT_SIZE;
    private final int CENTER_X;
    private final int CENTER_Y;
    private final int ORBIT_RADIUS;

    private CoinRing coinRing;

    Coins(int courtSize, int centerX, int centerY, int orbitRadius) {
        COURT_SIZE = courtSize;
        CENTER_X = centerX;
        CENTER_Y = centerY;
        ORBIT_RADIUS = orbitRadius;
        coinRing = new HashSetCoinRing();
        for (int i = 0; i < 20; i += 1) {
            addRandomCoin();
        }

        coinRing.removeCoinAtAngle(90);
    }

    public synchronized void addRandomCoin() {
        if (Math.random() > 0.03) {
            double randomAngle = Math.random() * 360;
            coinRing.addCoin(ScoreCoin.createCoin(ORBIT_RADIUS, randomAngle, COURT_SIZE, CENTER_X, CENTER_Y));
        } else {
            double randomAngle = Math.random() * 360;
            int roundedAngle = ((int) randomAngle/10 + 5) * 10;
            if (Math.random() > 0.5) {
                coinRing.addCoin(InvincibilityCoin.createICoin(ORBIT_RADIUS, roundedAngle, COURT_SIZE,
                        CENTER_X, CENTER_Y));
            } else {
                coinRing.addCoin(DoubleCoinsCoin.createDCoin(ORBIT_RADIUS, roundedAngle, COURT_SIZE,
                        CENTER_X, CENTER_Y));
            }
        }
    }

    public synchronized void collectCoins(Ship ship, GameCourt court) {
        Set<Coin> toRemoveCoins = new HashSet<>();
        for (Coin c : coinRing) {
            if (c.intersects(ship)) {
                c.modifyState(court);
                toRemoveCoins.add(c);
            }
        }

        coinRing.removeCoins(toRemoveCoins);
    }

    @Override
    public synchronized void draw(Graphics g) {
        for (Coin c : coinRing) {
            c.draw(g);
        }
    }
}
