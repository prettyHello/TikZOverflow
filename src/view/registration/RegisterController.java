package view.registration;

import business.DTO.UserDTO;
import utilities.Utility;
import business.factories.UserFactoryImpl;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import persistence.DALServices;
import persistence.DALServicesImpl;
import persistence.UserDAOImpl;

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




    public void registerBtn(){

        UserFactoryImpl dto = new UserFactoryImpl();
        UserDTO user = dto.createUser(0,"firstname","lastname","email","ring ring","password","random salt",Utility.getTimeStamp());
        DALServices dal = new DALServicesImpl();
        UserDAOImpl dao = new UserDAOImpl(dal,dto);
        dao.create(user);
        System.out.println("Register");
    }
}
