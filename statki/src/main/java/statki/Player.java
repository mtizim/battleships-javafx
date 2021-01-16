package statki;

import java.io.IOException;

public interface Player {
    void placeShips() throws IOException;

    void playRound();

    boolean hasShips();
}
