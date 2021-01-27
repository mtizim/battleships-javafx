import java.util.Collection;

// Easier than dependency injection, it's a small finite project anyway
public class Global {
    public static final ChangeListener<TileLocation> bottomClickedStream = new ChangeListener<>();
    public static final ChangeListener<TileLocation> topClickedStream = new ChangeListener<>();
    public static final ChangeListener<Collection<TileLocation>> topHoveredStream = new ChangeListener<>();
    public static final ChangeListener<Collection<TileLocation>> bottomHoveredStream = new ChangeListener<>();
    public static final ChangeListener<MWHEEL> mwheelStream = new ChangeListener<>(true);

}

enum MWHEEL {
    UP, DOWN
}

class TileLocation {
    TileLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int x;
    int y;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TileLocation)) {
            return false;
        }
        TileLocation o = (TileLocation) obj;
        return (this.x == o.x && this.y == o.y);
    }

    @Override
    public int hashCode() {
        return this.x + 20 * this.y;
    }
}