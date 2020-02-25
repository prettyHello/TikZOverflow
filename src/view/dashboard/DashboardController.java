package view.dashboard;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import view.ViewSwitcher;

public class DashboardController {

    private ViewSwitcher viewSwitcher;

    @FXML
    private ListView<String> projectList;

    @FXML
    private ListView<String> optionList;

    @FXML
    public Button button;

    private ObservableList<String> itemList;



    public DashboardController(){
        projectList = new ListView<>();
    }

    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }

    public void printGarbage(){

    }

    public void initialize(){
        itemList = FXCollections.observableArrayList();

        itemList.add("project");
        itemList.add("project 2");
        itemList.add("project 3");

        projectList.setItems(itemList);

        itemList = FXCollections.observableArrayList();

        itemList.add("create new project");
        itemList.add("Your projects");
        itemList.add("Shared with you");

        optionList.setItems(itemList);
    }




}
