package solr.posters;

import solr.util.ConsoleLogger;
import solr.util.PostException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class FilePoster {
	private static final String SOLR_OK_RESPONSE_EXCERPT = "<int name=\"status\">0</int>";
	public static final String POST_ENCODING = "UTF-8";

	public static void postFiles(String path) throws Exception{
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
	   * @throws UnsupportedEncodingException 
	   */
	  private static void postFile(File file, Writer output) 
	    throws FileNotFoundException, UnsupportedEncodingException {

	    // FIXME; use a real XML parser to read files, so as to support various encodings
	    // (and we can only post well-formed XML anyway)
	    Reader reader = new InputStreamReader(new FileInputStream(file),POST_ENCODING);
	    try {
	      DataPoster.postData(reader, output);
	    } finally {
	      try {
	        if(reader!=null) reader.close();
	      } catch (IOException e) {
	        throw new PostException("IOException while closing file", e);
	      }
	    }
	  }

}
