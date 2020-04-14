package config;

import controller.DTO.UserDTO;
import controller.UCC.ProjectUCC;
import controller.UCC.UserUCC;
import controller.factories.ProjectFactory;
import controller.factories.UserFactory;
import model.DALServices;
import model.DAO;
import model.ProjectDAO;

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
    private  static  ProjectDAO projectDAO;

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

    public static ProjectDAO getProjectDAO() { return projectDAO; }


    /**
     * Load the specified configuration, used in testing
     * @param conf_name
     */
    protected void loadConfiguration(String conf_name) {
        AbstractConfigurationSingleton.conf_name = conf_name;
        String[] args = {AbstractConfigurationSingleton.conf_name};
        loadConfiguration(args);
    }

    /**
     * Load the configuration specified in the Main's arguments
     * This is launch at the beginning of the application, in the Main
     * This method is called before JavaFX is launched, thus the error handling can only be a print stack trace
     * @param args
     */
    protected void loadConfiguration(String[] args){
        try {
            configuration = (Configuration) Class.forName("config.Configuration").getDeclaredConstructor().newInstance();
            configuration.initProperties(args);

            dalServices = (DALServices) configuration.getClassFor(("DALServices")).getDeclaredConstructor().newInstance();
            userFactory = (UserFactory) configuration.getClassFor(("UserFactory")).getDeclaredConstructor().newInstance();
            userDAO = (DAO<UserDTO>) configuration.getClassFor(("UserDAO")).getDeclaredConstructor(DALServices.class, UserFactory.class).newInstance(dalServices, userFactory);
            userUcc = (UserUCC) configuration.getClassFor("UserUCC").getConstructor(DALServices.class, DAO.class).newInstance(dalServices, userDAO);
            projectFactory = (ProjectFactory) configuration.getClassFor("ProjectFactory").getConstructor().newInstance();
            projectDAO = (ProjectDAO) configuration.getClassFor(("ProjectDAO")).getDeclaredConstructor(DALServices.class, ProjectFactory.class).newInstance(dalServices, projectFactory);
            projectUCC = (ProjectUCC) configuration.getClassFor(("ProjectUCC")).getDeclaredConstructor(DALServices.class, DAO.class).newInstance(dalServices, projectDAO);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exc) {
            //The reason behind this error handling is that this method is called before JavaFX is launched
            exc.printStackTrace();
            System.exit(1);
        }
    }


}
