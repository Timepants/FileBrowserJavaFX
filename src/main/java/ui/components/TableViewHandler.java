package ui.components;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.util.Callback;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class TableViewHandler {
    public static void refreshTableView(TreeItem<File> item, TableView tableView, boolean showHidden){
        refreshTableView(item.getValue(), tableView, showHidden);
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
    public static void refreshTableView(File item, TableView tableView, boolean showHidden){
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
        //TODO clean this up
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
                return new ReadOnlyObjectWrapper<>(readableFileSize(p.getValue().length()));
            }
        });

        TableColumn<File,String> fileModDateCol = new TableColumn<File,String>("Date Modified");
        fileModDateCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<File, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<File, String> p) {
                // p.getValue() returns the Person instance for a particular TableView row
                return new ReadOnlyObjectWrapper<>(dateFormatter(p.getValue().lastModified()));
            }
        });

        tableView.getColumns().setAll(fileNameCol, fileSizeCol, fileModDateCol);
    }
}
