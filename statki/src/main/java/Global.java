import java.util.Collection;

// Easier than dependency injection, it's a small finite project anyway
public class Global {
    public static Listenable<TileLocation> bottomTileStream = new Listenable<>();
    public static Listenable<TileLocation> topTileStream = new Listenable<>();
    public static Listenable<Collection<TileLocation>> topHovered = new Listenable<>();
    public static Listenable<Collection<TileLocation>> bottomHovered = new Listenable<>();
}

class TileLocation {
    TileLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int x;
    int y;
}