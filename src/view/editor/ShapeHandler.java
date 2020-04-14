package view.editor;

import controller.Canvas.Canvas;
import controller.shape.Coordinates;
import controller.shape.Square;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utilities.ColorUtils.getColorNameFromRgb;

public class ShapeHandler {

    protected static final String SQUARE = "SQUARE";
    protected static final String TRIANGLE = "TRIANGLE";
    protected static final String TRIANGLE_POINT2 = "TRIANGLE2";
    protected static final String TRIANGLE_POINT3 = "TRIANGLE3";
    protected static final String CIRCLE = "CIRCLE";
    protected static final String LINE = "LINE";
    protected static final String LINE_POINT2 = "LINE_POINT2";
    protected static final String ARROW = "ARROW";
    protected static final String ARROW_POINT2 = "ARROW_POINT2";

    protected double selectedX, selectedY;
    protected double previouslySelectedX, previouslySelectedY; // a line needs 2 points so last choice is saved
    protected double thirdSelectedX, thirdSelectedY; // since a triangle need three points
    protected boolean waitingForMoreCoordinate = false;
    protected boolean drawFromGUI = false;

    ContextMenu shapeContextMenu;
    Canvas canvas;
    EditorController editorController;

    public ShapeHandler(ContextMenu shapeContextMenu, Canvas canvas, EditorController editorController) {
        this.shapeContextMenu = shapeContextMenu;
        this.canvas = canvas;
        this.editorController = editorController;
    }

    /**
     * Rightclick dropdown menu, change FillColor
     *
     * @param color
     */
    public void setFillColor(Color color) {
        //TODO use canvas.updateShape to update the shape.
        if (shapeContextMenu.getOwnerNode() instanceof Shape) {
            Shape shape = (Shape) shapeContextMenu.getOwnerNode();
            shape.setFill(Color.valueOf(color.toString()));
            Color fillColor = (Color) shape.getFill();
            canvas.changeShapeFillColor(Integer.parseInt(shape.getId()), getColorNameFromRgb(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue()));
            drawFromGUI = true;
        }
        editorController.translateToTikz();
    }

    /**
     * Rightclick dropdown menu, change StokeColor
     */
    public void setDrawColor(Color color) {
        //TODO use canvas.updateShape to update the shape.
        if (shapeContextMenu.getOwnerNode() instanceof Shape) {
            Shape shape = (Shape) shapeContextMenu.getOwnerNode();
            shape.setStroke(Color.valueOf(color.toString()));
            Color drawColor = (Color) shape.getStroke();
            canvas.changeShapeDrawColor(Integer.parseInt(shape.getId()), getColorNameFromRgb(drawColor.getRed(), drawColor.getGreen(), drawColor.getBlue()));
            drawFromGUI = true;
        }
        editorController.translateToTikz();
    }

    /**
     * Handle rightclick set label option
     */
    public void handleSetLabel() {
        if (shapeContextMenu.getOwnerNode() instanceof Shape) {
            Shape shape = (Shape) shapeContextMenu.getOwnerNode();
            int shapeId = Integer.parseInt(shape.getId());
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("New shape label: ");
            Optional<String> result = dialog.showAndWait();
            String labelText = result.orElse("");
            canvas.setShapeLabel(shapeId, labelText);

            controller.shape.Shape controllerShape = canvas.getShapeById(shapeId);
            Coordinates labelPoint = getCoordintesForLabel(controllerShape);
            Text label = createLabel(controllerShape, labelPoint.getX(), labelPoint.getY());
            editorController.pane.getChildren().add(label);
            drawFromGUI = true;
        }
        editorController.translateToTikz();
    }

    /**
     * Return the point where the label is drawn
     *
     * @param shape controller shape to which a label is added.
     * @return the coordinates of the label placement point.
     */
    private Coordinates getCoordintesForLabel (controller.shape.Shape shape) {
        Coordinates point = null;

        switch (shape.getClass().toString()) {
            case "class controller.shape.Circle":
                point = ((controller.shape.Circle) shape).getCoordinates();
                break;
            case "class controller.shape.Square":
                point = ((controller.shape.Square) shape).getOriginCoordinates();
                break;
            case "class controller.shape.Triangle":
                point = ((controller.shape.Triangle) shape).getPoints().get(0);
                break;
            default:
                break;
        }

        return point;
    }


