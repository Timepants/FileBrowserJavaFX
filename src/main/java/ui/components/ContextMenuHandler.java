package ui.components;

import javafx.application.Platform;
import util.MD5Checksum;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

public class ContextMenuHandler {
    public static void showMD5Checksum(File file){
                try {
            FXDialogue.showInformation("MD5 Checksum"
                    ,"Checksum for " + file.getName()
                    , MD5Checksum.getCheckSum(file));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public static File renameFile(File file){
        String newName = FXDialogue.showTextInput("Rename File"
                ,"Rename file"
                , "Please enter the new file name"
                , file.getName());
        System.out.println(newName);
        Path source = Path.of(file.toURI());
        try {
            source = Files.move(source, source.resolveSibling(newName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return source.toFile();
    }
    public static void newFile(File file){
        String newName = FXDialogue.showTextInput("New File"
                ,"Create new file"
                , "Please enter the file name"
                , "");
        System.out.println(newName);
        Path newFilePath = null;
        try {
            newFilePath = Paths.get(file.getParentFile().getAbsolutePath()+"/"+newName);
            System.out.println(newFilePath);
            Files.createFile(newFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void deleteFile(File file){
        String buttonResult = FXDialogue.showConfirm("Delete"
                ,"Would you like to delete this file?"
                , file.getName());;
        Path source = Path.of(file.toURI());
        if (buttonResult.equals(FXDialogue.OK)){
            try {
                Files.delete(source);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
