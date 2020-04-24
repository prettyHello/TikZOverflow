package be.ac.ulb.infof307.g09;

import be.ac.ulb.infof307.g09.config.ConfigurationSingleton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import be.ac.ulb.infof307.g09.view.ViewName;
import be.ac.ulb.infof307.g09.view.ViewSwitcher;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * The be.ac.ulb.infof307.g09.Main class in the entry point of the application
 * In our case it will have two roles:
 *  - to load and build the configuration
 *  - to launch javaFX
 *  - create the database if it doesn't exists
 */
public class Main extends Application {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    /**
     * The start methods tells which javaFX should be shown first (the login page)
     * and setup the minimum size as well as the title of the window.
     *
     * @param primaryStage the main stage of the application
     */
    @Override
    public void start(Stage primaryStage) {
        ViewSwitcher viewSwitcher = new ViewSwitcher(primaryStage);
        viewSwitcher.switchView(ViewName.LOGIN);

        primaryStage.setTitle("Groupe9");
        try {
            Image logo_32 = new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("images/logos/logo_32.png")).toExternalForm());
            Image logo_64 = new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("images/logos/logo_64.png")).toExternalForm());
            Image logo_128 = new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("images/logos/logo_128.png")).toExternalForm());
            primaryStage.getIcons().addAll(logo_32, logo_64, logo_128);
        }catch (NullPointerException e){
            LOG.warning("Unable to load icons");
        }
        primaryStage.show();
    }

    /**
     * The main function is the first function called
     *
     * @param args the command line arguments passed to the application
     */
    public static void main(String[] args) {

        //Initialize the pseudo-singleton holding the configuration, this should be only done here
        ConfigurationSingleton prod = new ConfigurationSingleton(args);

        //Create the database if it doesn't exist
        try {
            ConfigurationSingleton.getDalServices().createTables();
        } catch (IOException e) {
            LOG.severe("Could not initialize the database");
            Platform.exit();
        }

        launch(args);
    }
}
