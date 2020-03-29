package utilities;

import config.Configuration;

/**
 * This class in meant to be used anywhere else than the tests
 * this pseudo-singleton will be instanciate only once by the Main application
 */
public class ProductionConfigurationSingleton  extends AbstractConfigurationSingleton {
    private static ProductionConfigurationSingleton INSTANCE;

    /**
     * the constructor should be private for a singleton
     * yet here i want it to use the args from the main to know if we are in dev or prod environement
     * This should only be called one in the main function
     */
    public ProductionConfigurationSingleton(String[] args) {

        this.loadConfiguration(args);
    }

    /**
     * Point d'accès pour l'instance unique du singleton
     *
     * @return
     */
    public static ProductionConfigurationSingleton getInstance() {
        return INSTANCE;
    }

}