import java.util.Collection;

import javafx.scene.canvas.GraphicsContext;

public class GameLoop implements Runnable {

    GraphicsContext gc;
    Board boardOne;
    Board boardTwo;
    Player playerOne;
    Player playerTwo;;

    public GameLoop(GraphicsContext gc) {
        this.gc = gc;
        this.boardOne = new Board();
        this.boardTwo = new Board();
        Painter.paint(boardOne.display(), boardTwo.displayToOpponent(), gc);
        this.playerOne = new HumanPlayer(boardOne, boardTwo, gc);
        this.playerTwo = new ComputerPlayer(boardTwo, boardOne, gc);

        Global.topHovered.subscribe((Collection<TileLocation> hovered) -> {
            Painter.paintHoveredTop(hovered, gc);
        }, 98);
        Global.bottomHovered.subscribe((Collection<TileLocation> hovered) -> {
            Painter.paintHoveredBottom(hovered, gc);
        }, 99);
    }

    @Override
    public void run() {
        playerOne.placeShips();
        playerTwo.placeShips();
        while (true) {
            playerOne.playRound();
            if (!playerTwo.hasShips())
                break;
            playerTwo.playRound();
            if (!playerOne.hasShips())
                break;
        }
    }
}