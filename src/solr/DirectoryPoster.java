package solr;


import java.io.File;
/**
 * Posts to Solr recursively through directories
 * @author greensxayu
 */
public class DirectoryPoster {
	
	public static boolean isDirectory(String path) {
		File pathToTest = new File(path);
		return pathToTest.isDirectory();
	}
	
	public static int postFilesInDirectories(String path) {
		int fileCount = 0;
		
		
		//TODO: finish
		return fileCount;
	}
}
