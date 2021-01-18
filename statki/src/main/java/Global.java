import java.util.Collection;

// Easier than dependency injection, it's a small finite project anyway
public class Global {
    public static final Listenable<TileLocation> bottomClickedStream = new Listenable<>();
    public static final Listenable<TileLocation> topClickedStream = new Listenable<>();
    public static final Listenable<Collection<TileLocation>> topHoveredStream = new Listenable<>();
    public static final Listenable<Collection<TileLocation>> bottomHoveredStream = new Listenable<>();
    public static final Listenable<MWHEEL> mwheelStream = new Listenable<>(true);
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