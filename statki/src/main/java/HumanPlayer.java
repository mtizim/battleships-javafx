import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class HumanPlayer extends Player {

    HumanPlayer(Board playerBoard, Board opponentBoard) {
        super(playerBoard, opponentBoard);
    }

    @Override
    void attack() {

        CompletableFuture<Void> f = new CompletableFuture<>();
        Global.topClickedStream.subscribe((TileLocation location) -> {
            if (this.opponentBoard.selfBoard[location.x][location.y] == BoardItem.MISS) {
                return;
            }
            this.opponentBoard.attack(location.x, location.y);

            Global.topClickedStream.unsubscribe(10);
            Painter.repaintTop(opponentBoard.displayToOpponent());
            f.complete(null);
        }, 10);
        try {
            f.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    void placeShips() {
        Global.topHoveredStream.pauseSubscription(98);
        // weird concurrency hacks but it's just ints and enums anyway
        // proper thread communication would be harder and bring no benefits
        Orientation[] currentOrientation = { Orientation.DOWN };

        Global.mwheelStream.subscribe((MWHEEL wheel) -> {
            if (wheel == MWHEEL.UP) {
                currentOrientation[0] = Orientation.values()[(currentOrientation[0].ordinal() + 1) % 4];
            }
            if (wheel == MWHEEL.DOWN) {
                int newi = (currentOrientation[0].ordinal() - 1) % 4;
                newi = newi == -1 ? 3 : newi;
                currentOrientation[0] = Orientation.values()[newi];
            }
            Global.bottomHoveredStream.reapply();
        }, 60);
        int[][] ships = { { 5 }, { 4 }, { 3 }, { 3 }, { 2 } };
        for (int[] size : ships) {
            Global.bottomHoveredStream.setTransform((Collection<TileLocation> singleHovered) -> {
                TileLocation s = singleHovered.iterator().next();
                Collection<TileLocation> r = new ArrayList<>();
                switch (currentOrientation[0]) {
                    case DOWN:
                        for (int y = 0; y < size[0]; y++) {
                            r.add(new TileLocation(s.x, s.y - y));
                        }
                        break;
                    case UP:
                        for (int y = 0; y < size[0]; y++) {
                            r.add(new TileLocation(s.x, s.y + y));
                        }
                        break;
                    case LEFT:
                        for (int x = 0; x < size[0]; x++) {
                            r.add(new TileLocation(s.x - x, s.y));
                        }
                        break;
                    case RIGHT:
                        for (int x = 0; x < size[0]; x++) {
                            r.add(new TileLocation(s.x + x, s.y));
                        }
                        break;
                }
                return r;
            });
            CompletableFuture<Void> f = new CompletableFuture<>();
            Global.bottomClickedStream.subscribe((TileLocation location) -> {
                boolean placed = this.playerBoard
                        .place(new Ship(size[0], currentOrientation[0], location.x, location.y));
                if (!placed) {
                    return;
                }
                Painter.repaintBottom(playerBoard.display());
                f.complete(null);
            }, 11);
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        Global.topHoveredStream.resumeSubscription(98);
        Global.mwheelStream.unsubscribe(60);
        Global.bottomHoveredStream.unsubscribe(99);
        Global.bottomHoveredStream.setTransform((Collection<TileLocation> singleHovered) -> singleHovered);
    }

}
