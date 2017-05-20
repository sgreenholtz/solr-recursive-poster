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

	private static Poster poster = new FilePoster();
	
	public static boolean isDirectory(String path) {
		File pathToTest = new File(path);
		return pathToTest.isDirectory();
	}
	
	public void postFiles(String pathStr) {
		int fileCount = 0;
		Path path = Paths.get(pathStr);
		try {
			Files.walkFileTree(path, new Walker(poster));
		} catch (IOException e) {
			ConsoleLogger.fatal("Read/Write error while taversing file system", e);
		}

	}
}
