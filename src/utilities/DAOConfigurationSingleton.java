package utilities;

public class DAOConfigurationSingleton extends AbstractConfigurationSingleton {
    private static DAOConfigurationSingleton INSTANCE = new DAOConfigurationSingleton("TestDAO");

    /** Constructeur privé */
    private DAOConfigurationSingleton(String conf_name) {

        this.loadConfiguration(conf_name);
    }

    /** Point d'accès pour l'instance unique du singleton
     * @return*/
    public static DAOConfigurationSingleton getInstance() {  return INSTANCE; }
}