package statki;

import java.util.Random;

public class ComputerPlayer implements Player {
    Board board;
    Random r = new Random();

    ComputerPlayer() {
        this.board = new Board();
        this.placeShips();
    }

    @Override
    public void placeShips() {
        // TODO place ships in some way
    }

    @Override
    public void playRound() {
        int x = r.nextInt(10);
        int y = r.nextInt(10);
        int hit = this.board.attack(x, y);
        if (hit == 0) {
            System.out.println("The computer missed");
        }
        if (hit == 0) {
            System.out.print("The computer hit you at x=");
            System.out.print(x);
            System.out.print(" y=");
            System.out.println(y);
        }
    }

    @Override
    public boolean hasShips() {
        // nie dodaję żadnych statków, więc na razie oczywiście nie ma statków
        return true;
        // return board.hasShips();
    }
}
