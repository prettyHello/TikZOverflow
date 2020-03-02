package view.profile;

import business.DTO.UserDTO;
import business.UCC.UserUCC;
import business.UCC.UserUCCImpl;
import business.factories.UserFactoryImpl;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.springframework.security.crypto.bcrypt.BCrypt;
import persistence.DALServices;
import persistence.DALServicesImpl;
import persistence.UserDAOImpl;
import utilities.Utility;
import view.ViewName;
import view.ViewSwitcher;
import utilities.Utility;

import javax.rmi.CORBA.Util;
import java.util.ArrayList;
import java.util.function.UnaryOperator;

public class profileController {

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
    BorderPane borderpane;

    String firstnameText;

    String lastnameText;

    String emailText;

    String phoneText;

    String passwordText;

    private ViewSwitcher viewSwitcher;

    @FXML
    Button bt_modify;

    @FXML
    Button bt_cancel;

    @FXML
    public void initialize() {
        //populating the fields
        UserFactoryImpl userFactory = new UserFactoryImpl();
        DALServices dal = new DALServicesImpl();
        UserDAOImpl dao = new UserDAOImpl(dal, userFactory);
        UserUCC userUcc = new UserUCCImpl(dal, dao);
        //TODO We need a way to know wich user we are talking about
        //userUcc.getUserInfo()




        borderpane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    handleModifyButton();
                }
            }
        });
    }

    //TODO INSERT LOGIC
    public void handleCancelButton(){
        System.out.println("INSERT LOGIC HERE");
        viewSwitcher.switchView(ViewName.DASHBOARD);
    }

    public void handleModifyButton(){
        if (this.executeValidation()) {
            UserFactoryImpl userFactory = new UserFactoryImpl();
            String salt = BCrypt.gensalt(12);
            String pw_hash = BCrypt.hashpw(passwordText, BCrypt.gensalt());
            UserDTO user = userFactory.createUser(0, firstnameText, lastnameText, emailText, phoneText, pw_hash, salt, Utility.getTimeStamp());
            DALServices dal = new DALServicesImpl();
            UserDAOImpl dao = new UserDAOImpl(dal, userFactory);
            UserUCC userUcc = new UserUCCImpl(dal, dao);

            if(userUcc.updateUserInfo(user)){
                System.out.println("Update");
                viewSwitcher.switchView(ViewName.LOGIN);
            }
            //Update failed on dao lvl
            System.out.println("Update Failed on dao lvl");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Account update");
            alert.setHeaderText("Undetermined error");
            alert.setContentText("Please fill in all the fields correctly and try again");

            alert.showAndWait();
        } else {
            System.out.println("User has not been updated");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Account update");
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

    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }

}
