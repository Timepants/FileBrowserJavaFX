package ui.components;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableView;

import java.io.File;

import static ui.components.TableViewHandler.refreshTableView;

public class ContextMenuMaker {
    public static ContextMenu getFileMenu(File file, TableView tableView) {
        // Create ContextMenu
        ContextMenu contextMenu = new ContextMenu();

        MenuItem item1 = new MenuItem("Rename");
        item1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Runnable runnableTask = () -> {
                    System.out.println("rename " + file.getName());
                    File newFile = ContextMenuHandler.renameFile(file);
                    System.out.println("newName " + newFile.getAbsolutePath());
                    refreshTableView(newFile.getParentFile(), tableView);
                };
                Platform.runLater(runnableTask);
            }
        });
        MenuItem item2 = new MenuItem("Delete");
        item2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("delete " + file.getName());
            }
        });
        MenuItem item3 = new MenuItem("Checksum");
        item3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ContextMenuHandler.showMD5Checksum(file);
            }
        });

        // Add MenuItem to ContextMenu
        contextMenu.getItems().addAll(item1, item2, new SeparatorMenuItem(),item3);
        
        return contextMenu;
    }


}
