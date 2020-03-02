package view.registration;

import business.DTO.UserDTO;
import business.UCC.UserUCC;
import business.UCC.UserUCCImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import utilities.Utility;
import business.factories.UserFactoryImpl;
import javafx.fxml.FXML;
import view.ViewName;
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

    @FXML
    Button bt_register;

    @FXML
    Button bt_cancel;

    @FXML
    BorderPane borderpane;


    private ViewSwitcher viewSwitcher;

    String firstnameText;

    String lastnameText;

    String emailText;

    String phoneText;

    String passwordText;

    @FXML
    public void initialize() {
        borderpane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    registerBtn();
                }
            }
        });
    }

    public void handleCancelButton(){
        viewSwitcher.switchView(ViewName.LOGIN);
    }

    public void registerBtn() {

        if (this.executeValidation()) {
            UserFactoryImpl userFactory = new UserFactoryImpl();
            String salt = BCrypt.gensalt(12);
            String pw_hash = BCrypt.hashpw(passwordText, BCrypt.gensalt());
            UserDTO user = userFactory.createUser(0, firstnameText, lastnameText, emailText, phoneText, pw_hash, salt, Utility.getTimeStamp());
            DALServices dal = new DALServicesImpl();
            UserDAOImpl dao = new UserDAOImpl(dal, userFactory);
            UserUCC userUcc = new UserUCCImpl(dal, dao);

            userUcc.register(user);

            System.out.println("Register");
            viewSwitcher.switchView(ViewName.LOGIN);
        } else {
            System.out.println("User has not been registered");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Account registration");
            alert.setHeaderText("Invalid input");
            alert.setContentText("Please fill in all the fields correctly and try again");

            alert.showAndWait();
        }
    }

    private boolean executeValidation() {
        ArrayList<Boolean> validations = new ArrayList<>();

        validations.add(Utility.checkFirstName(firstnameTF.getText()));
        validations.add(Utility.checkLastName(lastnameTF.getText()));
        validations.add(Utility.checkEmail(emailTF.getText()));
        validations.add(Utility.comparePasswords(passwordTF.getText(),secondPasswordTF.getText()));
        validations.add(Utility.checkPhone(phoneTF.getText()));

        for (Boolean x : validations) {
            System.out.println(x);
        }
        if(!validations.contains(false)){
            this.phoneText = this.phoneTF.getText();
            this.emailText = this.emailTF.getText();
            this.passwordText = this.passwordTF.getText();
            this.lastnameText = this.lastnameTF.getText().replaceAll(Utility.WHITE_SPACES_PATTERN, "");
            this.firstnameText = this.firstnameTF.getText().replaceAll(Utility.WHITE_SPACES_PATTERN, "");
            return true;
        }
        return false;
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


    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }

}


