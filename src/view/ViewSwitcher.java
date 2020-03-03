package view;

import business.DTO.UserDTO;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.dashboard.DashboardController;
import view.login.LoginController;
import view.registration.RegistrationController;

import java.io.IOException;

public class ViewSwitcher {
    private Stage stage;
    private  UserDTO user;

    public ViewSwitcher setUser(UserDTO user) {
        this.user = user;
        return this;
    }


    public ViewSwitcher(Stage stage) {
        this.stage = stage;
    }

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
                    break;
                default:
                    throw new IllegalArgumentException("Unhandled view");
            }
        } catch (IOException e) {
            System.out.println(e);
            System.err.println("couldn't change view");
            //TODO popup to warn user ?
            Platform.exit();
        }
    }

    private void toLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login/login.fxml"));
        Parent root = loader.load();
        LoginController loginController = loader.getController();
        loginController.setViewSwitcher(this);

        this.stage.setScene(new Scene(root));
    }

    private void toRegistration() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/registration/registration.fxml"));
        Parent root = loader.load();
        RegistrationController registrationController = loader.getController();
        registrationController.setViewSwitcher(this);

        this.stage.setScene(new Scene(root));
    }

    private void toProfile() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/profile/profile.fxml"));
        Parent root = loader.load();
        RegistrationController registrationController = loader.getController();
        registrationController.setViewSwitcher(this);

        this.stage.setScene(new Scene(root));
    }

    public void toDashboard() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard/dashboard.fxml"));
        Parent root = loader.load();
        DashboardController dashboardController = loader.getController();
        dashboardController.setViewSwitcher(this).setUserProjectView(user);
        this.stage.setScene(new Scene(root));
    }
}
