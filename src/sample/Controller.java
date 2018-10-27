package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TreeView;

import java.io.File;

public class Controller {
    @FXML
    private TreeView<File> treeView;

    public void initialize(){
        treeView = new TreeView<>(
                new SimpleFileTreeItem(new File(System.getProperty("user.home"))));
    }

    public void test(){
//        System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getProperty("user.home"));
        System.out.println(treeView.getRoot().getChildren());
//        File[] roots = File.listRoots();
//        System.out.println("Root directories in your system are:");
//
//        for (int i = 0; i < roots.length; i++) {
//            System.out.println(roots[i].toString());
//        }
    }
}
