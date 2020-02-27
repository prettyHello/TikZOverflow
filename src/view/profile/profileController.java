package view.profile;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import view.ViewName;
import view.ViewSwitcher;

public class profileController {

    @FXML
    Button bt_modify;

    @FXML
    Button bt_cancel;

    //TODO INSERT LOGIC
    public void handleCancelButton(){
        System.out.println("INSERT LOGIC HERE");
        viewSwitcher.switchView(ViewName.DASHBOARD);
    }

    public void handleModifyButton(){
        System.out.println("INSERT LOGIC HERE");
    }



    private ViewSwitcher viewSwitcher;
}
