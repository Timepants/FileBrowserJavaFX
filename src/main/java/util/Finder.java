package util;

/**
 * Sample code that finds files that match the specified glob pattern.
 * For more information on what constitutes a glob pattern, see
 * https://docs.oracle.com/javase/tutorial/essential/io/fileOps.html#glob
 * <p>
 * The file or directories that match the pattern are printed to
 * standard out.  The number of matches is also printed.
 * <p>
 * When executing this application, you must put the glob pattern
 * in quotes, so the shell will not expand any wild cards:
 * java Find . -name "*.java"
 */

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;

import static java.nio.file.FileVisitResult.*;
import static java.nio.file.FileVisitOption.*;

import java.util.*;


public class Finder
        extends SimpleFileVisitor<Path> {

    private final PathMatcher matcher;
    private int numMatches = 0;
    private List<File> resultFiles= new ArrayList<>();
    private boolean allowHidden = false;
    public Finder(String pattern, boolean allowHidden) {
        matcher = FileSystems.getDefault()
                .getPathMatcher("glob:"+ pattern +"*");
    }

    // Compares the glob pattern against
    // the file or directory name.
    public void find(Path file) {
        Path name = file.getFileName();
        if (name != null && (allowHidden || !file.toFile().isHidden()) && matcher.matches(name)) {
            numMatches++;
            resultFiles.add(file.toFile());
            System.out.println(file);
        }
    }

    // Prints the total number of
    // matches to standard out.
    public List<File> done() {
        System.out.println("Matched: "
                + numMatches);
        return resultFiles;
    }

    // Invoke the pattern matching
    // method on each file.
    @Override
    public FileVisitResult visitFile(Path file,
                                     BasicFileAttributes attrs) {
        if (allowHidden || file.toFile().isHidden()){
            return SKIP_SUBTREE;
        }
        find(file);
        return CONTINUE;
    }

    // Invoke the pattern matching
    // method on each directory.
    @Override
    public FileVisitResult preVisitDirectory(Path dir,
                                             BasicFileAttributes attrs) {
        if (allowHidden || dir.toFile().isHidden()){
            return SKIP_SUBTREE;
        }
        find(dir);
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file,
                                           IOException exc) {
        System.err.println(exc);
        return SKIP_SUBTREE;
    }
}
