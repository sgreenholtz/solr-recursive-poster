package solr;

public class ConsoleLogger {
	  /** Check what Solr replied to a POST, and complain if it's not what we expected.
	   *  TODO: parse the response and check it XMLwise, here we just check it as an unparsed String  
	   */
	  static void warnIfNotExpectedResponse(String actual,String expected) {
	    if(actual.indexOf(expected) < 0) {
	      warn("Unexpected response from Solr: '" + actual + "' does not contain '" + expected + "'");
	    }
	  }
	  
	  static void warn(String msg) {
	    System.err.println("SimplePostTool: WARNING: " + msg);
	  }

	  static void info(String msg) {
	    System.out.println("SimplePostTool: " + msg);
	  }

	  static void fatal(String msg) {
	    System.err.println("SimplePostTool: FATAL: " + msg);
	    System.exit(1);
	  }
}
