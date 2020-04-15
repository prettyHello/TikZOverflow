package view.profile;

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

import static utilities.Utility.showAlert;

/**
 * This class shows the connected user's profile and allows to modify user's data.
 */
public class ProfileController {

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
        this.userFactory = ConfigurationSingleton.getUserFactory();
        this.userUcc = ConfigurationSingleton.getUserUcc();
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
        Utility.showEula();
    }

    /**
     * Proceed with modification and check fields.
     */
    @FXML
    public void handleModifyButton() {
        try {
            String salt;
            String pw_hash;
            if (passwordTF.getText() != null && !passwordTF.getText().trim().isEmpty()) {
                String passwordText = passwordTF.getText();
                salt = BCrypt.gensalt(12);
                pw_hash = BCrypt.hashpw(passwordText, BCrypt.gensalt());
            } else {
                salt = connectedUser.getSalt();
                pw_hash = connectedUser.getPassword();
                //add temp password to fields for user data check
                passwordTF.setText("random");
                secondPasswordTF.setText("random");
            }
            Utility.checkUserData(firstnameTF.getText().replaceAll(Utility.WHITE_SPACES_PATTERN, ""), lastnameTF.getText().replaceAll(Utility.WHITE_SPACES_PATTERN, ""), emailTF.getText(), passwordTF.getText(), secondPasswordTF.getText(), phoneTF.getText());
            String phoneText = phoneTF.getText();
            String emailText = emailTF.getText();

            String lastnameText = lastnameTF.getText().replaceAll(Utility.WHITE_SPACES_PATTERN, "");
            String firstnameText = firstnameTF.getText().replaceAll(Utility.WHITE_SPACES_PATTERN, "");

            UserDTO user = userFactory.createUser(0, firstnameText, lastnameText, emailText, phoneText, pw_hash, salt, Utility.getTimeStamp());
            userUcc.updateUserInfo(user);
            showAlert(Alert.AlertType.CONFIRMATION, "Account update", "Success", "Information successfully updated");
        } catch (BizzException e) {
            //Update failed on dao lvl
            System.out.println("Update Failed on business lvl");
            showAlert(Alert.AlertType.WARNING, "Account update", "Business Error", e.getMessage());
        } catch (FatalException e) {
            //Update failed on dao lvl
            System.out.println("Update Failed on DAL/DAO lvl");
            e.printStackTrace();
            showAlert(Alert.AlertType.WARNING, "Account update", "Unexpected Error", e.getMessage());
        } finally {
            viewSwitcher.switchView(ViewName.DASHBOARD);
        }
    }

    /**
     * Filter for the phone number field to only allow integers.
     */
    public void allowIntegersOnly() {
        TextFormatter<String> textFormatter = new TextFormatter<>(Utility.textFormatterUnary());
        phoneTF.setTextFormatter(textFormatter);
    }

    /**
     * Required to load view.
     *
     * @param viewSwitcher the object responsible for the change in views
     */
    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }
}
