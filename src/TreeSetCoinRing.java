import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

public class TreeSetCoinRing implements CoinRing {

    private Set<Coin> coins;

    TreeSetCoinRing() {
        coins = new TreeSet<>();
    }


    @Override
    public void addCoin(Coin c) {
        double randomAngle = c.getAngle();
        int roundedAngle = ((int) randomAngle/10 + 5) * 10;
        int originalRoundedAngle = roundedAngle;

        if (roundedAngle > 350)
            roundedAngle = 0;

        while (isAlreadyCoin(roundedAngle)) {
            if (roundedAngle + 50 <= 350 && originalRoundedAngle % 50 != 0)
                roundedAngle += 50;
            else if (roundedAngle <= 350)
                roundedAngle += 10;
            else
                roundedAngle = 0;
        }

        c.setAngle(roundedAngle);

        coins.add(c);
    }

    private boolean isAlreadyCoin(int angle) {
        if (coins.size() < 36) {
            for (Coin coin : coins) {
                if (coin.getAngle() == angle)
                    return true;
            }
        }

        return false;
    }

    @Override
    public void removeCoins(Set<Coin> coins) {
        this.coins.removeAll(coins);
    }

    @Override
    public void removeCoinAtAngle(int angleInDegrees) {
        Set<Coin> toRemoveCoins = new TreeSet<>();
        for (Coin c : coins) {
            if (c.getAngle() == angleInDegrees) {
                toRemoveCoins.add(c);
            }
        }
        coins.removeAll(toRemoveCoins);
    }


    @Override
    public Iterator<Coin> iterator() {
        Set<Coin> copy = new TreeSet<>(coins);

        return copy.iterator();
    }

    @Override
    public void forEach(Consumer<? super Coin> action) {
        for (Coin c : coins) {
            action.accept(c);
        }
    }
}
