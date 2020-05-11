package be.ac.ulb.infof307.g09.view.registration;

import be.ac.ulb.infof307.g09.config.ConfigurationSingleton;
import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;
import be.ac.ulb.infof307.g09.controller.UCC.UserUCC;
import be.ac.ulb.infof307.g09.controller.factories.UserFactory;
import be.ac.ulb.infof307.g09.view.ViewUtility;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import org.springframework.security.crypto.bcrypt.BCrypt;
import be.ac.ulb.infof307.g09.controller.ControllerUtility;
import be.ac.ulb.infof307.g09.exceptions.BizzException;
import be.ac.ulb.infof307.g09.exceptions.FatalException;
import be.ac.ulb.infof307.g09.view.ViewName;
import be.ac.ulb.infof307.g09.view.ViewSwitcher;

import static be.ac.ulb.infof307.g09.view.ViewUtility.showAlert;

/**
 * This class handles the process of register a new user.
 */
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
    Button buttonRegister;
    @FXML
    Button buttonCancel;
    @FXML
    BorderPane borderPane;
    @FXML
    Button buttonEula;
    @FXML
    CheckBox checkboxEula;

    private ViewSwitcher viewSwitcher;
    private final UserFactory userFactory;
    private final UserUCC userUcc;

    public RegistrationController() {
        this.userFactory = ConfigurationSingleton.getUserFactory();
        this.userUcc = ConfigurationSingleton.getUserUcc();
    }

    @FXML
    public void initialize() {
        borderPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleRegisterButton();
            }
        });
        checkboxEula.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (checkboxEula.isSelected()) {
                    checkboxEula.setSelected(false);
                } else {
                    checkboxEula.setSelected(true);
                }
                event.consume();
            }
        });
        buttonCancel.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleCancelButton();
                event.consume();
            }
        });
        buttonEula.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleReadEulaButton();
                event.consume();
            }
        });
    }

    /**
     * Show EULA when requested.
     */
    public void handleReadEulaButton() {
        ViewUtility.showEula();
    }

    /**
     * Cancel registration and go back to login.
     */
    public void handleCancelButton() {
        viewSwitcher.switchView(ViewName.LOGIN);
    }

    /**
     * Proceed with registration and check fields.
     */
    public void handleRegisterButton() {
        try {
            ViewUtility.checkUserData(this.firstnameTF.getText().replaceAll(ViewUtility.WHITE_SPACES_PATTERN, ""), this.lastnameTF.getText().replaceAll(ViewUtility.WHITE_SPACES_PATTERN, ""), this.emailTF.getText(), this.passwordTF.getText(), this.secondPasswordTF.getText(), this.phoneTF.getText());
            if (!checkboxEula.isSelected()) {
                throw new IllegalStateException("EULA not accepted");
            }
            String salt = BCrypt.gensalt(12);
            String phoneText = this.phoneTF.getText();
            String emailText = this.emailTF.getText();
            String passwordText = this.passwordTF.getText();
            String lastnameText = this.lastnameTF.getText().replaceAll(ViewUtility.WHITE_SPACES_PATTERN, "");
            String firstnameText = this.firstnameTF.getText().replaceAll(ViewUtility.WHITE_SPACES_PATTERN, "");
            String pw_hash = BCrypt.hashpw(passwordText, BCrypt.gensalt());
            UserDTO user = userFactory.createUser(0, firstnameText, lastnameText, emailText, phoneText, pw_hash, salt, ControllerUtility.getTimeStamp());
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

    /**
     * Filter for the phone number field to only allow integers.
     */
    public void allowIntegersOnly() {
        TextFormatter<String> textFormatter = new TextFormatter<>(ViewUtility.textFormatterUnary());
        phoneTF.setTextFormatter(textFormatter);
    }

    /**
     * Required to load be.ac.ulb.infof307.g09.view.
     *
     * @param viewSwitcher the object that is responsible for the change in views
     */
    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }
}