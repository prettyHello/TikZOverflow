package view.editor;

import config.ConfigurationSingleton;
import controller.Canvas.ActiveCanvas;
import controller.Canvas.Canvas;
import controller.DTO.UserDTO;
import controller.UCC.ProjectUCC;
import controller.UCC.UserUCC;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import utilities.exceptions.FatalException;
import view.ViewName;
import view.ViewSwitcher;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utilities.ColorUtils.getColorNameFromRgb;
import static utilities.Utility.showAlert;

/**
 * This class is used to handle drawings and their corresponding tikz translation.
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
    protected Canvas canvas;
    private String colorsPattern = "";
    private ContextMenu shapeContextMenu;
    private ChoiceBox contextMenuFillColorPicker;
    private ChoiceBox contextMenuDrawColorPicker;

    protected String coordinatePattern;
    protected String squarePattern;
    protected String circlePattern;
    protected String trianglePattern;
    protected String pathPattern;
    protected String labelPattern;

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

        // Initialize TikZ regex patterns
        this.colorsPattern = String.join("|", colors);
        this.coordinatePattern = "\\((\\d+\\.\\d+),(\\d+\\.\\d+)\\)";
        this.labelPattern = "(node\\[align=center, right=([-+]?[0-9]\\d*.\\d+)cm, above=([-+]?[0-9]\\d*.\\d+)cm\\] \\{([\\w ]+)\\})";
        this.squarePattern = "\\\\filldraw\\[fill=(" + colorsPattern + "), draw=(" + colorsPattern + ")\\] " + coordinatePattern + " (\\w+) " + coordinatePattern + labelPattern + "?";
        this.circlePattern = "\\\\filldraw\\[fill=(" + colorsPattern + "), draw=(" + colorsPattern + ")\\] " + coordinatePattern + " (\\w+) \\[radius=(\\+?[1-9][0-9]*.\\d+)\\]" + labelPattern + "?";
        this.trianglePattern = "\\\\filldraw\\[fill=(" + colorsPattern + "), draw=(" + colorsPattern + ")\\] " + coordinatePattern + " -- " + coordinatePattern + " -- " + coordinatePattern + " -- cycle" + labelPattern + "?";
        this.pathPattern = "\\\\draw \\[(" + colorsPattern + ")(,->)*\\] " + coordinatePattern + " -- " + coordinatePattern;

        // Set start value dropdown to black
        fillColour.setValue(controller.shape.Color.BLACK);
        strokeColour.setValue(controller.shape.Color.BLACK);
        contextMenuFillColorPicker.setValue(controller.shape.Color.BLACK);
        contextMenuDrawColorPicker.setValue(controller.shape.Color.BLACK);

        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(t -> shapeHandler.rightClickDeleteShape());
        MenuItem fillColorMenu = new MenuItem("Fill color", contextMenuFillColorPicker);
        fillColorMenu.setOnAction(t -> shapeHandler.setFillColor(Color.valueOf(contextMenuFillColorPicker.getValue().toString())));
        MenuItem drawColorMenu = new MenuItem("Stroke color", contextMenuDrawColorPicker);
        drawColorMenu.setOnAction(t -> shapeHandler.setDrawColor(Color.valueOf(contextMenuDrawColorPicker.getValue().toString())));
        MenuItem setLabel = new MenuItem("Set label");
        setLabel.setOnAction(t -> shapeHandler.handleSetLabel());
        shapeContextMenu = new ContextMenu(delete, fillColorMenu, drawColorMenu, setLabel);

        shapeHandler = new ShapeHandler(shapeContextMenu, canvas, this);

        // show shapes at the start(don't have to interact to have them show up)
        tikzTA.textProperty().addListener(this.handleCodeChange);
        translateToTikz();
        //translateToDraw();
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
        try {
            this.projectUcc.save();
        } catch (FatalException e) {
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
    public void close(ActionEvent actionEvent) {
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
            try {
                this.projectUcc.save();
            } catch (FatalException e) {
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
        if (shapeHandler.waitingForMoreCoordinate) {
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
     */
    private ChangeListener<? super String> handleCodeChange = (observableValue, oldValue, newValue) -> {
        if (!shapeHandler.drawFromGUI) {
            ArrayList<String> patternsArray = new ArrayList<>(Arrays.asList(squarePattern, circlePattern, trianglePattern, pathPattern));
            String[] lines = newValue.split("\\n");
            List<String> al = new ArrayList<>(Arrays.asList(lines));

            // Only handle Tikz shape declarations not general headers/footers
            List<String> blackList = new ArrayList<>(Arrays.asList(
                    "\\documentclass{article}",
                    "\\usepackage[utf8]{inputenc}",
                    "\\usepackage{tikz}",
                    "\\begin{document}",
                    "\\begin{tikzpicture}",
                    "\\end{tikzpicture}",
                    "\\end{document}",
                    ""));
            al.removeIf(blackList::contains);

            boolean linesCorrect = true;
            int incorrectLineNum = -1;
            Pattern p;
            Matcher m;
            for (String filteredLine : al) {
                p = null;
                m = null;
                linesCorrect = false;
                for (String pattern : patternsArray) {
                    p = Pattern.compile(pattern);
                    m = p.matcher(filteredLine);
                    if (m.find())
                        linesCorrect = true;
                }
                if (!linesCorrect) {
                    incorrectLineNum = al.indexOf(filteredLine);
                    break;
                }
            }
            if (linesCorrect) {
                System.out.println("No incorrect line");
                canvas.clear();
                pane.getChildren().clear();
                for (String line : al) {
                    shapeHandler.sendTikzCode(line);
                }
            } else {
                // TODO: Highlight wrong line
                System.out.println("Incorrect line: " + incorrectLineNum);
            }
        } else {
            shapeHandler.drawFromGUI = false;
        }
    };
}
