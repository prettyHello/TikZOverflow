package view.editor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import persistence.SaveObject;
import view.ViewName;
import view.ViewSwitcher;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import static java.awt.Color.white;

public class EditorController {


    /**
     * We need to divide teh fxml so that we have separate controller for this one and the other view
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
    ColorPicker fillColour;
    @FXML
    ColorPicker strokeColour;


    private GraphicsContext gc;
    private ArrayList<Shape> selectedShapes = new ArrayList<>();
    private double selected_x, selected_y;
    private double previously_selected_x, previously_selected_y; //a line need 2 points so last choice is saved
    private double third_selected_x, third_selected_y;//since a triangle need three points
    private String selected_shape = "";
    private boolean waiting_for_more_coordinate = false;


    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }

    @FXML
    public void initialize() {

        pane.setOnMouseClicked((MouseEvent event) ->
        {
            if (selectedShapes.isEmpty() && selected_shape != "") { // don't forget
                selected_x = event.getX();
                selected_y = event.getY();
                draw();
            }
        });
    }

    //menuBar

    /**
     * Handles the menu item when the user wants to quit. Does not save progress
     */
    @FXML
    void close() {
        String title = "Quit without saving?";
        String header = "All progress will be lost";
        String content = "Are you ok with this?";
        alert(title, header, content, Alert.AlertType.CONFIRMATION);
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
                    disableToolbar(false);
                }
            }
            delete.setStyle("-fx-focus-color: transparent;");
        }
    }

    private void draw() {
        Shape shape = null;

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
                shape = constructTriangle();
                waiting_for_more_coordinate = false;
                break;
            case CIRCLE:
                Circle circle = new Circle();
                circle.setCenterX(selected_x);
                circle.setCenterY(selected_y);
                circle.setRadius(50.0f);
                shape = circle;
                break;
            case ARROW:
                previously_selected_x = selected_x;
                previously_selected_y = selected_y;
                selected_shape = ARROW_POINT2;
                waiting_for_more_coordinate = true;
                break;
            case ARROW_POINT2:
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
                shape = new Line(previously_selected_x, previously_selected_y, selected_x, selected_y);
                shape.setStroke(fillColour.getValue());
                waiting_for_more_coordinate = false;
                break;
            case SQUARE:
                shape = new Rectangle(selected_x, selected_y, 75, 75);
                break;
        }
        if (waiting_for_more_coordinate) {
            return;
        } else if (shape == null) { //No shape was previously selected
            alert("Select a shape", "You need to select a shape", "You need to select a shape first!", Alert.AlertType.INFORMATION);
        } else {
            shape.setStroke(strokeColour.getValue());
            shape.setFill(fillColour.getValue());
            pane.getChildren().add(shape);
            shape.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> onShapeSelected(e)); //add a listener allowing us to know if a shape was selected
            selected_shape = "";
        }
        disableButtonOverlay();

    }

    private void onShapeSelected(MouseEvent e) {

        Shape shape = (Shape) e.getSource();
        if (selectedShapes.contains(shape)) { //if already selected => unselect
            disableToolbar(false);
            shape.setStroke(Color.TRANSPARENT);
            selectedShapes.remove(shape);
            System.out.println("unselected");
        } else {                                 //if not selected => add to the list
            disableToolbar(true);
            shape.setStroke(Color.GREEN);
            selectedShapes.add(shape);
            System.out.println("selected");
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
            alert("Finish your action", "You need to select a second point", "You need to select a second point to finish the last shape!", Alert.AlertType.INFORMATION);
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
        Line line1 = new Line(previously_selected_x, previously_selected_y, selected_x, selected_y);
        Line line2 = new Line(previously_selected_x, previously_selected_y, third_selected_x, third_selected_y);
        Line line3 = new Line(selected_x, selected_y, third_selected_x, third_selected_y);
        return Shape.union(line1, Shape.union(line2, line3));
    }

    private void alert(String title, String header, String Content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(Content);
        Optional<ButtonType> result = alert.showAndWait();
        if (type == Alert.AlertType.CONFIRMATION) {
            if (result.get() == ButtonType.OK) {
                viewSwitcher.switchView(ViewName.DASHBOARD);
            }
        }

    }

    public void save(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        business.Canvas.CanvasImpl canvas = new business.Canvas.CanvasImpl(200,200);
        business.shape.Coordinates coord = new business.shape.Coordinates(20,4);
        System.out.println("Save Function");
        SaveObject saveObject = new SaveObject();
        business.shape.Circle circle = new business.shape.Circle(true,true,business.shape.Color.GREEN,business.shape.Color.RED,coord,5);
        canvas.addShape(circle);
        saveObject.save(canvas,"myfile");
    }
}