    /**
     * Rightclick dropdown menu, delete shape
     */
    public void rightClickDeleteShape() {
        if (shapeContextMenu.getOwnerNode() instanceof Shape) {
            Shape shape = (Shape) shapeContextMenu.getOwnerNode();
            editorController.pane.getChildren().remove(shape);
            canvas.rmShapeById(Integer.parseInt(shape.getId()));
            if (editorController.selectedShapes.contains(shape)) {
                editorController.selectedShapes.remove(shape);
                if (editorController.selectedShapes.isEmpty())
                    editorController.disableToolbar(false);
            }
        }
        editorController.translateToTikz();
    }

    /**
     * Draw selected shape
     */
    public void handleDrawCall() {
        Shape shape = null;
        controller.shape.Shape addToController = null;

        switch (editorController.shapeToDraw) {
            case TRIANGLE:
                thirdSelectedX = selectedX;
                thirdSelectedY = selectedY;
                editorController.shapeToDraw = TRIANGLE_POINT2;
                waitingForMoreCoordinate = true;
                break;
            case TRIANGLE_POINT2:
                previouslySelectedX = selectedX;
                previouslySelectedY = selectedY;
                editorController.shapeToDraw = TRIANGLE_POINT3;
                waitingForMoreCoordinate = true;
                break;
            case TRIANGLE_POINT3:
                Coordinates pt1 = new Coordinates(thirdSelectedX, thirdSelectedY);
                Coordinates pt2 = new Coordinates(previouslySelectedX, previouslySelectedY);
                Coordinates pt3 = new Coordinates(selectedX, selectedY);
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
                editorController.shapeToDraw = ARROW_POINT2;
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
                editorController.shapeToDraw = LINE_POINT2;
                waitingForMoreCoordinate = true;
                break;
            case LINE_POINT2:
                addToController = new controller.shape.Line(new Coordinates(previouslySelectedX, previouslySelectedY), new Coordinates(selectedX, selectedY), canvas.getIdForNewShape());
                shape = new Line(previouslySelectedX, previouslySelectedY, selectedX, selectedY);
                shape.setStroke(Color.valueOf(editorController.fillColour.getValue().toString()));
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
            editorController.alert("Select a shape", "You need to select a shape", "You need to select a shape first!");
        } else {
            shape.setFill(Color.valueOf(editorController.fillColour.getValue().toString()));
            shape.setStroke(Color.valueOf(editorController.strokeColour.getValue().toString()));
            drawFromGUI = true;
            editorController.pane.getChildren().add(shape);
            editorController.notifyController(addToController, shape);
            shape.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onShapeSelected); //add a listener allowing us to know if a shape was selected
            editorController.shapeToDraw = "";
        }
        editorController.disableButtonOverlay();
    }

    /**
     * Draw controller shape in diagram.
     *
     * @param shape
     */
    public void handleDraw(controller.shape.Shape shape) {
        Shape shapeDrawing = null;
        Text label = null;

        switch (shape.getClass().toString()) {
            case "class controller.shape.Circle": {
                Coordinates circleCenter = ((controller.shape.Circle) shape).getCoordinates();
                double circleRadius = ((controller.shape.Circle) shape).getRadius();
                shapeDrawing = new Circle(circleCenter.getX(), circleCenter.getY(), circleRadius);
                label = createLabel(shape, circleCenter.getX(), circleCenter.getY());
                break;
            }
            case "class controller.shape.Square": {
                double squareX = ((controller.shape.Square) shape).getOriginCoordinates().getX();
                double squareY = ((controller.shape.Square) shape).getOriginCoordinates().getY();
                double squareSize = ((controller.shape.Square) shape).getSize();
                shapeDrawing = new Rectangle(squareX, squareY, squareSize, squareSize);

                label = createLabel(shape, squareX, squareY);
                break;
            }
            case "class controller.shape.Triangle": {
                ArrayList<Coordinates> points = ((controller.shape.Triangle) shape).getPoints();
                Coordinates point1 = points.get(0);
                Coordinates point2 = points.get(1);
                Coordinates point3 = points.get(2);

                Polygon polygon = new Polygon();
                polygon.getPoints().addAll(point1.getX(), point1.getY(), point2.getX(), point2.getY(), point3.getX(), point3.getY());
                shapeDrawing = polygon;
                label = createLabel(shape, point1.getX(), point1.getY());
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
            editorController.pane.getChildren().add(shapeDrawing);
            if (label != null) {
                editorController.pane.getChildren().add(label);
            }

            shapeDrawing.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onShapeSelected);
        }
    }

    /**
     * Creates a label and places it at the center of the shape
     *
     * @param shape  the shape the label belongs to
     * @param shapeX the x-coordinate of the shape
     * @param shapeY the y-coordinate of the shape
     * @return the constructed label
     */
    private Text createLabel(controller.shape.Shape shape, double shapeX, double shapeY) {
        Text label = null;
        if (shape.getLabel() != null) {
            label = new Text(shape.getLabel());
            final Coordinates offset = shape.calcLabelOffset();
            label.setX(shapeX + offset.getX());
            label.setY(shapeY + offset.getY());
            label.setTextAlignment(TextAlignment.CENTER);
            label.setOnMouseClicked(event -> {
            });
            label.addEventHandler(MouseEvent.ANY, event -> {
            });
        }
        return label;
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
     * Listener leftclick select or unselect
     * Listener rightclick dropdown delete + change color
     *
     * @param mouseEvent
     */
    void onShapeSelected(MouseEvent mouseEvent) {
        if (!waitingForMoreCoordinate && editorController.shapeToDraw == "") {
            Shape shape = (Shape) mouseEvent.getSource();

            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (editorController.selectedShapes.contains(shape)) { //if already selected => unselect
                    shape.setEffect(null);
                    editorController.selectedShapes.remove(shape);
                    if (editorController.selectedShapes.isEmpty())
                        editorController.disableToolbar(false);
                } else {                                 //if not selected => add to the list
                    editorController.disableToolbar(true);
                    shape.setStrokeWidth(2);
                    DropShadow borderEffect = new DropShadow(
                            BlurType.THREE_PASS_BOX, Color.GREEN, 2, 1, 0, 0
                    );
                    shape.setEffect(borderEffect);
                    editorController.selectedShapes.add(shape);
                }
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                shape.setOnContextMenuRequested(t -> shapeContextMenu.show(shape, mouseEvent.getScreenX(), mouseEvent.getScreenY()));
            }
        }
    }

    /**
     * Translate code line to controller shape and draw it.
     *
     * @param line
     */
    protected void sendTikzCode(String line) {

        ArrayList<Pair<String, String>> patternsArray = new ArrayList<Pair<String, String>>(Arrays.asList(
                new Pair<>(editorController.squarePattern, SQUARE),
                new Pair<>(editorController.circlePattern, CIRCLE),
                new Pair<>(editorController.trianglePattern, TRIANGLE),
                new Pair<>(editorController.pathPattern, "PATH")
        ));

        // Find what shape has been created
        String shapeType = null;
        Pattern p = null;
        Matcher m = null;
        for (Pair<String, String> pattern : patternsArray) {
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
            } else if (shapeType.equals("PATH")) {
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

                    float radius;
                    try {
                        radius = Float.parseFloat(m.group(6));
                    } catch (NumberFormatException e) {
                        //todo handle
                        throw new Error("handle this");
                    }

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
                    } else {
                        shapeToDraw = new controller.shape.Arrow(begin, end, drawColor, canvas.getIdForNewShape());
                    }
                    break;
                }
                default:
                    throw new UnsupportedOperationException("Unknown shape");
            }

            Node node = getNodeInfo(line);
            if (node != null) {
                shapeToDraw.setLabel(node.label);
            }

            handleDraw(shapeToDraw);
            canvas.addShape(shapeToDraw);
        }
    }

    /**
     * If the given line corresponds to a valid Tikz instruction and contains a node definition at the end it will
     * return its information, otherwise return null
     *
     * @param line the Tikz code line
     * @return a Node instance that contains the offsets and the text of the label node
     */
    private Node getNodeInfo(String line) {
        Pattern p = Pattern.compile(editorController.labelPattern);
        Matcher m = p.matcher(line);

        Node node = null;
        if (m.find()) {
            try {
                double rightOffset = Double.parseDouble(m.group(2));
                double aboveOffset = Double.parseDouble(m.group(3));
                String label = m.group(4);
                node = new Node(rightOffset, aboveOffset, label);
            } catch (NumberFormatException e) {
                //TODO do stuff
            }
        }

        return node;
    }

    static class Node {
        double rightOffset;
        double aboveOffset;
        String label;

        public Node(double rightOffset, double aboveOffset, String label) {
            this.rightOffset = rightOffset;
            this.aboveOffset = aboveOffset;
            this.label = label;
        }
    }
}
