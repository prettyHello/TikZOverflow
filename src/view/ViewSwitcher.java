package view;

import business.DTO.UserDTO;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import utilities.Utility;
import view.dashboard.DashboardController;
import view.editor.EditorController;
import view.login.LoginController;
import view.profile.ProfileController;
import view.registration.RegistrationController;

import java.io.IOException;

/**
 * CLass that handles the switching from one screen to another during interaction with the application
 */
public class ViewSwitcher {
    /**
     * The stage containing the different scenes
     */
    private final Stage stage;
    private UserDTO user ;

    public ViewSwitcher setUser (UserDTO user) {
        this.user = user ;
        return this ;
    }

    public ViewSwitcher(Stage stage) {
        this.stage = stage;
    }

    /**
     * Given the name of a view sets up and switches to the view specified in the application
     *
     * @param viewName the name of the view to switch to
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
                    throw new IllegalArgumentException("Unhandled view");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("couldn't change view");
            Utility.showAlert(Alert.AlertType.ERROR, "Fatal error", "Unable to switch view", "The application is unable to change the current view. Exiting.");
            Platform.exit();
        }
    }

    /**
     * Switches to the login screen
     *
     * @throws IOException if the associated .fxml file can't be loaded
     */
    private void toLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login/login.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/registration/registration.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/profile/profile.fxml"));
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
    public void toDashboard() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard/dashboard.fxml"));
        Parent root = loader.load();
        DashboardController dashboardController = loader.getController();
        dashboardController.setViewSwitcher(this).setUserProjectView(user);
        this.stage.setScene(new Scene(root));
    }

    /**
     * Switches to the dashboard screen
     *
     * @throws IOException if the associated .fxml file can't be loaded
     */
    private void toEditor() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/editor/editor.fxml"));
        Parent root = loader.load();
        EditorController editorController = loader.getController();
        editorController.setViewSwitcher(this);
        this.stage.setScene(new Scene(root));
    }
}



