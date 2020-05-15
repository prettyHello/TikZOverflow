package be.ac.ulb.infof307.g09.view.editor;

import be.ac.ulb.infof307.g09.controller.Canvas.Canvas;
import be.ac.ulb.infof307.g09.controller.DTO.shapes.*;
import be.ac.ulb.infof307.g09.view.ViewUtility;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static be.ac.ulb.infof307.g09.view.ColorUtils.getColorNameFromRgb;

public class ShapeHandler {

    protected double selectedX, selectedY;
    protected double previouslySelectedX, previouslySelectedY; // a line needs 2 points so last choice is saved
    protected double thirdSelectedX, thirdSelectedY; // since a triangle need three points
    protected boolean waitingForMoreCoordinate = false;

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
        if (shapeContextMenu.getOwnerNode() instanceof Shape) {
            Shape shape = (Shape) shapeContextMenu.getOwnerNode();
            setFillColor(color, shape);
        }
    }

    /**
     * Change the shape color and update the view
     *
     * @param color the new color
     * @param shape the shape to update
     */
    public void setFillColor(Color color, Shape shape) {
        shape.setFill(Color.valueOf(color.toString()));
        Color fillColor = (Color) shape.getFill();
        canvas.changeShapeFillColor(Integer.parseInt(shape.getId()), getColorNameFromRgb(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue()));
        editorController.actionFromGUI = true;
        editorController.translateToTikz();
    }

    /**
     * Right-click dropdown menu, change StokeColor
     */
    public void changeStrokeColorRightClick(Color color) {
        if (shapeContextMenu.getOwnerNode() instanceof Shape) {
            Shape shape = (Shape) shapeContextMenu.getOwnerNode();
            setStrokeColor(color, shape);
        }
    }

    /**
     * Change the shape stroke color and update the view
     *
     * @param color the new color
     * @param shape the shape to update
     */
    public void setStrokeColor(Color color, Shape shape) {
        shape.setStroke(Color.valueOf(color.toString()));
        Color drawColor = (Color) shape.getStroke();
        canvas.changeShapeDrawColor(Integer.parseInt(shape.getId()), getColorNameFromRgb(drawColor.getRed(), drawColor.getGreen(), drawColor.getBlue()));
        editorController.actionFromGUI = true;
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
     *
     * @param thickness
     * @param shape
     */
    public void setShapeThickness(Thickness thickness, Shape shape) {
        shape.setStrokeWidth(thickness.thicknessValue());
        canvas.getShapeById(Integer.parseInt(shape.getId())).setThickness(thickness);
        editorController.actionFromGUI = true;
        editorController.translateToTikz();
    }

    /**
     * Handle right-click set label option
     */
    public void handleSetLabel(Color labelColor) {
        if (shapeContextMenu.getOwnerNode() instanceof Shape) {
            Shape shape = (Shape) shapeContextMenu.getOwnerNode();
            int shapeId = Integer.parseInt(shape.getId());

            ShapeDTO modelShapeDTO = canvas.getShapeById(shapeId);
            if (!(modelShapeDTO instanceof LabelizableShapeDTO)) {
                return;
            }

            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("New shape label: ");
            Optional<String> result = dialog.showAndWait();
            String labelText = result.orElse("");

            canvas.setShapeLabel(shapeId, labelText, getColorNameFromRgb(labelColor.getRed(), labelColor.getGreen(), labelColor.getBlue()));
            editorController.actionFromGUI = false;
        }
        editorController.translateToTikz();
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
                if (editorController.selectedShapes.isEmpty()) {
                    editorController.disableToolbar(false);
                }
            }
            editorController.actionFromGUI = false;
        }
        editorController.translateToTikz();
    }

    /**
     * Draw selected shape
     */
    public void handleDrawCall() {
        Shape shape = null;
        be.ac.ulb.infof307.g09.controller.DTO.shapes.ShapeDTO addToController = null;

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
                CoordinatesDTO pt1 = new CoordinatesDTO(thirdSelectedX, thirdSelectedY);
                CoordinatesDTO pt2 = new CoordinatesDTO(previouslySelectedX, previouslySelectedY);
                CoordinatesDTO pt3 = new CoordinatesDTO(selectedX, selectedY);
                addToController = new TriangleDTO(pt1, pt2, pt3, editorController.shapeThickness.getValue(), canvas.getIdForNewShape());
                shape = constructTriangle();
                waitingForMoreCoordinate = false;
                break;
            case EditorController.CIRCLE:
                float radius = 50.0f;
                Circle circle = new Circle();
                circle.setCenterX(selectedX);
                circle.setCenterY(selectedY);
                circle.setRadius(radius);
                addToController = new CircleDTO(new CoordinatesDTO(selectedX, selectedY), radius, editorController.shapeThickness.getValue(), canvas.getIdForNewShape());
                shape = circle;
                break;
            case EditorController.ARROW:
                previouslySelectedX = selectedX;
                previouslySelectedY = selectedY;
                editorController.shapeToDraw = EditorController.ARROW_POINT2;
                waitingForMoreCoordinate = true;
                break;
            case EditorController.ARROW_POINT2:
                addToController = new ArrowDTO(new CoordinatesDTO(previouslySelectedX, previouslySelectedY), new CoordinatesDTO(selectedX, selectedY), editorController.shapeThickness.getValue(), canvas.getIdForNewShape());
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
                addToController = new LineDTO(new CoordinatesDTO(previouslySelectedX, previouslySelectedY), new CoordinatesDTO(selectedX, selectedY), editorController.shapeThickness.getValue(), canvas.getIdForNewShape());
                shape = new Line(previouslySelectedX, previouslySelectedY, selectedX, selectedY);
                shape.setStroke(Color.valueOf(editorController.fillColour.getValue().toString()));
                waitingForMoreCoordinate = false;
                break;
            case EditorController.SQUARE:
                int size = 75;
                shape = new Rectangle(selectedX, selectedY, 75, 75);
                addToController = new SquareDTO(new CoordinatesDTO(selectedX, selectedY), size, editorController.shapeThickness.getValue(), canvas.getIdForNewShape());
                break;
        }
        if (waitingForMoreCoordinate) {
            return;
        } else if (shape == null) { //No shape was previously selected
            ViewUtility.showAlert(Alert.AlertType.INFORMATION, "Select a shape",
                    "You need to select a shape", "You need to select a shape first!");
        } else {
            shape.setFill(Color.valueOf(editorController.fillColour.getValue().toString()));
            shape.setStroke(Color.valueOf(editorController.strokeColour.getValue().toString()));
            shape.setStrokeWidth(editorController.shapeThickness.getValue().thicknessValue());
            editorController.actionFromGUI = true;
            editorController.pane.getChildren().add(shape);
            editorController.notifyController(addToController, shape);
            shape.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onShapeSelected); //add a listener allowing us to know if a shape was selected
            editorController.shapeToDraw = "";
        }
        editorController.disableButtonOverlay();
    }

    /**
     * Draw be.ac.ulb.infof307.g09.controller shapeDTO in diagram.
     *
     * @param shapeDTO the shapeDTO to draw
     */
    public Shape handleDraw(ShapeDTO shapeDTO) {
        Shape shapeDrawing = null;
        Text label = null;

        if (shapeDTO instanceof CircleDTO) {
            CircleDTO circle = (CircleDTO) shapeDTO;
            CoordinatesDTO circleCenter = circle.getCoordinates();
            double circleRadius = circle.getRadius();
            shapeDrawing = new Circle(circleCenter.getX(), circleCenter.getY(), circleRadius);
            label = createLabel((LabelizableShapeDTO) shapeDTO, circleCenter.getX(), circleCenter.getY());
        } else if (shapeDTO instanceof SquareDTO) {
            SquareDTO square = (SquareDTO) shapeDTO;
            CoordinatesDTO squareCoords = square.getOriginCoordinates();
            shapeDrawing = new Rectangle(squareCoords.getX(), squareCoords.getY(), square.getSize(), square.getSize());

            label = createLabel((LabelizableShapeDTO) shapeDTO, squareCoords.getX(), squareCoords.getY());
        } else if (shapeDTO instanceof TriangleDTO) {
            List<CoordinatesDTO> points = ((TriangleDTO) shapeDTO).getPoints();
            CoordinatesDTO point1 = points.get(0);
            CoordinatesDTO point2 = points.get(1);
            CoordinatesDTO point3 = points.get(2);

            Polygon polygon = new Polygon();
            polygon.getPoints().addAll(point1.getX(), point1.getY(), point2.getX(), point2.getY(), point3.getX(), point3.getY());
            shapeDrawing = polygon;
            label = createLabel((LabelizableShapeDTO) shapeDTO, point1.getX(), point1.getY());
        } else if (shapeDTO instanceof LineDTO) {
            List<CoordinatesDTO> points = ((LineDTO) shapeDTO).getPathPoints();
            CoordinatesDTO point1 = points.get(0);
            CoordinatesDTO point2 = points.get(1);

            shapeDrawing = new Line(point1.getX(), point1.getY(), point2.getX(), point2.getY());
        } else if (shapeDTO instanceof ArrowDTO) {
            List<CoordinatesDTO> points = ((ArrowDTO) shapeDTO).getPathPoints();
            CoordinatesDTO point1 = points.get(0);
            CoordinatesDTO point2 = points.get(1);

            previouslySelectedX = point1.getX();
            previouslySelectedY = point1.getY();
            selectedX = point2.getX();
            selectedY = point2.getY();

            shapeDrawing = constructArrow();
        }

        if (shapeDrawing != null) {
            shapeDrawing.setId(Integer.toString(shapeDTO.getId()));
            shapeDrawing.setFill(Color.valueOf(shapeDTO.getFillColor().toString()));
            shapeDrawing.setStroke(Color.valueOf(shapeDTO.getDrawColor().toString()));
            shapeDrawing.setStrokeWidth(shapeDTO.getThickness().thicknessValue());
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
    private Text createLabel(LabelizableShapeDTO shape, double shapeX, double shapeY) {
        Text label = null;
        if (shape.getLabel() != null) {
            label = new Text(shape.getLabel().getValue());
            label.setDisable(true);
            label.setStyle("-fx-font-weight: bold");
            label.setFill(Color.valueOf(shape.getLabel().getColor().toString()));
            final CoordinatesDTO offset = shape.calcLabelOffset();
            label.setX(shapeX + offset.getX());
            label.setY(shapeY + offset.getY());
            label.setTextAlignment(TextAlignment.CENTER);
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
        Line mainLine = new Line(previouslySelectedX, previouslySelectedY, selectedX, selectedY);
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

        return Shape.union(mainLine, Shape.union(arrow1, arrow2));
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
                    if (editorController.selectedShapes.isEmpty()){
                        editorController.disableToolbar(false);
                    }
                } else {                                 //if not selected => add to the list
                    editorController.disableToolbar(true);
                    shape.setStrokeWidth(canvas.getShapeById(Integer.parseInt(shape.getId())).getThickness().thicknessValue());
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
            ColorDTO fillColor = null, drawColor = null;
            if (shapeType.equals(EditorController.SQUARE) || shapeType.equals(EditorController.CIRCLE) || shapeType.equals(EditorController.TRIANGLE)) {
                fillColor = ColorDTO.get(m.group(1));
                drawColor = ColorDTO.get(m.group(2));
            } else if (shapeType.equals("PATH")) {
                drawColor = ColorDTO.get(m.group(1));
            }
            String thicknessStr = m.group(3).toUpperCase().replace(' ', '_');
            Thickness thickness = Thickness.valueOf(thicknessStr);

            // Process new shape
            ShapeDTO shapeToDraw = null;
            switch (shapeType) {
                case EditorController.SQUARE: {
                    CoordinatesDTO origin = new CoordinatesDTO(Double.parseDouble(m.group(4)), Double.parseDouble(m.group(5)));
                    CoordinatesDTO end = new CoordinatesDTO(Double.parseDouble(m.group(7)), Double.parseDouble(m.group(8)));

                    shapeToDraw = new SquareDTO(true, true, drawColor, fillColor, origin, end, thickness, canvas.getIdForNewShape());
                    break;
                }
                case EditorController.CIRCLE: {
                    CoordinatesDTO center = new CoordinatesDTO(Double.parseDouble(m.group(4)), Double.parseDouble(m.group(5)));
                    float radius = Float.parseFloat(m.group(7));
                    shapeToDraw = new CircleDTO(true, true, drawColor, fillColor, thickness, center, radius, canvas.getIdForNewShape());
                    break;
                }
                case EditorController.TRIANGLE: {
                    CoordinatesDTO p1 = new CoordinatesDTO(Double.parseDouble(m.group(4)), Double.parseDouble(m.group(5)));
                    CoordinatesDTO p2 = new CoordinatesDTO(Double.parseDouble(m.group(6)), Double.parseDouble(m.group(7)));
                    CoordinatesDTO p3 = new CoordinatesDTO(Double.parseDouble(m.group(8)), Double.parseDouble(m.group(9)));

                    shapeToDraw = new TriangleDTO(true, true, drawColor, fillColor, thickness, p1, p2, p3, canvas.getIdForNewShape());
                    break;
                }
                case "PATH": {
                    CoordinatesDTO begin = new CoordinatesDTO(Double.parseDouble(m.group(4)), Double.parseDouble(m.group(5)));
                    CoordinatesDTO end = new CoordinatesDTO(Double.parseDouble(m.group(6)), Double.parseDouble(m.group(7)));

                    if (m.group(2) == null) {
                        shapeToDraw = new LineDTO(begin, end, drawColor, thickness, canvas.getIdForNewShape());
                    } else {
                        shapeToDraw = new ArrowDTO(begin, end, drawColor, thickness, canvas.getIdForNewShape());
                    }
                    break;
                }
                default:
                    throw new UnsupportedOperationException("Unknown shape");
            }

            Node node = getNodeInfo(line);
            if (node != null) {
                ((LabelizableShapeDTO) shapeToDraw).setLabel(new LabelDTO(node.label, node.color));
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
            ColorDTO drawColor = ColorDTO.get(m.group(2));

            double rightOffset = Double.parseDouble(m.group(3));
            double aboveOffset = Double.parseDouble(m.group(4));
            String label = m.group(5);
            node = new Node(drawColor, rightOffset, aboveOffset, label);
        }

        return node;
    }

    static class Node {
        ColorDTO color;
        double rightOffset;
        double aboveOffset;
        String label;

        public Node(ColorDTO color, double rightOffset, double aboveOffset, String label) {
            this.color = color;
            this.rightOffset = rightOffset;
            this.aboveOffset = aboveOffset;
            this.label = label;
        }
    }
}
