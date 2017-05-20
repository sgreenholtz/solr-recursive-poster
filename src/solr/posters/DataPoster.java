package solr.posters;

import solr.SimplePostTool;
import solr.util.ConsoleLogger;
import solr.util.PostException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ProtocolException;

public class DataPoster {
	/**
	   * Reads data from the data reader and posts it to solr,
	   * writes to the response to output
	   */
	  public static void postData(Reader data, Writer output) {

	    HttpURLConnection urlc = null;
	    try {
	      urlc = (HttpURLConnection) SimplePostTool.solrUrl.openConnection();
	      try {
	        urlc.setRequestMethod("POST");
	      } catch (ProtocolException e) {
	        throw new PostException("Shouldn't happen: HttpURLConnection doesn't support POST??", e);
	      }
	      urlc.setDoOutput(true);
	      urlc.setDoInput(true);
	      urlc.setUseCaches(false);
	      urlc.setAllowUserInteraction(false);
	      urlc.setRequestProperty("Content-type", "text/xml; charset=" + SimplePostTool.POST_ENCODING);
	      
	      OutputStream out = urlc.getOutputStream();
	      
	      try {
	        Writer writer = new OutputStreamWriter(out, SimplePostTool.POST_ENCODING);
	        pipe(data, writer);
	        writer.close();
	      } catch (IOException e) {
	        throw new PostException("IOException while posting data", e);
	      } finally {
	        if(out!=null) out.close();
	      }
	      
	      InputStream in = urlc.getInputStream();
	      try {
	        Reader reader = new InputStreamReader(in);
	        pipe(reader, output);
	        reader.close();
	      } catch (IOException e) {
	        throw new PostException("IOException while reading response", e);
	      } finally {
	        if(in!=null) in.close();
	      }
	      
	    } catch (IOException e) {
	      try {
	        ConsoleLogger.fatal("Solr returned an error: " + urlc.getResponseMessage());
	      } catch (IOException f) { }
	      ConsoleLogger.fatal("Connection error (is Solr running at " + SimplePostTool.solrUrl + " ?): " + e);
	    } finally {
	      if(urlc!=null) urlc.disconnect();
	    }
	  }
	  
	  /**
	   * Pipes everything from the reader to the writer via a buffer
	   */
	  private static void pipe(Reader reader, Writer writer) throws IOException {
	    char[] buf = new char[1024];
	    int read = 0;
	    while ( (read = reader.read(buf) ) >= 0) {
	      writer.write(buf, 0, read);
	    }
	    writer.flush();
	  }
}
