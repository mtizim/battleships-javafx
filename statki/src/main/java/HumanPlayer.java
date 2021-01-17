import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javafx.scene.canvas.GraphicsContext;

public class HumanPlayer extends Player {

    HumanPlayer(Board playerBoard, Board opponentBoard, GraphicsContext gc) {
        super(playerBoard, opponentBoard, gc);
    }

    @Override
    void attack() {

        CompletableFuture<Void> f = new CompletableFuture<>();
        Global.topTileStream.subscribe((TileLocation location) -> {
            this.opponentBoard.attack(location.x, location.y);

            Global.topTileStream.unsubscribe(10);
            Painter.paintAI(opponentBoard.displayToOpponent(), gc);
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
        // weird hack but better than copy paster
        for (int[] size = { 1 }; size[0] <= 5; size[0] = size[0] + 1) {
            Global.topHovered.setTransform((Collection<TileLocation> singleHovered) -> {
                TileLocation s = singleHovered.iterator().next();
                Collection<TileLocation> r = new ArrayList<>();
                Orientation o = Orientation.DOWN;
                switch (o) {
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
                System.out.println(r);
                return r;
            });
            CompletableFuture<Void> f = new CompletableFuture<>();
            Global.bottomTileStream.subscribe((TileLocation location) -> {
                boolean placed = this.playerBoard.place(new Ship(size[0], Orientation.DOWN, location.x, location.y));
                if (!placed) {
                    return;
                }
                Painter.paintHuman(playerBoard.display(), gc);
                f.complete(null);
            }, 11);
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        Global.bottomHovered.setTransform((Collection<TileLocation> singleHovered) -> singleHovered);
    }

}
