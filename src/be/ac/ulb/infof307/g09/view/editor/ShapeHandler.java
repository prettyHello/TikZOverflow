package be.ac.ulb.infof307.g09.view.editor;

import be.ac.ulb.infof307.g09.controller.Canvas.Canvas;
import be.ac.ulb.infof307.g09.controller.shape.Coordinates;
import be.ac.ulb.infof307.g09.controller.shape.Label;
import be.ac.ulb.infof307.g09.controller.shape.Square;
import be.ac.ulb.infof307.g09.controller.shape.Thickness;
import be.ac.ulb.infof307.g09.view.Utility;
import javafx.scene.Node;
import javafx.scene.control.Alert;
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

import static be.ac.ulb.infof307.g09.view.ColorUtils.getColorNameFromRgb;

public class ShapeHandler {

    protected double selectedX, selectedY;
    protected double previouslySelectedX, previouslySelectedY; // a line needs 2 points so last choice is saved
    protected double thirdSelectedX, thirdSelectedY; // since a triangle need three points
    protected boolean waitingForMoreCoordinate = false;
    protected boolean actionFromGUI = false;

    final ContextMenu shapeContextMenu;
    final Canvas canvas;
    final EditorController editorController;

    public ShapeHandler(ContextMenu shapeContextMenu, Canvas canvas, EditorController editorController) {
        this.shapeContextMenu = shapeContextMenu;
        this.canvas = canvas;
        this.editorController = editorController;
    }

    /**
     * Right-click dropdown menu, change FillColor
     *
     * @param color the color to set
     */
    public void changeColorRightClick(Color color) {
        //TODO use canvas.updateShape to update the shape.
        if (shapeContextMenu.getOwnerNode() instanceof Shape) {
            Shape shape = (Shape) shapeContextMenu.getOwnerNode();
            setFillColor(color,shape);
        }
    }

    /**
     * Change the shape color and update the view
     * @param color the new color
     * @param shape the shape to update
     */
    public void setFillColor(Color color, Shape shape){
        shape.setFill(Color.valueOf(color.toString()));
        Color fillColor = (Color) shape.getFill();
        canvas.changeShapeFillColor(Integer.parseInt(shape.getId()), getColorNameFromRgb(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue()));
        actionFromGUI = true;
        editorController.translateToTikz();
    }

    /**
     * Right-click dropdown menu, change StokeColor
     */
    public void changeStrokeColorRightClick(Color color) {
        //TODO use canvas.updateShape to update the shape.
        if (shapeContextMenu.getOwnerNode() instanceof Shape) {
            Shape shape = (Shape) shapeContextMenu.getOwnerNode();
            setStrokeColor(color,shape);
        }

    }

    /**
     * Change the shape stroke color and update the view
     * @param color the new color
     * @param shape the shape to update
     */
    public void setStrokeColor(Color color, Shape shape) {
        shape.setStroke(Color.valueOf(color.toString()));
        Color drawColor = (Color) shape.getStroke();
        canvas.changeShapeDrawColor(Integer.parseInt(shape.getId()), getColorNameFromRgb(drawColor.getRed(), drawColor.getGreen(), drawColor.getBlue()));
        actionFromGUI = true;
        editorController.translateToTikz();
    }



    /**
     * Rightclick dropdown menu, change shape thickness
     */
    public void updateShapeThickness(Thickness thickness) {
        if (shapeContextMenu.getOwnerNode() instanceof Shape) {
            Shape shape = (Shape) shapeContextMenu.getOwnerNode();
            setShapeThickness(thickness, shape);
        }

    }

    /**
     * Change the shape stroke thickness and update the view
     * @param thickness
     * @param shape
     */
    public void setShapeThickness(Thickness thickness, Shape shape) {
        shape.setStrokeWidth(thickness.thicknessValue());
        canvas.getShapeById(Integer.parseInt(shape.getId())).setShapeThicknessKey(thickness.name());
        actionFromGUI = true;
        editorController.translateToTikz();
    }

