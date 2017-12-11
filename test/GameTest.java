import org.junit.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class GameTest {
    private CoinRing coinRing;
    private static final int CIRCLE_RADIUS = 100;
    private static final int COURT_SIZE = 200;

    @Before
    public void initialize() {
        coinRing = new HashSetCoinRing();
    }

    @Test
    public void addRemoveCoinTest() {
        Set<Coin> remove = new HashSet<>();
        ScoreCoin sc = ScoreCoin.createCoin(CIRCLE_RADIUS, 60, COURT_SIZE, COURT_SIZE / 2, COURT_SIZE / 2);
        remove.add(sc);
        coinRing.addCoin(sc);
        coinRing.removeCoins(remove);
        assertEquals(new HashSetCoinRing(), coinRing);
    }

    @Test
    public void removeCoinAtAngleTest() {
        ScoreCoin sc = ScoreCoin.createCoin(CIRCLE_RADIUS, 60, COURT_SIZE, COURT_SIZE / 2, COURT_SIZE / 2);
        coinRing.addCoin(sc);
        coinRing.removeCoinAtAngle(60);
        assertEquals(new HashSetCoinRing(), coinRing);
    }

    @Test
    public void removeDifferentCoin() {
        Set<Coin> remove = new HashSet<>();
        ScoreCoin sc = ScoreCoin.createCoin(CIRCLE_RADIUS, 60, COURT_SIZE, COURT_SIZE / 2, COURT_SIZE / 2);
        remove.add(InvincibilityCoin.createICoin(CIRCLE_RADIUS, 60, COURT_SIZE, COURT_SIZE / 2, COURT_SIZE / 2));
        coinRing.addCoin(sc);
        CoinRing cr = new HashSetCoinRing();
        cr.addCoin(sc);
        coinRing.removeCoins(remove);
        assertEquals(cr, coinRing);
    }

    @Test
    public void addCoinAtSameAngleTest() {
        ScoreCoin sc = ScoreCoin.createCoin(CIRCLE_RADIUS, 60, COURT_SIZE, COURT_SIZE / 2, COURT_SIZE / 2);
        coinRing.addCoin(sc);
        coinRing.addCoin(DoubleCoinsCoin.createDCoin(CIRCLE_RADIUS, 60, COURT_SIZE, COURT_SIZE / 2, COURT_SIZE / 2));
        assertEquals(2, coinRing.size());
    }

    @Test
    public void addTooManyCoinsTest() {
        for (int i = 0; i < 500; i += 10)
            coinRing.addCoin(ScoreCoin.createCoin(CIRCLE_RADIUS, i, COURT_SIZE, COURT_SIZE / 2, COURT_SIZE / 2));
        assertEquals(36, coinRing.size());
    }

    @Test
    public void addManyCoinsAtSameAngleTest() {
        for (int i = 0; i < 50; i++)
            coinRing.addCoin(ScoreCoin.createCoin(CIRCLE_RADIUS, 60, COURT_SIZE, COURT_SIZE / 2, COURT_SIZE / 2));
        assertEquals(36, coinRing.size());
    }

    @Test
    public void removeAllCoinsTest() {
        for (int i = 0; i < 50; i++)
            coinRing.addCoin(ScoreCoin.createCoin(CIRCLE_RADIUS, 60, COURT_SIZE, COURT_SIZE / 2, COURT_SIZE / 2));

        for (int i = 0; i < 360; i += 10)
            coinRing.removeCoinAtAngle(i);

        assertEquals(0, coinRing.size());
    }


    @Test
    public void allCoinsWithinRangeTest() {
        for (int i = 0; i < 50; i++)
            coinRing.addCoin(ScoreCoin.createCoin(CIRCLE_RADIUS, 60, COURT_SIZE, COURT_SIZE / 2, COURT_SIZE / 2));

        boolean allInRange = true;

        for (Coin c : coinRing) {
            if (c.getAngle() > 350 || c.getAngle() < 0)
                allInRange = false;
        }

        assertEquals(true, allInRange);
    }

    @Test
    public void removeCoinFromEmptyRingTest() {
        coinRing.removeCoinAtAngle(60);
        assertEquals(new HashSetCoinRing(), coinRing);
    }

    @Test (expected = IllegalArgumentException.class)
    public void addNullCoinTest() {
        coinRing.addCoin(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void removeNullSetTest() {
        coinRing.removeCoins(null);
    }

}

 