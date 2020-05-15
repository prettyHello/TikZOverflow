package be.ac.ulb.infof307.g09.config;

/**
 * This class extend AbstractTestConfigurationHolder which uses a singleton to ensure that each conf is loaded only once
 * This class load the configuration "TestBusiness", meant to be used when testing the business
 *
 * It tells AbstractConfigurationSingleton to use the configuration "TestBusiness"
 *  which will use it to instanciate the impelementations with introspection them and to make them avaible to the rest of the application.
 */
public final class TestBusinessConfigurationSingleton extends AbstractConfigurationSingleton {
    private static final TestBusinessConfigurationSingleton INSTANCE = new TestBusinessConfigurationSingleton("TestBusiness");

    /**
     * Constructeur privé
     */
    private TestBusinessConfigurationSingleton(String confName) {

        this.loadConfiguration(confName);
    }

    /**
     * Point d'accès pour l'instance unique du singleton
     *
     * @return
     */
    public static TestBusinessConfigurationSingleton getInstance() {
        return INSTANCE;
    }
}