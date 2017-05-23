package solr.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

/**
 * Reads properties file and returns the values. Properties file must be on the
 * the classpath to be read correctly by the application
 * @author Sebastian Greenholtz
 */
public class PropertyReader {

    private final String propertyFilePath = "post.properties";
    private Properties properties;
    private static PropertyReader instance;

    private PropertyReader() {
        if (instance == null) {
            instance = new PropertyReader();
        }

        try {
            loadProperties();
        } catch (FileNotFoundException f) {
            instance = null;
            ConsoleLogger.fatal("Properties file not found: " + propertyFilePath);
        } catch (Exception e) {
            instance = null;
            ConsoleLogger.fatal("Error occured while creating Property Reader: ", e);
        }
    }

    private void loadProperties() throws Exception{
        InputStream is = new FileInputStream(new File(propertyFilePath));
        properties = new Properties();
        properties.load(is);
    }

    private Properties getProperties() {
        return properties;
    }

    private static Collection<String> getPropertiesAsCollection(String propertyName, String delim) {
        String fileTypesString = instance.getProperties().getProperty(propertyName);
        return Arrays.asList(fileTypesString.split(delim));
    }

    /* Getters for specific properties */

    public static Collection<String> getIgnoredFileTypes() {
        return getPropertiesAsCollection("ignore.file.types", ",");
    }

    public static Collection<String> getIgnoredDirectories() {
        return getPropertiesAsCollection("ignore.directories", ",");
    }




}
