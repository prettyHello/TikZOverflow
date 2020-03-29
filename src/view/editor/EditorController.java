package view.editor;

import business.Canvas.ActiveCanvas;
import business.Canvas.Canvas;
import business.DTO.ProjectDTO;
import business.DTO.UserDTO;
import business.shape.Coordinates;
import business.shape.Square;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import persistence.SaveObject;
import utilities.ColorUtils;
import view.ViewName;
import view.ViewSwitcher;
import view.dashboard.DashboardController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;

import static utilities.ColorUtils.getColorNameFromRgb;

public class EditorController {


    /**
     * TODO We need to divide teh fxml so that we have separate controller for this one and the other view
     */

    private static final String SQUARE = "SQUARE";
    private static final String TRIANGLE = "TRIANGLE";
    private static final String TRIANGLE_POINT2 = "TRIANGLE2";
    private static final String TRIANGLE_POINT3 = "TRIANGLE3";
    private static final String CIRCLE = "CIRCLE";
    private static final String LINE = "LINE";
    private static final String LINE_POINT2 = "LINE_POINT2";
    private static final String ARROW = "ARROW";
    private static final String ARROW_POINT2 = "ARROW_POINT2";

    private ViewSwitcher viewSwitcher;


    @FXML
    Pane toolbar;
    @FXML
    private Pane pane;
    @FXML
    private TextArea tikzTA;
    @FXML
    Button square;
    @FXML
    Button circle;
    @FXML
    Button line;
    @FXML
    Button arrow;
    @FXML
    Button triangle;
    @FXML
    Button delete;
    @FXML
    ChoiceBox fillColour;
    @FXML
    ChoiceBox strokeColour;
    @FXML
    SplitPane mainSplitPane;
    @FXML
    AnchorPane leftAnchorPane;

    private ArrayList<Shape> selectedShapes = new ArrayList<>();
    private double selected_x, selected_y;
    private double previously_selected_x, previously_selected_y; // a line needs 2 points so last choice is saved
    private double third_selected_x, third_selected_y; // since a triangle need three points
    private String selected_shape = "";
    private boolean waiting_for_more_coordinate = false;
    private Canvas canvas = ActiveCanvas.getActiveCanvas();

    private ContextMenu shapeContextMenu;
    private ChoiceBox contextMenuColorPicker;

