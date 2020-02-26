package be.ac.ulb.infof307.g09.controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewSwitcher {
    private Stage stage;

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
                case DASHBOARD:
                    toDashboard();
                    break;
                case EDITOR:
                    break;
                default:
                    throw new IllegalArgumentException("Unhandled be.ac.ulb.infof307.g09.view");
            }
        } catch (IOException e) {
            System.err.println("couldn't change be.ac.ulb.infof307.g09.view");
            //TODO popup to warn user ?
            Platform.exit();
        }
    }

    private void toLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g09/view/login/login.fxml"));
        Parent root = loader.load();
        LoginController loginController = loader.getController();
        loginController.setViewSwitcher(this);

        this.stage.setScene(new Scene(root));
    }

    private void toRegistration() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g09/view/registration/registration.fxml"));
        Parent root = loader.load();
        RegistrationController registrationController = loader.getController();
        registrationController.setViewSwitcher(this);

        this.stage.setScene(new Scene(root));
    }

    private void toDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/be/ac/ulb/infof307/g09/view/dashboard/dashboard.fxml"));
        Parent root = loader.load();
        DashboardController registrationController = loader.getController();
        registrationController.setViewSwitcher(this);

        this.stage.setScene(new Scene(root));
    }
}
