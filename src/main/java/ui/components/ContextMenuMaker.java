package ui.components;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;

import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import javax.sound.sampled.Clip;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static ui.components.TableViewHandler.refreshTableView;

public class ContextMenuMaker {
    public static ContextMenu getFileMenu(File file, TableView tableView, boolean showHidden) {
        // Create ContextMenu
        ContextMenu contextMenu = new ContextMenu();

        MenuItem rename = new MenuItem("Rename");
        rename.setOnAction(getRenameEvent(file, tableView, showHidden));

        MenuItem newFile = new MenuItem("New File");
        newFile.setOnAction(getNewFileEvent(file.getParentFile(), tableView, showHidden));

        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(getDeleteEvent(file, tableView, showHidden));

        MenuItem checksumMenuItem = new MenuItem("MD5 Checksum");
        checksumMenuItem.setOnAction(getChecksumEvent(file, tableView));

        MenuItem copy = new MenuItem("Copy");
        copy.setOnAction(getCopyEvent(file));

        MenuItem paste = new MenuItem("Paste");
        paste.setOnAction(getPasteEvent(file.getParentFile(), tableView, showHidden));

        System.out.println(Clipboard.getSystemClipboard().hasFiles());
        if (!Clipboard.getSystemClipboard().hasFiles()){
            paste.setDisable(true);
        }
        // Add MenuItem to ContextMenu
        contextMenu.getItems().addAll(newFile, rename, copy, paste, deleteMenuItem, new SeparatorMenuItem(),checksumMenuItem);

        return contextMenu;
    }
    public static ContextMenu getTableViewMenu(File file, TableView tableView, boolean showHidden) {
        // Create ContextMenu
        ContextMenu contextMenu = new ContextMenu();

        MenuItem newFile = new MenuItem("New File");
        newFile.setOnAction(getNewFileEvent(file, tableView, showHidden));

        MenuItem paste = new MenuItem("Paste");
        paste.setOnAction(getPasteEvent(file, tableView, showHidden));

        System.out.println(Clipboard.getSystemClipboard().hasFiles());
        if (!Clipboard.getSystemClipboard().hasFiles()){
            paste.setDisable(true);
        }
        // Add MenuItem to ContextMenu
        contextMenu.getItems().addAll(newFile, paste);

        return contextMenu;
    }
    private static EventHandler<ActionEvent> getCopyEvent(File file){
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ClipboardContent cc = new ClipboardContent();
                List<File> files = new ArrayList<>();
                files.add(file);
                cc.putFiles(files);
                Clipboard.getSystemClipboard().setContent(cc);
            }
        };
    }


    private static EventHandler<ActionEvent> getPasteEvent(File file, TableView tableView, boolean showHidden){
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Runnable runnableTask = () -> {
                for (File tempFile : Clipboard.getSystemClipboard().getFiles()){
                    System.out.println(tempFile);

                    ContextMenuHandler.createCopyAtFileDirectory(tempFile, file);
                    refreshTableView(file, tableView, showHidden);

                }
                };
                Platform.runLater(runnableTask);
            }
        };
    }

    private static EventHandler<ActionEvent> getPasteDirectoryEvent(File file, TreeView<File> treeView, boolean showHidden){
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Runnable runnableTask = () -> {
                    for (File tempFile : Clipboard.getSystemClipboard().getFiles()){
                        System.out.println(tempFile);

                        ContextMenuHandler.createCopyAtFileDirectory(tempFile, file);
                        TreeItem<File> item = treeView.getRoot();
                        treeView.setRoot(new SimpleDirectoryTreeItem(item.getValue(), (ImageView) item.getGraphic(), showHidden));
                    }
                };
                Platform.runLater(runnableTask);
            }
        };
    }
    private static EventHandler<ActionEvent> getRenameEvent(File file, TableView tableView, boolean showHidden){
        return new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Runnable runnableTask = () -> {
                    File newFile = ContextMenuHandler.renameFile(file);
                    refreshTableView(newFile.getParentFile(), tableView, showHidden);
                };
                Platform.runLater(runnableTask);
            }
        };
    }
    private static EventHandler<ActionEvent> getRenameDirectoryEvent(File file, TreeView<File> treeView, boolean showHidden){
        return new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Runnable runnableTask = () -> {
                    File newFile = ContextMenuHandler.renameFile(file);
                    TreeItem<File> item = treeView.getRoot();
                    treeView.setRoot(new SimpleDirectoryTreeItem(item.getValue(), (ImageView) item.getGraphic(), showHidden));
                };
                Platform.runLater(runnableTask);
            }
        };
    }
    private static EventHandler<ActionEvent> getNewFileEvent(File file, TableView tableView, boolean showHidden){
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Runnable runnableTask = () -> {
                    ContextMenuHandler.newFileDialogue(file);
                    refreshTableView(file, tableView, showHidden);
                };
                Platform.runLater(runnableTask);
            }
        };
    }
    private static EventHandler<ActionEvent> getNewDirectoryEvent(File file, TreeView<File> treeView, boolean showHidden){
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Runnable runnableTask = () -> {
                    ContextMenuHandler.newDirectoryDialogue(file);
                    TreeItem<File> item = treeView.getRoot();
                    treeView.setRoot(new SimpleDirectoryTreeItem(item.getValue(), (ImageView) item.getGraphic(), showHidden));

                };
                Platform.runLater(runnableTask);
            }
        };
    }
    private static EventHandler<ActionEvent> getDeleteEvent(File file, TableView tableView, boolean showHidden){
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Runnable runnableTask = () -> {
                    File parent = file.getParentFile();
                    ContextMenuHandler.deleteFile(file);
                    refreshTableView(parent, tableView, showHidden);
                };
                Platform.runLater(runnableTask);
            }
        };
    }
    private static EventHandler<ActionEvent> getDeleteDirectoryEvent(File file, TreeView treeView, boolean showHidden){
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Runnable runnableTask = () -> {
                    ContextMenuHandler.deleteDirectory(file);
                    TreeItem<File> item = treeView.getRoot();
                    treeView.setRoot(new SimpleDirectoryTreeItem(item.getValue(), (ImageView) item.getGraphic(), showHidden));
                };
                Platform.runLater(runnableTask);
            }
        };
    }


    private static EventHandler<ActionEvent> getChecksumEvent(File file, TableView tableView){
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ContextMenuHandler.showMD5Checksum(file);
            }
        };
    }

    public static ContextMenu getDirectoryMenu(File item, TreeView<File> treeView, boolean showHidden) {
        // Create ContextMenu
        ContextMenu contextMenu = new ContextMenu();

        MenuItem rename = new MenuItem("Rename");
        rename.setOnAction(getRenameDirectoryEvent(item, treeView, showHidden));

        MenuItem newFolder = new MenuItem("New Folder");
        newFolder.setOnAction(getNewDirectoryEvent(treeView.getRoot().getValue(), treeView, showHidden));

        MenuItem paste = new MenuItem("Paste");
        paste.setOnAction(getPasteDirectoryEvent(item, treeView, showHidden));

        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(getDeleteDirectoryEvent(item, treeView, showHidden));

        MenuItem copy = new MenuItem("Copy");
        copy.setOnAction(getCopyEvent(item));


        if (!Clipboard.getSystemClipboard().hasFiles()){
            paste.setDisable(true);
        }
        // Add MenuItem to ContextMenu
        contextMenu.getItems().addAll(newFolder, rename,copy, paste, deleteMenuItem);

        return contextMenu;
    }


    public static ContextMenu getTreeViewMenu(File currentDirectory, TreeView<File> treeView, boolean showHidden) {
        // Create ContextMenu
        ContextMenu contextMenu = new ContextMenu();

        MenuItem paste = new MenuItem("Paste");
        paste.setOnAction(getPasteDirectoryEvent(currentDirectory, treeView, showHidden));
        MenuItem newFolder = new MenuItem("New Folder");
        newFolder.setOnAction(getNewDirectoryEvent(treeView.getRoot().getValue(), treeView, showHidden));

        if (!Clipboard.getSystemClipboard().hasFiles()){
            paste.setDisable(true);
        }
        // Add MenuItem to ContextMenu
        contextMenu.getItems().addAll(newFolder, paste);

        return contextMenu;
    }


}
