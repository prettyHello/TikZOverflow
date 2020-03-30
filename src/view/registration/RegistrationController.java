package view.registration;

import config.ConfigurationSingleton;
import controller.DTO.UserDTO;
import controller.UCC.UserUCC;
import controller.factories.UserFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import org.springframework.security.crypto.bcrypt.BCrypt;
import utilities.Utility;
import utilities.exceptions.BizzException;
import utilities.exceptions.FatalException;
import view.ViewName;
import view.ViewSwitcher;

import java.util.function.UnaryOperator;

import static utilities.Utility.showAlert;

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

    private String firstnameText;

    private String lastnameText;

    private String emailText;

    private String phoneText;

    private String passwordText;

    private UserFactory userFactory;
    private UserUCC userUcc;

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
        Utility.showEula();
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
            Utility.checkUserData(this.firstnameTF.getText().replaceAll(Utility.WHITE_SPACES_PATTERN, ""), this.lastnameTF.getText().replaceAll(Utility.WHITE_SPACES_PATTERN, ""), this.emailTF.getText(), this.passwordTF.getText(), this.secondPasswordTF.getText(), this.phoneTF.getText());
            if (!checkboxEula.isSelected()) {
                throw new IllegalStateException("EULA not accepted");
            }
            String salt = BCrypt.gensalt(12);
            this.phoneText = this.phoneTF.getText();
            this.emailText = this.emailTF.getText();
            this.passwordText = this.passwordTF.getText();
            this.lastnameText = this.lastnameTF.getText().replaceAll(Utility.WHITE_SPACES_PATTERN, "");
            this.firstnameText = this.firstnameTF.getText().replaceAll(Utility.WHITE_SPACES_PATTERN, "");
            String pw_hash = BCrypt.hashpw(passwordText, BCrypt.gensalt());
            UserDTO user = userFactory.createUser(0, firstnameText, lastnameText, emailText, phoneText, pw_hash, salt, Utility.getTimeStamp());
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

    /**
     * Required to load view.
     *
     * @param viewSwitcher
     */
    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }
}