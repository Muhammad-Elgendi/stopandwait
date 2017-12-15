package App;

import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Packet {

    public static VBox labelsContainer;
    public static GridPane packetsContainer;
    public static Pane packetWithWin;
    public static Pane slidingWindowPacketContainer;
    public Pane container;
    public PathTransition pt;
    public Rectangle rectangle;
    public Text text;
    public static int slidingFactor =-1;
    public  Rectangle window;

    public Packet(int i,int count) {

        labelsContainer = Main.labelsContainer;
        packetsContainer = Main.packetsContainer;
        packetWithWin=Main.packetWithWin;
        slidingWindowPacketContainer=Main.slidingWindowPacketContainer;
        window = new Rectangle();
        window.setFill(Color.TRANSPARENT);
        window.setStroke(Color.BLACK);
        window.setY(535);
        window.setHeight(30);
        window.setWidth((25*count)+((count-1)*10)+count);
        container = new Pane();
        rectangle = new Rectangle(0, 0, 25, 25);
        rectangle.setFill(Color.ORANGE);
        Line line = new Line();
        line.setStartX(0.0f);
        line.setStartY(50);
        line.setEndX(0.0f);
        line.setEndY(550.0f);
        line.setStroke(Color.TRANSPARENT);

        pt = new PathTransition();
        pt.setDuration(Duration.millis(6000));
        pt.setPath(line);
        text = new Text("" + i);
        StackPane stack = new StackPane();
        stack.getChildren().addAll(rectangle, text);
        pt.setNode(stack);

//        pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
//        pt.play();
        rectangle.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                pt.stop();
                labelsContainer.getChildren().add(new Label("--------Packet-----------X " + i));
                pt.setPath(line);
                KeyFrame mainkeyFrame = new KeyFrame(Duration.seconds(6), ev -> {
                    pt.play();
                });
                Timeline timelineTimer = new Timeline(mainkeyFrame);
                timelineTimer.setCycleCount(1);
                timelineTimer.play();
            }
        });
        pt.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                labelsContainer.getChildren().add(new Label("------------------> Packet " + i));
                slidingFactor=(slidingFactor++)+1;
                Acknowledgement acknowledgement =new Acknowledgement(i,count);
                packetsContainer.add(acknowledgement.getContainer(), i, 0);
                acknowledgement.getPt().play();
                window.setX(-15+(slidingFactor*35));
                slidingWindowPacketContainer.getChildren().clear();
                slidingWindowPacketContainer.getChildren().add(window);

            }
        });
//        rectangle.setOnMouseReleased(e -> pt.play());
//        pt.setNode(text);
        container.getChildren().addAll(line, stack);
    }

    public Pane getContainer() {
        return container;
    }

    public PathTransition getPt() {
        return pt;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Text getText() {
        return text;
    }

}