    /**
     * Handle right-click set label option
     */
    public void handleSetLabel(Color labelColor) {
        if (shapeContextMenu.getOwnerNode() instanceof Shape) {
            Shape shape = (Shape) shapeContextMenu.getOwnerNode();
            int shapeId = Integer.parseInt(shape.getId());
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("New shape label: ");
            Optional<String> result = dialog.showAndWait();
            String labelText = result.orElse("");

            be.ac.ulb.infof307.g09.controller.shape.Shape controllerShape = canvas.getShapeById(shapeId);
            Coordinates labelPoint = getCoordinatesForLabel(controllerShape);

            //TEST
            if(controllerShape.getLabel() != null){
                deleteLabel(controllerShape.getLabel().getId());
            }
            canvas.setShapeLabel(shapeId, labelText, labelPoint, getColorNameFromRgb(labelColor.getRed(), labelColor.getGreen(), labelColor.getBlue()));


            Text label = createLabel(controllerShape, labelPoint.getX(), labelPoint.getY());

            //ENDTEST
            editorController.pane.getChildren().add(label);

            actionFromGUI = true;
        }
        editorController.translateToTikz();
    }

    //TEST
    private void deleteLabel(String labelId){
        Text label = (Text) editorController.pane.lookup("#"+labelId);
        editorController.pane.getChildren().remove(label);
    }
    //ENDTEST

    /**
     * Return the point where the label is drawn
     *
     * @param shape be.ac.ulb.infof307.g09.controller shape to which a label is added.
     * @return the coordinates of the label placement point.
     */
    private Coordinates getCoordinatesForLabel(be.ac.ulb.infof307.g09.controller.shape.Shape shape) {
        Coordinates point = null;

        switch (shape.getClass().toString()) {
            case "class be.ac.ulb.infof307.g09.controller.shape.Circle":
                point = ((be.ac.ulb.infof307.g09.controller.shape.Circle) shape).getCoordinates();
                break;
            case "class be.ac.ulb.infof307.g09.controller.shape.Square":
                point = ((be.ac.ulb.infof307.g09.controller.shape.Square) shape).getOriginCoordinates();
                break;
            case "class be.ac.ulb.infof307.g09.controller.shape.Triangle":
                point = ((be.ac.ulb.infof307.g09.controller.shape.Triangle) shape).getPoints().get(0);
                break;
            default:
                break;
        }

        return point;
    }

