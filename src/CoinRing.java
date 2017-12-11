import java.util.Collection;

/**
 * Interface for a CoinRing.
 * A CoinRing models how coins can be distributed in one of 36 angles
 * (0 degrees - 350 degrees) around the circle. Max Size is 36.
 * If a coin already exists at the angle add should place it somewhere
 * else on the ring.  removeCoinAtAngle should remove any coin at the
 * specified angle but removeCoins should work based off coin equality
 * (A ScoreCoin and a DoubleCoinsCoin at the same angle are not equal)
 * CoinRing should not allow coins with angles greater than 350 or less than 0;
 */
public interface CoinRing extends Iterable<Coin>{
    void addCoin(Coin c);

    void removeCoins(Collection<Coin> coins);

    void removeCoinAtAngle(int angleInDegrees);

    int size();
}
