import java.util.Collection;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Painter {
    // graphic constant dump
    // boardpad
    static final double BPAD = 20;
    // divider thickness
    static final double STHI = 10;
    // tile size
    static final double TSIZE = 31;
    // tile spacing
    static final double TSPACE = 4.5;
    // winning indicator padding
    static final double WONSPACE = 50;
    // winning indicator size
    static final double WONS = 40;
    // place/attack indicator padding
    static final double IPADY = 800 / 4 - STHI / 2 - TSIZE / 2;
    static final double IPADX = 100;
    // indicators panel width
    static final double ISIZE = 180;

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
        gc.fillRect(0 + ISIZE, 0, 800, 400);

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 400 - STHI / 2, 800, STHI / 2);
    }

    private static void repaintBackgroundBottom() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0 + ISIZE, 400, 800, 400);

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 400, 800, STHI / 2);
    }

    public static void repaintIndicatorsTop() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, ISIZE, 400 - STHI / 2);
    }

    public static void repaintIndicatorsBottom() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 400 + STHI / 2, ISIZE, 400);
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
        double dy = BPAD + 800 / 2;
        repaintBoards();
        for (TileLocation h : hovered) {
            // human player can sometimes hover multiple tiles
            if (dy + h.y * (TSIZE + TSPACE) < 400 + STHI / 2) {
                return;
            }
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

    public static void paintWonTop(boolean wonOrLost) {
        double x = WONSPACE;
        double y = 800 / 2 - WONSPACE - STHI / 2;

        gc.setFill(wonOrLost ? Color.GOLD : Color.RED);
        // tri
        double[] xs = { x, x + WONS, x + WONS / 2 };
        double[] ys = { y, y, y - WONS * 0.7 };

        gc.fillPolygon(xs, ys, 3);
    }

    public static void paintWonBottom(boolean wonOrLost) {
        double x = WONSPACE;
        double y = 800 / 2 + WONSPACE + STHI / 2;

        gc.setFill(wonOrLost ? Color.GOLD : Color.RED);
        double[] xs = { x, x + WONS, x + WONS / 2 };
        double[] ys = { y, y, y + WONS * 0.7 };
        gc.fillPolygon(xs, ys, 3);

    }

    public static void paintPlaceBottom() {
        repaintIndicatorsBottom();
        double x = IPADX;
        double y = 800 / 2 + IPADY;
        paintBoardItem(x, y, BoardItem.SHIP);
        x = x + TSIZE + TSPACE;
        // tri
        double[] xs = { x, x, x + TSIZE * 0.7 };
        double[] ys = { y, y + TSIZE, y + TSIZE / 2 };
        gc.setFill(Color.WHITE);
        gc.fillPolygon(xs, ys, 3);
    }

    public static void paintAttackBottom() {
        repaintIndicatorsBottom();
        double x = IPADX;
        double y = 800 / 2 + IPADY;
        paintBoardItem(x, y, BoardItem.HIT);
        y = y - TSPACE;
        // tri
        double[] xs = { x, x + TSIZE, x + TSIZE / 2 };
        double[] ys = { y, y, y - TSIZE * 0.7 };
        gc.setFill(Color.WHITE);
        gc.fillPolygon(xs, ys, 3);
    }

}