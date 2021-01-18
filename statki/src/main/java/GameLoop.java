import java.util.Collection;

public class GameLoop implements Runnable {

    Board boardOne;
    Board boardTwo;
    Player playerOne;
    Player playerTwo;

    public GameLoop() {
        this.boardOne = new Board();
        this.boardTwo = new Board();
        Painter.repaintBoards(boardOne.display(), boardTwo.displayToOpponent());
        // humanplayer has to be bottom
        // it's hardcoded but not hard to refactor
        // there's no need to do that now though
        this.playerOne = new HumanPlayer(boardOne, (BoardItem[][] b) -> Painter.repaintBottom(b));
        this.playerTwo = new ComputerPlayer(boardTwo, (BoardItem[][] b) -> Painter.repaintTop(b));
        playerOne.setOpponent(playerTwo);

        Global.topHoveredStream.subscribe((Collection<TileLocation> hovered) -> Painter.paintHoveredTop(hovered), 98);
        Global.bottomHoveredStream.subscribe((Collection<TileLocation> hovered) -> Painter.paintHoveredBottom(hovered),
                99);
    }

    @Override
    public void run() {
        playerOne.placeShips();
        playerTwo.placeShips();

        while (true) {
            playerOne.attack();

            playerTwo.attack();
            if (!playerTwo.hasShips() || !playerOne.hasShips())
                break;

        }

        Painter.repaintBottom(playerOne.playerBoard.display());
        Painter.repaintTop(playerTwo.playerBoard.display());
    }
}