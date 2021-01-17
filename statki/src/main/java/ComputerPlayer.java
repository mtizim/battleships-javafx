
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;

public class ComputerPlayer extends Player {

    ComputerPlayer(Board playerBoard, Board opponentBoard, GraphicsContext gc) {
        super(playerBoard, opponentBoard, gc);
    }

    Random r = new Random();

    @Override
    void attack() {
        // TODO attack
        int x = r.nextInt(10);
        int y = r.nextInt(10);
        this.opponentBoard.attack(x, y);
        Painter.paintHuman(opponentBoard.display(), gc);
        return;
    }

    @Override
    void placeShips() {
        // TODO place ships in some way
        return;
    }

}
