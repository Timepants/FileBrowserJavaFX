package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;

public class Main extends Application {
    String lightTheme;
    String darkTheme;
    {
        try {
            lightTheme = new File("src/main/java/ui/resources/css/material-fx-v0_3-copy.css").toURI().toURL().toString();
            darkTheme = new File("src/main/java/ui/resources/css/material-fx-v0_3.css").toURI().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override public void start(final Stage stage) throws Exception {
        FXMLLoader loader =  new FXMLLoader(new File("src/main/java/ui/mainUI.fxml").toURI().toURL());
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setGetHostController(getHostServices());
        controller.setStyleSheets(root.getStylesheets(), lightTheme, darkTheme);
        root.getStylesheets().add(lightTheme);
//        root.getStylesheets().add("src/main/java/ui/resources/css/material-fx-v0_3.css");

        stage.setScene(new Scene(root));
        stage.setTitle("Bowser Browser");
        stage.setMinWidth(660);
//        stage.initStyle(StageStyle.TRANSPARENT);
//        stage.getIcons().add(new Image(getClass().getResourceAsStream("myIcon.png")));
        stage.show();
    }

    public static void main(String[] args) { launch(args); }
}
