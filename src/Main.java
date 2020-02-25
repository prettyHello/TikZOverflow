import business.DTO.UserDTO;
import business.UCC.UserUCC;
import business.factories.UserFactory;
import config.Configuration;
import persistence.DALServices;
import persistence.DAO;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.ViewName;
import view.ViewSwitcher;
import view.login.LoginController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/login/login.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
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
            System.out.println("oups");
            System.exit(1);
        }

        test(dalServices,userUcc,userFactory);

        launch(args);
    }

    //in this test I show how it will be used in the front end
    private static void test(DALServices dalServices, UserUCC userUcc, UserFactory userFactory){
        System.out.println("Test");
        try {
            dalServices.createTables();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //upon receiving the form data we create a DTO
        UserDTO user = userFactory.createUser();
        user.setFirst_name("bob");
        user.setPassword("clear for now");
        user.setSalt("123");
        user.setEmail("b@b.b");
        user.setLast_name("art");
        user.setPhone("xx");
        user.setRegister_date(LocalDateTime.now().toString());


        // then we send that DTO to our Use Case view.Controller which will take care of all the logic
        userUcc.register(user);
        if(user.isAuthorized()){
            System.out.println("ok");
        }else{
        }

    }
}
