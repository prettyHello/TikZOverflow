package be.ac.ulb.infof307.g09.view.login;


import be.ac.ulb.infof307.g09.config.ConfigurationHolder;
import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.controller.UCC.UserUCC;
import be.ac.ulb.infof307.g09.controller.factories.UserFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import be.ac.ulb.infof307.g09.controller.ControllerUtility;
import be.ac.ulb.infof307.g09.exceptions.BizzException;
import be.ac.ulb.infof307.g09.exceptions.FatalException;
import be.ac.ulb.infof307.g09.view.ViewName;
import be.ac.ulb.infof307.g09.view.ViewSwitcher;

import java.util.logging.Logger;

import static be.ac.ulb.infof307.g09.view.ViewUtility.showAlert;

/**
 * This class handles the process of login of a user.
 */
public class LoginController {
    private static final Logger LOG = Logger.getLogger(AbstractConfigurationSingleton.class.getName());

    @FXML
    public TextField usernameTF;
    @FXML
    public PasswordField passwordTF;
    @FXML
    public Button buttonLogin;
    @FXML
    public Button buttonCreateAccount;
    @FXML
    public BorderPane bpRootPane;

    final UserFactory userFactory = ConfigurationHolder.getUserFactory();
    final UserUCC userUcc = ConfigurationHolder.getUserUcc();

    private ViewSwitcher viewSwitcher;

    public LoginController() {
    }

    @FXML
    public void initialize() {
        bpRootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLoginButton();
            }
        });
    }

    /**
     * Proceed with login and check fields.
     */
    @FXML
    public void handleLoginButton() {
        String username = usernameTF.getText();
        String password = passwordTF.getText();

        try {
            ControllerUtility.checkString(usernameTF.getText(), "username");
            ControllerUtility.checkString(passwordTF.getText(), "password");
            UserDTO user = userFactory.createUser(username, password);
            userUcc.login(user);

            viewSwitcher.setUser(userUcc.getUserInfo(user));
            viewSwitcher.switchView(ViewName.DASHBOARD);

        } catch (BizzException e) {
            //Update failed on dao lvl
            LOG.severe("Login Failed on business lvl");
            showAlert(Alert.AlertType.WARNING, "Login", "Business Error", e.getMessage());
        } catch (FatalException e) {
            //Update failed on dao lvl
            LOG.severe("Login Failed on DAL/DAO lvl");
            LOG.severe(e.getMessage());
            showAlert(Alert.AlertType.WARNING, "Login", "Unexpected Error", e.getMessage());
        }
    }

    /**
     * Switch to registration be.ac.ulb.infof307.g09.view when the create account button is pressed.
     *
     * @param event the event fired by the user
     */
    @FXML
    public void handleCreateAccountButton(ActionEvent event) {
        viewSwitcher.switchView(ViewName.REGISTRATION);
    }

    /**
     * Required to load be.ac.ulb.infof307.g09.view.
     *
     * @param viewSwitcher the event fired by the user
     */
    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }
}
