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

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ContextMenuHandler {
    public static void showMD5Checksum(File file) {
        try {
            FXDialogue.showInformation("MD5 Checksum"
                    , "Checksum for " + file.getName()
                    , MD5Checksum.getCheckSum(file));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static File renameFile(File file) {
        String newName = FXDialogue.showTextInput("Rename File"
                , "Rename file"
                , "Please enter the new name"
                , file.getName());
        System.out.println(newName);

        return doRename(newName, file, false);


    }
    private static File doRename(String newName, File file, boolean overwrite){
        if (newName != null) {
            Path source = Path.of(file.toURI());
            try {
                if(overwrite){
                    source = Files.move(source, source.resolveSibling(newName), REPLACE_EXISTING);
                }else{
                    source = Files.move(source, source.resolveSibling(newName));
                }
            }catch (FileAlreadyExistsException e1){
                return overwriteRename(newName,file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return source.toFile();
        }
        return null;
    }
    public static File overwriteRename(String newName, File newFile) {
        String buttonResult = FXDialogue.showConfirm("File Already Exists"
                , "Overwrite File:\n\""+newFile.getName()+"\""
                , "This file already exists, would \nyou like to overwrite it?");

        if (buttonResult.equals(FXDialogue.OK)) {
            return doRename(newName, newFile, true);
        }
        return null;
    }



    public static void newFileDialogue(File file) {
        String newName = FXDialogue.showTextInput("New File"
                , "Create new file"
                , "Please enter the file name"
                , "");
        if (newName != null)
            addFileAtCurrentDirectory(file, newName, false);

    }

    public static void newDirectoryDialogue(File file) {
        String newName = FXDialogue.showTextInput("New Folder"
                , "Create new folder"
                , "Please enter the folder name"
                , "");
        if (newName != null)
            addFileAtCurrentDirectory(file, newName, true);

    }

    public static void deleteFile(File file) {
        String buttonResult = FXDialogue.showConfirm("Delete"
                , "Would you like to delete this file?"
                , file.getName());
        ;
        Path source = Path.of(file.toURI());
        if (buttonResult.equals(FXDialogue.OK)) {
            try {

                Files.delete(source);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void overWrite(File newFile, File directory) {
        String buttonResult = FXDialogue.showConfirm("File Already Exists"
                , "Overwrite File:\n\""+newFile.getName()+"\""
                , "This file already exists, would \nyou like to overwrite it?");

        if (buttonResult.equals(FXDialogue.OK)) {
            createCopyAtFileDirectory(newFile, directory, true);
        }
    }

    public static void deleteDirectory(File file) {
        String buttonResult = FXDialogue.showConfirm("Delete"
                , "Would you like to delete this directory?"
                , file.getName());
        ;
        Path source = Path.of(file.toURI());
        if (buttonResult.equals(FXDialogue.OK)) {
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

    public static void addFileAtCurrentDirectory(File file, String newName, boolean isDirectory) {
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
                if (!isDirectory) {
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

    public static void createCopyAtFileDirectory(File newFile, File directory, boolean overwrite) {
        if (newFile != null) {

            String destination = directory.getAbsolutePath() + "/" + newFile.getName();
            System.out.println("newFile: " + newFile.getAbsolutePath());
            System.out.println("destination: " + destination);
            try {
                if(overwrite){
                    Files.copy(Paths.get(newFile.getAbsolutePath()),
                            Paths.get(destination), REPLACE_EXISTING);
                } else {
                    Files.copy(Paths.get(newFile.getAbsolutePath()),
                            Paths.get(destination));
                }
            } catch (FileAlreadyExistsException e1) {
                overWrite(newFile, directory);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (newFile.isDirectory()) {
                for (File temp : newFile.listFiles()) {
                    createCopyAtFileDirectory(temp, new File(destination), false);
                }
            }

        }
    }


}
