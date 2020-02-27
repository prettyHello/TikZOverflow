package view.login;

import business.DTO.UserDTO;
import business.UCC.UserUCC;
import business.UCC.UserUCCImpl;
import business.factories.UserFactoryImpl;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import persistence.DALServices;
import persistence.DALServicesImpl;
import persistence.UserDAOImpl;
import utilities.Utility;
import view.ViewName;
import view.ViewSwitcher;

public class LoginController {
    @FXML
    public TextField tf_username;

    @FXML
    public PasswordField tf_password;

    @FXML
    public Button bt_login;

    @FXML
    public Button bt_createAccount;

    @FXML
    public AnchorPane anchorpane;

    private ViewSwitcher viewSwitcher;

    @FXML
    public void initialize() {
        anchorpane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    handleLoginButton();
                }
            }
        });
    }

    @FXML
    public void handleLoginButton() {
        String username = tf_username.getText();
        String password = tf_password.getText();

        if (!username.isEmpty() && !password.isEmpty()) {
            UserFactoryImpl dto = new UserFactoryImpl();
            UserDTO user = dto.createUser(username, password);
            DALServices dal = new DALServicesImpl();
            UserDAOImpl dao = new UserDAOImpl(dal, dto);
            UserUCC userUcc = new UserUCCImpl(dal, dao);

            if (userUcc.login(user)) {
                System.out.println(username + " logged in");
                viewSwitcher.switchView(ViewName.DASHBOARD);
            } else {
                signalBadCredentials();
            }
        } else {
            signalInvalidFields();
        }
    }

    @FXML
    public void handleCreateAccountButton(ActionEvent event) {
        viewSwitcher.switchView(ViewName.REGISTRATION);
    }

    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }

    private void signalBadCredentials() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login");
        alert.setHeaderText("Incorrect credentials");
        alert.setContentText("No match for given username and password combination");
        alert.showAndWait();
    }

    private void signalInvalidFields() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login");
        alert.setHeaderText("Invalid fields");
        alert.setContentText("Please fill in all fields correctly and try again");
        alert.showAndWait();
    }
}
