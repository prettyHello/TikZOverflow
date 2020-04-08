package view.editor;

import config.ConfigurationSingleton;
import controller.Canvas.ActiveCanvas;
import controller.Canvas.ActiveProject;
import controller.Canvas.Canvas;
import controller.DTO.ProjectDTO;
import controller.DTO.UserDTO;
import controller.UCC.UserUCC;
import controller.shape.Coordinates;
import controller.shape.Square;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import model.SaveObject;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.demo.JavaKeywords;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import view.ViewName;
import view.ViewSwitcher;

import java.io.IOException;
import java.util.*;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utilities.ColorUtils.getColorNameFromRgb;

/**
 * This class is used to handle drawings and their corresponding tikz translation.
 * Could be split in two classes, one for translation, one for drawing if this controller gets too long
 */
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
    private UserUCC userUcc;

    private ViewSwitcher viewSwitcher;

    @FXML
    Pane toolbar;
    @FXML
    private Pane pane;
    @FXML
    private StyleClassedTextArea tikzTA = new StyleClassedTextArea();
    //private TextArea tikzTA;
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
    private double selectedX, selectedY;
    private double previouslySelectedX, previouslySelectedY; // a line needs 2 points so last choice is saved
    private double thirdSelectedX, thirdSelectedY; // since a triangle need three points
    private String shapeToDraw = "";
    private boolean waitingForMoreCoordinate = false;
    private Canvas canvas;

    ContextMenu menu = new ContextMenu();

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
                selectedX = event.getX();
                selectedY = event.getY();
                handleDrawCall();
            }
        });

        // to make the split pane non resizeable
        leftAnchorPane.maxWidthProperty().bind(mainSplitPane.widthProperty().multiply(0.5));
        leftAnchorPane.minWidthProperty().bind(mainSplitPane.widthProperty().multiply(0.5));


        contextMenuFillColorPicker = new ChoiceBox();
        contextMenuDrawColorPicker = new ChoiceBox();


        // Fill dropdowns (fill & stroke & context) with appropriate colors
        for (controller.shape.Color colour : controller.shape.Color.values()) {
            fillColour.getItems().add(colour);
            strokeColour.getItems().add(colour);
            contextMenuFillColorPicker.getItems().add(colour);
            contextMenuDrawColorPicker.getItems().add(colour);
        }
        // Set start value dropdown to black
        fillColour.setValue(controller.shape.Color.BLACK);
        strokeColour.setValue(controller.shape.Color.BLACK);
        contextMenuFillColorPicker.setValue(controller.shape.Color.BLACK);
        contextMenuDrawColorPicker.setValue(controller.shape.Color.BLACK);

        MenuItem delete = new MenuItem("delete");
        delete.setOnAction(t -> rightClickDeleteShape());
        MenuItem fillColorMenu = new MenuItem("Fill color", contextMenuFillColorPicker);
        fillColorMenu.setOnAction(t -> setFillColor());
        MenuItem drawColorMenu = new MenuItem("Stroke color", contextMenuDrawColorPicker);
        drawColorMenu.setOnAction(t -> setDrawColor());
        shapeContextMenu = new ContextMenu(delete, fillColorMenu, drawColorMenu);

        // show shapes at the start(don't have to interact to have thel show up)
        translateToTikz();
    }

    /**
     * Rightclick dropdown menu, change FillColor
     */
    private void setFillColor() {
        //TODO use canvas.updateShape to update the shape.
        if (shapeContextMenu.getOwnerNode() instanceof Shape) {
            Shape shape = (Shape) shapeContextMenu.getOwnerNode();
            shape.setFill(Color.valueOf(contextMenuFillColorPicker.getValue().toString()));
            Color fillColor = (Color) shape.getFill();
            canvas.changeShapeFillColor(Integer.parseInt(shape.getId()), getColorNameFromRgb(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue()));
        }
        translateToTikz();
    }

    /**
     * Rightclick dropdown menu, change StokeColor
     */
    private void setDrawColor() {
        //TODO use canvas.updateShape to update the shape.
        if (shapeContextMenu.getOwnerNode() instanceof Shape) {
            Shape shape = (Shape) shapeContextMenu.getOwnerNode();
            shape.setStroke(Color.valueOf(contextMenuDrawColorPicker.getValue().toString()));
            Color drawColor = (Color) shape.getStroke();
            canvas.changeShapeDrawColor(Integer.parseInt(shape.getId()), getColorNameFromRgb(drawColor.getRed(), drawColor.getGreen(), drawColor.getBlue()));
        }
        translateToTikz();
    }

    /**
     * Rightclick dropdown menu, delete shape
     */
    private void rightClickDeleteShape() {
        if (shapeContextMenu.getOwnerNode() instanceof Shape) {
            Shape shape = (Shape) shapeContextMenu.getOwnerNode();
            pane.getChildren().remove(shape);
            canvas.rmShapeById(Integer.parseInt(shape.getId()));
            if (selectedShapes.contains(shape)) {
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
     * Draw selected shape
     */
    private void handleDrawCall() {
        Shape shape = null;
        controller.shape.Shape addToController = null;

        switch (shapeToDraw) {
            case TRIANGLE:
                thirdSelectedX = selectedX;
                thirdSelectedY = selectedY;
                shapeToDraw = TRIANGLE_POINT2;
                waitingForMoreCoordinate = true;
                break;
            case TRIANGLE_POINT2:
                previouslySelectedX = selectedX;
                previouslySelectedY = selectedY;
                shapeToDraw = TRIANGLE_POINT3;
                waitingForMoreCoordinate = true;
                break;
            case TRIANGLE_POINT3:
                addToController = new controller.shape.Triangle(new Coordinates(selectedX, selectedY), canvas.getIdForNewShape());
                shape = constructTriangle();
                waitingForMoreCoordinate = false;
                break;
            case CIRCLE:
                float radius = 50.0f;
                Circle circle = new Circle();
                circle.setCenterX(selectedX);
                circle.setCenterY(selectedY);
                circle.setRadius(radius);
                addToController = new controller.shape.Circle(new Coordinates(selectedX, selectedY), radius, canvas.getIdForNewShape());
                shape = circle;
                break;
            case ARROW:
                previouslySelectedX = selectedX;
                previouslySelectedY = selectedY;
                shapeToDraw = ARROW_POINT2;
                waitingForMoreCoordinate = true;
                break;
            case ARROW_POINT2:
                addToController = new controller.shape.Arrow(new Coordinates(previouslySelectedX, previouslySelectedY), new Coordinates(selectedX, selectedY), canvas.getIdForNewShape());
                shape = constructArrow();
                waitingForMoreCoordinate = false;
                break;
            case LINE:
                previouslySelectedX = selectedX;
                previouslySelectedY = selectedY;
                shapeToDraw = LINE_POINT2;
                waitingForMoreCoordinate = true;
                break;
            case LINE_POINT2:
                addToController = new controller.shape.Line(new Coordinates(previouslySelectedX, previouslySelectedY), new Coordinates(selectedX, selectedY), canvas.getIdForNewShape());
                shape = new Line(previouslySelectedX, previouslySelectedY, selectedX, selectedY);
                shape.setStroke(Color.valueOf(fillColour.getValue().toString()));
                waitingForMoreCoordinate = false;
                break;
            case SQUARE:
                int size = 75;
                shape = new Rectangle(selectedX, selectedY, 75, 75);
                addToController = new Square(new Coordinates(selectedX, selectedY), size, canvas.getIdForNewShape());
                break;
        }
        if (waitingForMoreCoordinate) {
            return;
        } else if (shape == null) { //No shape was previously selected
            alert("Select a shape", "You need to select a shape", "You need to select a shape first!");
        } else {
            shape.setFill(Color.valueOf(fillColour.getValue().toString()));
            shape.setStroke(Color.valueOf(strokeColour.getValue().toString()));
            pane.getChildren().add(shape);
            notifyController(addToController, shape);
            shape.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onShapeSelected); //add a listener allowing us to know if a shape was selected
            shapeToDraw = "";
        }
        disableButtonOverlay();
    }

    /**
     * Notify Shape controller of javafx shape creation
     *
     * @param addToController
     * @param shape           JavaFX shape
     */
    private void notifyController(controller.shape.Shape addToController, Shape shape) {
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
     * Listener leftclick select or unselect
     * Listener rightclick dropdown delete + change color
     *
     * @param mouseEvent
     */
    private void onShapeSelected(MouseEvent mouseEvent) {
        if (!waitingForMoreCoordinate && shapeToDraw == "") {
            Shape shape = (Shape) mouseEvent.getSource();

            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (selectedShapes.contains(shape)) { //if already selected => unselect
                    shape.setEffect(null);
                    selectedShapes.remove(shape);
                    translateToTikz();

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
                    translateToTikz();
                }
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                shape.setOnContextMenuRequested(t -> shapeContextMenu.show(shape, mouseEvent.getScreenX(), mouseEvent.getScreenY()));
            }
        }
    }

    /**
     * Disables all the buttons except delete button when shape is selected
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

    /**
     * Disable the buttons highlight
     */
    private void disableButtonOverlay() {
        square.setStyle("-fx-focus-color: transparent;");
        circle.setStyle("-fx-focus-color: transparent;");
        line.setStyle("-fx-focus-color: transparent;");
        arrow.setStyle("-fx-focus-color: transparent;");
        triangle.setStyle("-fx-focus-color: transparent;");
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
     * Construct three lines and merge to make an arrow
     *
     * @return arrow JavaFX shape
     */
    private Shape constructArrow() {
        double arrowLength = 5;
        double arrowWidth = 3;
        Line main_line = new Line(previouslySelectedX, previouslySelectedY, selectedX, selectedY);
        Line arrow1 = new Line();
        Line arrow2 = new Line();

        arrow1.setEndX(selectedX);
        arrow1.setEndY(selectedY);
        arrow2.setEndX(selectedX);
        arrow2.setEndY(selectedY);

        if (selectedX == previouslySelectedX && selectedY == previouslySelectedY) {
            // arrow parts of length 0
            arrow1.setStartX(selectedX);
            arrow1.setStartY(selectedY);
            arrow2.setStartX(selectedX);
            arrow2.setStartY(selectedY);
        } else {
            double factor = arrowLength / Math.hypot(previouslySelectedX - selectedX, previouslySelectedY - selectedY);
            double factorO = arrowWidth / Math.hypot(previouslySelectedX - selectedX, previouslySelectedY - selectedY);

            // part in direction of main line
            double dx = (previouslySelectedX - selectedX) * factor;
            double dy = (previouslySelectedY - selectedY) * factor;

            // part ortogonal to main line
            double ox = (previouslySelectedX - selectedX) * factorO;
            double oy = (previouslySelectedY - selectedY) * factorO;

            arrow1.setStartX(selectedX + dx - oy);
            arrow1.setStartY(selectedY + dy + ox);
            arrow2.setStartX(selectedX + dx + oy);
            arrow2.setStartY(selectedY + dy - ox);
        }

        return Shape.union(main_line, Shape.union(arrow1, arrow2));
    }

    /**
     * Construct triangle from polygon
     *
     * @return triangle JavaFX shape
     */
    private Shape constructTriangle() {
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(selectedX, selectedY,
                previouslySelectedX, previouslySelectedY,
                thirdSelectedX, thirdSelectedY);
        return polygon;
    }

    /**
     * Show alert
     *
     * @param title
     * @param header
     * @param Content
     */
    private void alert(String title, String header, String Content) {
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
    public void save(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        ProjectDTO projectDTO = ActiveProject.getActiveProject();
        UserDTO user = userUcc.getConnectedUser();
        SaveObject saveObject = new SaveObject();
        saveObject.save(canvas, projectDTO.getProjectName(),user);
    }

    /**
     * Close project and ask if it needs to be saved
     *
     * @param actionEvent
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void close(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
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
            SaveObject saveObject = new SaveObject();
            ProjectDTO projectDTO = ActiveProject.getActiveProject();
            saveObject.save(canvas, projectDTO.getProjectName(), user);
        }

        ActiveCanvas.deleteActiveCanvas();
        viewSwitcher.switchView(ViewName.DASHBOARD);
    }

    /**
     * Translate canvas to tikz and fill textarea with corresponding style
     */
    private void translateToTikz() {
        String[] tikzCodeWords = {"filldraw","draw","path","node","begin","tikzstyle","fill","end"}; // Words use by tikz and that will be highlight.
        tikzTA.replaceText(canvas.toTikZ());
        tikzTA.clearStyle(0,canvas.toTikZ().length());
        if(!canvas.toTikZ().isEmpty()){ // Check if the canvas isn't empty (for example at start). Because if the canvas is empty it will make an error.
            for(int x = 0; x < tikzCodeWords.length; x ++){ // Loop to find EVERY tikzCodeWords in the canvas.
                List<Integer> positions = findWordUpgrade(canvas.toTikZ(), tikzCodeWords[x]); // Positions where the word start and end in the canvas.
                for(int y = 0; y < positions.size(); y++){
                    tikzTA.setStyleClass(positions.get(y), positions.get(y)+tikzCodeWords[x].length(), "blue"); // Apply the highlight or font to the canvas.
                }
            }
        }
        System.out.println(selectedShapes.size());
        if(!selectedShapes.isEmpty()) // If user select a shape.
        {
            for(int z = 0 ; z < selectedShapes.size(); z ++) // Loop for every shape selected.
            {
                String myShape = selectedShapes.get(selectedShapes.size() - (z+1)).toString(); // Selection of the actual selected shape.
                System.out.println(myShape);
                int id = Integer.parseInt(printMatches(myShape,"(id=)[0-9]*").substring(3)); // Take the id of the shape from the var selectedShapes.
                System.out.println("id = "+id);
                controller.shape.Shape mySelectedShape = canvas.getShapeById(id);   // Take the shape with the id.
                List<Integer> positions = findWordUpgrade(canvas.toTikZ(), mySelectedShape.print()); // Get the line position in the canvas of the selected shape.
                for(int y = 0; y < positions.size(); y++){
                    tikzTA.setStyleClass(positions.get(y), positions.get(y)+mySelectedShape.print().length(), "highlight"); // Highlight the position of the shape.
                }
            }
        }
    }
    /**
     * Apply a regex on a string
     */
    public static String printMatches(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        // Check all occurrences
        while (matcher.find()) {
            break;
        }
        return matcher.group();
    }
    /**
     * Return the positions of each given word in a string
     */
    public List<Integer> findWordUpgrade(String textString, String word) {
        List<Integer> indexes = new ArrayList<Integer>();
        String lowerCaseTextString = textString.toLowerCase();
        String lowerCaseWord = word.toLowerCase();
        int wordLength = 0;

        int index = 0;
        while(index != -1){
            index = lowerCaseTextString.indexOf(lowerCaseWord, index + wordLength);  // Slight improvement
            if (index != -1) {
                indexes.add(index);
            }
            wordLength = word.length();
        }
        return indexes;
    }


}

