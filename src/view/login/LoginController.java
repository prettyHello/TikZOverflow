package view.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    public TextField tf_username;

    @FXML
    public TextField tf_password;

    @FXML
    public Button bt_login;

    @FXML
    public Button bt_createAccount;

    @FXML
    public void handleLoginButton(ActionEvent event) {
        tf_username.setText("blabla"); //TODO remove + implement
    }

    @FXML
    public void handleCreateAccountButton(ActionEvent event){
        //TODO implement
    }
}
