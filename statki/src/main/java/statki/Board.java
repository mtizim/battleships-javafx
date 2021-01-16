package statki;

public class Board {

    enum BoardItem {
        SHIP, EMPTY, MISS, HIT
    }

    BoardItem[][] board = new BoardItem[10][10];

    Board() {
        // init boardu
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = BoardItem.EMPTY;
            }
        }

    }

    BoardItem[][] display() {
        BoardItem[][] retboard = new BoardItem[10][10];
        for (int y = 9; y >= 0; y--) {
            for (int x = 0; x < 10; x++) {
                BoardItem s = switch (board[x][y]) {
                    case SHIP -> BoardItem.SHIP;
                    case MISS -> BoardItem.MISS;
                    case EMPTY -> BoardItem.EMPTY;
                    case HIT -> BoardItem.HIT;

                };
                retboard[x][y] = s;
            }
        }
        return retboard;
    }

    BoardItem[][] displayToOpponent() {
        BoardItem[][] retboard = new BoardItem[10][10];
        for (int y = 9; y >= 0; y--) {
            for (int x = 0; x < 10; x++) {
                BoardItem s = switch (board[x][y]) {
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
                if (board[x][y] == BoardItem.SHIP)
                    return true;
            }
        }
        return false;
    }

    private BoardItem boardAccess(int x, int y) {
        if (x < 0 || x > 9 || y < 0 || y > 9) {
            return BoardItem.EMPTY;
        }
        return board[x][y];
    }

    private boolean checkForShipsAround(int x, int y) {
        // return true if there's a ship around given coords
        return !(boardAccess(x + 1, y + 1) != BoardItem.SHIP && boardAccess(x, y + 1) != BoardItem.SHIP
                && boardAccess(x - 1, y + 1) != BoardItem.SHIP && boardAccess(x - 1, y) != BoardItem.SHIP
                && boardAccess(x + 1, y) != BoardItem.SHIP && boardAccess(x + 1, y - 1) != BoardItem.SHIP
                && boardAccess(x, y - 1) != BoardItem.SHIP && boardAccess(x - 1, y - 1) != BoardItem.SHIP);
    }

    void place(Ship ship) throws InvalidPlacementException {
        int x = ship.x;
        int y = ship.y;
        int size = ship.size;
        Orientation or = ship.orientation;
        switch (or) {
            case UP:
                for (int dy = 0; dy < size; dy++) {
                    if (y + dy < 0 || y + dy > 9 || !checkForShipsAround(x, y + dy)) {
                        throw new InvalidPlacementException();
                    }
                }

                for (int dy = 0; dy < size; dy++) {
                    board[x][y + dy] = BoardItem.SHIP;
                }
                break;
            case LEFT:
                for (int dx = 0; dx < size; dx++) {
                    if (x - dx < 0 || x - dx > 10 || !checkForShipsAround(x - dx, y)) {
                        throw new InvalidPlacementException();
                    }
                }
                for (int dx = 0; dx < size; dx++) {
                    board[x - dx][y] = BoardItem.SHIP;
                }
                break;
            case RIGHT:
                for (int dx = 0; dx < size; dx++) {
                    if (x + dx < 0 || x + dx > 9 || !checkForShipsAround(x + dx, y)) {
                        throw new InvalidPlacementException();
                    }
                }
                for (int dx = 0; dx < size; dx++) {
                    board[x + dx][y] = BoardItem.SHIP;
                }
                break;
            case DOWN:
                for (int dy = 0; dy < size; dy++) {
                    if (y - dy < 0 || y - dy > 9 || !checkForShipsAround(x, y - dy)) {
                        throw new InvalidPlacementException();
                    }
                }
                for (int dy = 0; dy < size; dy++) {
                    board[x][y - dy] = BoardItem.SHIP;
                }
                break;
        }
    }

    int attack(int x, int y) {
        if (board[x][y] == BoardItem.EMPTY) {
            board[x][y] = BoardItem.MISS;
            return 0;
        }
        board[x][y] = BoardItem.HIT;
        int s = 1;
        while (true) {
            if (x + 1 < 10 && board[x + 1][y] == BoardItem.SHIP || x + 1 < 10 && board[x + 1][y] == BoardItem.HIT) {
                s += 1;
                x++;
            } else if (x - 1 >= 0 && board[x - 1][y] == BoardItem.SHIP
                    || x - 1 >= 0 && board[x - 1][y] == BoardItem.HIT) {
                s += 1;
                x--;
            } else if (y + 1 < 10 && board[x][y + 1] == BoardItem.SHIP
                    || y + 1 < 10 && board[x][y + 1] == BoardItem.HIT) {
                s += 1;
                y++;
            } else if (y - 1 >= 0 && board[x][y - 1] == BoardItem.SHIP
                    || y - 1 >= 0 && board[x][y - 1] == BoardItem.HIT) {
                s += 1;
                y--;
            } else {
                return s;
            }
        }
    }

}
