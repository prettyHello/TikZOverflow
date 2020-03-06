package view.login;

import business.DTO.UserDTO;
import business.UCC.UserUCC;
import business.UCC.UserUCCImpl;
import business.factories.UserFactoryImpl;
import exceptions.BizzException;
import exceptions.FatalException;
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

import static utilities.Utility.showAlert;

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

        try {
            Utility.checkString(tf_username.getText(), "username");
            Utility.checkString(tf_password.getText(), "password");
            UserFactoryImpl dto = new UserFactoryImpl();
            UserDTO user = dto.createUser(username, password);
            DALServices dal = new DALServicesImpl();
            UserDAOImpl dao = new UserDAOImpl(dal, dto);
            UserUCC userUcc = new UserUCCImpl(dal, dao);
            userUcc.login(user);

            viewSwitcher.switchView(ViewName.DASHBOARD);

        }catch (BizzException e) {
            //Update failed on dao lvl
            System.out.println("Login Failed on buisness lvl");
            showAlert(Alert.AlertType.WARNING, "Login", "Business Error", e.getMessage());
        } catch (FatalException e) {
            //Update failed on dao lvl
            System.out.println("Login Failed on DAL/DAO lvl");
            e.printStackTrace();
            showAlert(Alert.AlertType.WARNING, "Login", "Unexpected Error", e.getMessage());
        }
    }

    @FXML
    public void handleCreateAccountButton(ActionEvent event) {
        viewSwitcher.switchView(ViewName.REGISTRATION);
    }

    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }

}
