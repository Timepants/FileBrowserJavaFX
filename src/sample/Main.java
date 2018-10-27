package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Main extends Application {

//    @Override
//    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Beans");
////        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.setScene(new Scene(root));
//        primaryStage.show();
//    }
//
//
//    public static void main(String[] args) {
//        launch(args);
//    }

    @Override public void start(final Stage stage) throws Exception {
        // load the scene fxml UI.
        // grabs the UI scenegraph view from the loader.
        // grabs the UI controller for the view from the loader.
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        final Parent root = loader.load();
//        final Controller controller = loader.<Controller>getController();

        // continuously refresh the TreeItems.
        // demonstrates using controller methods to manipulate the controlled UI.
//        final Timeline timeline = new Timeline(
//                new KeyFrame(
//                        Duration.seconds(3),
//                        new TreeLoadingEventHandler(controller)
//                )
//        );
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.play();

//        // close the app if the user clicks on anywhere on the window.
//        // just provides a simple way to kill the demo app.
//        root.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//            @Override public void handle(MouseEvent t) {
//                stage.hide();
//            }
//        });

        // initialize the stage.
        stage.setScene(new Scene(root));
//        stage.initStyle(StageStyle.TRANSPARENT);
//        stage.getIcons().add(new Image(getClass().getResourceAsStream("myIcon.png")));
        stage.show();
    }

//    /** small helper class for handling tree loading events. */
//    private class TreeLoadingEventHandler implements EventHandler<ActionEvent> {
//        private Controller controller;
//        private int idx = 0;
//
//        TreeLoadingEventHandler(Controller controller) {
//            this.controller = controller;
//        }
//
//        @Override public void handle(ActionEvent t) {
//            controller.loadTreeItems("Loaded " + idx, "Loaded " + (idx + 1), "Loaded " + (idx + 2));
//            idx += 3;
//        }
//    }

    public static void main(String[] args) { launch(args); }
}
