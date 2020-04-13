package view.editor;

import controller.Canvas.Canvas;
import controller.shape.Coordinates;
import controller.shape.Square;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;


import java.util.ArrayList;

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
    protected boolean drawnFromToolbar = false;

    ContextMenu shapeContextMenu;
    Canvas canvas;
    EditorController editorController;

    public ShapeHandler(ContextMenu shapeContextMenu, Canvas canvas, EditorController editorController){
        this.shapeContextMenu = shapeContextMenu;
        this.canvas = canvas;
        this.editorController = editorController;
    }

    /**
     * Rightclick dropdown menu, change FillColor
     * @param color
     */
    public void setFillColor(Color color) {
        //TODO use canvas.updateShape to update the shape.
        if (shapeContextMenu.getOwnerNode() instanceof Shape) {
            Shape shape = (Shape) shapeContextMenu.getOwnerNode();
            shape.setFill(Color.valueOf(color.toString()));
            Color fillColor = (Color) shape.getFill();
            canvas.changeShapeFillColor(Integer.parseInt(shape.getId()), getColorNameFromRgb(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue()));
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
        }
        editorController.translateToTikz();
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
            drawnFromToolbar = true;
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
    public void handleDraw (controller.shape.Shape shape) {
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
                ArrayList<Coordinates> points = ((controller.shape.Triangle) shape).getPoints();
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
            editorController.pane.getChildren().add(shapeDrawing);
            shapeDrawing.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onShapeSelected);
        }
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
}
