package view.registration;

import business.DTO.UserDTO;
import business.UCC.UserUCC;
import business.UCC.UserUCCImpl;
import com.sun.javafx.runtime.eula.Eula;
import exceptions.BizzException;
import exceptions.FatalException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import org.apache.commons.compress.utils.IOUtils;
import utilities.Utility;
import business.factories.UserFactoryImpl;
import javafx.fxml.FXML;
import view.ViewName;
import view.ViewSwitcher;
import persistence.DALServices;
import persistence.DALServicesImpl;
import persistence.UserDAOImpl;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCrypt;

import static utilities.Utility.showAlert;

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

    @FXML
    Button bt_eula;

    @FXML
    CheckBox checkbox_eula;

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

    public void handleReadEulaButton() {
Utility.showEula();
    }

    public void handleCancelButton() {
        viewSwitcher.switchView(ViewName.LOGIN);
    }

    public void registerBtn() {
        try {
            Utility.checkUserData(this.firstnameTF.getText().replaceAll(Utility.WHITE_SPACES_PATTERN, ""), this.lastnameTF.getText().replaceAll(Utility.WHITE_SPACES_PATTERN, ""), this.emailTF.getText(), this.passwordTF.getText(), this.secondPasswordTF.getText(), this.phoneTF.getText());
            if (!checkbox_eula.isSelected()) {
                throw new IllegalStateException("EULA not accepted");
            }
            UserFactoryImpl userFactory = new UserFactoryImpl();
            String salt = BCrypt.gensalt(12);
            this.phoneText = this.phoneTF.getText();
            this.emailText = this.emailTF.getText();
            this.passwordText = this.passwordTF.getText();
            this.lastnameText = this.lastnameTF.getText().replaceAll(Utility.WHITE_SPACES_PATTERN, "");
            this.firstnameText = this.firstnameTF.getText().replaceAll(Utility.WHITE_SPACES_PATTERN, "");
            String pw_hash = BCrypt.hashpw(passwordText, BCrypt.gensalt());
            UserDTO user = userFactory.createUser(0, firstnameText, lastnameText, emailText, phoneText, pw_hash, salt, Utility.getTimeStamp());
            DALServices dal = new DALServicesImpl();
            UserDAOImpl dao = new UserDAOImpl(dal, userFactory);
            UserUCC userUcc = new UserUCCImpl(dal, dao);
            userUcc.register(user);
            showAlert(Alert.AlertType.CONFIRMATION, "Account registration", "Success", "Account successfully created");
            viewSwitcher.switchView(ViewName.LOGIN);
        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.WARNING, "User registration", "Incomplete form", "You must read and accept the EULA in order to register");
        } catch (BizzException e) {
            //Update failed on dao lvl
            System.out.println("Registration Failed on business lvl");
            showAlert(Alert.AlertType.WARNING, "Account registration", "Business Error", e.getMessage());
        } catch (FatalException e) {
            //Update failed on dao lvl
            System.out.println("Update Failed on DAL/DAO lvl");
            e.printStackTrace();
            showAlert(Alert.AlertType.WARNING, "Account registration", "Unexpected Error", e.getMessage());
        }
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


