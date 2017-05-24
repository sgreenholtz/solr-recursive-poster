package solr.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/**
 * Reads properties file and returns the values. Properties file must be on the
 * the classpath to be read correctly by the application
 * TODO: These options should really be passed as arguments on the command line
 * @author Sebastian Greenholtz
 */
public class PropertyReader {

    private final static String propertyFilePath = "resources/post.properties";
    private static Properties properties;

    private PropertyReader() {
        try {
            loadProperties();
        } catch (FileNotFoundException f) {
            ConsoleLogger.fatal("Properties file not found: " + propertyFilePath);
        } catch (Exception e) {
            ConsoleLogger.fatal("Error occurred while creating Property Reader: ", e);
        }
    }

    private static void loadProperties() throws Exception{
        if (properties == null) {
            InputStream is = new FileInputStream(new File(propertyFilePath));
            properties = new Properties();
            properties.load(is);
        }
    }

    private static Properties getProperties() throws Exception{
        loadProperties();
        return properties;
    }

    private static Collection<String> getPropertiesAsCollection(String propertyName, String delim) {
        try {
            String fileTypesString = PropertyReader.getProperties().getProperty(propertyName);
            return Arrays.asList(fileTypesString.split(delim));
        } catch (Exception e) {
            ConsoleLogger.fatal("Error while attempting to read properties file: ", e);
            return null;
        }
    }

    /* Getters for specific properties */

    public static Collection<String> getIgnoredFileTypes() {
        return getPropertiesAsCollection("ignore.file.types", ",");
    }

    public static Collection<String> getIgnoredDirectories() {
        return getPropertiesAsCollection("ignore.directories", ",");
    }




}
