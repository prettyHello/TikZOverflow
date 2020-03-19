package view.editor;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import view.ViewSwitcher;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class EditorController {


    private static final String SQUARE = "SQUARE";
    private static final String TRIANGLE = "TRIANGLE";
    private static final String CIRCLE = "CIRCLE";
    private static final String LINE = "LINE";
    private static final String ARROW = "ARROW";

    private ViewSwitcher viewSwitcher;
    @FXML
    private Canvas canvas;
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

    private  GraphicsContext gc;
    private List<Shape> selectedShapes = new ArrayList<>();
    private double selected_x = 30;
    private double selected_y = 30;
    private String selected_shape = "";


    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }

    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        canvas.setOnMouseClicked((MouseEvent event) ->
        {
            selected_x = event.getX();
            selected_y = event.getY();
            draw();
        });



    }

    @FXML
     void drawLine() {
        this.selected_shape = LINE;
    }

    @FXML
     void drawSquare() {
        this.selected_shape = SQUARE;
    }

    @FXML
     void drawCircle() {
        this.selected_shape = CIRCLE;
    }

    @FXML
    void drawTriangle() {
        this.selected_shape = TRIANGLE;

    }

    @FXML
    void drawArrow() {
        this.selected_shape = ARROW;
    }

    @FXML
    void delete() {
        System.out.println("no del");
    }

    private  void draw(){
        switch (selected_shape){
            case TRIANGLE:
                gc.lineTo(150.5, 30.5);
                gc.lineTo(150.5, 150.5);
                gc.lineTo(30.5, 30.5);
                break;
            case CIRCLE:
                gc.fillOval(selected_x, selected_y, 50, 50);
                break;
            case ARROW:
                System.out.println("no arro");
                break;
            case LINE:
                Line line = new Line(selected_x, selected_y, 150, 50);
                break;
            case SQUARE:
                //gc.fillRect(selected_x, selected_y, 50, 50);
                Rectangle rect = new Rectangle(120, 75);
                break;
            default:
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Select a shape");
                alert.setHeaderText("You need to select a shape");
                alert.setContentText("You need to select a shape first!");
                alert.showAndWait();

        }
    }



//gc.beginPath();
// gc.moveTo(selected_x, selected_y);
//gc.stroke();


}

