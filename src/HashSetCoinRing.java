import java.util.*;
import java.util.function.Consumer;

/**
 * Implementation of CoinRing using a HashSet.
 * HashSet works well for this implementation because of
 * its use of .equals instead of compareTo because DoubleCoins,
 * ScoreCoins, and InvincibilityCoins at the same angle are not
 * equal.
 */
public class HashSetCoinRing implements CoinRing {

    private Set<Coin> coins;

    HashSetCoinRing() {
        coins = new HashSet<>();
    }

    @Override
    public void addCoin(Coin c) {
        if (c == null)
            throw new IllegalArgumentException();

        double angle = c.getAngle();
        int roundedAngle = (((int) angle/10) ) * 10;

        if (coins.size() < 36) {

//            boolean tooBig = false;



            while(isAlreadyCoin(roundedAngle) || roundedAngle > 350 || roundedAngle < 0) {
                while (isAlreadyCoin(roundedAngle) && roundedAngle > 350) {
                    roundedAngle -= 10;
                }

                while (isAlreadyCoin(roundedAngle) && roundedAngle < 360) {
                    roundedAngle += 10;
                }

                if (roundedAngle > 350)
                    roundedAngle = 0;

            }

            c.setAngle(roundedAngle);

            coins.add(c);
        }
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
    public void removeCoins(Collection<Coin> coins) {
        if (coins == null)
            throw new IllegalArgumentException();

        this.coins.removeAll(coins);
    }

    @Override
    public void removeCoinAtAngle(int angleInDegrees) {
        Set<Coin> toRemoveCoins = new HashSet<>();
        for (Coin c : coins) {
            if (c.getAngle() == angleInDegrees) {
                toRemoveCoins.add(c);
            }
        }
        coins.removeAll(toRemoveCoins);
    }

    @Override
    public int size() {
        return coins.size();
    }


    @Override
    public Iterator<Coin> iterator() {
        Set<Coin> copy = new HashSet<>(coins);

        return copy.iterator();
    }

    @Override
    public void forEach(Consumer<? super Coin> action) {
        for (Coin c : coins) {
            action.accept(c);

            if (c.getAngle() > 350 || c.getAngle() < 0) {
                coins.remove(c);
                addCoin(c);
            }

        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashSetCoinRing coins1 = (HashSetCoinRing) o;
        return Objects.equals(coins, coins1.coins);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coins);
    }

    @Override
    public String toString() {
        return coins.toString();
    }
}
