import java.util.Collection;

public class GameLoop implements Runnable {

    Board boardOne;
    Board boardTwo;
    Player playerOne;
    Player playerTwo;;

    public GameLoop() {
        this.boardOne = new Board();
        this.boardTwo = new Board();
        Painter.repaintBoards(boardOne.display(), boardTwo.displayToOpponent());
        this.playerOne = new HumanPlayer(boardOne, boardTwo);
        this.playerTwo = new ComputerPlayer(boardTwo, boardOne);

        Global.topHoveredStream.subscribe((Collection<TileLocation> hovered) -> {
            Painter.paintHoveredTop(hovered);
        }, 98);
        Global.bottomHoveredStream.subscribe((Collection<TileLocation> hovered) -> {
            Painter.paintHoveredBottom(hovered);
        }, 99);
    }

    @Override
    public void run() {
        playerOne.placeShips();
        playerTwo.placeShips();

        while (true) {
            playerOne.playRound();

            playerTwo.playRound();
            if (!playerTwo.hasShips() || !playerOne.hasShips())
                break;
        }
    }
}