package be.ac.ulb.infof307.g09.view;

import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import be.ac.ulb.infof307.g09.view.dashboard.DashboardController;
import be.ac.ulb.infof307.g09.view.editor.EditorController;
import be.ac.ulb.infof307.g09.view.login.LoginController;
import be.ac.ulb.infof307.g09.view.profile.ProfileController;
import be.ac.ulb.infof307.g09.view.registration.RegistrationController;

import java.io.IOException;

/**
 * Class that handles the switching from one screen to another during interaction with the application
 */
public class ViewSwitcher {

    /**
     * The stage containing the different scenes
     */
    private final Stage stage;
    private UserDTO user;

    public ViewSwitcher(Stage stage) {
        this.stage = stage;
    }

    /**
     * Given the name of a be.ac.ulb.infof307.g09.view sets up and switches to the be.ac.ulb.infof307.g09.view specified in the application
     *
     * @param viewName the name of the be.ac.ulb.infof307.g09.view to switch to
     */
    public void switchView(ViewName viewName) {
        try {
            switch (viewName) {
                case LOGIN:
                    toLogin();
                    break;
                case REGISTRATION:
                    toRegistration();
                    break;
                case PROFILE:
                    toProfile();
                    break;
                case DASHBOARD:
                    toDashboard();
                    break;
                case EDITOR:
                    toEditor();
                    break;
                default:
                    throw new IllegalArgumentException("Unhandled be.ac.ulb.infof307.g09.view");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("couldn't change be.ac.ulb.infof307.g09.view");
            Utility.showAlert(Alert.AlertType.ERROR, "Fatal error", "Unable to switch be.ac.ulb.infof307.g09.view", "The application is unable to change the current be.ac.ulb.infof307.g09.view. Exiting.");
            Platform.exit();
        }
    }

    /**
     * Switches to the login screen
     *
     * @throws IOException if the associated .fxml file can't be loaded
     */
    private void toLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g09/view/login/login.fxml"));
        Parent root = loader.load();
        LoginController loginController = loader.getController();
        loginController.setViewSwitcher(this);

        this.stage.setScene(new Scene(root));
    }

    /**
     * Switches to the registration screen
     *
     * @throws IOException if the associated .fxml file can't be loaded
     */
    private void toRegistration() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g09/view/registration/registration.fxml"));
        Parent root = loader.load();
        RegistrationController registrationController = loader.getController();
        registrationController.setViewSwitcher(this);

        this.stage.setScene(new Scene(root));
    }

    /**
     * Switches to the profile screen
     *
     * @throws IOException if the associated .fxml file can't be loaded
     */
    private void toProfile() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g09/view/profile/profile.fxml"));
        Parent root = loader.load();
        ProfileController profileController = loader.getController();
        profileController.setViewSwitcher(this);

        this.stage.setScene(new Scene(root));
    }

    /**
     * Switches to the dashboard screen
     *
     * @throws IOException if the associated .fxml file can't be loaded
     */
    private void toDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g09/view/dashboard/dashboard.fxml"));
        Parent root = loader.load();
        DashboardController dashboardController = loader.getController();
        dashboardController.setViewSwitcher(this);
        dashboardController.setUserProjectView(user);
        this.stage.setScene(new Scene(root));
    }

    /**
     * Switches to the dashboard screen
     *
     * @throws IOException if the associated .fxml file can't be loaded
     */
    private void toEditor() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g09/view/editor/editor.fxml"));
        Parent root = loader.load();
        EditorController editorController = loader.getController();
        editorController.setViewSwitcher(this);
        this.stage.setScene(new Scene(root));
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}