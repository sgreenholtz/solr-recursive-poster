package solr.posters;

/**
 * @author Sebastian Greenholtz
 */
public class TestPoster implements Poster {

    public void postFiles(String path) {
        System.out.println("Posting file: " + path);
    }
}
