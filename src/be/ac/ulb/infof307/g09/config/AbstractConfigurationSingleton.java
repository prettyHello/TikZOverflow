package be.ac.ulb.infof307.g09.config;

import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.controller.UCC.ProjectUCC;
import be.ac.ulb.infof307.g09.controller.UCC.UserUCC;
import be.ac.ulb.infof307.g09.controller.factories.ProjectFactory;
import be.ac.ulb.infof307.g09.controller.factories.UserFactory;
import be.ac.ulb.infof307.g09.model.DALServices;
import be.ac.ulb.infof307.g09.model.DAO;
import be.ac.ulb.infof307.g09.model.ProjectDAO;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

/**
 * This class is meant to be used by:
 * - the tests, in order to instantiate the configuration and Mocks only once
 * - the Main class in order to build the application only once
 * - The getters are used by every class to access the implementation hiding behind the interface
 * This is done through a singleton pattern
 */
public abstract class AbstractConfigurationSingleton {
    private static final Logger LOG = Logger.getLogger(AbstractConfigurationSingleton.class.getName());

    private static String confName;
    private static Configuration configuration;
    private static DALServices dalServices;
    private static UserFactory userFactory;
    private static DAO<UserDTO> userDAO;
    private static UserUCC userUcc;
    private static ProjectFactory projectFactory;
    private static ProjectUCC projectUCC;
    private static ProjectDAO projectDAO;

    public static String getConfName() {
        return confName;
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static DALServices getDalServices() {
        return dalServices;
    }

    public static UserFactory getUserFactory() {
        return userFactory;
    }

    public static DAO<UserDTO> getUserDAO() {
        return userDAO;
    }

    public static UserUCC getUserUcc() {
        return userUcc;
    }

    public static ProjectFactory getProjectFactory() {
        return projectFactory;
    }

    public static ProjectUCC getProjectUCC() {
        return projectUCC;
    }

    public static ProjectDAO getProjectDAO() {
        return projectDAO;
    }

    /**
     * Load the specified configuration, used in testing
     *
     * @param conf_name the name of the configuration to load
     */
    protected void loadConfiguration(String conf_name) {
        AbstractConfigurationSingleton.confName = conf_name;
        String[] args = {AbstractConfigurationSingleton.confName};
        loadConfiguration(args);
    }

    /**
     * Load the configuration specified in the Main's arguments
     * This is launch at the beginning of the application, in the Main
     * This method is called before JavaFX is launched, thus the error handling can only be a print stack trace
     *
     * @param args the command line arguments passed
     */
    protected void loadConfiguration(String[] args) {
        try {
            configuration = (Configuration) Class.forName("config.Configuration").getDeclaredConstructor().newInstance();
            configuration.initProperties(args);

            dalServices = (DALServices) configuration.getClassFor("DALServices").getDeclaredConstructor().newInstance();
            userFactory = (UserFactory) configuration.getClassFor("UserFactory").getDeclaredConstructor().newInstance();
            userDAO = (DAO<UserDTO>) configuration.getClassFor("UserDAO").getDeclaredConstructor(DALServices.class, UserFactory.class).newInstance(dalServices, userFactory);
            userUcc = (UserUCC) configuration.getClassFor("UserUCC").getConstructor(DALServices.class, DAO.class).newInstance(dalServices, userDAO);
            projectFactory = (ProjectFactory) configuration.getClassFor("ProjectFactory").getConstructor().newInstance();
            projectDAO = (ProjectDAO) configuration.getClassFor("ProjectDAO").getDeclaredConstructor(DALServices.class, ProjectFactory.class).newInstance(dalServices, projectFactory);
            projectUCC = (ProjectUCC) configuration.getClassFor("ProjectUCC").getDeclaredConstructor(DALServices.class, DAO.class).newInstance(dalServices, projectDAO);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exc) {
            //The reason behind this error handling is that this method is called before JavaFX is launched
            LOG.severe("Unable to set up configuration singleton");
            exc.printStackTrace();
            System.exit(1);
        }
    }
}