    ProjectDTO projectDTO;
    public EditorController setNewProject(ProjectDTO projectDTO)  {
        this.projectDTO = projectDTO;
        return this;
    }

    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }

    @FXML
    public void initialize() {
        pane.setOnMouseClicked((MouseEvent event) ->
        {
            if (selectedShapes.isEmpty() && !selected_shape.equals("")) { // don't forget
                selected_x = event.getX();
                selected_y = event.getY();
                handle_draw_call();
            }
        });

        // to make the split pane non resizeable
        leftAnchorPane.maxWidthProperty().bind(mainSplitPane.widthProperty().multiply(0.5));
        leftAnchorPane.minWidthProperty().bind(mainSplitPane.widthProperty().multiply(0.5));



        contextMenuColorPicker = new ChoiceBox();

        for (business.shape.Color colour : business.shape.Color.values()) {
            fillColour.getItems().add(colour);
            strokeColour.getItems().add(colour);
            contextMenuColorPicker.getItems().add(colour);
        }

        fillColour.setValue(business.shape.Color.BLACK);
        strokeColour.setValue(business.shape.Color.BLACK);
        contextMenuColorPicker.setValue(business.shape.Color.BLACK);

        MenuItem delete = new MenuItem("delete");
        delete.setOnAction(t -> rightClickDeleteShape());
        MenuItem color = new MenuItem("colour", contextMenuColorPicker);
        color.setOnAction(t -> setColor());
        shapeContextMenu = new ContextMenu(delete, color);

    }

    private void setColor() {
        //TODO use canvas.updateShape to update the shape.
        if (shapeContextMenu.getOwnerNode() instanceof Shape) {
            Shape shape = (Shape) shapeContextMenu.getOwnerNode();
            shape.setFill(Color.valueOf(contextMenuColorPicker.getValue().toString()));
        }
        translateToTikz();
    }

    private void rightClickDeleteShape() {
        if (shapeContextMenu.getOwnerNode() instanceof Shape) {
            Shape shape = (Shape) shapeContextMenu.getOwnerNode();
            pane.getChildren().remove(shape);
            canvas.rmShapeById(Integer.parseInt(shape.getId()));
            if(selectedShapes.contains(shape)){
                selectedShapes.remove(shape);
                if (selectedShapes.isEmpty())
                    disableToolbar(false);
            }
        }
        translateToTikz();
    }

    @FXML
    void drawLine() {
        if (!checkIfMoreCoordinateRequired()) {
            this.selected_shape = LINE;
        }
    }

    @FXML
    void drawSquare() {
        if (!checkIfMoreCoordinateRequired()) {
            this.selected_shape = SQUARE;
        }
    }

    @FXML
    void drawCircle() {
        if (!checkIfMoreCoordinateRequired()) {
            this.selected_shape = CIRCLE;
        }
    }

    @FXML
    void drawTriangle() {
        if (!checkIfMoreCoordinateRequired()) {
            this.selected_shape = TRIANGLE;
        }
    }

    @FXML
    void drawArrow() {
        if (!checkIfMoreCoordinateRequired()) {
            this.selected_shape = ARROW;
        }
    }

    @FXML
    void delete() {
        if (!checkIfMoreCoordinateRequired()) {
            if (!selectedShapes.isEmpty()) {
                Iterator<Shape> it = selectedShapes.iterator();
                while (it.hasNext()) {
                    Shape s = it.next();
                    pane.getChildren().remove(s);
                    it.remove();
                    canvas.rmShapeById(Integer.parseInt(s.getId()));
                    disableToolbar(false);
                }
            }
            delete.setStyle("-fx-focus-color: transparent;");
        }
        translateToTikz();
    }

    private void handle_draw_call() {
        Shape shape = null;
        business.shape.Shape addToModel = null;

        switch (selected_shape) {
            case TRIANGLE:
                third_selected_x = selected_x;
                third_selected_y = selected_y;
                selected_shape = TRIANGLE_POINT2;
                waiting_for_more_coordinate = true;
                break;
            case TRIANGLE_POINT2:
                previously_selected_x = selected_x;
                previously_selected_y = selected_y;
                selected_shape = TRIANGLE_POINT3;
                waiting_for_more_coordinate = true;
                break;
            case TRIANGLE_POINT3:
                addToModel = new business.shape.Triangle(new Coordinates(selected_x, selected_y),canvas.getIdForNewShape());
                shape = constructTriangle();
                waiting_for_more_coordinate = false;
                break;
            case CIRCLE:
                float radius = 50.0f;
                Circle circle = new Circle();
                circle.setCenterX(selected_x);
                circle.setCenterY(selected_y);
                circle.setRadius(radius);
                addToModel = new business.shape.Circle(new Coordinates(selected_x, selected_y), radius,canvas.getIdForNewShape());
                shape = circle;
                break;
            case ARROW:
                previously_selected_x = selected_x;
                previously_selected_y = selected_y;
                selected_shape = ARROW_POINT2;
                waiting_for_more_coordinate = true;
                break;
            case ARROW_POINT2:
                addToModel = new business.shape.Arrow(new Coordinates(previously_selected_x, previously_selected_y), new Coordinates(selected_x, selected_y),canvas.getIdForNewShape());
                shape = constructArrow();
                waiting_for_more_coordinate = false;
                break;
            case LINE:
                previously_selected_x = selected_x;
                previously_selected_y = selected_y;
                selected_shape = LINE_POINT2;
                waiting_for_more_coordinate = true;
                break;
            case LINE_POINT2:
                addToModel = new business.shape.Line(new Coordinates(previously_selected_x, previously_selected_y), new Coordinates(selected_x, selected_y),canvas.getIdForNewShape());
                shape = new Line(previously_selected_x, previously_selected_y, selected_x, selected_y);
                shape.setStroke(Color.valueOf(fillColour.getValue().toString()));
                waiting_for_more_coordinate = false;
                break;
            case SQUARE:
                int size = 75;
                shape = new Rectangle(selected_x, selected_y, 75, 75);
                addToModel = new Square(new Coordinates(selected_x, selected_y), size,canvas.getIdForNewShape());
                break;
        }
        if (waiting_for_more_coordinate) {
            return;
        } else if (shape == null) { //No shape was previously selected
            alert("Select a shape", "You need to select a shape", "You need to select a shape first!");
        } else {
            shape.setFill(Color.valueOf(fillColour.getValue().toString()));
            shape.setStroke(Color.valueOf(strokeColour.getValue().toString()));
            pane.getChildren().add(shape);
            notifyModel(addToModel, shape);
            shape.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onShapeSelected); //add a listener allowing us to know if a shape was selected
            selected_shape = "";
        }
        disableButtonOverlay();
    }

    private void notifyModel(business.shape.Shape addToModel, Shape shape) {
        Color fillColor =  (Color) shape.getFill();
        Color drawColor = (Color) shape.getStroke();
        shape.setId(Integer.toString(addToModel.getId()));
        if(drawColor!=null){
            addToModel.setDraw(true);
            addToModel.setDrawColor(getColorNameFromRgb(drawColor.getRed(), drawColor.getGreen(), drawColor.getBlue()));
        }else{
            addToModel.setDraw(false);
        }
        if(fillColor!=null){
            addToModel.setFill(true);
            addToModel.setFillColor(getColorNameFromRgb(fillColor.getRed(), fillColor.getGreen(),fillColor.getBlue()));
        }else{
            addToModel.setFill(false);
        }
        canvas.addShape(addToModel); //warn the model
        translateToTikz();
    }

    ContextMenu menu = new ContextMenu();


    private void onShapeSelected(MouseEvent mouseEvent) {
        if (!waiting_for_more_coordinate && selected_shape =="") {
            Shape shape = (Shape) mouseEvent.getSource();

            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (selectedShapes.contains(shape)) { //if already selected => unselect
                    shape.setEffect(null);
                    selectedShapes.remove(shape);
                    if (selectedShapes.isEmpty())
                        disableToolbar(false);
                } else {                                 //if not selected => add to the list
                    disableToolbar(true);
                    shape.setStrokeWidth(2);
                    DropShadow borderEffect = new DropShadow(
                            BlurType.THREE_PASS_BOX, Color.GREEN, 2, 1, 0, 0
                    );
                    shape.setEffect(borderEffect);
                    selectedShapes.add(shape);
                }
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                shape.setOnContextMenuRequested(t -> shapeContextMenu.show(shape, mouseEvent.getScreenX(), mouseEvent.getScreenY()));
            }
        }
    }

    /**
     * Disables all the buttons except the delete button when a shape is selected
     * Gets all children in case new buttons are added
     *
     * @param isDisabled disables the buttons when true
     */
    private void disableToolbar(boolean isDisabled) {
        for (Node node : toolbar.getChildren()) {
            node.setDisable(isDisabled);
        }
        delete.setDisable(false);
    }

    private void disableButtonOverlay() {
        square.setStyle("-fx-focus-color: transparent;");
        circle.setStyle("-fx-focus-color: transparent;");
        line.setStyle("-fx-focus-color: transparent;");
        arrow.setStyle("-fx-focus-color: transparent;");
        triangle.setStyle("-fx-focus-color: transparent;");
    }

    private boolean checkIfMoreCoordinateRequired() {
        if (waiting_for_more_coordinate) {
            alert("Finish your action", "You need to select a second point", "You need to select a second point to finish the last shape!");
            disableButtonOverlay();
            return true;
        }
        return false;
    }

    private Shape constructArrow() {
        double arrowLength = 5;
        double arrowWidth = 3;
        Line main_line = new Line(previously_selected_x, previously_selected_y, selected_x, selected_y);
        Line arrow1 = new Line();
        Line arrow2 = new Line();

        arrow1.setEndX(selected_x);
        arrow1.setEndY(selected_y);
        arrow2.setEndX(selected_x);
        arrow2.setEndY(selected_y);

        if (selected_x == previously_selected_x && selected_y == previously_selected_y) {
            // arrow parts of length 0
            arrow1.setStartX(selected_x);
            arrow1.setStartY(selected_y);
            arrow2.setStartX(selected_x);
            arrow2.setStartY(selected_y);
        } else {
            double factor = arrowLength / Math.hypot(previously_selected_x - selected_x, previously_selected_y - selected_y);
            double factorO = arrowWidth / Math.hypot(previously_selected_x - selected_x, previously_selected_y - selected_y);

            // part in direction of main line
            double dx = (previously_selected_x - selected_x) * factor;
            double dy = (previously_selected_y - selected_y) * factor;

            // part ortogonal to main line
            double ox = (previously_selected_x - selected_x) * factorO;
            double oy = (previously_selected_y - selected_y) * factorO;

            arrow1.setStartX(selected_x + dx - oy);
            arrow1.setStartY(selected_y + dy + ox);
            arrow2.setStartX(selected_x + dx + oy);
            arrow2.setStartY(selected_y + dy - ox);
        }

        return Shape.union(main_line, Shape.union(arrow1, arrow2));
    }

    private Shape constructTriangle() {
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(new Double[]{
                selected_x, selected_y,
                previously_selected_x, previously_selected_y,
                third_selected_x, third_selected_y });
        return  polygon;
    }

    private void alert(String title, String header, String Content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(Content);
        alert.showAndWait();
    }

    public void save(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        SaveObject saveObject = new SaveObject();
        saveObject.save(canvas,projectDTO.getProjectName());
    }

    public void close(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close project");
        alert.setHeaderText("Do you want to save your project?");

        ButtonType buttonTypeOne = new ButtonType("Yes");
        ButtonType buttonTypeTwo = new ButtonType("No");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            SaveObject saveObject = new SaveObject();
            saveObject.save(canvas,projectDTO.getProjectName());
            viewSwitcher.switchView(ViewName.DASHBOARD);
        } else if (result.get() == buttonTypeTwo) {
            viewSwitcher.switchView(ViewName.DASHBOARD);
        }
    }

    private void translateToTikz(){
        tikzTA.setText(canvas.toTikZ());
    }
}

