
import java.util.ArrayList;
import java.util.Collection;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ShipGame extends Application {

    GameLoop loop;
    GraphicsContext gc;

    public static void main(String[] args) {
        // player boards

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        StackPane root = new StackPane();
        Canvas canvas = new Canvas(800, 800);
        this.gc = canvas.getGraphicsContext2D();
        Painter.setGC(gc);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        stage.setTitle("Battleships");
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.setScene(scene);
        stage.show();
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {

                double x = e.getX();
                double y = e.getY();
                Listenable<TileLocation> s;
                // Heavy relying on constants from Painter.java
                // Those wil never change though
                if (y > 400) {
                    s = Global.bottomClickedStream;
                    y = y - 400 - 20;
                } else {
                    y = y - 20;
                    s = Global.topClickedStream;
                }
                x = (x - 400 + 5 * (31) + 4.5 / 2 + 5 * 4.5);
                if (x > (10 * 31 + 9 * 4.5) || x < 0 || y < 0 || y > (10 * 31 + 9 * 4.5)) {
                    return;
                }
                int tx = (int) (x / (31 + 4.5));
                int ty = (int) (y / (31 + 4.5));
                s.emit(new TileLocation(tx, ty));
            }
        });

        canvas.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() == 0) {
                    return;
                }
                boolean up = event.getDeltaY() > 0;
                Global.mwheelStream.emit(up ? MWHEEL.UP : MWHEEL.DOWN);
            }
        });

        canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {

                double x = e.getX();
                double y = e.getY();
                Listenable<Collection<TileLocation>> s;
                // Heavy relying on constants from Painter.java
                // Those wil never change though
                if (y > 400) {
                    s = Global.bottomHoveredStream;

                    y = y - 400 - 20;
                } else {
                    s = Global.topHoveredStream;
                    y = y - 20;
                }
                x = (x - 400 + 5 * (31) + 4.5 / 2 + 5 * 4.5);
                if (x > (10 * 31 + 9 * 4.5) || x < 0 || y < 0 || y > (10 * 31 + 9 * 4.5)) {
                    return;
                }
                int tx = (int) (x / (31 + 4.5));
                int ty = (int) (y / (31 + 4.5));
                Collection<TileLocation> r = new ArrayList<>();
                r.add(new TileLocation(tx, ty));
                s.emit(r);
            }
        });

        restart();
        (new Thread(loop)).start();

    }

    void restart() {
        loop = new GameLoop();
    }
}
