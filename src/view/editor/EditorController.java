package view.editor;

import config.ConfigurationSingleton;
import controller.Canvas.ActiveCanvas;
import controller.Canvas.Canvas;
import controller.DTO.UserDTO;
import controller.UCC.ProjectUCC;
import controller.UCC.UserUCC;
import controller.shape.Coordinates;
import controller.shape.Square;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import utilities.exceptions.FatalException;
import javafx.util.Pair;
import view.ViewName;
import view.ViewSwitcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static utilities.ColorUtils.getColorNameFromRgb;
import static utilities.Utility.showAlert;

/**
 * This class is used to handle drawings and their corresponding tikz translation.
 *
 */
public class EditorController {

    /**
     * TODO We need to divide teh fxml so that we have separate controller for this one and the other view
     */
    protected static final String SQUARE = "SQUARE";
    protected static final String TRIANGLE = "TRIANGLE";
    protected static final String TRIANGLE_POINT2 = "TRIANGLE2";
    protected static final String TRIANGLE_POINT3 = "TRIANGLE3";
    protected static final String CIRCLE = "CIRCLE";
    protected static final String LINE = "LINE";
    protected static final String LINE_POINT2 = "LINE_POINT2";
    protected static final String ARROW = "ARROW";
    protected static final String ARROW_POINT2 = "ARROW_POINT2";
    private UserUCC userUcc;

    private ViewSwitcher viewSwitcher;

    private ShapeHandler shapeHandler;

    private ProjectUCC projectUcc = ConfigurationSingleton.getProjectUCC();

    @FXML
    Pane toolbar;
    @FXML
    public Pane pane;
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


    public ArrayList<Shape> selectedShapes = new ArrayList<>();
    protected String shapeToDraw = "";
    protected boolean waitingForMoreCoordinate = false;
    protected Canvas canvas;
    private String colorsPattern = "";
    private ContextMenu shapeContextMenu;
    private ChoiceBox contextMenuFillColorPicker;
    private ChoiceBox contextMenuDrawColorPicker;

    public EditorController() {
        this.userUcc = ConfigurationSingleton.getUserUcc();
        this.canvas = ActiveCanvas.getActiveCanvas();
    }

