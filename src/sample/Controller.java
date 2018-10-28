package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;

import java.io.File;

public class Controller {
    @FXML
    private TreeView<File> treeView;
    @FXML
    private TextField directoryTextBar;
    @FXML
    private TilePane tilePane;

    public void initialize(){
        changeTree(System.getProperty("user.home"));
        directoryTextBar.setText(System.getProperty("user.home"));
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
        SimpleDirectoryTreeItem root = new SimpleDirectoryTreeItem(new SimpleFile(path));
        root.setExpanded(true);
        directoryTextBar.setText(path);
        treeView.setRoot(root);
        updateTiledPane(root);
    }
    private void changeTree(TreeItem<File> item){
        directoryTextBar.setText(item.getValue().getPath());
        item.setExpanded(true);
        treeView.setRoot(item);
        updateTiledPane(item);
    }
    private void updateTiledPane(TreeItem<File> item){
        tilePane.getChildren().clear();
        for (File child : item.getValue().listFiles()){
            if(child.isFile()){
                if (child.getName().length() > 6){
                    tilePane.getChildren().add(new Button(child.getName().substring(0,6)));
                } else {
                    tilePane.getChildren().add(new Button(child.getName()));
                }
            }
        }
    }
    public void itemDoubleClicked(MouseEvent mouseEvent){
//        System.out.println(mouseEvent.getButton());
        if(mouseEvent.getClickCount() == 2)
        {
            changeTree(treeView.getSelectionModel().getSelectedItem());
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



}
