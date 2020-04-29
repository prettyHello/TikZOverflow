package be.ac.ulb.infof307.g09.view.editor;

import be.ac.ulb.infof307.g09.config.ConfigurationSingleton;
import be.ac.ulb.infof307.g09.controller.Canvas.ActiveCanvas;
import be.ac.ulb.infof307.g09.controller.Canvas.Canvas;
import be.ac.ulb.infof307.g09.controller.UCC.ProjectUCC;
import be.ac.ulb.infof307.g09.controller.shape.Thickness;
import be.ac.ulb.infof307.g09.exceptions.FatalException;
import be.ac.ulb.infof307.g09.view.ViewName;
import be.ac.ulb.infof307.g09.view.ViewSwitcher;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import org.fxmisc.richtext.LineNumberFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static be.ac.ulb.infof307.g09.view.ColorUtils.getColorNameFromRgb;
import static be.ac.ulb.infof307.g09.view.Utility.showAlert;

/**
 * This class is used to handle drawings and their corresponding tikz translation.
 */
public class EditorController {

    protected static final String SQUARE = "SQUARE";
    protected static final String TRIANGLE = "TRIANGLE";
    protected static final String TRIANGLE_POINT2 = "TRIANGLE2";
    protected static final String TRIANGLE_POINT3 = "TRIANGLE3";
    protected static final String CIRCLE = "CIRCLE";
    protected static final String LINE = "LINE";
    protected static final String LINE_POINT2 = "LINE_POINT2";
    protected static final String ARROW = "ARROW";
    protected static final String ARROW_POINT2 = "ARROW_POINT2";

    private ViewSwitcher viewSwitcher;
    private ShapeHandler shapeHandler;
    private final ProjectUCC projectUcc = ConfigurationSingleton.getProjectUCC();

    @FXML
    Pane toolbar;
    @FXML
    public Pane pane;
    @FXML
    public HighlightTextColor tikzTA = new HighlightTextColor();
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
    ChoiceBox<be.ac.ulb.infof307.g09.controller.shape.Color> fillColour;
    @FXML
    ChoiceBox<be.ac.ulb.infof307.g09.controller.shape.Color> strokeColour;
    @FXML
    ChoiceBox<be.ac.ulb.infof307.g09.controller.shape.Thickness> shapeThickness;
    @FXML
    SplitPane mainSplitPane;
    @FXML
    StackPane leftStackPane;
    @FXML
    Label lbCoordinates;
    @FXML
    BorderPane bpRootpane;

    public final ArrayList<Shape> selectedShapes = new ArrayList<>();
    protected String shapeToDraw = "";
    protected Canvas canvas;
    private String colorsPattern = "";
    private ContextMenu shapeContextMenu;
    private ChoiceBox<be.ac.ulb.infof307.g09.controller.shape.Color> contextMenuFillColorPicker;
    private ChoiceBox<be.ac.ulb.infof307.g09.controller.shape.Color> contextMenuDrawColorPicker;
    private ChoiceBox<be.ac.ulb.infof307.g09.controller.shape.Thickness> contextMenuChangeThickness;
    private ChoiceBox<be.ac.ulb.infof307.g09.controller.shape.Color> contextMenuLabelColorPicker;

    protected String intNumber;
    protected String floatNumber;
    protected String coordinatePattern;
    protected String squarePattern;
    protected String circlePattern;
    protected String trianglePattern;
    protected String pathPattern;
    protected String labelPattern;
    protected String thicknessPattern;
    private String oldCode;
    private boolean writableOldCode = true;

    public EditorController() {
        this.canvas = ActiveCanvas.getActiveCanvas();
    }

