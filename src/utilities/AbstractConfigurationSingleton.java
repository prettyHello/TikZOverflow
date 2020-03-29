package utilities;

import business.DTO.UserDTO;
import business.UCC.ProjectUCC;
import business.UCC.UserUCC;
import business.factories.ProjectFactory;
import business.factories.UserFactory;
import config.Configuration;
import persistence.DALServices;
import persistence.DAO;
import persistence.ProjectDAO;

import java.lang.reflect.InvocationTargetException;

//TODO:: check if the singleton is correct once we see how to it in class

/**
 * This class is meant to be used by:
 *      - the tests, in order to instanciate the configuration and Mocks only once
 *      - the Main class in order to build the application only once
 *      - The getters are used by every class to access the implementation hiding behind the interface
 * This is done through a pattern singleton
 */

public abstract class AbstractConfigurationSingleton {
    private static String conf_name;
    private static Configuration configuration;
    private static DALServices dalServices;
    private static UserFactory userFactory;
    private static DAO<UserDTO> userDAO;
    private static UserUCC userUcc;
    private  static ProjectFactory projectFactory;
    private static ProjectUCC projectUCC;
    private  static  DAO<ProjectDAO> projectDAO;

    public static String getConf_name() {
        return conf_name;
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

    public static ProjectFactory getProjectFactory() { return projectFactory; }

    public static ProjectUCC getProjectUCC() { return projectUCC; }

    public static DAO<ProjectDAO> getProjectDAO() { return projectDAO; }


    /**
     * Load the specified configuration, used in testing
     * @param conf_name
     */
    protected void loadConfiguration(String conf_name) {
        this.conf_name = conf_name;
        String[] args = {this.conf_name};
        loadConfiguration(args);
    }

    /**
     * Load the configuration specified in the Main's arguments
     * @param args
     */
    protected void loadConfiguration(String[] args){
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
            this.projectFactory = (ProjectFactory) configuration.getClassFor("ProjectFactory").getConstructor().newInstance();
            this.projectDAO = (DAO<ProjectDAO>) configuration.getClassFor(("ProjectDAO")).getDeclaredConstructor(DALServices.class, ProjectFactory.class).newInstance(dalServices, projectFactory);
            this.projectUCC = (ProjectUCC) configuration.getClassFor(("ProjectUCC")).getDeclaredConstructor(DALServices.class, DAO.class).newInstance(dalServices, projectDAO);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exc) {
            System.exit(1);
        }
    }


}
