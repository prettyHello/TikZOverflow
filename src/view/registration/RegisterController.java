package view.registration;

import business.DTO.UserDTO;
import business.UCC.UserUCC;
import business.UCC.UserUCCImpl;
import utilities.Utility;
import business.factories.UserFactoryImpl;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import persistence.DALServices;
import persistence.DALServicesImpl;
import persistence.UserDAOImpl;

import java.time.Clock;
import java.util.regex.Pattern;

public class RegisterController {

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

    String firstnameText;

    String lastnameText;

    String emailText;

    String phoneText;

    String passwordText;

    private String unallowedCharactersPattern = "[\\\\|@#~€¬\\[\\]{}!\"·$%&\\/()=?¿^*¨;:_`\\+´,.-]";
    private String whitespacesPattern = "^[\\s]+|[\\s]+$";

    public void registerBtn(){


        UserFactoryImpl dto = new UserFactoryImpl();
        UserDTO user = dto.createUser(0,"firstname","lastname","email2","ring ring2","password","random blob",Utility.getTimeStamp());
        DALServices dal = new DALServicesImpl();
        UserDAOImpl dao = new UserDAOImpl(dal,dto);
        UserUCC userUcc = new UserUCCImpl(dal,dao);
        userUcc.register(user);

        System.out.println("Register");

        this.checkFirstName();
        this.checkLastName();
    }

    public boolean checkFirstName() {

        if (firstnameTF.getText().matches(this.unallowedCharactersPattern))
            return false;
        else
            // Remove whitespaces at the beginning and at the end
            this.firstnameText = this.firstnameTF.getText().replaceAll(this.whitespacesPattern, "");

        return true;

    }

    public boolean checkLastName() {

        if (lastnameTF.getText().matches(this.unallowedCharactersPattern))
            return false;
        else
            // Remove whitespaces at the beginning and at the end
            this.lastnameText = this.lastnameTF.getText().replaceAll(this.whitespacesPattern, "");

        return true;
    }

    public boolean checkEmail() { System.out.println("Register"); return false; }

    public boolean checkPhone() { System.out.println("Register"); return false; }
}
