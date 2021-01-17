import java.util.Collection;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Painter {

    // DO NOT TOUCH THIS
    static final double BPAD = 20;
    static final double STHI = 10;
    static final double TSIZE = 31;
    static final double TSPACE = 4.5;

    static private BoardItem[][] cachedAiBoard;
    static private BoardItem[][] cachedHumanBoard;

    public static void paint(BoardItem[][] humanBoard, BoardItem[][] aiBoard, GraphicsContext gc) {
        Painter.cachedAiBoard = aiBoard;
        Painter.cachedHumanBoard = humanBoard;

        paintBackground(gc);
        paintBoard(BPAD + 800 / 2 - ((TSIZE + STHI) * 10 - STHI) / 2, 400 + BPAD, humanBoard, gc);
        paintBoard(BPAD + 800 / 2 - ((TSIZE + STHI) * 10 - STHI) / 2, BPAD, aiBoard, gc);
    }

    public static void paint(GraphicsContext gc) {
        paintBackground(gc);
        paintBoard(BPAD + 800 / 2 - ((TSIZE + STHI) * 10 - STHI) / 2, 400 + BPAD, cachedHumanBoard, gc);
        paintBoard(BPAD + 800 / 2 - ((TSIZE + STHI) * 10 - STHI) / 2, BPAD, cachedAiBoard, gc);
    }

    public static void paintHuman(BoardItem[][] humanBoard, GraphicsContext gc) {
        Painter.cachedHumanBoard = humanBoard;

        paintBackground(gc);
        paintBoard(BPAD + 800 / 2 - ((TSIZE + STHI) * 10 - STHI) / 2, 400 + BPAD, humanBoard, gc);
        paintBoard(BPAD + 800 / 2 - ((TSIZE + STHI) * 10 - STHI) / 2, BPAD, cachedAiBoard, gc);
    }

    public static void paintAI(BoardItem[][] aiBoard, GraphicsContext gc) {
        Painter.cachedAiBoard = aiBoard;

        paintBackground(gc);
        paintBoard(BPAD + 800 / 2 - ((TSIZE + STHI) * 10 - STHI) / 2, 400 + BPAD, cachedHumanBoard, gc);
        paintBoard(BPAD + 800 / 2 - ((TSIZE + STHI) * 10 - STHI) / 2, BPAD, aiBoard, gc);
    }

    private static void paintBackground(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 800, 800);

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 400 - STHI, 800, STHI);
    }

    private static void paintBoard(double dx, double dy, BoardItem[][] board, GraphicsContext gc) {

        for (int y = 9; y >= 0; y--) {
            for (int x = 0; x < 10; x++) {
                paintBoardItem(dx + x * (TSIZE + TSPACE), dy + y * (TSIZE + TSPACE), board[x][y], gc);
            }
        }
    }

    private static void paintBoardItem(double dx, double dy, BoardItem tile, GraphicsContext gc) {

        Color c = switch (tile) {
            case SHIP -> Color.GREEN.brighter().brighter();
            case MISS -> Color.GRAY.brighter();
            case EMPTY -> Color.WHITE;
            case HIT -> Color.RED;
        };
        if (false) {
            c = c.darker();
        }

        gc.setFill(c);
        gc.fillRect(dx, dy, TSIZE, TSIZE);
    }

    private static void paintHovered(double dx, double dy, GraphicsContext gc) {
        gc.setFill(Color.rgb(0, 0, 0, 0.3));
        gc.fillRect(dx, dy, TSIZE, TSIZE);
    }

    public static void paintHoveredBottom(Collection<TileLocation> hovered, GraphicsContext gc) {
        double dx = BPAD + 800 / 2 - ((TSIZE + STHI) * 10 - STHI) / 2;
        double dy = BPAD;
        paint(gc);
        for (TileLocation h : hovered) {
            paintHovered(dx + h.x * (TSIZE + TSPACE), dy + h.y * (TSIZE + TSPACE), gc);
        }
    }

    public static void paintHoveredTop(Collection<TileLocation> hovered, GraphicsContext gc) {
        double dx = BPAD + 800 / 2 - ((TSIZE + STHI) * 10 - STHI) / 2;
        double dy = 400 + BPAD;
        paint(gc);
        for (TileLocation h : hovered) {
            paintHovered(dx + h.x * (TSIZE + TSPACE), dy + h.y * (TSIZE + TSPACE), gc);
        }
    }
}