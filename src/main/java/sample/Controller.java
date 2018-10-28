package sample;

import javafx.application.HostServices;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.stage.StageStyle;
import javafx.util.Callback;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    @FXML
    private TableView<File> tableView;
    private File imageFile = new File("src/main/java/sample/resources/file.png");
    private Image imageDecline = new Image(imageFile.toURI().toString());
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
        File file = new SimpleFile(path);
        if (file.exists()) {
            SimpleDirectoryTreeItem root = new SimpleDirectoryTreeItem(file);
            root.setExpanded(true);
            directoryTextBar.setText(path);
            treeView.setRoot(root);
            updateTiledPane(root);
            updateListView(root);
            updateTableView(root);
        }
        //TODO add else
    }
    private void changeTree(TreeItem<File> item){
        directoryTextBar.setText(item.getValue().getPath());
        item.setExpanded(true);
        treeView.setRoot(item);
        updateTiledPane(item);
        updateListView(item);
        updateTableView(item);
    }
    private void updateTiledPane(TreeItem<File> item){

        tilePane.getChildren().clear();
        List<File> files = new ArrayList();
        for (File child : item.getValue().listFiles()){
            files.add(child);
        }
        files.sort((Comparator<File>) (o1, o2) -> o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase()));
        for (File child : files){
            //TODO hidden files
            if(child.isFile() && !child.isHidden()){
                Button button = new Button(child.getName(), new ImageView(imageDecline));
                button.addEventHandler(ActionEvent.ACTION, getButtonAndOpenFile());
//                button.setWrapText(true);
                button.setMaxWidth(150);
                button.setMinWidth(150);
                tilePane.getChildren().add(button);

            }
        }

    }

    private void updateListView(TreeItem<File> item){
        listView.getItems().clear();

        for (File child : item.getValue().listFiles()){
            //TODO hidden files
            if(child.isFile() && !child.isHidden()){
                Button button = new Button(child.getName(), new ImageView(imageDecline));
                button.addEventHandler(ActionEvent.ACTION, getButtonAndOpenFile());
                listView.getItems().add(button);
            }
        }
        listView.getItems().sort((Comparator<Button>) (o1, o2) -> o1.getText().toLowerCase().compareTo(o2.getText().toLowerCase()));
    }

    private void updateTableView(TreeItem<File> item){
        tableView.getItems().clear();
//        System.out.println(tableView.getColumns());
//        for (File child : item.getValue().listFiles()){
//            //TODO hidden files
//            if(child.isFile() && !child.isHidden()){
//                Button button = new Button(child.getName(), new ImageView(imageDecline));
//                button.addEventHandler(ActionEvent.ACTION, getButtonAndOpenFile());
//                tableView.edit(0, (TableColumn) tableView.getColumns().get(0));
//            }
//        }
//        tableView.getItems().sort((Comparator<Button>) (o1, o2) -> o1.getText().toLowerCase().compareTo(o2.getText().toLowerCase()));
        List<File> files = new ArrayList();
        for (File child : item.getValue().listFiles()){
            if(child.isFile() && !child.isHidden()) {
                files.add(child);
            }
        }
        files.sort((Comparator<File>) (o1, o2) -> o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase()));

        ObservableList<File> allFiles = FXCollections.observableList(files);
        tableView.setItems(allFiles);

        TableColumn<File,String> fileNameCol = new TableColumn<File,String>("File Name");
        fileNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<File, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<File, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return new ReadOnlyObjectWrapper<>(p.getValue().getName());
            }
        });

        TableColumn<File,String> fileSizeCol = new TableColumn<File,String>("File Size");
        fileSizeCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<File, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<File, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return new ReadOnlyObjectWrapper<>(String.valueOf(p.getValue().length()));
            }
        });

        TableColumn<File,String> fileModDateCol = new TableColumn<File,String>("Date Modified");
        fileModDateCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<File, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<File, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return new ReadOnlyObjectWrapper<>(String.valueOf(p.getValue().lastModified()));
            }
        });
//
//        TableColumn<File,String> lastNameCol = new TableColumn<File,String>("Last Name");
//        lastNameCol.setCellValueFactory(new PropertyValueFactory("lastName"));

        tableView.getColumns().setAll(fileNameCol, fileSizeCol, fileModDateCol);
    }
    public void directoryDoubleClicked(MouseEvent mouseEvent){
//        System.out.println(mouseEvent.getButton());
        if(mouseEvent.getClickCount() == 2)
        {
            if (treeView.getSelectionModel().getSelectedItem() != null){
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
        if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
            System.out.println("herewego");
            //TODO move this
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("MD5 Checksum");
            alert.setHeaderText("Checksum for " +tableView.getSelectionModel().getSelectedItem().getName());

            //TODO move this as well
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(tableView.getSelectionModel().getSelectedItem());
                //Create byte array to read data in chunks
                byte[] byteArray = new byte[1024];
                int bytesCount = 0;

                //Read file data and update in message digest
                while ((bytesCount = fis.read(byteArray)) != -1) {
                    md.update(byteArray, 0, bytesCount);
                };

                //close the stream; We don't need it now.
                fis.close();
                byte[] digest = md.digest();
                alert.setContentText(bytesToHex(digest));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            alert.showAndWait();
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
        }
//        try {
//            Desktop.getDesktop().edit(new File("/home/timepants/Downloads/play.png"));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    public void openFile(String path){
        openFile(new File(path));
    }
    public void openFile(){
        //to shut up test button
    }
    HostServices hostServices ;

    public void setGetHostController(HostServices hostServices)
    {
        this.hostServices = hostServices;
    }
    private EventHandler<ActionEvent> getButtonAndOpenFile(){
       return new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Button button = (Button) e.getSource();
                System.out.println(button.getText());
                openFile(treeView.getRoot().getValue().getAbsolutePath() + "/" + button.getText());
            }
        };
    }

//TODO move this
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
