package be.ac.ulb.infof307.g09.config;

/**
 * This class extend AbstractTestConfigurationHolder which uses a singleton to ensure that each conf is loaded only once
 * This class load the configuration "TestDAO", meant to be used when unit testing the model
 *
 * It tells AbstractConfigurationSingleton to use the configuration "TestDAO"
 *  which will use it to instanciate the impelementations with introspection them and to make them avaible to the rest of the application.
 */
public final class TestDAOConfigurationSingleton extends AbstractConfigurationSingleton {
    private static final TestDAOConfigurationSingleton INSTANCE = new TestDAOConfigurationSingleton("TestDAO");

    /**
     * Private constructor
     * @param confName
     */
    private TestDAOConfigurationSingleton(String confName) {

        this.loadConfiguration(confName);
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