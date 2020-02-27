package view.dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import view.ViewName;
import view.ViewSwitcher;

public class DashboardController {
    @FXML
    public Button bt_disconnect;

    ViewSwitcher viewSwitcher;

    public void handleDisconnectButton(){
        viewSwitcher.switchView(ViewName.LOGIN);
    }

    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }
}
