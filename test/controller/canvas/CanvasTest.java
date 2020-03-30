package controller.canvas;

import controller.Canvas.Canvas;
import controller.Canvas.CanvasImpl;
import controller.shape.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utilities.exceptions.FatalException;

import static org.junit.jupiter.api.Assertions.*;

public class CanvasTest {
    Canvas canvas;
    int id;
    Shape shape;

    @BeforeEach
    void setUp() {
        canvas = new CanvasImpl(500, 500);
        id = canvas.getIdForNewShape();
        shape = new Rectangle(new Coordinates(0, 0), new Coordinates(50, 50), id);
    }

    @Test
    void addShape() {
        canvas.addShape(shape);
        assertEquals(shape, canvas.getShapeById(id));
    }

    @Test
    void getShapeById() {
        canvas.addShape(shape);
        assertEquals(shape, canvas.getShapeById(id));
    }

    @Test
    void getShapesById_notPresent() {
        assertThrows(IllegalArgumentException.class, () -> {
            canvas.getShapeById(2);
        }, "Id present in canvas that hasn't been previously added");
    }

    @Test
    void getShapes_containsAddedShape() {
        canvas.addShape(shape);
        assertTrue(canvas.getShapes().contains(shape));
    }

    @Test
    void updateShape_notPresent() {
        assertThrows(FatalException.class, () -> {
            canvas.updateShape(shape);
        }, "Was able to update shape not previously added");
    }

    @Test
    void updateShape_sameType() {
        canvas.addShape(shape);
        Shape newShape = new Rectangle(new Coordinates(0, 0), new Coordinates(0, 0), id);
        canvas.updateShape(newShape);
        assertEquals(canvas.getShapeById(id), shape);
        assertEquals(newShape, shape);
    }

    @Test
    void updateShape_differentType() {
        canvas.addShape(shape);
        Shape newShape = new Circle(new Coordinates(0, 0), 50, id);
        assertEquals(shape, canvas.getShapeById(id));
        assertEquals(newShape, canvas.getShapeById(id));
    }

    @Test
    void changeShapeDrawColor() {
        shape.setDrawColor(Color.BLUE);
        canvas.addShape(shape);
        canvas.changeShapeDrawColor(id, Color.BLACK);
        assertEquals(canvas.getShapeById(id).getDrawColor(), Color.BLACK);
    }

    @Test
    void changeShapeFillColor() {
        shape.setFillColor(Color.RED);
        canvas.addShape(shape);
        canvas.getShapeById(id);
        canvas.changeShapeFillColor(id, Color.BLACK);
        assertEquals(canvas.getShapeById(id).getFillColor(), Color.BLACK);
    }

    @Test
    void getIdForNewShape_consecutivesShouldBeDifferent() {
        assertNotEquals(canvas.getIdForNewShape(), canvas.getIdForNewShape());
    }

    @Test
    void getIdForNewShape_uniquenessGuaranteed() {
        canvas.addShape(new Square(new Coordinates(0, 0), new Coordinates(0, 0), canvas.getIdForNewShape()));
        canvas.addShape(new Square(new Coordinates(0, 0), new Coordinates(0, 0), canvas.getIdForNewShape()));
    }

    @Test
    void rmShape_notPresent() {
        canvas.rmShape(shape);
    }

    @Test
    void rmShape() {
        canvas.addShape(shape);
        canvas.rmShape(shape);
        assertThrows(IllegalArgumentException.class, () -> {
            canvas.getShapeById(id);
        });
    }

    @Test
    void toTikz_empty() {
        assertEquals(canvas.toTikZ(), "");
    }

    @Test
    void toTikz() {
        Shape toAdd = new Square(new Coordinates(0, 0), new Coordinates(0, 0), canvas.getIdForNewShape());
        canvas.addShape(toAdd);
        assertEquals(canvas.toTikZ(), toAdd.print() + "\n");
    }

    @Test
    void toTikz_multipleShapes() {
        Shape toAdd1 = new Square(new Coordinates(0, 0), new Coordinates(0, 0), canvas.getIdForNewShape());
        Shape toAdd2 = new Circle(new Coordinates(0, 0), 50, canvas.getIdForNewShape());
        canvas.addShape(toAdd1);
        canvas.addShape(toAdd2);
        String control = toAdd1.print() + "\n" + toAdd2.print() + "\n";
        assertEquals(control, canvas.toTikZ());
    }
}
