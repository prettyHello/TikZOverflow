package view.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import view.ViewName;
import view.ViewSwitcher;

public class LoginController {
    @FXML
    public TextField tf_username;

    @FXML
    public TextField tf_password;

    @FXML
    public Button bt_login;

    @FXML
    public Button bt_createAccount;
    private ViewSwitcher viewSwitcher;

    @FXML
    public void handleLoginButton(ActionEvent event) {
        tf_username.setText("blabla"); //TODO remove + implement
    }

    @FXML
    public void handleCreateAccountButton(ActionEvent event){
        viewSwitcher.switchView(ViewName.REGISTRATION);
    }

    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }
}
