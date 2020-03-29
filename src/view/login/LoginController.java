package view.login;

import business.DTO.UserDTO;
import business.UCC.UserUCC;
import business.factories.UserFactory;
import exceptions.BizzException;
import exceptions.FatalException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import utilities.ProductionConfigurationSingleton;
import utilities.Utility;
import view.ViewName;
import view.ViewSwitcher;

import static utilities.Utility.showAlert;

/**
 * This class handles the process of login of a user.
 */
public class LoginController {
    UserFactory userFactory = ProductionConfigurationSingleton.getUserFactory();
    UserUCC userUcc = ProductionConfigurationSingleton.getUserUcc();

    @FXML
    public TextField usernameTF;

    @FXML
    public PasswordField passwordTF;

    @FXML
    public Button buttonLogin;

    @FXML
    public Button buttonCreateAccount;

    @FXML
    public AnchorPane anchorPane;

    private ViewSwitcher viewSwitcher;

    public LoginController() {
        ProductionConfigurationSingleton.getInstance();
    }

    @FXML
    public void initialize() {
        anchorPane.setOnKeyPressed(event -> {
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
            Utility.checkString(usernameTF.getText(), "username");
            Utility.checkString(passwordTF.getText(), "password");
            UserDTO user = userFactory.createUser(username, password);
            userUcc.login(user);

            viewSwitcher.setUser(userUcc.getUserInfo(user));
            viewSwitcher.switchView(ViewName.DASHBOARD);

        } catch (BizzException e) {
            //Update failed on dao lvl
            System.out.println("Login Failed on business lvl");
            showAlert(Alert.AlertType.WARNING, "Login", "Business Error", e.getMessage());
        } catch (FatalException e) {
            //Update failed on dao lvl
            System.out.println("Login Failed on DAL/DAO lvl");
            e.printStackTrace();
            showAlert(Alert.AlertType.WARNING, "Login", "Unexpected Error", e.getMessage());
        }
    }

    /**
     * Switch to registration view when the create account button is pressed.
     *
     * @param event
     */
    @FXML
    public void handleCreateAccountButton(ActionEvent event) {
        viewSwitcher.switchView(ViewName.REGISTRATION);
    }

    /**
     * Required to load view.
     *
     * @param viewSwitcher
     */
    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }
}
