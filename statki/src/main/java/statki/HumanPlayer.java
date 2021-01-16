package statki;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HumanPlayer implements Player {

    public Board board;

    HumanPlayer() {
        this.board = new Board();
    }

    void attack() {
        try {

            // Enter data using BufferReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // Reading data using readLine
            System.out.println("Attack!(format: xy):");
            String line = reader.readLine();
            int x = Integer.parseInt(String.valueOf(line.charAt(0)));
            int y = "ABCDEFGHIJ".indexOf(line.charAt(1));
            int info = this.board.attack(x, y);
            if (info == 0) {
                System.out.println("You missed");
            } else {
                System.out.print("You hit a ");
                String type = new String[] { "Carrier", "Battleship", "Destroyer", "Patrol Boat" }[info];
                System.out.println(type);
                System.out.print("At x=");
                System.out.print(x);
                System.out.print(" y=");
                System.out.println(y);
            }
        } catch (Exception e) {
            System.out.println("Wrong input");
        }
    }

    @Override
    public void placeShips() {
        System.out.println("Place a Carrier/Battleship/Destroyer/PatrolBoat "
                + "at x,y, orientation with ShipName Orientation x y");
        String[] line = System.console().readLine().split(" ");
        String name = line[0];
        String orientation = line[1];
        int x = Integer.parseInt(line[2]);
        int y = Integer.parseInt(line[3]);
        try {
            this.board.place(new Ship(name, orientation, x, y));
        } catch (InvalidPlacementException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playRound() {
        this.attack();
        board.display();
    }

    @Override
    public boolean hasShips() {
        return board.hasShips();
    }
}
