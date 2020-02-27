package view.dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import view.ViewName;
import view.ViewSwitcher;

public class DashboardController {
    @FXML
    public Button bt_disconnect;

    @FXML
    public Button bt_profile;

    ViewSwitcher viewSwitcher;

    public void handleProfileButton(){viewSwitcher.switchView(ViewName.PROFILE);}

    public void handleDisconnectButton(){
        viewSwitcher.switchView(ViewName.LOGIN);
    }

    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }
}
