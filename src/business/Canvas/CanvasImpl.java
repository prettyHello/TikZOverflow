package business.Canvas;

import java.util.ArrayList;
import java.util.List;

public class CanvasImpl implements Canvas {

    final List<Shape> shapes;

    /**
     * package visibility constructor. Should use the singleton to get an instance of this class.
     */
    CanvasImpl() {
        shapes = new ArrayList<>();
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
     * Adds a shape to the canvas
     *
     * @param shape the shape to add
     * @throws IllegalArgumentException if the shape is already present on the canvas
     */
    @Override
    public void addShape(Shape shape) {
        if (this.shapes.contains(shape)) {
            throw new IllegalArgumentException("Shape already exists on the canvas. Need to be able to differentiate between shapes");
        }
        this.shapes.add(shape);
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

    @Override
    public String toTikZ() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
