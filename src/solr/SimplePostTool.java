package solr;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional ConsoleLogger.information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import solr.posters.DataPoster;
import solr.posters.DirectoryPoster;
import solr.posters.FilePoster;
import solr.util.ConsoleLogger;

import java.io.*;
import java.util.Set;
import java.util.HashSet;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple utility class for posting raw updates to a Solr server, 
 * has a main method so it can be run on the command line.
 * 
 */
public class SimplePostTool {
  public static final String DEFAULT_POST_URL = "http://localhost:8080/solr/update";
  public static final String POST_ENCODING = "UTF-8";
  public static final String VERSION_OF_THIS_TOOL = "1.2";
  private static final String DEFAULT_COMMIT = "yes";
  private static final String SOLR_OK_RESPONSE_EXCERPT = "<int name=\"status\">0</int>";

  private static final String DATA_MODE_FILES = "files";
  private static final String DATA_MODE_ARGS = "args";
  private static final String DATA_MODE_STDIN = "stdin";
  private static final String DEFAULT_DATA_MODE = DATA_MODE_FILES;

  private static final Set<String> DATA_MODES = new HashSet<String>();
  static {
    DATA_MODES.add(DATA_MODE_FILES);
    DATA_MODES.add(DATA_MODE_ARGS);
    DATA_MODES.add(DATA_MODE_STDIN);
  }
  
  public static URL solrUrl;

  public static void main(String[] args) {
    ConsoleLogger.info("version " + VERSION_OF_THIS_TOOL);

    if (0 < args.length && "-help".equals(args[0])) {
      System.out.println
        ("This is a simple command line tool for POSTing raw XML to a Solr\n"+
         "port.  XML data can be read from files specified as commandline\n"+
         "args; as raw commandline arg strings; or via STDIN.\n"+
         "Examples:\n"+
         "  java -Ddata=files -jar post.jar *.xml\n"+
         "  java -Ddata=args  -jar post.jar '<delete><id>42</id></delete>'\n"+
         "  java -Ddata=stdin -jar post.jar < hd.xml\n"+
         "Other options controlled by System Properties include the Solr\n"+
         "URL to POST to, and whether a commit should be executed.  These\n"+
         "are the defaults for all System Properties...\n"+
         "  -Ddata=" + DEFAULT_DATA_MODE + "\n"+
         "  -Durl=" + DEFAULT_POST_URL + "\n"+
         "  -Dcommit=" + DEFAULT_COMMIT + "\n");
      return;
    }

    
    URL u = null;
    try {
      u = new URL(System.getProperty("url", DEFAULT_POST_URL));
    } catch (MalformedURLException e) {
      ConsoleLogger.fatal("System Property 'url' is not a valid URL: " + u);
    }
    final SimplePostTool t = new SimplePostTool(u);

    final String mode = System.getProperty("data", DEFAULT_DATA_MODE);
    if (! DATA_MODES.contains(mode)) {
      ConsoleLogger.fatal("System Property 'data' is not valid for this tool: " + mode);
    }

    try {
      if (DATA_MODE_FILES.equals(mode)) {
        if (0 < args.length) {
          ConsoleLogger.info("POSTing files to " + u + "..");
          t.postFiles(args,0);
        }
        
      } else if (DATA_MODE_ARGS.equals(mode)) {
        if (0 < args.length) {
          ConsoleLogger.info("POSTing args to " + u + "..");
          for (String a : args) {
            final StringWriter sw = new StringWriter();
            DataPoster.postData(new StringReader(a), sw);
            ConsoleLogger.warnIfNotExpectedResponse(sw.toString(),SOLR_OK_RESPONSE_EXCERPT);
          }
        }
        
      } else if (DATA_MODE_STDIN.equals(mode)) {
        ConsoleLogger.info("POSTing stdin to " + u + "..");
        final StringWriter sw = new StringWriter();
        DataPoster.postData(new InputStreamReader(System.in,POST_ENCODING), sw);
        ConsoleLogger.warnIfNotExpectedResponse(sw.toString(),SOLR_OK_RESPONSE_EXCERPT);
      }
      if ("yes".equals(System.getProperty("commit",DEFAULT_COMMIT))) {
        ConsoleLogger.info("COMMITting Solr index changes..");
        final StringWriter sw = new StringWriter();
        t.commit(sw);
        ConsoleLogger.warnIfNotExpectedResponse(sw.toString(),SOLR_OK_RESPONSE_EXCERPT);
      }
    
    } catch(Exception ioe) {
      ConsoleLogger.fatal("Unexpected Exception " + ioe);
    }
  }
  
  private boolean isDirectory(String path) {
	  return DirectoryPoster.isDirectory(path);
  }
 
  /** Post all filenames provided in args, return the number of files posted*/
  void postFiles(String [] args,int startIndexInArgs) throws Exception {
    DirectoryPoster poster = new DirectoryPoster();
    for (int j = startIndexInArgs; j < args.length; j++) {
		if (isDirectory(args[0])) {
			poster.postFiles(args[0]);
		} else {
			FilePoster.postFiles(args[j]);
		}
      
    }
  }
  


  /**
   * Constructs an instance for posting data to the specified Solr URL 
   * (ie: "http://localhost:8983/solr/update")
   */
  public SimplePostTool(URL solrUrl) {
	  SimplePostTool.solrUrl = solrUrl;
    ConsoleLogger.warn("Make sure your XML documents are encoded in " + POST_ENCODING
        + ", other encodings are not currently supported");
  }

  /**
   * Does a simple commit operation 
   */
  public void commit(Writer output) throws IOException {
    DataPoster.postData(new StringReader("<commit/>"), output);
  }



  


}