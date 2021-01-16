package statki;

enum Orientation {
    UP, LEFT, RIGHT, DOWN
}

public class Ship {
    public int size;
    public Orientation orientation;
    public int x;
    public int y;

    Ship(String name, String orientation, int x, int y) {
        size = switch (name) {
            case "Carrier" -> 4;
            case "Battleship" -> 3;
            case "Destroyer" -> 2;
            case "PatrolBoat" -> 1;
            default -> throw new IllegalArgumentException("Unexpected value: " + name);
        };
        this.orientation = switch (orientation) {
            case "LEFT" -> Orientation.LEFT;
            case "RIGHT" -> Orientation.RIGHT;
            case "UP" -> Orientation.UP;
            case "DOWN" -> Orientation.DOWN;
            default -> throw new IllegalArgumentException("Unexpected value: " + orientation);
        };

        this.x = x;
        this.y = y;

    }

}
