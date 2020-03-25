import business.DTO.UserDTO;
import business.UCC.ProjectUCC;
import business.UCC.ProjectUCCImpl;
import business.UCC.UserUCC;
import business.UCC.UserUCCImpl;
import business.factories.ProjectFactory;
import business.factories.ProjectFactoryImpl;
import business.factories.UserFactory;
import config.Configuration;
import persistence.DALServices;
import persistence.DALServicesImpl;
import persistence.DAO;
import persistence.ProjectDAOImpl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class testMain {
    public static void main(String[] args) throws IOException {

        //Load the configuration file (if 'dev' is given in argument load the src/config/dev.properties), load the production configuration otherwise
        Configuration configuration = null;
        try {
            configuration = (Configuration) Class.forName("config.Configuration").getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exc) {
            //logger.fatal("Unexpected error", exc);
            System.exit(1);
        }
        configuration.initProperties(args);

        // Instantiate the Data Access Layer implementation of the loaded the configuration
        DALServices dalServices = null;
        UserFactory userFactory = null;
        DAO<UserDTO> userDAO = null;
        UserUCC userUcc = null;

        try {
            dalServices = (DALServices) configuration.getClassFor(("DALServices")).getDeclaredConstructor().newInstance();
            userFactory = (UserFactory) configuration.getClassFor(("UserFactory")).getDeclaredConstructor().newInstance();
            userDAO = (DAO<UserDTO>) configuration.getClassFor(("UserDAO")).getDeclaredConstructor(DALServices.class, UserFactory.class).newInstance(dalServices, userFactory);
            userUcc = (UserUCC) configuration.getClassFor("UserUCC").getConstructor(DALServices.class, DAO.class).newInstance(dalServices, userDAO);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exc) {
            exc.printStackTrace();
            System.exit(1);
        }

        try {
            dalServices.createTables();
        } catch (IOException e) {
            e.printStackTrace();
        }


        DALServices dal = new DALServicesImpl();

        UserUCC userUCC = new UserUCCImpl(dal, userDAO);
        UserDTO toLogin = userFactory.createUser("olivier.dc@email.com", "password");
        userUCC.login(toLogin);


        ProjectFactory dto = new ProjectFactoryImpl();
        ProjectDAOImpl projectDAO = new ProjectDAOImpl(dal, dto);
        ProjectUCC projectUCC = new ProjectUCCImpl(dal, projectDAO);
        projectUCC.createNewProject("testProject");
        //userUcc.login(user);
    }
}
