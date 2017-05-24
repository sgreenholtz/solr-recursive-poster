package solr.posters;


import solr.util.ConsoleLogger;
import solr.walker.Walker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Posts to Solr recursively through directories
 * @author greensxayu
 */
public class DirectoryPoster implements Poster {

	private Poster poster;

	public DirectoryPoster(Poster poster) {
	    this.poster = poster;
    }
	
	public void postFiles(String pathStr) {
		Path path = Paths.get(pathStr);
		try {
			Files.walkFileTree(path, new Walker(poster));
		} catch (IOException e) {
			ConsoleLogger.fatal("Read/Write error while traversing file system", e);
		}

	}
}
