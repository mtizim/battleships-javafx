
import java.util.Random;

public class ComputerPlayer extends Player {

    ComputerPlayer(Board playerBoard, Board opponentBoard) {
        super(playerBoard, opponentBoard);
    }

    Random r = new Random();

    @Override
    void attack() {
        // TODO attack
        while (true) {
            int x = r.nextInt(10);
            int y = r.nextInt(10);
            if (this.opponentBoard.selfBoard[x][y] == BoardItem.MISS) {
                continue;
            }
            this.opponentBoard.attack(x, y);
            Painter.repaintBottom(opponentBoard.display());
            return;
        }
    }

    @Override
    void placeShips() {
        int[] ships = { 5, 4, 3, 3, 2 };
        for (int size : ships) {
            while (!playerBoard.place(new Ship(size, Orientation.values()[r.nextInt(4)], r.nextInt(10), r.nextInt(10))))
                ;
        }
        // TODO remove on release
        Painter.repaintTop(playerBoard.display());
    }

}
