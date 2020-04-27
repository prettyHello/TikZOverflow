package be.ac.ulb.infof307.g09.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * This class will read the properties files which are on the format (interface-implementation)
 * It is used by AbstractConfigurationSingleton to instanciate the implementations by introspection and make them avaible
 * everywhere in the app.
 */
public class Configuration {
    public static Properties properties;
    public static final String DEV = "dev";
    public static final String PROD = "prod";
    public static final String TEST_DAO = "TestDAO";
    public static final String TEST_BUSINESS = "TestBusiness";
    public static final String DEV_PATH = "src/be/ac/ulb/infof307/g09/config/dev.properties";
    public static final String PROD_PATH = "src/be/ac/ulb/infof307/g09/config/prod.properties";
    public static final String TEST_DAO_PATH = "src/be/ac/ulb/infof307/g09/config/TestDAO.properties";
    public static final String TEST_BUSINESS_PATH = "src/be/ac/ulb/infof307/g09/config/TestBusiness.properties";
    public static final Logger LOG = Logger.getLogger(Configuration.class.getName());

    /**
     * Initializes the properties loaded from a file.
     *
     * @param path the path to the .properties file
     */
    public static void loadProperties(String path) {
        Properties prop = new Properties();
        FileInputStream stream;
        LOG.info("Loading config from " + path);
        try {
            stream = new FileInputStream(path);

            try {
                prop.load(stream);
            } catch (FileNotFoundException exc) {
                LOG.severe("Config file doesn't exist : " + path);
            } finally {
                stream.close();
            }
        } catch (Exception exc) {
            LOG.severe("Unexpected error");
        }
        Configuration.properties = prop;
    }

    public Class<?> getClassFor(String property) throws ClassNotFoundException {
        Class<?> cls = Class.forName(Configuration.properties.getProperty(property));
        LOG.info("Loaded class " + cls.getName());
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
            } else if (args[0].equals(Configuration.TEST_BUSINESS)) {
                path = Configuration.TEST_BUSINESS_PATH;
            } else if (args[0].equals(Configuration.TEST_DAO)) {
                path = Configuration.TEST_DAO_PATH;
            }

        }
        loadProperties(path);
    }
}
