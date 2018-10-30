package ui;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import ui.components.ContextMenuMaker;
import ui.components.SimpleDirectoryTreeItem;
import ui.components.SimpleFile;


import java.awt.*;
import java.io.*;

import static ui.components.TableViewHandler.refreshTableView;

public class Controller {
    private ContextMenu contextMenu = null;
    @FXML
    private TreeView<File> treeView;
    @FXML
    private TextField directoryTextBar;
    @FXML
    private TableView<File> tableView;
    private Image folder = new Image( new File("src/main/java/ui/resources/img/open_folder.png").toURI().toString());
    public void initialize(){
        changeTree(System.getProperty("user.home"));
//        treeView.setCellFactory(new Callback<TreeView<String>,TreeCell<String>>(){
//            @Override
//            public TreeCell<File> call(TreeView<File> p) {
//                return new TextFieldTreeCellImpl();
//            }
//        });
    }
    private void changeTree(String path){
        System.out.println(folder.getUrl());
        File file = new SimpleFile(path);
        if (file.exists()) {
            SimpleDirectoryTreeItem root = new SimpleDirectoryTreeItem(file,folder);
            root.setExpanded(true);
            directoryTextBar.setText(path);
            treeView.setRoot(root);
            updateTableView(root);
        }
        //TODO add else
    }
    private void changeTree(TreeItem<File> item){
        directoryTextBar.setText(item.getValue().getPath());
        item.setExpanded(true);
        treeView.setRoot(item);
        updateTableView(item);
    }

    private void updateTableView(TreeItem<File> item){
        refreshTableView(item, tableView);
    }
    public void directoryDoubleClicked(MouseEvent mouseEvent){
//        System.out.println(mouseEvent.getButton());
        if(mouseEvent.getClickCount() == 2)
        {
            if (treeView.getSelectionModel().getSelectedItem() != null){
                System.out.println(treeView.getSelectionModel().getSelectedItem());
                changeTree(treeView.getSelectionModel().getSelectedItem());
            }
        }

    }
    public void fileDoubleClicked(MouseEvent mouseEvent){
        System.out.println(mouseEvent.getButton());
        if (tableView.getSelectionModel().getSelectedItem() != null){
            if(mouseEvent.getClickCount() == 2)
                {
                    openFile(tableView.getSelectionModel().getSelectedItem());
                }
            }


    }
    public void changeTreeToParentOfCurrent(){
        TreeItem<File> item = treeView.getRoot().getParent();
        if (item != null) {
            treeView.getRoot().setExpanded(false);
            directoryTextBar.setText(item.getValue().getPath());
            changeTree(item);
        } else if (treeView.getRoot().getValue().getParent() != null) {
            treeView.getRoot().setExpanded(false);
            changeTree(treeView.getRoot().getValue().getParent());
        }

    }
    public void moveDirectoryFromBar(KeyEvent key){
        if(key.getCode().equals(KeyCode.ENTER)){
            changeTree(directoryTextBar.getText());
        }
    }
    public void openFile(File file){
//        hostServices.showDocument(new File("/home/timepants/Downloads/play.png").toURI().toString());

        System.out.println(System.getProperty("os.name"));
        if (System.getProperty("os.name").equals("Linux")) {
            try {
                System.out.println("opening: " + file.getAbsolutePath());
                Runtime.getRuntime().exec("xdg-open " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //TODO try this on windows
            try {
                Desktop.getDesktop().open(file);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void openFile(String path){
        openFile(new File(path));
    }

    public void test(ContextMenuEvent event){
        if(contextMenu != null){
            contextMenu.hide();
        }


        File item = tableView.getSelectionModel().getSelectedItem();
        if (item != null) {
            contextMenu = ContextMenuMaker.getFileMenu(item, tableView);
            contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
        }
    }

    HostServices hostServices ;

    public void setGetHostController(HostServices hostServices)
    {
        this.hostServices = hostServices;
    }
}
