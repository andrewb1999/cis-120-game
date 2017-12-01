public abstract class CollectibleCircleObject extends CircleObj implements CollectibleObject{

    /**
     * Constructor
     *
     * @param px
     * @param py
     * @param radius
     * @param courtWidth
     * @param courtHeight
     */
    public CollectibleCircleObject(int px, int py, int radius, int courtWidth, int courtHeight) {
        super(px, py, radius, courtWidth, courtHeight);
    }
}
