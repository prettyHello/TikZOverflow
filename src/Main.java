import javafx.application.Application;
import javafx.stage.Stage;
import utilities.ConfigurationSingleton;
import view.ViewName;
import view.ViewSwitcher;

import java.io.IOException;

/**
 * The Main class in the entry point of the application
 * In our case it will have two roles:
 *  - to load and build the configuration
 *  - to launch javaFX
 *  - create the database if it doesn't exists
 */
public class Main extends Application {

    /**
     * The start methods tells which javaFX should be shown first (the login page)
     * and setup the minimum size as well as the title of the window.
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        ViewSwitcher viewSwitcher = new ViewSwitcher(primaryStage);
        viewSwitcher.switchView(ViewName.LOGIN);

        primaryStage.setMinHeight(500.0);
        primaryStage.setMinWidth(500.0);
        primaryStage.setTitle("Groupe9");
        primaryStage.show();
    }

    /**
     * The main function is the first function called
     * @param args
     */
    public static void main(String[] args) {

        //Initialize the pseudo-singleton holding the configuration, this should be only done here
        ConfigurationSingleton prod = new ConfigurationSingleton(args);

        //Create the database if it doesn't exist
        try {
            ConfigurationSingleton.getDalServices().createTables();
        } catch (IOException e) {
            e.printStackTrace();
        }

        launch(args);
    }

}

