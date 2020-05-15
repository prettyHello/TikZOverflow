package be.ac.ulb.infof307.g09.view.profile;

import be.ac.ulb.infof307.g09.config.ConfigurationHolder;
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

import java.util.logging.Logger;

import static be.ac.ulb.infof307.g09.view.ViewUtility.showAlert;

/**
 * This class shows the connected user's profile and allows to modify user's data.
 */
public class ProfileController {
    private static final Logger LOG = Logger.getLogger(ConfigurationHolder.class.getName());

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
    @FXML
    Button buttonEula;
    @FXML
    Button buttonModify;
    @FXML
    Button buttonCancel;

    private ViewSwitcher viewSwitcher;
    private final UserDTO connectedUser;
    private final UserFactory userFactory;
    private final UserUCC userUcc;

    public ProfileController() {
        this.userFactory = ConfigurationHolder.getUserFactory();
        this.userUcc = ConfigurationHolder.getUserUcc();
        this.connectedUser = userUcc.getConnectedUser();
    }

    @FXML
    public void initialize() {
        preFillFields(connectedUser);

        borderpane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleModifyButton();
            }
        });
    }

    /**
     * Fill profile fields with user's data.
     */
    private void preFillFields(UserDTO user) {
        firstnameTF.setText(user.getFirstName());
        lastnameTF.setText(user.getLastName());
        emailTF.setText(user.getEmail());
        phoneTF.setText(user.getPhone());
        emailTF.setDisable(true);
    }

    /**
     * Cancel registration and go back to login.
     */
    public void handleCancelButton() {
        viewSwitcher.switchView(ViewName.DASHBOARD);
    }

    /**
     * Show EULA when requested.
     */
    public void handleReadEulaButton() {
        ViewUtility.showEula();
    }

    /**
     * Proceed with modification and check fields.
     */
    @FXML
    public void handleModifyButton() {
        try {
            String salt;
            String pwHash;
            if (passwordTF.getText() != null && !passwordTF.getText().trim().isEmpty()) {
                String passwordText = passwordTF.getText();
                salt = BCrypt.gensalt(12);
                pwHash = BCrypt.hashpw(passwordText, BCrypt.gensalt());
            } else {
                salt = connectedUser.getSalt();
                pwHash = connectedUser.getPassword();
                //add temp password to fields for user data check
                passwordTF.setText("random");
                secondPasswordTF.setText("random");
            }
            ViewUtility.checkUserData(firstnameTF.getText().replaceAll(ViewUtility.WHITE_SPACES_PATTERN, ""), lastnameTF.getText().replaceAll(ViewUtility.WHITE_SPACES_PATTERN, ""), emailTF.getText(), passwordTF.getText(), secondPasswordTF.getText(), phoneTF.getText());
            String phoneText = phoneTF.getText();
            String emailText = emailTF.getText();

            String lastnameText = lastnameTF.getText().replaceAll(ViewUtility.WHITE_SPACES_PATTERN, "");
            String firstnameText = firstnameTF.getText().replaceAll(ViewUtility.WHITE_SPACES_PATTERN, "");

            UserDTO user = userFactory.createUser(0, firstnameText, lastnameText, emailText, phoneText, pwHash, salt, ControllerUtility.getTimeStamp());
            userUcc.updateUserInfo(user);
            showAlert(Alert.AlertType.CONFIRMATION, "Account update", "Success", "Information successfully updated");
        } catch (BizzException e) {
            //Update failed on dao lvl
            LOG.severe("Update Failed on business lvl");
            showAlert(Alert.AlertType.WARNING, "Account update", "Business Error", e.getMessage());
        } catch (FatalException e) {
            //Update failed on dao lvl
            LOG.severe("Update Failed on DAL/DAO lvl");
            LOG.severe(e.getMessage());
            showAlert(Alert.AlertType.WARNING, "Account update", "Unexpected Error", e.getMessage());
        } finally {
            viewSwitcher.switchView(ViewName.DASHBOARD);
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
     * @param viewSwitcher the object responsible for the change in views
     */
    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }
}
