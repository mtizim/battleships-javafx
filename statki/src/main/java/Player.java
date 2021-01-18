import java.util.function.Consumer;

public abstract class Player {
    public Board playerBoard;
    public Player opponent;
    public Consumer<BoardItem[][]> repaintSelfFunction;
    public Consumer<BoardItem[][]> repaintOpponentFunction;

    // alway construct then set opponent
    Player(Board playerBoard, Consumer<BoardItem[][]> repaintSelfFunction) {
        this.playerBoard = playerBoard;
        this.repaintSelfFunction = repaintSelfFunction;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
        opponent.opponent = this;
    }

    void attack() {

    }

    void repaintSelf(BoardItem[][] board) {
        this.repaintSelfFunction.accept(board);
    }

    void placeShips() {

    }

    boolean hasShips() {
        return playerBoard.hasShips();
    }
}
