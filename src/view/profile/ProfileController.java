package view.profile;

import business.DTO.UserDTO;
import business.UCC.UserUCC;
import business.UCC.UserUCCImpl;
import business.factories.UserFactoryImpl;
import exceptions.BizzException;
import exceptions.FatalException;
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

//import javax.rmi.CORBA.Util;
import java.util.ArrayList;
import java.util.function.UnaryOperator;

import static utilities.Utility.showAlert;

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
    Button bt_eula;

    @FXML
    Button bt_modify;

    @FXML
    Button bt_cancel;

    String firstnameText;

    String lastnameText;

    String emailText;

    String phoneText;

    String passwordText;

    private ViewSwitcher viewSwitcher;

    @FXML
    public void initialize() {

        //TODO We need a way to know wich user we are talking about
        UserFactoryImpl userFactory = new UserFactoryImpl();
        DALServices dal = new DALServicesImpl();
        UserDAOImpl dao = new UserDAOImpl(dal, userFactory);
        UserUCC userUcc = new UserUCCImpl(dal, dao);
        UserDTO user = userUcc.getConnectedUser();
        prefillFields(user);

        borderpane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    handleModifyButton();
                }
            }
        });
    }

    private void prefillFields(UserDTO user) {
        firstnameTF.setText(user.getFirst_name());
        lastnameTF.setText(user.getLast_name());
        emailTF.setText(user.getEmail());
        phoneTF.setText(user.getPhone());
        emailTF.setDisable(true);
    }

    //TODO INSERT LOGIC
    public void handleCancelButton() {
        System.out.println("INSERT LOGIC HERE");
        viewSwitcher.switchView(ViewName.DASHBOARD);
    }

    public void handleReadEulaButton() {
        Utility.showEula();
    }

    @FXML
    public void handleModifyButton() {
        try {
            Utility.checkUserData(this.firstnameTF.getText().replaceAll(Utility.WHITE_SPACES_PATTERN, ""), this.lastnameTF.getText().replaceAll(Utility.WHITE_SPACES_PATTERN, ""), this.emailTF.getText(), this.passwordTF.getText(), this.secondPasswordTF.getText(), this.phoneTF.getText());
            this.phoneText = this.phoneTF.getText();
            this.emailText = this.emailTF.getText();
            this.passwordText = this.passwordTF.getText();
            this.lastnameText = this.lastnameTF.getText().replaceAll(Utility.WHITE_SPACES_PATTERN, "");
            this.firstnameText = this.firstnameTF.getText().replaceAll(Utility.WHITE_SPACES_PATTERN, "");
            UserFactoryImpl userFactory = new UserFactoryImpl();
            String salt = BCrypt.gensalt(12);
            String pw_hash = BCrypt.hashpw(passwordText, BCrypt.gensalt());
            UserDTO user = userFactory.createUser(0, firstnameText, lastnameText, emailText, phoneText, pw_hash, salt, Utility.getTimeStamp());
            DALServices dal = new DALServicesImpl();
            UserDAOImpl dao = new UserDAOImpl(dal, userFactory);
            UserUCC userUcc = new UserUCCImpl(dal, dao);
            userUcc.updateUserInfo(user);
            showAlert(Alert.AlertType.CONFIRMATION, "Account update", "Success", "Information succesfully updated");
        } catch (BizzException e) {
            //Update failed on dao lvl
            System.out.println("Update Failed on buisness lvl");
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
