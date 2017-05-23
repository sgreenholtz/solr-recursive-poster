package solr.walker;

import solr.posters.TestPoster;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestWalker {

    public static Walker walker = new Walker(new TestPoster());

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("c:/DOJ/config/");
        Files.walkFileTree(path, walker);
    }
}
