package sample;

import java.io.File;
import java.net.URI;

public class SimpleFile extends File {

    public SimpleFile(String pathname) {
        super(pathname);
    }

    public SimpleFile(String parent, String child) {
        super(parent, child);
    }

    public SimpleFile(File parent, String child) {

        super(parent, child);
    }

    public SimpleFile(File copy) {

        super(copy.getAbsolutePath());
    }



    public SimpleFile(URI uri) {
        super(uri);
    }

    @Override
    public String toString() {
        return"/" +this.getName();
    }

}
