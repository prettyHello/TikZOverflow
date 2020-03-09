import business.DTO.UserDTO;
import business.UCC.UserUCC;
import business.factories.UserFactory;
import config.Configuration;
import javafx.application.Application;
import javafx.stage.Stage;
import persistence.DALServices;
import persistence.DAO;
import view.ViewName;
import view.ViewSwitcher;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        ViewSwitcher viewSwitcher = new ViewSwitcher(primaryStage);
        viewSwitcher.switchView(ViewName.LOGIN);

        primaryStage.setTitle("Hello World");
        primaryStage.show();
    }

    public static void main(String[] args) {

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

        launch(args);
    }
}

