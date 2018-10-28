package sample;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Controller {
    @FXML
    private TreeView<File> treeView;
    @FXML
    private TextField directoryTextBar;
    @FXML
    private TilePane tilePane;
    @FXML
    private ListView listView;

    public void initialize(){
        changeTree(System.getProperty("user.home"));
//        changeTree("/");

        tilePane.setHgap(10);
        tilePane.setVgap(10);
//        treeView.setCellFactory(new Callback<TreeView<String>,TreeCell<String>>(){
//            @Override
//            public TreeCell<File> call(TreeView<File> p) {
//                return new TextFieldTreeCellImpl();
//            }
//        });
    }
    private void changeTree(String path){
        File file = new SimpleFile(path);
        if (file.exists()) {
            SimpleDirectoryTreeItem root = new SimpleDirectoryTreeItem(file);
            root.setExpanded(true);
            directoryTextBar.setText(path);
            treeView.setRoot(root);
            updateTiledPane(root);
            updateListView(root);
        }
        //TODO add else
    }
    private void changeTree(TreeItem<File> item){
        directoryTextBar.setText(item.getValue().getPath());
        item.setExpanded(true);
        treeView.setRoot(item);
        updateTiledPane(item);
        updateListView(item);
    }
    private void updateTiledPane(TreeItem<File> item){
        File imageFile = new File("src/sample/resources/file.png");
        System.out.println(imageFile.toURI());
        Image imageDecline = new Image(imageFile.toURI().toString());
        System.out.println(imageDecline.getException());
        tilePane.getChildren().clear();
        List<File> files = new ArrayList();
        for (File child : item.getValue().listFiles()){
            files.add(child);
        }
//        files.sort(new Comparator() {
//            @Override
//            public int compare(File o1, File o2) {
//                return o1.getName().compareTo(o2.getName());
//            }
//        });
        for (File child : files){
            //TODO hidden files
            if(child.isFile() && !child.isHidden()){
                if (child.getName().length() > 6){
                    tilePane.getChildren().add(new Button(child.getName().substring(0,6), new ImageView(imageDecline)));
                } else {
                    tilePane.getChildren().add(new Button(child.getName(), new ImageView(imageDecline)));
                }
            }
        }

    }

    private void updateListView(TreeItem<File> item){
        listView.getItems().clear();
        for (File child : item.getValue().listFiles()){
            //TODO hidden files
            if(child.isFile() && !child.isHidden()){
                listView.getItems().add(new Button(child.getName()));
            }
        }
//        listView.getItems().sort(new Comparator<Button>() {
//            @Override
//            public int compare(Button o1, Button o2) {
//                return o1.getText().compareTo(o2.getText());
//            }
//        });
    }
    public void itemDoubleClicked(MouseEvent mouseEvent){
//        System.out.println(mouseEvent.getButton());
        if(mouseEvent.getClickCount() == 2)
        {
            if (treeView.getSelectionModel().getSelectedItem() != null){
                changeTree(treeView.getSelectionModel().getSelectedItem());
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
    public void test(){
//        System.out.println(System.getProperty("user.dir"));
//        System.out.println(System.getProperty("user.home"));
//        System.out.println(treeView.getRoot().getChildren());
        File file = new File(directoryTextBar.getText());
            System.out.println(file.getName());

        changeTree(directoryTextBar.getText());
//        File[] roots = File.listRoots();
//        System.out.println("Root directories in your system are:");
//
//        for (int i = 0; i < roots.length; i++) {
//            System.out.println(roots[i].toString());
//        }
    }
    public void openFile(){
//        hostServices.showDocument(new File("/home/timepants/Downloads/play.png").toURI().toString());
        try {
            Desktop.getDesktop().open(new File("/home/timepants/Downloads/play.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    HostServices hostServices ;

    public void setGetHostController(HostServices hostServices)
    {
        this.hostServices = hostServices;
    }
}
