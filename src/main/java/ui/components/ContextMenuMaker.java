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

        MenuItem rename = new MenuItem("Rename");
        rename.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Runnable runnableTask = () -> {
                    File newFile = ContextMenuHandler.renameFile(file);
                    refreshTableView(newFile.getParentFile(), tableView);
                };
                Platform.runLater(runnableTask);
            }
        });
        MenuItem newFile = new MenuItem("New File");
        newFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Runnable runnableTask = () -> {
                    ContextMenuHandler.newFile(file);
                    refreshTableView(file.getParentFile(), tableView);
                };
                Platform.runLater(runnableTask);
            }
        });
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Runnable runnableTask = () -> {
                    File parent = file.getParentFile();
                    ContextMenuHandler.deleteFile(file);
                    refreshTableView(parent, tableView);
                };
                Platform.runLater(runnableTask);
            }
        });
        MenuItem checksumMenuItem = new MenuItem("Checksum");
        checksumMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ContextMenuHandler.showMD5Checksum(file);
            }
        });

        // Add MenuItem to ContextMenu
        contextMenu.getItems().addAll(newFile, rename, deleteMenuItem, new SeparatorMenuItem(),checksumMenuItem);

        return contextMenu;
    }


}
