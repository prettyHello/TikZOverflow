package utilities;

/**
 * This class extend AbstractTestConfigurationHolder which uses a singleton to ensure that each conf is loaded only once
 * This class load the configuration "TestBusiness", meant to be used when testing the business
 */
public class BusinessConfigurationSingleton extends AbstractConfigurationSingleton {
    private static BusinessConfigurationSingleton INSTANCE = new BusinessConfigurationSingleton("TestBusiness");

    /** Constructeur privé */
    private BusinessConfigurationSingleton(String conf_name) {

        this.loadConfiguration(conf_name);
    }

    /** Point d'accès pour l'instance unique du singleton
     * @return*/
    public static BusinessConfigurationSingleton getInstance() {  return INSTANCE; }
}