package be.ac.ulb.infof307.g09.config;

/**
 * This class in meant to be used anywhere else than the tests
 * this pseudo-singleton will be instantiate only once by the Main application
 *
 * It uses the configuration given in args to the main, then uses the AbstractConfigurationSingleton methods
 *  to instanciate with introspection them and to make them avaible to the rest of the application.
 */
public class ConfigurationSingleton extends AbstractConfigurationSingleton {
    private static ConfigurationSingleton INSTANCE;

    /**
     * the constructor should be private for a singleton
     * yet here i want it to use the args from the main to know if we are in dev or prod environment
     * This should only be called one in the main function
     */
    public ConfigurationSingleton(String[] args) {

        this.loadConfiguration(args);
    }

    /**
     * Point d'accès pour l'instance unique du singleton
     *
     * @return returns the configuration singleton instance
     */
    public static ConfigurationSingleton getInstance() {
        return INSTANCE;
    }
}