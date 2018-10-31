package ui.components;

import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import util.MD5Checksum;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
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
                , "Please enter the new name"
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
    public static void newFileDialogue(File file){
        String newName = FXDialogue.showTextInput("New File"
                ,"Create new file"
                , "Please enter the file name"
                , "");
        addFileAtCurrentDirectory(file, newName, false);

    }
    public static void newDirectoryDialogue(File file){
        String newName = FXDialogue.showTextInput("New Folder"
                ,"Create new folder"
                , "Please enter the folder name"
                , "");
        addFileAtCurrentDirectory(file, newName, true);

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
    public static void deleteDirectory(File file){
        String buttonResult = FXDialogue.showConfirm("Delete"
                ,"Would you like to delete this directory?"
                , file.getName());;
        Path source = Path.of(file.toURI());
        if (buttonResult.equals(FXDialogue.OK)){
            try {

                Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void addFileAtCurrentDirectory(File file, String newName, boolean isDirectory){
        Path newFilePath = null;
        int count = 0;
        boolean flag = true;
        while (flag) {
            try {
                if (count++ <= 0) {
                    newFilePath = Paths.get(file.getAbsolutePath() + "/" + newName);
                } else {

                    StringBuilder builder = new StringBuilder();
                    builder.append(file.getAbsolutePath());
                    builder.append("/");
                    builder.append(newName.substring(0, newName.lastIndexOf(".")));
                    builder.append(count);
                    builder.append(newName.substring(newName.lastIndexOf("."), newName.length()));
                    newFilePath = Paths.get(builder.toString());
                }
                if (!isDirectory){
                    Files.createFile(newFilePath);
                } else {
                    System.out.println("Creating a directory");
                    Files.createDirectory(newFilePath);
                }
                flag = false;
            } catch (FileAlreadyExistsException e) {
                flag = true;
            } catch (IOException e2) {
                e2.printStackTrace();
                flag = false;
            }
        }
    }
    public static void createCopyAtFileDirectory(File newFile, File directory){
        if (newFile != null) {
            try {
                String destination = directory.getAbsolutePath() + "/" + newFile.getName();
                System.out.println("newFile: " + newFile.getAbsolutePath());
                System.out.println("destination: " + destination);

                Files.copy(Paths.get(newFile.getAbsolutePath()),
                        Paths.get(destination));
                if (newFile.isDirectory()) {
                    for (File temp : newFile.listFiles()) {
                        createCopyAtFileDirectory(temp, new File(destination));
                    }
                }
            } catch (IOException e) {
                //TODO overwrite dialogue
                e.printStackTrace();
            }
        }
    }


}
