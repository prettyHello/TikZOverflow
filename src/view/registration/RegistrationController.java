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


    private String unallowedCharactersPattern = "[\\\\|@#~€¬\\[\\]{}!\"·$%&\\/()=?¿^*¨;:_`\\+´,.-]";

    private String whitespacesPattern = "^[\\s]+|[\\s]+$";


    //TODO Change capital letters
    private String emailPattern = "(?:[a-zA-Z0-9!#$%&'*+\\/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+\\/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

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
            UserFactoryImpl dto = new UserFactoryImpl();
            UserDTO user = dto.createUser(0, firstnameText, lastnameText, emailText, phoneText, passwordText, generateSalt(), Utility.getTimeStamp());
            DALServices dal = new DALServicesImpl();
            UserDAOImpl dao = new UserDAOImpl(dal, dto);
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

        validations.add(this.checkFirstName());
        validations.add(this.checkLastName());
        validations.add(this.checkEmail());
        validations.add(this.comparePasswords());
        validations.add(this.checkPhone());

        for (Boolean x:validations) {
            System.out.println(x);
        }

        return !validations.contains(false);
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

    //TODO change function for salting passwords
    //temporary function for generating random salts
    private String generateSalt(){
        String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        String NUMBER = "0123456789";

        String salt = "";
        Random rnd = new Random();
        int saltLength = rnd.nextInt(15) + 5;
        for (int i = 0; i <= saltLength ; i++) {

            int alphabet = rnd.nextInt(3);
            switch (alphabet){
                case 0: salt += CHAR_LOWER.charAt(rnd.nextInt(CHAR_LOWER.length()));
                case 1: salt += CHAR_UPPER.charAt(rnd.nextInt(CHAR_UPPER.length()));
                case 2: salt += NUMBER.charAt(rnd.nextInt(NUMBER.length()));
            }

        }
        System.out.println("My salt" + salt);
        return salt;
    }


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
        if (passwordTF.getText().equals(secondPasswordTF.getText())){
            passwordText = passwordTF.getText();
            return true;
        }
        else
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

    private boolean checkPhone(){
        System.out.println("Checking phone");
        System.out.println(phoneTF.getText().length());
        if((phoneTF.getText().length() > 9) && (phoneTF.getText().length() < 11)){
            phoneText = phoneTF.getText();
            System.out.println(phoneText);
            return true;
        }
        else return false;
    }




    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }

}


