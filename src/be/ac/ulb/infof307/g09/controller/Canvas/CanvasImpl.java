package be.ac.ulb.infof307.g09.controller.Canvas;

import be.ac.ulb.infof307.g09.controller.shape.*;
import be.ac.ulb.infof307.g09.exceptions.FatalException;

import java.util.ArrayList;
import java.util.List;

public class CanvasImpl implements Canvas {
    final List<Shape> shapes;
    final List<Shape> clipboard;
    private int idCounter = 0;

    /**
     * package visibility constructor. Should use the singleton to get an instance of this class.
     */
    public CanvasImpl() {
        shapes = new ArrayList<>();
        clipboard = new ArrayList<>();
    }

    /**
     * Getter for the list of shapes on the canvas
     *
     * @return returns a defensive copy of the list
     */
    @Override
    public List<Shape> getShapes() {
        return new ArrayList<>(this.shapes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape getShapeById(int id) throws IllegalArgumentException {
        for (Shape shape : this.shapes) {
            if (shape.getId() == id) {
                return shape;
            }
        }
        throw new IllegalArgumentException("No shape with this id exists in the canvas");
    }

    /**
     * Adds a shape to the canvas
     *
     * @param shape the shape to add
     * @throws IllegalArgumentException if the shape is already present on the canvas
     */
    @Override
    public void addShape(Shape shape) throws FatalException {
        if (this.shapes.contains(shape)) {
            throw new FatalException("Shape already exists on the canvas. Need to be able to differentiate between shapes");
        }
        this.shapes.add(shape);
    }

    /**
     * Update a shape to the canvas
     *
     * @param shape the shape to update
     * @throws IllegalArgumentException if the shape is not present on the canvas
     */
    @Override
    public void updateShape(Shape shape) throws FatalException {
        if (!this.shapes.contains(shape)) {
            throw new FatalException("Shape doesn't exists on the canvas.");
        }
        this.shapes.remove(shape);
        this.shapes.add(shape);
    }

    /**
     * Change the fill color of a shape
     *
     * @param id        id of the shape
     * @param fillColor color to fill whit
     */
    public void changeShapeFillColor(int id, Color fillColor) {
        for (Shape shape : shapes) {
            if (shape.getId() == id) {
                shape.setFill(true);
                shape.setFillColor(fillColor);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setShapeLabel(int shapeId, String label, Color labelColor) {
        Shape toChange = null;
        for (Shape shape : shapes) {
            if (shape.getId() == shapeId) {
                toChange = shape;
                break;
            }
        }

        if (toChange == null) {
            throw new IllegalArgumentException("canvas does not contain a shape with the specified id");
        } else if (!(toChange instanceof LabelizableShape)) {
            return;
        }

        LabelizableShape toChangeLabelizable = (LabelizableShape) toChange;
        Label shapeLabel = new Label(label, labelColor);
        toChangeLabelizable.setLabel(shapeLabel);
    }

    /**
     * Change the draw color of a shape
     *
     * @param id        id of the shape
     * @param drawColor color to draw whit
     */
    public void changeShapeDrawColor(int id, Color drawColor) {
        for (Shape shape : shapes) {
            if (shape.getId() == id) {
                shape.setDraw(true);
                shape.setDrawColor(drawColor);
            }
        }
    }

    /**
     * Get an unique id for a new shape
     *
     * @return unique id
     */
    public int getIdForNewShape() {
        this.idCounter = this.idCounter + 1;
        return this.idCounter;
    }

    /**
     * Deletes a shape from the canvas. If the canvas does not contain the given shape, silently returns
     *
     * @param shape the shape to remove
     */
    @Override
    public void rmShape(Shape shape) {
        this.shapes.remove(shape);
    }

    /**
     * Deletes a shape from the canvas. If the canvas does not contain the given shape, silently returns
     *
     * @param id the id of the shape to remove
     */
    @Override
    public void rmShapeById(int id) {
        //We need to create a temporary shape for the array to find the one in the list, shape are only compared on their id, nothing else mather for the list.
        Shape tmpShape = new Square(new Coordinates(0, 0), 0, "THIN", id);
        this.shapes.remove(tmpShape);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toTikZ() {
        StringBuilder tikz = new StringBuilder();
        tikz.append("\\documentclass{article}\n\\usepackage[utf8]{inputenc}\n\\usepackage{tikz}\n\n\\begin{document}\n\\begin{tikzpicture}\n\n");
        for (Shape shape : shapes) {
            tikz.append(shape.print()).append("\n");
        }
        tikz.append("\n\\end{tikzpicture}\n\\end{document}");
        return tikz.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        this.shapes.clear();
        this.idCounter = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void copyToClipboard(List<Integer> shapeIds) {
        this.clipboard.clear();
        for (Integer id : shapeIds){
            for (Shape s : this.shapes){
                if (s.getId() == id){
                    this.clipboard.add(s);
                    break;
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pasteClipBoard(Coordinates destinationPos) {
        if (this.clipboard.isEmpty()) {
            return;
        }

        // calc mean position of shapes in clipboard
        Coordinates meanPos = new Coordinates(0,0);
        for (Shape s : clipboard){
            meanPos = meanPos.add(s.getCoordinates());
        }
        meanPos = new Coordinates(meanPos.getX() / clipboard.size(), meanPos.getY() / clipboard.size());

        // new shape relative to mean pos
        for (Shape shape : clipboard){
            Shape toAdd = null;

            if (shape instanceof Circle){
                Circle c = new Circle((Circle) shape, getIdForNewShape());
                Coordinates offsetToMean = new Coordinates(meanPos.sub(c.getCoordinates()));
                c.setCoordinates(destinationPos.sub(offsetToMean));
                toAdd = c;
            }else if (shape instanceof Triangle){
                Triangle t = new Triangle((Triangle) shape, getIdForNewShape());
                Coordinates offsetToMean = new Coordinates(meanPos.sub(t.getCoordinates()));
                t.setOriginPoint(destinationPos.sub(offsetToMean));
                Coordinates offsetMeanToSec = new Coordinates(meanPos.sub(t.getSecondPoint()));
                t.setSecondPoint(destinationPos.sub(offsetMeanToSec));
                Coordinates offsetMeanToThi = new Coordinates(meanPos.sub(t.getThirdPoint()));
                t.setThirdPoint(destinationPos.sub(offsetMeanToThi));
                toAdd = t;
            }else if (shape instanceof Square){

            }else if (shape instanceof Line){
                Line l = new Line((Line) shape, getIdForNewShape());
                Coordinates offsetToMean = new Coordinates(meanPos.sub(l.getCoordinates()));
                l.setStartCoordinates(destinationPos.sub(offsetToMean));
                Coordinates offsetMeanToSec = new Coordinates(meanPos.sub(l.getEndCoordinates()));
                l.setEndCoordinates(destinationPos.sub(offsetMeanToSec));
                toAdd = l;
            }else if (shape instanceof Arrow){

            }
            this.shapes.add(toAdd);
        }
    }
}
