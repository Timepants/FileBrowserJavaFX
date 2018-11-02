package ui.components;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.robot.Robot;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

public class FXDialogue {
    private static boolean isDarkMode = false;
    public static void setIsDarkMode(boolean isDarkMode){
        FXDialogue.isDarkMode = isDarkMode;
    }

    public static void showInformation(String header, String message) {
        showInformation("Information", header, message);
    }

    public static void addStyle(Alert alert){
        addStyle(alert.getDialogPane());

    }
    public static void addStyle(DialogPane dialogPane){
        try {
            if(isDarkMode){
                dialogPane.getStylesheets().add(new File("src/main/java/ui/resources/css/material-fx-v0_3.css").toURI().toURL().toString());
                dialogPane.getStyleClass().add("alert-box");
            } else {
                dialogPane.getStylesheets().add(new File("src/main/java/ui/resources/css/material-fx-v0_3-copy.css").toURI().toURL().toString());
                dialogPane.getStyleClass().add("alert-box");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    public static void addStyle(Dialog dialog){
        addStyle(dialog.getDialogPane());


    }

    public static void showInformation(String title, String header, String message) {
        Runnable runnableTask = () -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(message);
            addStyle(alert);
            alert.showAndWait();
        };
        Platform.runLater(runnableTask);
    }

    public static void showWarning(String title, String message) {
        Runnable runnableTask = () -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Warning");
            alert.setHeaderText(title);
            alert.setContentText(message);
            addStyle(alert);
            alert.showAndWait();
        };
        Platform.runLater(runnableTask);
    }

    public static void showError(String title, String message) {
        Runnable runnableTask = () -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Error");
            alert.setHeaderText(title);
            alert.setContentText(message);
            addStyle(alert);
            alert.showAndWait();
        };
        Platform.runLater(runnableTask);
    }

    public static void showException(String title, String message, Exception exception) {
        Runnable runnableTask = () -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Exception");
            alert.setHeaderText(title);
            alert.setContentText(message);

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exception.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("Details:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            alert.getDialogPane().setExpandableContent(expContent);
            addStyle(alert);
            alert.showAndWait();
        };
        Platform.runLater(runnableTask);
    }

    public static final String YES = "Yes";
    public static final String NO = "No";
    public static final String OK = "OK";
    public static final String CANCEL = "Cancel";

    public static String showConfirm(String title, String header, String message, String... options) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        //To make enter key press the actual focused button, not the first one. Just like pressing "space".
        alert.getDialogPane().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                event.consume();
                try {
                    Robot r = new Robot();
                    r.keyPress(KeyCode.SPACE);
                    r.keyRelease(KeyCode.SPACE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (options == null || options.length == 0) {
            options = new String[]{OK, CANCEL};
        }

        List<ButtonType> buttons = new ArrayList<>();
        for (String option : options) {
            buttons.add(new ButtonType(option));
        }
        addStyle(alert);
        alert.getButtonTypes().setAll(buttons);

        Optional<ButtonType> result = alert.showAndWait();
        if (!result.isPresent()) {
            return CANCEL;
        } else {
            return result.get().getText();
        }
    }

    public static String showTextInput(String header, String message, String defaultValue) {
        return showTextInput("Input", header, message, defaultValue);
    }
    public static String showTextInput(String title, String header, String message, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(message);
        addStyle(dialog);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }

}
