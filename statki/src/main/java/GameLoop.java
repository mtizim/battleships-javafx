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

        Global.topHoveredStream.subscribe((Collection<TileLocation> hovered) -> Painter.paintHoveredTop(hovered), 1);
        Global.bottomHoveredStream.subscribe((Collection<TileLocation> hovered) -> Painter.paintHoveredBottom(hovered),
                1);
    }

    @Override
    public void run() {
        Painter.repaintIndicatorsTop();
        Painter.repaintIndicatorsBottom();
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
        Painter.repaintIndicatorsBottom();

        boolean pOneWon = !playerTwo.hasShips();
        boolean pTwoWon = !playerOne.hasShips();
        if (pOneWon && pTwoWon) {
            pOneWon = false;
            pTwoWon = false;
        }
        // if both won then it's a draw - so both are displayed as
        // losing
        Painter.paintWonBottom(pOneWon);
        Painter.paintWonTop(pTwoWon);

        // make game restartable

        Global.topClickedStream.resumeSubscription(10);
        Global.bottomClickedStream.resumeSubscription(10);

    }
}