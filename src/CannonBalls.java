import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CannonBalls {


    private class CannonBallList {
        private List<CannonBall> collectibles;

        CannonBallList() {
            collectibles = new LinkedList<>();
        }

        synchronized List<CannonBall> getCollectibles() {
            List<CannonBall> newC = new LinkedList<>();
            newC.addAll(collectibles);
            return newC;
        }

        synchronized void add(CannonBall c) {
            collectibles.add(c);
        }

        synchronized void removeAll(Collection<CannonBall> c) {
            collectibles.removeAll(c);
        }
    }
}
