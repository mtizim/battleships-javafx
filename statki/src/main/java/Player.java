import javafx.scene.canvas.GraphicsContext;

public abstract class Player {
    public Board playerBoard;
    public Board opponentBoard;
    public GraphicsContext gc;

    Player(Board playerBoard, Board opponentBoard, GraphicsContext gc) {
        this.playerBoard = playerBoard;
        this.opponentBoard = opponentBoard;
        this.gc = gc;
    }

    void attack() {

    }

    void placeShips() {

    }

    void playRound() {
        this.attack();
        playerBoard.display();
    }

    boolean hasShips() {
        return playerBoard.hasShips();
    }
}
