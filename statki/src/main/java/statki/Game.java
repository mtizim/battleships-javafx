package statki;

import java.io.IOException;

public class Game {
    public static void main(String[] args) throws IOException {

        HumanPlayer playerOne = new HumanPlayer();
        Player playerTwo = new ComputerPlayer();

        playerOne.placeShips();
        playerTwo.placeShips();
        playerOne.board.display();

        while (true) {
            playerOne.playRound();
            if (!playerTwo.hasShips())
                break;
            playerTwo.playRound();
            if (!playerOne.hasShips())
                break;
        }
        System.out.println("The game's ended");
    }
}
