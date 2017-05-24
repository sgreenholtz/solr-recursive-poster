package solr.walker;

import solr.posters.Poster;
import solr.util.ConsoleLogger;
import solr.util.PropertyReader;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author Sebastian Greenholtz
 */
public class Walker extends SimpleFileVisitor<Path> {
    private Poster poster;

    public Walker(Poster poster) {
        this.poster = poster;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes)
            throws IOException {
        if (PropertyReader.getIgnoredDirectories().contains(path.toString())) {
            return FileVisitResult.SKIP_SIBLINGS;
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes)
            throws IOException {
        if (PropertyReader.getIgnoredFileTypes().contains(Files.probeContentType(path))) {
            printSkippedInfoMessage(path);
        } else {
            try {
                poster.postFiles(path.toString());
            } catch (Exception e) {
                ConsoleLogger.fatal("Error while posting files to Solr: ", e);
            }
        }
        return FileVisitResult.CONTINUE;
    }

    private void printSkippedInfoMessage(Path path) {
        StringBuilder sb = new StringBuilder();
        sb.append("Skipping directory: ");
        sb.append(path.toString());
        sb.append(" based on properties file exclusion.");
        ConsoleLogger.info(sb.toString());
    }

    @Override
    public FileVisitResult visitFileFailed(Path path, IOException e)
            throws IOException {
        ConsoleLogger.fatal("Error visiting file", e);
        return FileVisitResult.TERMINATE;
    }
}
