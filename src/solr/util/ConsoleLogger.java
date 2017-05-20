package solr.util;

public class ConsoleLogger {
	  /** Check what Solr replied to a POST, and complain if it's not what we expected.
	   *  TODO: parse the response and check it XMLwise, here we just check it as an unparsed String  
	   */
	  public static void warnIfNotExpectedResponse(String actual,String expected) {
	    if(actual.indexOf(expected) < 0) {
	      warn("Unexpected response from Solr: '" + actual + "' does not contain '" + expected + "'");
	    }
	  }
	  
	  public static void warn(String msg) {
	    System.err.println("SimplePostTool: WARNING: " + msg);
	  }

	  public static void info(String msg) {
	    System.out.println("SimplePostTool: " + msg);
	  }

	  public static void fatal(String msg) {
	    System.err.println("SimplePostTool: FATAL: " + msg);
	    System.exit(1);
	  }

	  public static void fatal(String msg, Exception e) {
          System.err.println("SimplePostTool: FATAL: " + msg);
          System.err.println(getFormatedStackTrace(e.getStackTrace()));
          System.exit(1);
	  }

	  private static String getFormatedStackTrace(StackTraceElement[] st) {
	      StringBuilder sb = new StringBuilder();
	      String newLine = System.lineSeparator();
	      for (StackTraceElement stackTraceElement : st) {
	          sb.append(stackTraceElement);
	          sb.append(newLine);
          }
          return sb.toString();
      }
}