    @FXML
    public void initialize() {
        //add a column of numbers in front of each line of the text box
        tikzTA.setParagraphGraphicFactory(LineNumberFactory.get(tikzTA));

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

        contextMenuFillColorPicker = new ChoiceBox<>();
        contextMenuDrawColorPicker = new ChoiceBox<>();
        contextMenuChangeThickness = new ChoiceBox<>();
        contextMenuLabelColorPicker = new ChoiceBox<>();

        // Fill dropdowns (fill & stroke & context) with appropriate colors
        ArrayList<String> colors = new ArrayList<>();
        for (be.ac.ulb.infof307.g09.controller.shape.Color colour : be.ac.ulb.infof307.g09.controller.shape.Color.values()) {
            fillColour.getItems().add(colour);
            strokeColour.getItems().add(colour);
            contextMenuFillColorPicker.getItems().add(colour);
            contextMenuDrawColorPicker.getItems().add(colour);
            contextMenuLabelColorPicker.getItems().add(colour);
            colors.add(colour.getValue());
        }

        //Fill the thickness ChoiceBox on contextMenu
        ArrayList<String> thicknessKeys = new ArrayList<>();
        for (be.ac.ulb.infof307.g09.controller.shape.Thickness thickness : be.ac.ulb.infof307.g09.controller.shape.Thickness.values()) {
            shapeThickness.getItems().add(thickness);
            contextMenuChangeThickness.getItems().add(thickness);
            thicknessKeys.add(thickness.toString().toLowerCase().replace('_', ' '));
        }

        // Initialize TikZ regex patterns
        this.colorsPattern = String.join("|", colors);
        this.thicknessPattern = String.join("|", thicknessKeys);
        this.intNumber = "[+-]?\\d+";
        this.floatNumber = "[+-]?\\d+\\.\\d+";
        this.coordinatePattern = "\\((" + intNumber + "|" + floatNumber + "),[ ]*(" + intNumber + "|" + floatNumber + ")\\)";
        this.labelPattern = "(node\\[text=(" + colorsPattern + "),[ ]*align=center,[ ]*right=(" + intNumber + "|" + floatNumber + ")cm,[ ]*above=(" + intNumber + "|" + floatNumber + ")cm\\] \\{([\\w ]+)\\})";
        this.squarePattern = "\\\\filldraw[ ]*\\[[ ]*fill=(" + colorsPattern + "),[ ]*draw=(" + colorsPattern + "),[ ]*(" + thicknessPattern + ")\\] " + coordinatePattern + " (rectangle) " + coordinatePattern + labelPattern + "?";
        this.circlePattern = "\\\\filldraw[ ]*\\[[ ]*fill=(" + colorsPattern + "),[ ]*draw=(" + colorsPattern + "),[ ]*(" + thicknessPattern + ")\\] " + coordinatePattern + " (circle) \\[radius=(" + intNumber + "|" + floatNumber + ")\\]" + labelPattern + "?";
        this.trianglePattern = "\\\\filldraw[ ]*\\[[ ]*fill=(" + colorsPattern + "),[ ]*draw=(" + colorsPattern + "),[ ]*(" + thicknessPattern + ")\\] " + coordinatePattern + " -- " + coordinatePattern + " -- " + coordinatePattern + " -- cycle" + labelPattern + "?";
        this.pathPattern = "\\\\draw[ ]*\\[(" + colorsPattern + ")(,[ ]*->)*[ ]*,[ ]*("+ thicknessPattern +")\\] " + coordinatePattern + " -- " + coordinatePattern;

        // Set start value dropdown to black
        fillColour.setValue(be.ac.ulb.infof307.g09.controller.shape.Color.BLACK);
        strokeColour.setValue(be.ac.ulb.infof307.g09.controller.shape.Color.BLACK);
        shapeThickness.setValue(Thickness.THIN);
        contextMenuFillColorPicker.setValue(be.ac.ulb.infof307.g09.controller.shape.Color.BLACK);
        contextMenuDrawColorPicker.setValue(be.ac.ulb.infof307.g09.controller.shape.Color.BLACK);
        contextMenuLabelColorPicker.setValue(be.ac.ulb.infof307.g09.controller.shape.Color.BLACK);
        contextMenuChangeThickness.setValue(Thickness.THIN);


        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(t -> shapeHandler.rightClickDeleteShape());
        MenuItem fillColorMenu = new MenuItem("Fill color", contextMenuFillColorPicker);
        fillColorMenu.setOnAction(t -> shapeHandler.changeColorRightClick(Color.valueOf(contextMenuFillColorPicker.getValue().toString())));
        MenuItem drawColorMenu = new MenuItem("Stroke color", contextMenuDrawColorPicker);
        drawColorMenu.setOnAction(t -> shapeHandler.changeStrokeColorRightClick(Color.valueOf(contextMenuDrawColorPicker.getValue().toString())));
        MenuItem setLabel = new MenuItem("Set label", contextMenuLabelColorPicker);
        setLabel.setOnAction(t -> shapeHandler.handleSetLabel(Color.valueOf(contextMenuLabelColorPicker.getValue().toString())));
        MenuItem shapeThicknessMenu = new MenuItem("Change thickness", contextMenuChangeThickness);
        shapeThicknessMenu.setOnAction(t-> shapeHandler.updateShapeThickness(Thickness.valueOf(contextMenuChangeThickness.getValue().toString())));

        fillColour.setOnAction(t-> {
            if(!selectedShapes.isEmpty()){
                selectedShapes.forEach(shape -> shapeHandler.setFillColor(Color.valueOf(fillColour.getValue().toString()),shape));
            }
        });
        strokeColour.setOnAction(t-> {
            if(!selectedShapes.isEmpty()){
                selectedShapes.forEach(shape -> shapeHandler.setStrokeColor(Color.valueOf(strokeColour.getValue().toString()),shape));
            }
        });
        shapeThickness.setOnAction(t-> {
            if(!selectedShapes.isEmpty()){
                selectedShapes.forEach(shape -> shapeHandler.setShapeThickness(Thickness.valueOf(shapeThickness.getValue().toString()),shape));
            }
        });

        shapeContextMenu = new ContextMenu(delete, fillColorMenu, drawColorMenu, shapeThicknessMenu, setLabel);
        shapeHandler = new ShapeHandler(shapeContextMenu, canvas, this);

        // show shapes at the start(don't have to interact to have them show up)
        tikzTA.textProperty().addListener(this.handleCodeChange);
        translateToTikz();

        pane.setOnMouseMoved(event ->
                lbCoordinates.setText(String.format("x=%d, y=%d", (int) event.getX(), (int) event.getY())));

        bpRootpane.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.S) {
                this.save();
            } else if (event.isControlDown() && event.getCode() == KeyCode.W) {
                this.close();
            }
        });
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
     * @param addToController the shape that will be added to the model
     * @param shape           JavaFX shape
     */
    public void notifyController(be.ac.ulb.infof307.g09.controller.shape.Shape addToController, Shape shape) {
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
     * Disables all the buttons except delete and edits buttons when shape is selected
     * @param isDisabled disables the buttons when true
     */
    public void disableToolbar(boolean isDisabled) {
        circle.setDisable(isDisabled);
        square.setDisable(isDisabled);
        triangle.setDisable(isDisabled);
        line.setDisable(isDisabled);
        arrow.setDisable(isDisabled);
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
     * Save the project
     */
    public void save() {
        try {
            this.projectUcc.save();
            be.ac.ulb.infof307.g09.view.Utility.showAlert(Alert.AlertType.INFORMATION, "Task completed", "Project was successfully saved", "");
        } catch (FatalException e) {
            showAlert(Alert.AlertType.WARNING, "Save", "Unexpected Error", e.getMessage());
        }
    }

    /**
     * Close project and ask if it needs to be saved
     */
    public void close() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close project");
        alert.setHeaderText("Do you want to save your project?");

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
        } else if (result.get() == buttonTypeOne) {
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
            be.ac.ulb.infof307.g09.view.Utility.showAlert(Alert.AlertType.INFORMATION, "Finish your action",
                    "You need to select a second point", "You need to select a second point to finish the last shape!");
            disableButtonOverlay();
            return true;
        }
        return false;
    }

    /**
     * Translate canvas to tikz and fill textarea
     */
    public void translateToTikz() {
        tikzTA.replaceText(canvas.toTikZ());
        tikzTA.clearStyle(0, tikzTA.getText().length());
        tikzTA.setSelectedShapes(this.selectedShapes);
        tikzTA.highlightOnSelect();
    }

    /**
     * Detects and handles changes in the TextArea
     */
    private final ChangeListener<? super String> handleCodeChange = (observableValue, oldValue, newValue) -> {
        if (!shapeHandler.actionFromGUI) {
            ArrayList<String> patternsArray = new ArrayList<>(Arrays.asList(squarePattern, circlePattern, trianglePattern, pathPattern));
            List<String> newLines = new ArrayList<>(Arrays.asList(newValue.split("\\n")));

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
            newLines.removeIf(blackList::contains);

            boolean linesCorrect = true;
            String incorrectLine = null;
            Pattern p;
            Matcher m;
            for (String filteredLine : newLines) {
                linesCorrect = false;
                for (String pattern : patternsArray) {
                    p = Pattern.compile(pattern);
                    m = p.matcher(filteredLine);
                    if (m.find())
                        linesCorrect = true;
                }
                if (!linesCorrect) {
                    incorrectLine = filteredLine;
                    break;
                }
            }
            if (linesCorrect) {
                tikzTA.setWrongLine(null);
                ArrayList<Integer> selectedShapesIds = new ArrayList<>();
                if (!selectedShapes.isEmpty()) {
                    if (oldCode == null)
                        oldCode = oldValue;

                    List<String> oldLines = new ArrayList<>(Arrays.asList(oldCode.split("\\n")));
                    oldLines.removeIf(blackList::contains);
                    // Save the IDs of the shapes selected
                    for (Shape selectedShape : selectedShapes)
                        selectedShapesIds.add(Integer.parseInt(selectedShape.getId()));

                    Collections.sort(selectedShapesIds);

                    if (newLines.size() < oldLines.size()) {
                        // X lines have been removed. selectedShapesIds at some point is X ahead
                        int nLinesChanged = oldLines.size() - newLines.size();

                        // Find first line that changed
                        int i;
                        for (i = 0; i < newLines.size(); i++)
                            if (!oldLines.get(i).equals(newLines.get(i)))
                                break;
                        i += 1;

                        // If the shape selected was removed, remove it from selectedShapes.
                        for (int j = 0; j < nLinesChanged; j++)
                            if (selectedShapesIds.contains(i + j))
                                selectedShapesIds.remove(Integer.valueOf(i + j));
                        // Update the IDs of the selected shapes;
                        for (int j = 0; j < selectedShapesIds.size(); j++)
                            if (selectedShapesIds.get(j) > i)
                                selectedShapesIds.set(j, selectedShapesIds.get(j) - nLinesChanged);

                    }
                    else if (newLines.size() > oldLines.size()) {
                        // X lines have been added. selectedShapesIds at some point is X behind
                        int nLinesChanged = newLines.size() - oldLines.size();
                        // Find first line that changed
                        int i;
                        for (i = 0; i < oldLines.size(); i++)
                            if (!newLines.get(i).equals(oldLines.get(i)))
                                break;
                        i += 1;

                        // Update the IDs of the selected shapes;
                        for (int j = 0; j < selectedShapesIds.size(); j++)
                            if (selectedShapesIds.get(j) >= i)
                                selectedShapesIds.set(j, selectedShapesIds.get(j) + nLinesChanged);
                    }
                }

                canvas.clear();
                selectedShapes.clear();
                pane.getChildren().clear();
                Shape shapeDrawn;
                for (String line : newLines) {
                    shapeDrawn = shapeHandler.sendTikZCode(line);
                    if (selectedShapesIds.contains(Integer.parseInt(shapeDrawn.getId()))) {
                       shapeDrawn = shapeHandler.highlightShape(shapeDrawn);
                       selectedShapes.add(shapeDrawn);
                   }
                }

                if (selectedShapes.isEmpty())
                    disableToolbar(false);

                this.oldCode = null;
                writableOldCode = true;
            } else {
                tikzTA.setWrongLine(incorrectLine);
                if (writableOldCode) {
                    this.oldCode = oldValue;
                    writableOldCode = false;
                }
            }

            if (!selectedShapes.isEmpty()) {
                tikzTA.setSelectedShapes(this.selectedShapes);
            }

        } else {
            shapeHandler.actionFromGUI = false;
        }
    };

    /**
     * Required to load view
     *
     * @param viewSwitcher the object responsible for the changing the be.ac.ulb.infof307.g09.view presented to the user
     */
    public void setViewSwitcher(ViewSwitcher viewSwitcher) {
        this.viewSwitcher = viewSwitcher;
    }
}
