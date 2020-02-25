package view.registration;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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



    @FXML
    public void registerBtn(){
        System.out.println(firstnameTF.getText());
        System.out.println(lastnameTF.getText());
        System.out.println(emailTF.getText());
        System.out.println(phoneTF.getText());
        System.out.println(passwordTF.getText());
    }

    public boolean checkFirstName() { System.out.println("Register"); return false; }

    public boolean checkLastName() { System.out.println("Register"); return false; }

    public boolean checkEmail() { System.out.println("Register"); return false; }

    public boolean checkPhone() { System.out.println("Register"); return false; }
}
