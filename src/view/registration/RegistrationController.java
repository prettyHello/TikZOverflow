package view.registration;

import business.DTO.UserDTO;
import business.UCC.UserUCC;
import business.UCC.UserUCCImpl;
import utilities.Utility;
import business.factories.UserFactoryImpl;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import view.ViewSwitcher;
import persistence.DALServices;
import persistence.DALServicesImpl;
import persistence.UserDAOImpl;

import java.time.Clock;
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

    private ViewSwitcher viewSwitcher;
    String firstnameText;

    String lastnameText;

    String emailText;

    String phoneText;

    String passwordText;

    private String unallowedCharactersPattern = "[\\\\|@#~€¬\\[\\]{}!\"·$%&\\/()=?¿^*¨;:_`\\+´,.-]";

    private String whitespacesPattern = "^[\\s]+|[\\s]+$";

    private String emailPattern = "(?:[a-z0-9!#$%&'*+\\/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+\\/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public void registerBtn(){


        /*UserFactoryImpl dto = new UserFactoryImpl();
        UserDTO user = dto.createUser(0,"firstname","lastname","email2","ring ring2","password","random blob",Utility.getTimeStamp());
        DALServices dal = new DALServicesImpl();
        UserDAOImpl dao = new UserDAOImpl(dal,dto);
        UserUCC userUcc = new UserUCCImpl(dal,dao);
        userUcc.register(user);*/

        System.out.println("Register");

        this.checkFirstName();
        this.checkLastName();
        this.checkEmail();
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

    public boolean checkLastName() {
        System.out.println("Checking lastname");
        if (this.lastnameTF.getText().isEmpty() || this.lastnameTF.getText().matches(this.unallowedCharactersPattern))
            return false;
        else
            // Remove whitespaces at the beginning and at the end
            this.lastnameText = this.lastnameTF.getText().replaceAll(this.whitespacesPattern, "");

        return true;
    }

    public boolean checkEmail() {
        System.out.println("Checking email");
        if (this.emailTF.getText().matches(this.emailPattern)) {
            this.emailText = this.emailTF.getText();
            return true;
        }

        return false;

    }

    public boolean checkPhone() { System.out.println("Register"); return false; }

    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }
}
