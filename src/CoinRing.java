import java.util.Set;

/**
 * Interface for a CoinRing.
 * A CoinRing models how coins can be distributed in one of 36 angles around the circle.
 */
public interface CoinRing extends Iterable<Coin>{
    void addCoin(Coin c);

    void removeCoins(Set<Coin> coins);

    void removeCoinsAtAngle(int angleInDegrees);
}
