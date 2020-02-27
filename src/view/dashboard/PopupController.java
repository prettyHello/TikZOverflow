package view.dashboard;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class PopupController {

    public static void projectExists(String title, String message) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle(title);
        popupStage.setMinWidth(250);
        Label label = new Label();
        label.setText(message);
        Button cancel = new Button("Cancel");

        cancel.setOnAction(e -> {
            popupStage.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, cancel);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }
}
