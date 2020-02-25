package view.registration;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.time.Clock;
import java.util.regex.Pattern;

public class RegisterController {

    @FXML
    TextField firstnameTF;

    @FXML
    TextField lastnameTF;

    @FXML
    TextField emailTF;

    @FXML
    TextField phoneTF;

    @FXML
    PasswordField passwordTF;

    String firstnameText;

    String lastnameText;

    String emailText;

    String phoneText;

    String passwordText;

    private String unallowedCharactersPattern = "[\\\\|@#~€¬\\[\\]{}!\"·$%&\\/()=?¿^*¨;:_`\\+´,.-]";
    private String whitespacesPattern = "^[\\s]+|[\\s]+$";

    public void registerBtn(){
        this.checkFirstName();
        this.checkLastName();
    }

    public boolean checkFirstName() {

        if (firstnameTF.getText().matches(this.unallowedCharactersPattern))
            return false;
        else
            // Remove whitespaces at the beginning and at the end
            this.firstnameText = this.firstnameTF.getText().replaceAll(this.whitespacesPattern, "");

        return true;
    }

    public boolean checkLastName() {

        if (lastnameTF.getText().matches(this.unallowedCharactersPattern))
            return false;
        else
            // Remove whitespaces at the beginning and at the end
            this.lastnameText = this.lastnameTF.getText().replaceAll(this.whitespacesPattern, "");

        return true;
    }

    public boolean checkEmail() { System.out.println("Register"); return false; }

    public boolean checkPhone() { System.out.println("Register"); return false; }
}
