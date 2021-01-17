import java.util.ArrayList;
import java.util.List;

public class Board {

    BoardItem[][] selfBoard = new BoardItem[10][10];

    Board() {
        // init boardu
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                selfBoard[i][j] = BoardItem.EMPTY;
            }
        }

    }

    BoardItem[][] display() {
        BoardItem[][] retboard = new BoardItem[10][10];
        for (int y = 9; y >= 0; y--) {
            for (int x = 0; x < 10; x++) {
                BoardItem s = switch (selfBoard[x][y]) {
                    case SHIP -> BoardItem.SHIP;
                    case MISS -> BoardItem.MISS;
                    case EMPTY -> BoardItem.EMPTY;
                    case HIT -> BoardItem.HIT;

                };
                // todo get hover
                retboard[x][y] = s;
            }
        }
        return retboard;
    }

    BoardItem[][] displayToOpponent() {
        BoardItem[][] retboard = new BoardItem[10][10];
        for (int y = 9; y >= 0; y--) {
            for (int x = 0; x < 10; x++) {
                BoardItem s = switch (selfBoard[x][y]) {
                    case SHIP -> BoardItem.EMPTY;
                    case MISS -> BoardItem.MISS;
                    case EMPTY -> BoardItem.EMPTY;
                    case HIT -> BoardItem.HIT;

                };
                retboard[x][y] = s;
            }
        }
        return retboard;
    }

    boolean hasShips() {
        for (int y = 9; y >= 0; y--) {
            for (int x = 0; x < 10; x++) {
                if (selfBoard[x][y] == BoardItem.SHIP)
                    return true;
            }
        }
        return false;
    }

    private BoardItem boardAccess(int x, int y) {
        if (x < 0 || x > 9 || y < 0 || y > 9) {
            return BoardItem.EMPTY;
        }
        return selfBoard[x][y];
    }

    private boolean checkForShipsAround(int x, int y) {
        // return true if there's a ship around given coords
        return (boardAccess(x + 1, y + 1) != BoardItem.SHIP && boardAccess(x, y + 1) != BoardItem.SHIP
                && boardAccess(x - 1, y + 1) != BoardItem.SHIP && boardAccess(x - 1, y) != BoardItem.SHIP
                && boardAccess(x + 1, y) != BoardItem.SHIP && boardAccess(x + 1, y - 1) != BoardItem.SHIP
                && boardAccess(x, y - 1) != BoardItem.SHIP && boardAccess(x - 1, y - 1) != BoardItem.SHIP);
    }

    boolean place(Ship ship) {
        // bool value is success state
        int x = ship.x;
        int y = ship.y;
        int size = ship.size;
        Orientation or = ship.orientation;
        switch (or) {
            case UP:
                for (int dy = 0; dy < size; dy++) {
                    if (y + dy < 0 || y + dy > 9 || !checkForShipsAround(x, y + dy)) {
                        return false;
                    }
                }

                for (int dy = 0; dy < size; dy++) {
                    selfBoard[x][y + dy] = BoardItem.SHIP;
                }
                break;
            case LEFT:
                for (int dx = 0; dx < size; dx++) {
                    if (x - dx < 0 || x - dx > 10 || !checkForShipsAround(x - dx, y)) {
                        return false;
                    }
                }
                for (int dx = 0; dx < size; dx++) {
                    selfBoard[x - dx][y] = BoardItem.SHIP;
                }
                break;
            case RIGHT:
                for (int dx = 0; dx < size; dx++) {
                    if (x + dx < 0 || x + dx > 9 || !checkForShipsAround(x + dx, y)) {
                        return false;
                    }
                }
                for (int dx = 0; dx < size; dx++) {
                    selfBoard[x + dx][y] = BoardItem.SHIP;
                }
                break;
            case DOWN:

                for (int dy = 0; dy < size; dy++) {
                    if (y - dy < 0 || y - dy > 9 || !checkForShipsAround(x, y - dy)) {
                        return false;
                    }
                }
                for (int dy = 0; dy < size; dy++) {
                    selfBoard[x][y - dy] = BoardItem.SHIP;
                }
                break;
        }
        return true;
    }

    void attack(int x, int y) {
        //
        if (selfBoard[x][y] == BoardItem.EMPTY) {
            selfBoard[x][y] = BoardItem.MISS;
            return;
        }
        selfBoard[x][y] = BoardItem.HIT;

        List<TileLocation> shiptiles = new ArrayList<>();
        shiptiles.add(new TileLocation(x, y));
        // sink checks
        // DOWN
        // the "<100" is just for java static analysis
        // it cannot infer that the loop is finite if substituted
        // with "true"
        for (int dy = 1; dy < 100; dy++) {
            if (y + dy < 10 && y + dy >= 0 && x < 10 && x >= 0
                    && (selfBoard[x][y + dy] == BoardItem.MISS || selfBoard[x][y + dy] == BoardItem.EMPTY)) {
                break;
            }
            if (y + dy < 10 && y + dy >= 0 && x < 10 && x >= 0 && selfBoard[x][y + dy] == BoardItem.SHIP) {
                return;
            }
            shiptiles.add(new TileLocation(x, y + dy));
        }
        // UP
        for (int dy = 1; dy < 100; dy++) {
            if (y - dy < 10 && y - dy >= 0 && x < 10 && x >= 0
                    && (selfBoard[x][y - dy] == BoardItem.MISS || selfBoard[x][y - dy] == BoardItem.EMPTY)) {
                break;
            }
            if (y - dy < 10 && y - dy >= 0 && x < 10 && x >= 0 && selfBoard[x][y - dy] == BoardItem.SHIP) {
                return;
            }
            shiptiles.add(new TileLocation(x, y - dy));
        }
        // RIGHT
        for (int dx = 1; dx < 100; dx++) {
            if (y < 10 && y >= 0 && x + dx < 10 && x + dx >= 0
                    && (selfBoard[x + dx][y] == BoardItem.MISS || selfBoard[x + dx][y] == BoardItem.EMPTY)) {
                break;
            }
            if (y < 10 && y >= 0 && x + dx < 10 && x + dx >= 0 && selfBoard[x + dx][y] == BoardItem.SHIP) {
                return;
            }
            shiptiles.add(new TileLocation(x + dx, y));
        }
        // LEFT
        for (int dx = 1; dx < 100; dx++) {
            if (y < 10 && y >= 0 && x - dx < 10 && x - dx >= 0
                    && (selfBoard[x - dx][y] == BoardItem.MISS || selfBoard[x - dx][y] == BoardItem.EMPTY)) {
                break;
            }
            if (y < 10 && y >= 0 && x - dx < 10 && x - dx >= 0 && selfBoard[x - dx][y] == BoardItem.SHIP) {
                return;
            }
            shiptiles.add(new TileLocation(x - dx, y));
        }
        // now we know that the ship is sunk

        // fill misses around the ship
        for (TileLocation loc : shiptiles) {
            fillMissesAround(loc.x, loc.y);
        }

    }

    private void fillMissesAround(int x, int y) {
        final TileLocation[] coords = { new TileLocation(x - 1, y), new TileLocation(x + 1, y),
                new TileLocation(x - 1, y - 1), new TileLocation(x + 1, y + 1), new TileLocation(x, y + 1),
                new TileLocation(x, y - 1), new TileLocation(x + 1, y - 1), new TileLocation(x - 1, y + 1) };
        for (TileLocation p : coords) {
            if (p.x > 9 || p.x < 0 || p.y > 9 || p.y < 0) {
                continue;
            }
            BoardItem item = switch (selfBoard[p.x][p.y]) {
                case HIT -> BoardItem.HIT;
                // shhappen
                case SHIP -> BoardItem.HIT;
                case MISS -> BoardItem.MISS;
                case EMPTY -> BoardItem.MISS;
            };
            selfBoard[p.x][p.y] = item;
        }

    }
}

enum BoardItem {
    SHIP, EMPTY, MISS, HIT
}