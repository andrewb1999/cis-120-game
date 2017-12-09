public enum OrbitDirection {
    CW (-1),
    CCW (1);

    private final int direction;
    OrbitDirection(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }
}