
public abstract class Player {
    public Board playerBoard;
    public Board opponentBoard;

    Player(Board playerBoard, Board opponentBoard) {
        this.playerBoard = playerBoard;
        this.opponentBoard = opponentBoard;
    }

    void attack() {

    }

    void placeShips() {

    }

    void playRound() {
        this.attack();
    }

    boolean hasShips() {
        return playerBoard.hasShips();
    }
}
