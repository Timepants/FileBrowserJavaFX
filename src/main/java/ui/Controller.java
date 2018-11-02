package ui;

import javafx.application.HostServices;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import ui.components.ContextMenuMaker;
import ui.components.FXDialogue;
import ui.components.SimpleDirectoryTreeItem;
import ui.components.SimpleFile;
import util.Finder;


import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static ui.components.TableViewHandler.refreshTableView;
import static ui.components.TableViewHandler.refreshSearchTableView;

public class Controller {
    private static final int MIN_CHAR_TO_SEARCH = 3;
    private ContextMenu contextMenu = null;
    @FXML
    private TreeView<File> treeView;
    @FXML
    private TextField directoryTextBar;
    @FXML
    private TextField searchTxtBox;
    @FXML
    private TableView<File> tableView;
    @FXML
    private CheckBox hiddenItemsCheckbox;
    @FXML
    private CheckBox darkMode;
    @FXML
    private Button homeButton;
    @FXML
    private Button backButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button refreshButton;

    private boolean showHidden = false;

    private Image folder;
    private Image file;
    private Image back;
    private Image home;
    private Image search;
    private Image sort;
    private Image refresh;

    public void initialize(){
        setImageGroup(false);
        changeTree(System.getProperty("user.home"));
        searchButton.setDisable(true);
//        treeView.setCellFactory(new Callback<TreeView<String>,TreeCell<String>>(){
//            @Override
//            public TreeCell<File> call(TreeView<File> p) {
//                return new TextFieldTreeCellImpl();
//            }
//        });

    }
    public void toggleDarkMode(){
        if(darkMode.isSelected()){
            setImageGroup(true);
            stylesheets.remove(lightTheme);
            stylesheets.add(darkTheme);
            FXDialogue.setIsDarkMode(true);
        } else {
            setImageGroup(false);
            stylesheets.remove(darkTheme);
            stylesheets.add(lightTheme);
            FXDialogue.setIsDarkMode(false);
        }
        changeTree(treeView.getRoot().getValue().getAbsolutePath());
    }
    private void setImageGroup(boolean isDark){
        if(isDark){
            folder = new Image( new File("src/main/java/ui/resources/img/whiteIcons/open_folder.png").toURI().toString());
            file = new Image( new File("src/main/java/ui/resources/img/whiteIcons/file.png").toURI().toString());
            back = new Image( new File("src/main/java/ui/resources/img/whiteIcons/back.png").toURI().toString());
            home = new Image( new File("src/main/java/ui/resources/img/whiteIcons/home.png").toURI().toString());
            search = new Image( new File("src/main/java/ui/resources/img/whiteIcons/search.png").toURI().toString());
            sort = new Image( new File("src/main/java/ui/resources/img/whiteIcons/sort.png").toURI().toString());
            refresh = new Image( new File("src/main/java/ui/resources/img/whiteIcons/refresh.png").toURI().toString());
        } else {
            folder = new Image( new File("src/main/java/ui/resources/img/blackIcons/open_folder.png").toURI().toString());
            file = new Image( new File("src/main/java/ui/resources/img/blackIcons/file.png").toURI().toString());
            back = new Image( new File("src/main/java/ui/resources/img/blackIcons/back.png").toURI().toString());
            home = new Image( new File("src/main/java/ui/resources/img/blackIcons/home.png").toURI().toString());
            search = new Image( new File("src/main/java/ui/resources/img/blackIcons/search.png").toURI().toString());
            sort = new Image( new File("src/main/java/ui/resources/img/blackIcons/sort.png").toURI().toString());
            refresh = new Image( new File("src/main/java/ui/resources/img/blackIcons/refresh.png").toURI().toString());
        }
        homeButton.setGraphic(new ImageView(home));
        backButton.setGraphic(new ImageView(back));
        searchButton.setGraphic(new ImageView(search));
        refreshButton.setGraphic(new ImageView(refresh));
    }
    private void changeTree(String path){
        File file = new SimpleFile(path);
        if (file.exists()) {
            SimpleDirectoryTreeItem root = null;
            if (file.isDirectory()){
                root = new SimpleDirectoryTreeItem(file,folder,showHidden);
            } else {
                root = new SimpleDirectoryTreeItem(file.getParentFile(),folder,showHidden);
            }
            root.setExpanded(true);
            directoryTextBar.setText(root.getValue().getAbsolutePath());
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
        refreshTableView(item, tableView, showHidden, file);
    }
    public void directoryDoubleClicked(MouseEvent mouseEvent){
        clickAnywhere(mouseEvent);
//        System.out.println(mouseEvent.getButton());
        if(mouseEvent.getClickCount() == 2)
        {
            if (treeView.getSelectionModel().getSelectedItem() != null){
                System.out.println(treeView.getSelectionModel().getSelectedItem());
                changeTree(treeView.getSelectionModel().getSelectedItem());
            }
        } else if (mouseEvent.getClickCount() == 1){
            if (treeView.getSelectionModel().getSelectedItem() != null){

                treeView.getSelectionModel().getSelectedItem().setExpanded(
                        !treeView.getSelectionModel().getSelectedItem().isExpanded());
            }
        }
        if(mouseEvent.getButton()!=MouseButton.SECONDARY){
                    treeView.getSelectionModel().clearSelection();
        }

    }
    public void fileDoubleClicked(MouseEvent mouseEvent){
        clickAnywhere(mouseEvent);
        if (tableView.getSelectionModel().getSelectedItem() != null){
        if(mouseEvent.getClickCount() == 2)
            {
                if (tableView.getSelectionModel().getSelectedItem().isDirectory()){
                    changeTree(tableView.getSelectionModel().getSelectedItem().getParentFile().getAbsolutePath());
                } else {
                    openFile(tableView.getSelectionModel().getSelectedItem());
                }
            }
        }
        if(mouseEvent.getButton()!=MouseButton.SECONDARY){
            tableView.getSelectionModel().clearSelection();
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
//                String[] command = {"xdg-open", file.getAbsolutePath().replaceAll("\\s+", "\\\\ ")};
                String[] command = {"xdg-open", file.getAbsolutePath()};
                ProcessBuilder pb = new ProcessBuilder(command);
                pb.start();
                System.out.println("\\command: " + Arrays.toString(command));
//                Runtime.getRuntime().exec(command);
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

    public void goHome(){
        changeTree(System.getProperty("user.home"));
    }

    public void tableViewContextMenu(ContextMenuEvent event){
        if(contextMenu != null){
            contextMenu.hide();
        }


        File item = tableView.getSelectionModel().getSelectedItem();
        if (item != null) {
            contextMenu = ContextMenuMaker.getFileMenu(item, tableView, showHidden, file);
            contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
        } else {
            File currentDirectory = treeView.getRoot().getValue();
            contextMenu = ContextMenuMaker.getTableViewMenu(currentDirectory, tableView, showHidden, file);
            contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
        }
    }

    public void treeViewContextMenu(ContextMenuEvent event){
        if(contextMenu != null){
            contextMenu.hide();
        }
        TreeItem<File> item = treeView.getSelectionModel().getSelectedItem();
        if (item != null) {
            contextMenu = ContextMenuMaker.getDirectoryMenu(item.getValue(), treeView, showHidden);
            contextMenu.show(treeView, event.getScreenX(), event.getScreenY());
        } else {
            File currentDirectory = treeView.getRoot().getValue();
            contextMenu = ContextMenuMaker.getTreeViewMenu(currentDirectory, treeView, showHidden);
            contextMenu.show(treeView, event.getScreenX(), event.getScreenY());
        }
    }
    public void clickAnywhere(MouseEvent mouseEvent){
        if(contextMenu!=null &&contextMenu.isShowing()&&mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            contextMenu.hide();
        }
    }

    HostServices hostServices ;

    public void setGetHostController(HostServices hostServices)
    {
        this.hostServices = hostServices;
    }
    public void showHiddenItemsAction(){
        showHidden = hiddenItemsCheckbox.isSelected();
        refresh();
    }
    public void refresh(){
        updateTableView(treeView.getRoot());
        TreeItem<File> item = treeView.getRoot();
        treeView.setRoot(new SimpleDirectoryTreeItem(item.getValue(), (ImageView) item.getGraphic(), showHidden));
    }
    public void search(){
        String searchText = searchTxtBox.getText();
        if(searchText.length() >= MIN_CHAR_TO_SEARCH) {
            refreshSearchTableView(treeView.getRoot().getValue(), tableView, searchText, showHidden, file, folder);
        }
//        System.out.println(Searcher.search(treeView.getRoot().getValue(), searchText).toString());
    }
    public void searchButtonPress(){
        //Show dialog asking for search type
        search();
    }
    public void searchBarKeyPress(KeyEvent key){
//        if(searchTxtBox.getText().length() < MIN_CHAR_TO_SEARCH){
            searchButton.setDisable(!(searchTxtBox.getText().length() >= MIN_CHAR_TO_SEARCH));
//        } else {
//            searchButton.setDisable(false);
//        }
        if(key.getCode().equals(KeyCode.ENTER)){
            search();
        }
    }
    private ObservableList<String> stylesheets;
    private String lightTheme;
    private String darkTheme;
    public void setStyleSheets(ObservableList<String> stylesheets, String lightTheme, String darkTheme) {
        this.stylesheets = stylesheets;
        this.lightTheme = lightTheme;
        this.darkTheme = darkTheme;
    }

}
