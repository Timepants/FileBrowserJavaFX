package ui.components;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import util.Finder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class TableViewHandler {
    public static void refreshTableView(TreeItem<File> item, TableView tableView, boolean showHidden, Image file){
        refreshTableView(item.getValue(), tableView, showHidden, file);
    }
    public static String dateFormatter(Long pDate) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        return dateFormat.format(new java.util.Date(pDate));

    }
    public static String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    public static void refreshTableView(File item, TableView tableView, boolean showHidden, Image file){
        tableView.getItems().clear();
        List<File> files = new ArrayList();
        System.out.println(item);
        for (File child : item.listFiles()){
            if(child.isFile() && (showHidden || !child.isHidden())) {
                files.add(child);
            }
        }
        files.sort((Comparator<File>) (o1, o2) -> o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase()));

        ObservableList<File> allFiles = FXCollections.observableList(files);
        tableView.setItems(allFiles);


        TableColumn<File, ImageView> firstColumn = new TableColumn<File, ImageView>("");
        firstColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<File, ImageView>, ObservableValue<ImageView>>() {
            public ObservableValue<ImageView> call(TableColumn.CellDataFeatures<File, ImageView> p) {
                return new ReadOnlyObjectWrapper<>(new ImageView(file));

            }
        });
        TableColumn<File,String> fileNameCol = new TableColumn<File,String>("File Name");
        fileNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<File, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<File, String> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getName());

            }
        });

        TableColumn<File,String> fileSizeCol = new TableColumn<File,String>("File Size");
        fileSizeCol.setCellValueFactory(getFileSizeColEvent());

        TableColumn<File,String> fileModDateCol = new TableColumn<File,String>("Date Modified");
        fileModDateCol.setCellValueFactory(getFileModDateColEvent());

        tableView.getColumns().setAll(firstColumn, fileNameCol, fileSizeCol, fileModDateCol);
    }

    public static void refreshSearchTableView(File currentDirectory, TableView tableView, String pattern, boolean showHidden, Image file, Image folder){
        tableView.getItems().clear();


        Finder finder = new Finder(pattern, showHidden);
        try {

            Files.walkFileTree(currentDirectory.toPath(), finder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<File> files = finder.done();

        files.sort((Comparator<File>) (o1, o2) -> o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase()));

        ObservableList<File> allFiles = FXCollections.observableList(files);
        tableView.setItems(allFiles);

        TableColumn<File, ImageView> firstColumn = new TableColumn<File, ImageView>("");
        firstColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<File, ImageView>, ObservableValue<ImageView>>() {
            public ObservableValue<ImageView> call(TableColumn.CellDataFeatures<File, ImageView> p) {
                if (p.getValue().isDirectory()){
                    return new ReadOnlyObjectWrapper<>(new ImageView(folder));
                }
                return new ReadOnlyObjectWrapper<>(new ImageView(file));

            }
        });


        TableColumn<File,String> fileNameCol = new TableColumn<File,String>("File Name");
        fileNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<File, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<File, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return new ReadOnlyObjectWrapper<>(
                        p.getValue()
                                .getAbsolutePath()
                                .replaceFirst(currentDirectory
                                        .getAbsolutePath(),"")
                );
            }
        });

        TableColumn<File,String> fileSizeCol = new TableColumn<File,String>("File Size");
        fileSizeCol.setCellValueFactory(getFileSizeColEvent());

        TableColumn<File,String> fileModDateCol = new TableColumn<File,String>("Date Modified");
        fileModDateCol.setCellValueFactory(getFileModDateColEvent());

        tableView.getColumns().setAll(firstColumn, fileNameCol, fileSizeCol, fileModDateCol);
    }

    private static Callback<TableColumn.CellDataFeatures<File, String>, ObservableValue<String>> getFileModDateColEvent(){
        return new Callback<TableColumn.CellDataFeatures<File, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<File, String> p) {
                return new ReadOnlyObjectWrapper<>(dateFormatter(p.getValue().lastModified()));
            }
        };
    }
    private static Callback<TableColumn.CellDataFeatures<File, String>, ObservableValue<String>> getFileSizeColEvent(){
        return new Callback<TableColumn.CellDataFeatures<File, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<File, String> p) {
                return new ReadOnlyObjectWrapper<>(readableFileSize(p.getValue().length()));
            }
        };
    }
}
