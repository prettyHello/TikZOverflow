package utilities;

import business.DTO.UserDTO;
import business.UCC.UserUCC;
import business.factories.UserFactory;
import config.Configuration;
import persistence.DALServices;
import persistence.DAO;

import java.lang.reflect.InvocationTargetException;

//----TO DO:: check if the singleton is correct once we see how to it in class

/**
 * This class is meant to be used by the tests, in order to instanciate the configuration and Mocks only once
 * This is done through a pattern singleton
 */
//TODO the pattern was done from memory, check if it fits the one presented in class when that happens
public abstract class AbstractTestConfigurationHolder {
    protected static  ConfigurationSingleton conf;

    //this method is called the first time the class is loaded
    // fill it when extending this class
    static  {
        //example: this.conf= new ConfigurationSingleton("TestBusiness");
    }


    public static String getConf_name() {
        return conf.conf_name;
    }

    public static Configuration getConfiguration() {
        return conf.configuration;
    }

    public static DALServices getDalServices() {
        return conf.dalServices;
    }

    public static UserFactory getUserFactory() {
        return conf.userFactory;
    }

    public static DAO<UserDTO> getUserDAO() {
        return conf.userDAO;
    }

    public static UserUCC getUserUcc() {
        return conf.userUcc;
    }

    protected class ConfigurationSingleton {
        String conf_name;
        private Configuration configuration;
        private DALServices dalServices;
        private UserFactory userFactory;
        private DAO<UserDTO> userDAO;
        private UserUCC userUcc;

        public ConfigurationSingleton(String conf_name) {

            this.conf_name = conf_name;
            String[] args = {conf_name};

            this.dalServices = dalServices;
            this.userFactory = userFactory;
            this.userDAO = userDAO;
            this.userUcc = userUcc;

            //Load the configuration file (if 'dev' is given in argument load the src/config/dev.properties), load the production configuration otherwise
            try {
                this.configuration = (Configuration) Class.forName("config.Configuration").getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exc) {
                System.exit(1);
            }

            configuration.initProperties(args);
            try {
                this.dalServices = (DALServices) configuration.getClassFor(("DALServices")).getDeclaredConstructor().newInstance();
                this.userFactory = (UserFactory) configuration.getClassFor(("UserFactory")).getDeclaredConstructor().newInstance();
                this.userDAO = (DAO<UserDTO>) configuration.getClassFor(("UserDAO")).getDeclaredConstructor(DALServices.class, UserFactory.class).newInstance(dalServices, userFactory);
                this.userUcc = (UserUCC) configuration.getClassFor("UserUCC").getConstructor(DALServices.class, DAO.class).newInstance(dalServices, userDAO);

            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exc) {
                System.exit(1);
            }
        }
    }


}
