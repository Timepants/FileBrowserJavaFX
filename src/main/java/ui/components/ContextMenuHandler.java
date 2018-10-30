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
}
