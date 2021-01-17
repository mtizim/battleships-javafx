
enum Orientation {
    UP, LEFT, RIGHT, DOWN
}

public class Ship {
    public int size;
    public Orientation orientation;
    public int x;
    public int y;

    Ship(int size, Orientation orientation, int x, int y) {
        this.size = size;
        this.orientation = orientation;
        this.x = x;
        this.y = y;

    }

}
