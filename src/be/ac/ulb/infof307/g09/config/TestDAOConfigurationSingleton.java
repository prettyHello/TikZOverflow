package be.ac.ulb.infof307.g09.config;

/**
 * This class extend AbstractTestConfigurationHolder which uses a singleton to ensure that each conf is loaded only once
 * This class load the configuration "TestDAO", meant to be used when unit testing the model
 */
public class TestDAOConfigurationSingleton extends AbstractConfigurationSingleton {
    private static final TestDAOConfigurationSingleton INSTANCE = new TestDAOConfigurationSingleton("TestDAO");

    /**
     * Private constructor
     * @param conf_name
     */
    private TestDAOConfigurationSingleton(String conf_name) {

        this.loadConfiguration(conf_name);
    }

    /**
     * Access point of the instance
     * Not really needed since getters are static
     * @return
     */
    public static TestDAOConfigurationSingleton getInstance() {
        return INSTANCE;
    }
}