package view.registration;

import business.DTO.UserDTO;
import business.UCC.UserUCC;
import business.UCC.UserUCCImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextFormatter;
import utilities.Utility;
import business.factories.UserFactoryImpl;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import view.ViewSwitcher;
import persistence.DALServices;
import persistence.DALServicesImpl;
import persistence.UserDAOImpl;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class RegistrationController {

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
    PasswordField secondPasswordTF;


    private ViewSwitcher viewSwitcher;

    String firstnameText;

    String lastnameText;

    String emailText;

    String phoneText;

    String passwordText;


    private String unallowedCharactersPattern = "[\\\\|@#~€¬\\[\\]{}!\"·$%&\\/()=?¿^*¨;:_`\\+´,.-]";

    private String whitespacesPattern = "^[\\s]+|[\\s]+$";


    //TODO Change capital letters
    private String emailPattern = "(?:[a-zA-Z0-9!#$%&'*+\\/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+\\/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public void registerBtn() {

        if (this.executeValidation()) {
            UserFactoryImpl dto = new UserFactoryImpl();
            String salt = BCrypt.gensalt(12);
            String pw_hash = BCrypt.hashpw(passwordText, BCrypt.gensalt());
            UserDTO user = dto.createUser(0, firstnameText, lastnameText, emailText, phoneText, pw_hash, salt, Utility.getTimeStamp());
            DALServices dal = new DALServicesImpl();
            UserDAOImpl dao = new UserDAOImpl(dal, dto);
            UserUCC userUcc = new UserUCCImpl(dal, dao);

            userUcc.register(user);

            System.out.println("Register");
        } else {
            System.out.println("User has not been registred");
        }

    }

    private boolean executeValidation() {
        ArrayList<Boolean> validations = new ArrayList<Boolean>();

        validations.add(this.checkFirstName());
        validations.add(this.checkLastName());
        validations.add(this.checkEmail());
        validations.add(this.comparePasswords());
        validations.add(this.checkPhone());

        for (Boolean x : validations) {
            System.out.println(x);
        }

        if (validations.contains(false)) return false;
        else return true;


    }

    //test
    // Only allows the user to type numbers on phone textfield
    public void allowIntegersOnly() {

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();

            if (text.matches("[0-9]*")) {
                return change;
            }

            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        phoneTF.setTextFormatter(textFormatter);

    }
    //endtest

    public boolean checkFirstName() {
        System.out.println("Checking firstname");
        if (this.firstnameTF.getText().isEmpty() || this.firstnameTF.getText().matches(this.unallowedCharactersPattern))
            return false;
        else
            // Remove whitespaces at the beginning and at the end
            this.firstnameText = this.firstnameTF.getText().replaceAll(this.whitespacesPattern, "");

        return true;

    }


    private boolean checkLastName() {


        System.out.println("Checking lastname");
        if (this.lastnameTF.getText().isEmpty() || this.lastnameTF.getText().matches(this.unallowedCharactersPattern))

            return false;
        else
            // Remove whitespaces at the beginning and at the end
            this.lastnameText = this.lastnameTF.getText().replaceAll(this.whitespacesPattern, "");

        return true;

    }


    private boolean comparePasswords() {
        System.out.println("Checking password");
        if (passwordTF.getText().equals(secondPasswordTF.getText())) {
            passwordText = passwordTF.getText();
            return true;
        } else
            return false;
    }


    public boolean checkEmail() {
        System.out.println("Checking email");
        if (this.emailTF.getText().matches(this.emailPattern)) {
            this.emailText = this.emailTF.getText();
            return true;
        }

        return false;

    }

    private boolean checkPhone() {
        System.out.println("Checking phone");
        System.out.println(phoneTF.getText().length());
        if ((phoneTF.getText().length() > 9) && (phoneTF.getText().length() < 11)) {
            phoneText = phoneTF.getText();
            System.out.println(phoneText);
            return true;
        } else return false;
    }


    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }

}


