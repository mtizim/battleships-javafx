
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ComputerPlayer extends Player {

    ComputerPlayer(Board playerBoard, Consumer<BoardItem[][]> repaintSelfFunction) {
        super(playerBoard, repaintSelfFunction);
    }

    // efectiveness calculations
    // public int reps = 0;

    Random r = new Random();

    private TileLocation tryAttackEmpty(TileLocation loc, BoardItem[][] opBoard) {
        // return null indicates no egilible location was found

        int x = loc.x;
        int y = loc.y;
        final TileLocation[] coords = { new TileLocation(x, y - 1), new TileLocation(x + 1, y),
                new TileLocation(x, y + 1), new TileLocation(x - 1, y) };

        // try random attack direction
        Integer[] dirsArray = { 0, 1, 2, 3 };
        List<Integer> dirs = Arrays.asList(dirsArray);
        Collections.shuffle(dirs);
        for (int i : dirs) {
            TileLocation p = coords[i];
            if (p.x >= 10 || p.x < 0 || p.y >= 10 || p.y < 0) {
                continue;
            }
            if (opBoard[p.x][p.y] == BoardItem.EMPTY) {
                return p;
            }
        }
        return null;
    }

    private TileLocation tryAttackShips(TileLocation loc, BoardItem[][] opBoard) {
        // return null indicates no egilible location was found
        int x = loc.x;
        int y = loc.y;
        int hitsAround = 0;

        Orientation knownOrientation = null;
        // up right down left
        final TileLocation[] coords = { new TileLocation(x, y - 1), new TileLocation(x + 1, y),
                new TileLocation(x, y + 1), new TileLocation(x - 1, y) };
        // find out what the orientation might be
        for (int i = 0; i < 4; i++) {
            TileLocation p = coords[i];

            if (p.x >= 10 || p.x < 0 || p.y >= 10 || p.y < 0) {
                continue;
            }
            if (opBoard[p.x][p.y] == BoardItem.HIT) {
                hitsAround++;
                // this means that we're not on the edge of the ship
                if (hitsAround > 1) {
                    return null;
                }
                knownOrientation = Orientation.values()[i];

            }
        }
        if (knownOrientation == null) {
            return null;
        }

        // up -> down , left->right and so on
        TileLocation toCheck = switch (knownOrientation) {
            case UP -> coords[2];
            case RIGHT -> coords[3];
            case DOWN -> coords[0];
            case LEFT -> coords[1];
        };

        // attack in the direction implied by the ship shape
        if (toCheck.x < 10 && toCheck.x >= 0 && toCheck.y < 10 && toCheck.y >= 0
                && opBoard[toCheck.x][toCheck.y] == BoardItem.EMPTY) {
            return toCheck;
        }

        return null;

    }

    @Override
    void attack() {
        // reps++;
        // get known hit locations

        BoardItem[][] opBoard = opponent.playerBoard.displayToOpponent();
        List<TileLocation> knownHits = new ArrayList<>();
        List<Tuple<TileLocation, Integer>> emptyPlaces = new ArrayList<>();

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (opBoard[x][y] == BoardItem.HIT) {
                    knownHits.add(new TileLocation(x, y));

                }
                // programmer time >>>>> execution time
                // it's only 1200 runs/attack anyway
                if (opBoard[x][y] == BoardItem.EMPTY) {
                    Integer adjacentEmptyScore = 0;
                    final TileLocation[] coords = { new TileLocation(x - 1, y), new TileLocation(x + 1, y),
                            new TileLocation(x - 1, y - 1), new TileLocation(x + 1, y + 1), new TileLocation(x, y + 1),
                            new TileLocation(x, y - 1), new TileLocation(x + 1, y - 1),
                            new TileLocation(x - 1, y + 1) };

                    final TileLocation[] pluscoords = { new TileLocation(x, y - 1), new TileLocation(x + 1, y),
                            new TileLocation(x, y + 1), new TileLocation(x - 1, y) };

                    for (TileLocation l : coords) {
                        if (!(l.x < 10 && l.x >= 0 && l.y < 10 && l.y >= 0)) {
                            adjacentEmptyScore++;
                            continue;
                        }
                        if (opBoard[l.x][l.y] == BoardItem.EMPTY) {
                            adjacentEmptyScore++;
                        }
                    }
                    for (TileLocation l : pluscoords) {
                        if (!(l.x < 10 && l.x >= 0 && l.y < 10 && l.y >= 0)) {
                            continue;
                        }
                        if (opBoard[l.x][l.y] == BoardItem.EMPTY) {
                            adjacentEmptyScore++;
                        }
                    }
                    emptyPlaces.add(new Tuple<>(new TileLocation(x, y), adjacentEmptyScore));
                }
            }
        }
        // unpredictability
        Collections.shuffle(knownHits);

        // try to attack ships around them
        for (TileLocation hitloc : knownHits) {

            TileLocation ret = tryAttackShips(hitloc, opBoard);
            if (ret != null) {
                this.opponent.playerBoard.attack(ret.x, ret.y);
                opponent.repaintSelf(opponent.playerBoard.display());
                return;
            }
        }
        // try to attack empty places near known ships
        for (TileLocation hitloc : knownHits) {

            TileLocation ret = tryAttackEmpty(hitloc, opBoard);
            if (ret != null) {
                this.opponent.playerBoard.attack(ret.x, ret.y);
                opponent.repaintSelf(opponent.playerBoard.display());
                return;
            }
        }

        // get max empty heuristic score
        int max = 0;
        for (Tuple<TileLocation, Integer> t : emptyPlaces) {
            if (t.b > max) {
                max = t.b;
            }
        }

        // at random chance, just attack a random empty place
        if ((max / 4) * 55 - 15 > r.nextInt(100)) {
            int chosenidx = r.nextInt(emptyPlaces.size());
            TileLocation chosen = emptyPlaces.get(chosenidx).a;
            this.opponent.playerBoard.attack(chosen.x, chosen.y);
            opponent.repaintSelf(opponent.playerBoard.display());
            return;
        }
        ;

        // if couldnt do any of that - attack at random highest empty place

        final int m = max;
        List<TileLocation> possible = emptyPlaces.stream().filter((Tuple<TileLocation, Integer> t) -> (t.b == m))
                .map((Tuple<TileLocation, Integer> t) -> (t.a)).collect(Collectors.toList());

        int chosenidx = r.nextInt(possible.size());
        TileLocation chosen = possible.get(chosenidx);

        this.opponent.playerBoard.attack(chosen.x, chosen.y);
        opponent.repaintSelf(opponent.playerBoard.display());
        return;
    }

    @Override
    void placeShips() {
        int[] ships = { 5, 4, 3, 3, 2 };
        for (int size : ships) {
            while (!playerBoard.place(new Ship(size, Orientation.values()[r.nextInt(4)], r.nextInt(10), r.nextInt(10))))
                ;
        }
    }

}