    /**
     * Required to load view
     *
     * @param viewSwitcher
     */
    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }


    @FXML
    public void initialize() {

        // Get coordinate of click in canvas and draw selected shape
        pane.setOnMouseClicked((MouseEvent event) ->
        {
            // No shapes must be selected and a drawing shape has to be selected
            if (selectedShapes.isEmpty() && !shapeToDraw.equals("")) {
                shapeHandler.selectedX = event.getX();
                shapeHandler.selectedY = event.getY();
                shapeHandler.handleDrawCall();
            }
        });

        // to make the split pane non resizeable
        leftAnchorPane.maxWidthProperty().bind(mainSplitPane.widthProperty().multiply(0.5));
        leftAnchorPane.minWidthProperty().bind(mainSplitPane.widthProperty().multiply(0.5));


        contextMenuFillColorPicker = new ChoiceBox();
        contextMenuDrawColorPicker = new ChoiceBox();


        // Fill dropdowns (fill & stroke & context) with appropriate colors
        ArrayList<String> colors = new ArrayList<>();
        for (controller.shape.Color colour : controller.shape.Color.values()) {
            fillColour.getItems().add(colour);
            strokeColour.getItems().add(colour);
            contextMenuFillColorPicker.getItems().add(colour);
            contextMenuDrawColorPicker.getItems().add(colour);
            colors.add(colour.getValue());
        }
        colorsPattern = String.join("|", colors);

        // Set start value dropdown to black
        fillColour.setValue(controller.shape.Color.BLACK);
        strokeColour.setValue(controller.shape.Color.BLACK);
        contextMenuFillColorPicker.setValue(controller.shape.Color.BLACK);
        contextMenuDrawColorPicker.setValue(controller.shape.Color.BLACK);

        MenuItem delete = new MenuItem("delete");
        delete.setOnAction(t -> shapeHandler.rightClickDeleteShape());
        MenuItem fillColorMenu = new MenuItem("Fill color", contextMenuFillColorPicker);
        fillColorMenu.setOnAction(t -> shapeHandler.setFillColor(Color.valueOf(contextMenuFillColorPicker.getValue().toString())));
        MenuItem drawColorMenu = new MenuItem("Stroke color", contextMenuDrawColorPicker);
        drawColorMenu.setOnAction(t -> shapeHandler.setDrawColor(Color.valueOf(contextMenuDrawColorPicker.getValue().toString())));
        shapeContextMenu = new ContextMenu(delete, fillColorMenu, drawColorMenu);

        shapeHandler = new ShapeHandler(shapeContextMenu, canvas, this);

        // show shapes at the start(don't have to interact to have thel show up)
        tikzTA.textProperty().addListener(this.handleCodeChange);
        translateToTikz();
        //translateToDraw();


        /*tikzTA.textProperty().addListener((obs,old,niu)-> {

        });*/
    }


    @FXML
    void drawLine() {
        if (!checkIfMoreCoordinateRequired()) {
            this.shapeToDraw = LINE;
        }
    }

    @FXML
    void drawSquare() {
        if (!checkIfMoreCoordinateRequired()) {
            this.shapeToDraw = SQUARE;
        }
    }

    @FXML
    void drawCircle() {
        if (!checkIfMoreCoordinateRequired()) {
            this.shapeToDraw = CIRCLE;
        }
    }

    @FXML
    void drawTriangle() {
        if (!checkIfMoreCoordinateRequired()) {
            this.shapeToDraw = TRIANGLE;
        }
    }

    @FXML
    void drawArrow() {
        if (!checkIfMoreCoordinateRequired()) {
            this.shapeToDraw = ARROW;
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

    /**
     * Notify Shape controller of javafx shape creation
     *
     * @param addToController
     * @param shape           JavaFX shape
     */
    public void notifyController(controller.shape.Shape addToController, Shape shape) {
        Color fillColor = (Color) shape.getFill();
        Color drawColor = (Color) shape.getStroke();
        shape.setId(Integer.toString(addToController.getId()));
        if (drawColor != null) {
            addToController.setDraw(true);
            addToController.setDrawColor(getColorNameFromRgb(drawColor.getRed(), drawColor.getGreen(), drawColor.getBlue()));
        } else {
            addToController.setDraw(false);
        }
        if (fillColor != null) {
            addToController.setFill(true);
            addToController.setFillColor(getColorNameFromRgb(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue()));
        } else {
            addToController.setFill(false);
        }
        canvas.addShape(addToController); //warn the model
        translateToTikz();
    }

    /**
     * Disables all the buttons except delete button when shape is selected
     * Gets all children in case new buttons are added
     *
     * @param isDisabled disables the buttons when true
     */
    public void disableToolbar(boolean isDisabled) {
        for (Node node : toolbar.getChildren()) {
            node.setDisable(isDisabled);
        }
        delete.setDisable(false);
    }

    /**
     * Disable the buttons highlight
     */
    public void disableButtonOverlay() {
        square.setStyle("-fx-focus-color: transparent;");
        circle.setStyle("-fx-focus-color: transparent;");
        line.setStyle("-fx-focus-color: transparent;");
        arrow.setStyle("-fx-focus-color: transparent;");
        triangle.setStyle("-fx-focus-color: transparent;");
    }

    /**
     * Show alert
     *
     * @param title
     * @param header
     * @param Content
     */
    public void alert(String title, String header, String Content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(Content);
        alert.showAndWait();
    }

    /**
     * Save project
     *
     * @param actionEvent
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void save(ActionEvent actionEvent) {
        try{
            this.projectUcc.save();
        }catch (FatalException e){
            showAlert(Alert.AlertType.WARNING, "Save", "Unexpected Error", e.getMessage());
        }
    }

    /**
     * Close project and ask if it needs to be saved
     *
     * @param actionEvent
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void close(ActionEvent actionEvent){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close project");
        alert.setHeaderText("Do you want to save your project?");

        UserDTO user = userUcc.getConnectedUser();

        ButtonType buttonTypeOne = new ButtonType("Yes");
        ButtonType buttonTypeTwo = new ButtonType("No");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (!result.isPresent()) {
            return;
        }
        if (result.get() == buttonTypeCancel) {
            return;
        }
        if (result.get() == buttonTypeOne) {
            try{
                this.projectUcc.save();
            }catch (FatalException e){
                showAlert(Alert.AlertType.WARNING, "Save", "Unexpected Error", e.getMessage());
            }
        }

        ActiveCanvas.deleteActiveCanvas();
        viewSwitcher.switchView(ViewName.DASHBOARD);
    }

    /**
     * Check if drawing is finished
     *
     * @return if not show error
     */
    private boolean checkIfMoreCoordinateRequired() {
        if (waitingForMoreCoordinate) {
            alert("Finish your action", "You need to select a second point", "You need to select a second point to finish the last shape!");
            disableButtonOverlay();
            return true;
        }
        return false;
    }

    /**
     * Translate canvas to tikz and fill textarea
     */
    public void translateToTikz() {
        tikzTA.setText(canvas.toTikZ());
    }


    /**
     * Detects and handles changes in the TextArea
     *
     * @param observableValue
     * @param oldValue
     * @param newValue
     */
    private ChangeListener<? super String> handleCodeChange = (observableValue, oldValue, newValue) -> {
        System.out.println("Patata");
        if (!shapeHandler.drawnFromToolbar) {
            String coordinatePattern = "\\((\\d+\\.\\d+),(\\d+\\.\\d+)\\)";
            String squarePattern = "\\\\filldraw\\[fill=(" + colorsPattern + "), draw=(" + colorsPattern + ")\\] " + coordinatePattern + " (\\w+) " + coordinatePattern;
            String circlePattern = "\\\\filldraw\\[fill=(" + colorsPattern + "), draw=(" + colorsPattern + ")\\] " + coordinatePattern + " (\\w+) \\[radius=(\\d+\\.\\d+)\\]";
            String trianglePattern = "\\\\filldraw\\[fill=(" + colorsPattern + "), draw=(" + colorsPattern + ")\\] " + coordinatePattern + " -- " + coordinatePattern + " -- " + coordinatePattern + " -- cycle";
            String pathPattern = "\\\\draw \\[(" + colorsPattern + ")(,->)*\\] " + coordinatePattern + " -- " + coordinatePattern;

            ArrayList<String> patternsArray = new ArrayList<>(Arrays.asList(
                    squarePattern, circlePattern, trianglePattern, pathPattern));

            String[] lines = newValue.split("\\n");
            List<String> al = Arrays.asList(lines);

            boolean lineCorrect = false;
            int incorrectLineNum = -1;
            Pattern p;
            Matcher m;
            for (String line : lines) {
                p = null;
                m = null;
                lineCorrect = false;
                for (String pattern : patternsArray) {
                    p = Pattern.compile(pattern);
                    m = p.matcher(line);
                    if (m.find())
                        lineCorrect = true;
                }
                if (!lineCorrect) {
                    incorrectLineNum = al.indexOf(line);
                    break;
                }
            }
            if (lineCorrect) {
                canvas.clear();
                pane.getChildren().clear();
                for (String line : lines) {
                    sendTikzCode(line);
                }
            }
            else {
                // TODO: Highlight wrong line
                System.out.println("Incorrect line: " + incorrectLineNum);
            }
        }
        else {
            shapeHandler.drawnFromToolbar = false;
        }
    };

    /**
     * Translate code line to controller shape and draw it.
     * @param line
     */
    private void sendTikzCode(String line){
        // Regular expressions of the different shapes
        String coordinatePattern = "\\((\\d+\\.\\d+),(\\d+\\.\\d+)\\)";
        String squarePattern = "\\\\filldraw\\[fill=(\\w+), draw=(\\w+)\\] "+ coordinatePattern +" (\\w+) " + coordinatePattern;
        String circlePattern = "\\\\filldraw\\[fill=(\\w+), draw=(\\w+)\\] "+ coordinatePattern +" (\\w+) \\[radius=(\\d+\\.\\d+)\\]";
        String trianglePattern = "\\\\filldraw\\[fill=(\\w+), draw=(\\w+)\\] " + coordinatePattern + " -- " + coordinatePattern + " -- " + coordinatePattern + " -- cycle";
        String pathPattern = "\\\\draw \\[(\\w+)(,->)*\\] " + coordinatePattern + " -- " + coordinatePattern;

        ArrayList<Pair<String, String>> patternsArray = new ArrayList<Pair<String, String>>(Arrays.asList(
                new Pair<>(squarePattern, SQUARE),
                new Pair<>(circlePattern, CIRCLE),
                new Pair<>(trianglePattern, TRIANGLE),
                new Pair<>(pathPattern, "PATH")
        ));

        // Find what shape has been created
        String shapeType = null;
        Pattern p = null;
        Matcher m = null;
        for (Pair<String, String> pattern: patternsArray) {
            p = Pattern.compile(pattern.getKey());
            m = p.matcher(line);
            if (m.find()) {
                shapeType = pattern.getValue();
                break;
            }
        }

        if (shapeType != null) {
            controller.shape.Color fillColor = null, drawColor = null;
            if (shapeType.equals(SQUARE) || shapeType.equals(CIRCLE) || shapeType.equals(TRIANGLE)) {
                fillColor = controller.shape.Color.get(m.group(1));
                drawColor = controller.shape.Color.get(m.group(2));
            }
            else if (shapeType.equals("PATH")) {
                drawColor = controller.shape.Color.get(m.group(1));
            }

            // Process new shape
            controller.shape.Shape shapeToDraw = null;
            switch (shapeType) {
                case SQUARE: {
                    Coordinates origin = new Coordinates(Double.parseDouble(m.group(3)), Double.parseDouble(m.group(4)));
                    Coordinates end = new Coordinates(Double.parseDouble(m.group(6)), Double.parseDouble(m.group(7)));

                    shapeToDraw = new Square(true, true, drawColor, fillColor, origin, end, canvas.getIdForNewShape());
                    break;
                }
                case CIRCLE: {
                    Coordinates center = new Coordinates(Double.parseDouble(m.group(3)), Double.parseDouble(m.group(4)));
                    Float radius = Float.parseFloat(m.group(6));

                    shapeToDraw = new controller.shape.Circle(true, true, drawColor, fillColor, center, radius, canvas.getIdForNewShape());
                    break;
                }
                case TRIANGLE: {
                    Coordinates p1 = new Coordinates(Double.parseDouble(m.group(3)), Double.parseDouble(m.group(4)));
                    Coordinates p2 = new Coordinates(Double.parseDouble(m.group(5)), Double.parseDouble(m.group(6)));
                    Coordinates p3 = new Coordinates(Double.parseDouble(m.group(7)), Double.parseDouble(m.group(8)));

                    shapeToDraw = new controller.shape.Triangle(true, true, drawColor, fillColor, p1, p2, p3, canvas.getIdForNewShape());
                    break;
                }
                case "PATH": {
                    Coordinates begin = new Coordinates(Double.parseDouble(m.group(3)), Double.parseDouble(m.group(4)));
                    Coordinates end = new Coordinates(Double.parseDouble(m.group(5)), Double.parseDouble(m.group(6)));

                    if (m.group(2) == null) {
                        shapeToDraw = new controller.shape.Line(begin, end, drawColor, canvas.getIdForNewShape());
                    }
                    else {
                        shapeToDraw = new controller.shape.Arrow(begin, end, drawColor, canvas.getIdForNewShape());
                    }
                    break;
                }
                default:
                    break;
            };
            if (shapeToDraw != null) {
                shapeHandler.handleDraw(shapeToDraw);
                canvas.addShape(shapeToDraw);
            }
        }
    }
}

