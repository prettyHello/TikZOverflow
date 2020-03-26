package config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;


public class Configuration {
    public static Properties properties;
    public static final String DEV = "dev";
    public static final String PROD = "prod";
    public static final String TestDAO = "TestDAO";
    public static final String TestBusiness = "TestBusiness";
    public static final String DEV_PATH = "src/config/dev.properties";
    public static final String PROD_PATH = "src/config/prod.properties";
    public static final String TestDAO_PATH = "src/config/TestDAO.properties";
    public static final String TestBusiness_PATH = "src/config/TestBusiness.properties";

    /**
     * Initializes the properties loaded from a file.
     *
     * @param path the path to the .properties file
     */
    public static void loadProperties(String path) {
        Properties prop = new Properties();
        FileInputStream stream;
        //logger.info("Loading config from " + path);
        try {
            stream = new FileInputStream(path);

            try {
                prop.load(stream);
            } catch (FileNotFoundException exc) {
                //logger.fatal("Config file doesn't exist : " + path, exc);
            } finally {
                stream.close();
            }
        } catch (Exception exc) {
            //logger.fatal("Unexpected error", exc);
        }
        Configuration.properties = prop;
    }

    public Class<?> getClassFor(String property) throws ClassNotFoundException {
        Class<?> cls = null;
        cls = Class.forName(Configuration.properties.getProperty(property));
        //logger.debug("Loaded class " + cls.getName());
        return cls;
    }

    /**
     * Loads the config file for either dev or prod environment.
     *
     * @param args the arguments passed to the main.
     */
    public void initProperties(String[] args) {
        String path = Configuration.PROD_PATH;
        if (args.length == 1) {
            if (args[0].equals(Configuration.DEV)) {
                path = Configuration.DEV_PATH;
            } else if (args[0].equals(Configuration.TestBusiness)) {
                path = Configuration.TestBusiness_PATH;
            } else if (args[0].equals(Configuration.TestDAO)) {
                path = Configuration.TestDAO_PATH;
            }

        }
        loadProperties(path);
    }
}
