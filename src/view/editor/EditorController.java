package view.editor;

import config.ConfigurationSingleton;
import controller.Canvas.ActiveCanvas;
import controller.Canvas.ActiveProject;
import controller.Canvas.Canvas;
import controller.DTO.ProjectDTO;
import controller.DTO.UserDTO;
import controller.UCC.UserUCC;
import controller.shape.Arrow;
import controller.shape.Coordinates;
import controller.shape.Square;
import controller.shape.Triangle;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Pair;
import model.SaveObject;
import utilities.exceptions.FatalException;
import view.ViewName;
import view.ViewSwitcher;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.IOException;
import java.util.*;
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
    private String lineTikz = "";
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
    private double selectedX, selectedY;
    private double previouslySelectedX, previouslySelectedY; // a line needs 2 points so last choice is saved
    private double thirdSelectedX, thirdSelectedY; // since a triangle need three points
    private String shapeToDraw = "";
    private boolean waitingForMoreCoordinate = false;
    private Canvas canvas;
    private boolean drawnFromToolbar = false;
    private String colorsPattern = "";

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
        delete.setOnAction(t -> rightClickDeleteShape());
        MenuItem fillColorMenu = new MenuItem("Fill color", contextMenuFillColorPicker);
        fillColorMenu.setOnAction(t -> setFillColor());
        MenuItem drawColorMenu = new MenuItem("Stroke color", contextMenuDrawColorPicker);
        drawColorMenu.setOnAction(t -> setDrawColor());
        shapeContextMenu = new ContextMenu(delete, fillColorMenu, drawColorMenu);

        // show shapes at the start(don't have to interact to have thel show up)
        tikzTA.textProperty().addListener(this.handleCodeChange);
        translateToTikz();
        //translateToDraw();


        /*tikzTA.textProperty().addListener((obs,old,niu)-> {

        });*/
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
                Coordinates pt1 = new Coordinates(selectedX, selectedY);
                Coordinates pt2 = new Coordinates(previouslySelectedX, previouslySelectedY);
                Coordinates pt3 = new Coordinates(thirdSelectedX, thirdSelectedY);
                addToController = new controller.shape.Triangle(pt1, pt2, pt3, canvas.getIdForNewShape());
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
                double size = 75;
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
            drawnFromToolbar = true;
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
     * Translate canvas to tikz and fill textarea
     */
    private void translateToTikz() {
        tikzTA.setText(canvas.toTikZ());
    }

    /**
     * Draw controller shape in diagram.
     *
     * @param shape
     */
    private void handleDraw (controller.shape.Shape shape) {
        Shape shapeDrawing = null;

        switch (shape.getClass().toString()) {
            case "class controller.shape.Circle": {
                Coordinates circleCenter = ((controller.shape.Circle) shape).getCoordinates();
                double circleRadius = ((controller.shape.Circle) shape).getRadius();

                shapeDrawing = new Circle(circleCenter.getX(), circleCenter.getY(), circleRadius);
                break;
            }
            case "class controller.shape.Square": {
                double squareX = ((controller.shape.Square) shape).getOriginCoordinates().getX();
                double squareY = ((controller.shape.Square) shape).getOriginCoordinates().getY();
                double squareSize = ((controller.shape.Square) shape).getSize();

                shapeDrawing = new Rectangle(squareX, squareY, squareSize, squareSize);
                break;
            }
            case "class controller.shape.Triangle": {
                ArrayList<Coordinates>  points = ((controller.shape.Triangle) shape).getPoints();
                Coordinates point1 = points.get(0);
                Coordinates point2 = points.get(1);
                Coordinates point3 = points.get(2);

                Polygon polygon = new Polygon();
                polygon.getPoints().addAll(point1.getX(), point1.getY(), point2.getX(), point2.getY(), point3.getX(), point3.getY());
                shapeDrawing = polygon;
                break;
            }
            case "class controller.shape.Line": {
                ArrayList<Coordinates> points = ((controller.shape.Line) shape).getPathPoints();
                Coordinates point1 = points.get(0);
                Coordinates point2 = points.get(1);

                shapeDrawing = new Line(point1.getX(), point1.getY(), point2.getX(), point2.getY());
                break;
            }
            case "class controller.shape.Arrow": {
                ArrayList<Coordinates> points = ((controller.shape.Arrow) shape).getPathPoints();
                Coordinates point1 = points.get(0);
                Coordinates point2 = points.get(1);

                previouslySelectedX = point1.getX();
                previouslySelectedY = point1.getY();
                selectedX = point2.getX();
                selectedY = point2.getY();

                shapeDrawing = constructArrow();
                break;
            }
        }
        if (shapeDrawing != null) {
            shapeDrawing.setId(Integer.toString(shape.getId()));
            shapeDrawing.setFill(Color.valueOf(shape.getFillColor().toString()));
            shapeDrawing.setStroke(Color.valueOf(shape.getDrawColor().toString()));
            pane.getChildren().add(shapeDrawing);
            shapeDrawing.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onShapeSelected);
        }
    }

    /**
     * Translate controller shapes in diagram.
     */
    private void translateToDraw () {
        for (controller.shape.Shape shape : canvas.getShapes()) {
            handleDraw(shape);
        }
    }

    /**
     * Control events in TextArea.
     *
     * @param keyEvent
     */
    /*public void checkTikzCode(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER) {
            String [] lines = tikzTA.getText().split("\\n");
            String line = lines[lines.length - 1];
            sendTikzCode(line);
        }
    }*/

    /**
     * Detects and handles changes in the TextArea
     *
     */
    private ChangeListener<? super String> handleCodeChange = (observableValue, oldValue, newValue) -> {
        System.out.println("Patata");
        if (!drawnFromToolbar) {
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
            drawnFromToolbar = false;
        }
    };

    /*
     * \filldraw[fill=black, draw=black] (319.0,75.0) circle [radius=50.0];
     * \filldraw[fill=red, draw=black] (102.0,199.0) rectangle (177.0,274.0) ;
     * \filldraw[fill=green, draw=black] (258.0,191.0) -- (167.0,191.0) -- (175.0,96.0) -- cycle;
     * \draw [black] (292.0,192.0) -- (375.0,347.0) ;
     * \draw [black,->] (432.0,191.0) -- (509.0,372.0) ;
     */

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
                handleDraw(shapeToDraw);
                canvas.addShape(shapeToDraw);
            }
        }
    }

    private String peek(String search, String line){
        String x ="";
        x = line.substring(line.indexOf(search)+1);
        return x;
    }
}

