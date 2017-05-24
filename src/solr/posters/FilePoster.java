package solr.posters;

import solr.util.ConsoleLogger;

import java.io.*;

public class FilePoster implements Poster {
	private static final String SOLR_OK_RESPONSE_EXCERPT = "<int name=\"status\">0</int>";
	public static final String POST_ENCODING = "UTF-8";

	public void postFiles(String path) {
		File srcFile = new File(path);
	      final StringWriter sw = new StringWriter();
	      
	      if (srcFile.canRead()) {
	        ConsoleLogger.info("POSTing file " + srcFile.getName());
	        postFile(srcFile, sw);
	        ConsoleLogger.warnIfNotExpectedResponse(sw.toString(),SOLR_OK_RESPONSE_EXCERPT);
	      } else {
	    	  ConsoleLogger.warn("Cannot read input file: " + srcFile);
	      }
	}
	
	  /**
	   * Opens the file and posts it's contents to the solrUrl,
	   * writes to response to output.
	   */
	  private static void postFile(File file, Writer output) {

	    // FIXME; use a real XML parser to read files, so as to support various encodings
	    // (and we can only post well-formed XML anyway)
	    try (Reader reader = new InputStreamReader(new FileInputStream(file),POST_ENCODING)) {
			DataPoster.postData(reader, output);
		} catch (Exception e){
	    	ConsoleLogger.fatal("Error while posting files: ", e);
	    }
	  }

}