    /**
     * Right-click dropdown menu, delete shape
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
            actionFromGUI = true;
        }
        editorController.translateToTikz();
    }

    /**
     * Draw selected shape
     */
    public void handleDrawCall() {
        Shape shape = null;
        be.ac.ulb.infof307.g09.controller.shape.Shape addToController = null;

        switch (editorController.shapeToDraw) {
            case EditorController.TRIANGLE:
                thirdSelectedX = selectedX;
                thirdSelectedY = selectedY;
                editorController.shapeToDraw = EditorController.TRIANGLE_POINT2;
                waitingForMoreCoordinate = true;
                break;
            case EditorController.TRIANGLE_POINT2:
                previouslySelectedX = selectedX;
                previouslySelectedY = selectedY;
                editorController.shapeToDraw = EditorController.TRIANGLE_POINT3;
                waitingForMoreCoordinate = true;
                break;
            case EditorController.TRIANGLE_POINT3:
                Coordinates pt1 = new Coordinates(thirdSelectedX, thirdSelectedY);
                Coordinates pt2 = new Coordinates(previouslySelectedX, previouslySelectedY);
                Coordinates pt3 = new Coordinates(selectedX, selectedY);
                addToController = new be.ac.ulb.infof307.g09.controller.shape.Triangle(pt1, pt2, pt3, editorController.shapeThickness.getValue().toString(), canvas.getIdForNewShape());
                shape = constructTriangle();
                waitingForMoreCoordinate = false;
                break;
            case EditorController.CIRCLE:
                float radius = 50.0f;
                Circle circle = new Circle();
                circle.setCenterX(selectedX);
                circle.setCenterY(selectedY);
                circle.setRadius(radius);
                addToController = new be.ac.ulb.infof307.g09.controller.shape.Circle(new Coordinates(selectedX, selectedY), radius, editorController.shapeThickness.getValue().toString(), canvas.getIdForNewShape());
                shape = circle;
                break;
            case EditorController.ARROW:
                previouslySelectedX = selectedX;
                previouslySelectedY = selectedY;
                editorController.shapeToDraw = EditorController.ARROW_POINT2;
                waitingForMoreCoordinate = true;
                break;
            case EditorController.ARROW_POINT2:
                addToController = new be.ac.ulb.infof307.g09.controller.shape.Arrow(new Coordinates(previouslySelectedX, previouslySelectedY), new Coordinates(selectedX, selectedY), editorController.shapeThickness.getValue().toString(), canvas.getIdForNewShape());
                shape = constructArrow();
                waitingForMoreCoordinate = false;
                break;
            case EditorController.LINE:
                previouslySelectedX = selectedX;
                previouslySelectedY = selectedY;
                editorController.shapeToDraw = EditorController.LINE_POINT2;
                waitingForMoreCoordinate = true;
                break;
            case EditorController.LINE_POINT2:
                addToController = new be.ac.ulb.infof307.g09.controller.shape.Line(new Coordinates(previouslySelectedX, previouslySelectedY), new Coordinates(selectedX, selectedY), editorController.shapeThickness.getValue().toString(), canvas.getIdForNewShape());
                shape = new Line(previouslySelectedX, previouslySelectedY, selectedX, selectedY);
                shape.setStroke(Color.valueOf(editorController.fillColour.getValue().toString()));
                waitingForMoreCoordinate = false;
                break;
            case EditorController.SQUARE:
                int size = 75;
                shape = new Rectangle(selectedX, selectedY, 75, 75);
                addToController = new Square(new Coordinates(selectedX, selectedY), size, editorController.shapeThickness.getValue().toString(), canvas.getIdForNewShape());
                break;
        }
        if (waitingForMoreCoordinate) {
            return;
        } else if (shape == null) { //No shape was previously selected
            Utility.showAlert(Alert.AlertType.INFORMATION, "Select a shape",
                    "You need to select a shape", "You need to select a shape first!");
        } else {
            shape.setFill(Color.valueOf(editorController.fillColour.getValue().toString()));
            shape.setStroke(Color.valueOf(editorController.strokeColour.getValue().toString()));
            shape.setStrokeWidth(Thickness.valueOf(editorController.shapeThickness.getValue().toString()).thicknessValue());
            actionFromGUI = true;
            editorController.pane.getChildren().add(shape);
            editorController.notifyController(addToController, shape);
            shape.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onShapeSelected); //add a listener allowing us to know if a shape was selected
            editorController.shapeToDraw = "";
        }
        editorController.disableButtonOverlay();
    }

    /**
     * Draw be.ac.ulb.infof307.g09.controller shape in diagram.
     *
     * @param shape
     */
    public Shape handleDraw(be.ac.ulb.infof307.g09.controller.shape.Shape shape) {
        Shape shapeDrawing = null;
        Text label = null;

        switch (shape.getClass().toString()) {
            case "class be.ac.ulb.infof307.g09.controller.shape.Circle": {
                Coordinates circleCenter = ((be.ac.ulb.infof307.g09.controller.shape.Circle) shape).getCoordinates();
                double circleRadius = ((be.ac.ulb.infof307.g09.controller.shape.Circle) shape).getRadius();
                shapeDrawing = new Circle(circleCenter.getX(), circleCenter.getY(), circleRadius);
                label = createLabel(shape, circleCenter.getX(), circleCenter.getY());
                break;
            }
            case "class be.ac.ulb.infof307.g09.controller.shape.Square": {
                double squareX = ((be.ac.ulb.infof307.g09.controller.shape.Square) shape).getOriginCoordinates().getX();
                double squareY = ((be.ac.ulb.infof307.g09.controller.shape.Square) shape).getOriginCoordinates().getY();
                double squareSize = ((be.ac.ulb.infof307.g09.controller.shape.Square) shape).getSize();
                shapeDrawing = new Rectangle(squareX, squareY, squareSize, squareSize);

                label = createLabel(shape, squareX, squareY);
                break;
            }
            case "class be.ac.ulb.infof307.g09.controller.shape.Triangle": {
                ArrayList<Coordinates> points = ((be.ac.ulb.infof307.g09.controller.shape.Triangle) shape).getPoints();
                Coordinates point1 = points.get(0);
                Coordinates point2 = points.get(1);
                Coordinates point3 = points.get(2);

                Polygon polygon = new Polygon();
                polygon.getPoints().addAll(point1.getX(), point1.getY(), point2.getX(), point2.getY(), point3.getX(), point3.getY());
                shapeDrawing = polygon;
                label = createLabel(shape, point1.getX(), point1.getY());
                break;
            }
            case "class be.ac.ulb.infof307.g09.controller.shape.Line": {
                ArrayList<Coordinates> points = ((be.ac.ulb.infof307.g09.controller.shape.Line) shape).getPathPoints();
                Coordinates point1 = points.get(0);
                Coordinates point2 = points.get(1);

                shapeDrawing = new Line(point1.getX(), point1.getY(), point2.getX(), point2.getY());
                break;
            }
            case "class be.ac.ulb.infof307.g09.controller.shape.Arrow": {
                ArrayList<Coordinates> points = ((be.ac.ulb.infof307.g09.controller.shape.Arrow) shape).getPathPoints();
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
            shapeDrawing.setStrokeWidth(shape.getShapeThicknessValue());
            editorController.pane.getChildren().add(shapeDrawing);
            shapeDrawing.toFront();
            shapeDrawing.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onShapeSelected);
            if (label != null) {
                editorController.pane.getChildren().add(label);
            }
        }

        return shapeDrawing;
    }

    /**
     * Creates a label and places it at the center of the shape
     *
     * @param shape  the shape the label belongs to
     * @param shapeX the x-coordinate of the shape
     * @param shapeY the y-coordinate of the shape
     * @return the constructed label
     */
    private Text createLabel(be.ac.ulb.infof307.g09.controller.shape.Shape shape, double shapeX, double shapeY) {
        Text label = null;
        //TODO vérifier utilité de cette condition
        if (shape.getLabel() != null) {
            label = new Text(shape.getLabel().getTitle());
            label.setId(shape.getLabel().getId());
            label.setFill(Color.valueOf(shape.getLabel().getColor().toString()));
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

            // part orthogonal to main line
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
     * Add highlight effect on a shape
     *
     * @param shape to which add the effect.
     * @return the shape with the highlight effect added.
     */
    protected Shape highlightShape(Shape shape) {
        DropShadow borderEffect = new DropShadow(
                BlurType.THREE_PASS_BOX, Color.GREEN, 2, 1, 0, 0
        );
        shape.setEffect(borderEffect);

        return shape;
    }

    /**
     * Listener left-click select or unselect
     * Listener right-click dropdown delete + change color
     *
     * @param mouseEvent the event fired by the user
     */
    void onShapeSelected(MouseEvent mouseEvent) {
        if (!waitingForMoreCoordinate && editorController.shapeToDraw.equals("")) {
            Shape shape = (Shape) mouseEvent.getSource();

            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (editorController.selectedShapes.contains(shape)) { //if already selected => unselect
                    shape.setEffect(null);
                    editorController.selectedShapes.remove(shape);
                    editorController.translateToTikz();
                    if (editorController.selectedShapes.isEmpty())
                        editorController.disableToolbar(false);
                } else {                                 //if not selected => add to the list
                    editorController.disableToolbar(true);
                    shape.setStrokeWidth(canvas.getShapeById(Integer.parseInt(shape.getId())).getShapeThicknessValue());
                    DropShadow borderEffect = new DropShadow(
                            BlurType.THREE_PASS_BOX, Color.GREEN, 2, 1, 0, 0
                    );
                    shape.setEffect(borderEffect);
                    editorController.selectedShapes.add(shape);
                    editorController.translateToTikz();
                }
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                shape.setOnContextMenuRequested(t -> shapeContextMenu.show(shape, mouseEvent.getScreenX(), mouseEvent.getScreenY()));
            }
        }
    }

    /**
     * Translate code line to be.ac.ulb.infof307.g09.controller shape and draw it.
     *
     * @param line the line of Tikz code
     */
    protected Shape sendTikZCode(String line) {
        Shape shape = null;
        ArrayList<Pair<String, String>> patternsArray = new ArrayList<>(Arrays.asList(
                new Pair<>(editorController.squarePattern, EditorController.SQUARE),
                new Pair<>(editorController.circlePattern, EditorController.CIRCLE),
                new Pair<>(editorController.trianglePattern, EditorController.TRIANGLE),
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
            be.ac.ulb.infof307.g09.controller.shape.Color fillColor = null, drawColor = null;
            String thickness = null;
            if (shapeType.equals(EditorController.SQUARE) || shapeType.equals(EditorController.CIRCLE) || shapeType.equals(EditorController.TRIANGLE)) {
                fillColor = be.ac.ulb.infof307.g09.controller.shape.Color.get(m.group(1));
                drawColor = be.ac.ulb.infof307.g09.controller.shape.Color.get(m.group(2));
            } else if (shapeType.equals("PATH")) {
                drawColor = be.ac.ulb.infof307.g09.controller.shape.Color.get(m.group(1));
            }
            thickness = m.group(3).toUpperCase().replace(' ', '_');

            // Process new shape
            be.ac.ulb.infof307.g09.controller.shape.Shape shapeToDraw = null;
            switch (shapeType) {
                case EditorController.SQUARE: {
                    Coordinates origin = new Coordinates(Double.parseDouble(m.group(4)), Double.parseDouble(m.group(5)));
                    Coordinates end = new Coordinates(Double.parseDouble(m.group(7)), Double.parseDouble(m.group(8)));

                    shapeToDraw = new Square(true, true, drawColor, fillColor, origin, end, thickness, canvas.getIdForNewShape());
                    break;
                }
                case EditorController.CIRCLE: {
                    Coordinates center = new Coordinates(Double.parseDouble(m.group(4)), Double.parseDouble(m.group(5)));
                    float radius = Float.parseFloat(m.group(7));
                    shapeToDraw = new be.ac.ulb.infof307.g09.controller.shape.Circle(true, true, drawColor, fillColor, thickness, center, radius, canvas.getIdForNewShape());
                    break;
                }
                case EditorController.TRIANGLE: {
                    Coordinates p1 = new Coordinates(Double.parseDouble(m.group(4)), Double.parseDouble(m.group(5)));
                    Coordinates p2 = new Coordinates(Double.parseDouble(m.group(6)), Double.parseDouble(m.group(7)));
                    Coordinates p3 = new Coordinates(Double.parseDouble(m.group(8)), Double.parseDouble(m.group(9)));

                    shapeToDraw = new be.ac.ulb.infof307.g09.controller.shape.Triangle(true, true, drawColor, fillColor, thickness, p1, p2, p3, canvas.getIdForNewShape());
                    break;
                }
                case "PATH": {
                    Coordinates begin = new Coordinates(Double.parseDouble(m.group(4)), Double.parseDouble(m.group(5)));
                    Coordinates end = new Coordinates(Double.parseDouble(m.group(6)), Double.parseDouble(m.group(7)));

                    if (m.group(2) == null) {
                        shapeToDraw = new be.ac.ulb.infof307.g09.controller.shape.Line(begin, end, drawColor, thickness, canvas.getIdForNewShape());
                    } else {
                        shapeToDraw = new be.ac.ulb.infof307.g09.controller.shape.Arrow(begin, end, drawColor, thickness, canvas.getIdForNewShape());
                    }
                    break;
                }
                default:
                    throw new UnsupportedOperationException("Unknown shape");
            }

            Node node = getNodeInfo(line);
            if (node != null) {

                shapeToDraw.setLabel(new Label(shapeToDraw.getId(),node.label, new Coordinates(node.rightOffset, node.aboveOffset),node.color));
            }

            shape = handleDraw(shapeToDraw);
            canvas.addShape(shapeToDraw);
        }
        return shape;
    }

    /**
     * If the given line corresponds to a valid TikZ instruction and contains a node definition at the end it will
     * return its information, otherwise return null
     *
     * @param line the TikZ code line
     * @return a Node instance that contains the offsets and the text of the label node
     */
    private Node getNodeInfo(String line) {
        Pattern p = Pattern.compile(editorController.labelPattern);
        Matcher m = p.matcher(line);

        Node node = null;
        if (m.find()) {
            //TODO change m.group
            be.ac.ulb.infof307.g09.controller.shape.Color drawColor = be.ac.ulb.infof307.g09.controller.shape.Color.get(m.group(2));

            double rightOffset = Double.parseDouble(m.group(3));
            double aboveOffset = Double.parseDouble(m.group(4));
            String label = m.group(5);
            node = new Node(drawColor,rightOffset, aboveOffset, label);
        }

        return node;
    }

    static class Node {
        be.ac.ulb.infof307.g09.controller.shape.Color color;
        double rightOffset;
        double aboveOffset;
        String label;

        public Node(be.ac.ulb.infof307.g09.controller.shape.Color color, double rightOffset, double aboveOffset, String label) {
            this.color = color;
            this.rightOffset = rightOffset;
            this.aboveOffset = aboveOffset;
            this.label = label;
        }
    }
}
