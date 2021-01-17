import java.util.Collection;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Painter {

    // DO NOT TOUCH THIS
    static final double BPAD = 20;
    static final double STHI = 10;
    static final double TSIZE = 31;
    static final double TSPACE = 4.5;

    private static GraphicsContext gc;

    public static void setGC(GraphicsContext gc) {
        Painter.gc = gc;
    }

    private static BoardItem[][] cachedAiBoard;
    private static BoardItem[][] cachedHumanBoard;

    public static void repaintBoards(BoardItem[][] humanBoard, BoardItem[][] aiBoard) {
        repaintBottom(humanBoard);
        repaintTop(aiBoard);

    }

    public static void repaintBottom(BoardItem[][] humanBoard) {
        Painter.repaintBackgroundBottom();
        Painter.cachedHumanBoard = humanBoard;
        paintBoard(BPAD + 800 / 2 - ((TSIZE + STHI) * 10 - STHI) / 2, 400 + BPAD, humanBoard);
    }

    public static void repaintBottom() {
        Painter.repaintBackgroundBottom();
        paintBoard(BPAD + 800 / 2 - ((TSIZE + STHI) * 10 - STHI) / 2, 400 + BPAD, cachedHumanBoard);
    }

    public static void repaintTop(BoardItem[][] aiBoard) {
        Painter.cachedAiBoard = aiBoard;
        Painter.repaintBackgroundTop();
        paintBoard(BPAD + 800 / 2 - ((TSIZE + STHI) * 10 - STHI) / 2, BPAD, aiBoard);
    }

    public static void repaintTop() {
        Painter.repaintBackgroundTop();
        paintBoard(BPAD + 800 / 2 - ((TSIZE + STHI) * 10 - STHI) / 2, BPAD, cachedAiBoard);
    }

    public static void repaintBoards() {
        repaintBackgroundTop();
        repaintTop();
        repaintBottom();
    }

    private static void repaintBackgroundTop() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 800, 400);

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 400 - STHI / 2, 800, STHI / 2);
    }

    private static void repaintBackgroundBottom() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 400, 800, 400);

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 400, 800, STHI / 2);
    }

    private static void paintBoard(double dx, double dy, BoardItem[][] board) {

        for (int y = 9; y >= 0; y--) {
            for (int x = 0; x < 10; x++) {
                paintBoardItem(dx + x * (TSIZE + TSPACE), dy + y * (TSIZE + TSPACE), board[x][y]);
            }
        }
    }

    private static void paintBoardItem(double dx, double dy, BoardItem tile) {

        Color c = switch (tile) {
            case SHIP -> Color.GREEN.brighter().brighter();
            case MISS -> Color.GRAY.brighter();
            case EMPTY -> Color.WHITE;
            case HIT -> Color.RED;
        };

        gc.setFill(c);
        gc.fillRect(dx, dy, TSIZE, TSIZE);
    }

    private static void paintHovered(double dx, double dy) {
        gc.setFill(Color.rgb(0, 0, 0, 0.3));
        gc.fillRect(dx, dy, TSIZE, TSIZE);
    }

    public static void paintHoveredBottom(Collection<TileLocation> hovered) {
        double dx = BPAD + 800 / 2 - ((TSIZE + STHI) * 10 - STHI) / 2;
        double dy = BPAD + 400;
        repaintBoards();
        for (TileLocation h : hovered) {
            paintHovered(dx + h.x * (TSIZE + TSPACE), dy + h.y * (TSIZE + TSPACE));
        }
    }

    public static void paintHoveredTop(Collection<TileLocation> hovered) {
        double dx = BPAD + 800 / 2 - ((TSIZE + STHI) * 10 - STHI) / 2;
        double dy = BPAD;
        repaintBoards();
        for (TileLocation h : hovered) {
            paintHovered(dx + h.x * (TSIZE + TSPACE), dy + h.y * (TSIZE + TSPACE));
        }
    }
}