package solr.util;

import solr.SimplePostTool;

public class PostException extends RuntimeException {
	PostException(String reason,Throwable cause) {
		super(reason + " (POST URL=" + SimplePostTool.solrUrl + ")", cause);
	}
}
