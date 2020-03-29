import business.DTO.UserDTO;
import business.UCC.UserUCC;
import business.factories.UserFactory;
import config.Configuration;
import javafx.application.Application;
import javafx.stage.Stage;
import persistence.DALServices;
import persistence.DAO;
import utilities.ProductionConfigurationSingleton;
import view.ViewName;
import view.ViewSwitcher;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        ViewSwitcher viewSwitcher = new ViewSwitcher(primaryStage);
        viewSwitcher.switchView(ViewName.LOGIN);

        primaryStage.setMinHeight(500.0);
        primaryStage.setMinWidth(500.0);
        primaryStage.setTitle("Groupe9");
        primaryStage.show();
    }

    public static void main(String[] args) {

        //Init the pseudo-singleton holding the configuration, this should be only done here
        ProductionConfigurationSingleton prod = new ProductionConfigurationSingleton(args);

        //Create the database if it doesn't exist
        try {
            ProductionConfigurationSingleton.getDalServices().createTables();
        } catch (IOException e) {
            e.printStackTrace();
        }

        launch(args);
    }

}

