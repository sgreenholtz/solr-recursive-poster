package solr;

public class PostException extends RuntimeException {
	PostException(String reason,Throwable cause) {
		super(reason + " (POST URL=" + SimplePostTool.solrUrl + ")", cause);
	}
}
