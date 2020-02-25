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
import persistence.DALServices;
import persistence.DALServicesImpl;
import persistence.UserDAOImpl;

import java.time.Clock;
import java.util.ArrayList;
import java.util.function.UnaryOperator;
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

    @FXML
    PasswordField secondPasswordTF;

    String firstnameText;

    String lastnameText;

    String emailText;

    String phoneText;

    String passwordText;


    private String unallowedCharactersPattern = "[\\\\|@#~€¬\\[\\]{}!\"·$%&\\/()=?¿^*¨;:_`\\+´,.-]";
    private String whitespacesPattern = "^[\\s]+|[\\s]+$";

    public void registerBtn() {

        if (this.executeValidation()) {
            UserFactoryImpl dto = new UserFactoryImpl();
            UserDTO user = dto.createUser(0, "firstname", "lastname", "email3", "ring ring3", "password", "random blob2", Utility.getTimeStamp());
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

        if (firstnameTF.getText().matches(this.unallowedCharactersPattern))
            return false;
        else
            // Remove whitespaces at the beginning and at the end
            this.firstnameText = this.firstnameTF.getText().replaceAll(this.whitespacesPattern, "");

        return true;

    }

    private boolean checkLastName() {

        if (lastnameTF.getText().matches(this.unallowedCharactersPattern))
            return false;
        else
            // Remove whitespaces at the beginning and at the end
            this.lastnameText = this.lastnameTF.getText().replaceAll(this.whitespacesPattern, "");

        return true;
    }

    private boolean comparePasswords() {
        if (passwordTF.getText().equals(secondPasswordTF.getText()))
            return true;
        else
            return false;
    }

    private boolean checkEmail() {
        System.out.println("Register");
        return false;
    }

}
