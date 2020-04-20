package be.ac.ulb.infof307.g09.config;

/**
 * This class extend AbstractTestConfigurationHolder which uses a singleton to ensure that each conf is loaded only once
 * This class load the configuration "TestBusiness", meant to be used when testing the business
 */
public class TestBusinessConfigurationSingleton extends AbstractConfigurationSingleton {
    private static final TestBusinessConfigurationSingleton INSTANCE = new TestBusinessConfigurationSingleton("TestBusiness");

    /**
     * Constructeur privé
     */
    private TestBusinessConfigurationSingleton(String conf_name) {

        this.loadConfiguration(conf_name);
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