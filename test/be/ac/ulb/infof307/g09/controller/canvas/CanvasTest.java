package be.ac.ulb.infof307.g09.controller.canvas;

import be.ac.ulb.infof307.g09.controller.Canvas.Canvas;
import be.ac.ulb.infof307.g09.controller.Canvas.CanvasImpl;
import be.ac.ulb.infof307.g09.controller.shape.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import be.ac.ulb.infof307.g09.exceptions.FatalException;

import static org.junit.jupiter.api.Assertions.*;

public class CanvasTest {
    Canvas canvas;
    int id;
    Shape shape;

    @BeforeEach
    void setUp() {
        canvas = new CanvasImpl();
        id = canvas.getIdForNewShape();
        shape = new Rectangle(new Coordinates(0, 0), new Coordinates(50, 50),Thickness.THIN.toString(), id);
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
        Shape newShape = new Rectangle(new Coordinates(0, 0), new Coordinates(0, 0),Thickness.VERY_THIN.toString(), id);
        canvas.updateShape(newShape);
        assertEquals(canvas.getShapeById(id), shape);
        assertEquals(newShape, shape);
    }

    @Test
    void updateShape_differentType() {
        canvas.addShape(shape);
        Shape newShape = new Circle(new Coordinates(0, 0), 50,Thickness.SEMI_THICK.toString(), id);
        assertEquals(shape, canvas.getShapeById(id));
        assertEquals(newShape, canvas.getShapeById(id));
    }

    @Test
    void changeShapeDrawColor_expectedBehaviour() {
        shape.setDrawColor(Color.BLUE);
        canvas.addShape(shape);
        canvas.changeShapeDrawColor(id, Color.BLACK);
        assertEquals(canvas.getShapeById(id).getDrawColor(), Color.BLACK);
    }

    @Test
    void changeShapeFillColor_expectedBehaviour() {
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
        canvas.addShape(new Square(new Coordinates(0, 0), new Coordinates(0, 0), Thickness.SEMI_THICK.toString(), canvas.getIdForNewShape()));
        canvas.addShape(new Square(new Coordinates(0, 0), new Coordinates(0, 0), Thickness.ULTRA_THICK.toString(), canvas.getIdForNewShape()));
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
        assertEquals(canvas.toTikZ(), "\\documentclass{article}\n\\usepackage[utf8]{inputenc}\n\\usepackage{tikz}\n\n\\begin{document}\n\\begin{tikzpicture}\n\n\n\\end{tikzpicture}\n\\end{document}");
    }

    @Test
    void toTikz_expectedBehaviour() {
        Shape toAdd = new Square(new Coordinates(0, 0), new Coordinates(0, 0), Thickness.ULTRA_THIN.toString(),canvas.getIdForNewShape());
        canvas.addShape(toAdd);

        String control = "\\documentclass{article}\n\\usepackage[utf8]{inputenc}\n\\usepackage{tikz}\n\n\\begin{document}\n\\begin{tikzpicture}\n\n";
        control += toAdd.print() + "\n";
        control += "\n\\end{tikzpicture}\n\\end{document}";
        assertEquals(canvas.toTikZ(), control);
    }

    @Test
    void toTikz_multipleShapes() {
        Shape toAdd1 = new Square(new Coordinates(0, 0), new Coordinates(0, 0), Thickness.VERY_THICK.toString(),canvas.getIdForNewShape());
        Shape toAdd2 = new Circle(new Coordinates(0, 0), 50, Thickness.VERY_THICK.toString(),canvas.getIdForNewShape());
        canvas.addShape(toAdd1);
        canvas.addShape(toAdd2);

        String control = "\\documentclass{article}\n\\usepackage[utf8]{inputenc}\n\\usepackage{tikz}\n\n\\begin{document}\n\\begin{tikzpicture}\n\n";
        control += toAdd1.print() + "\n";
        control += toAdd2.print() + "\n";
        control += "\n\\end{tikzpicture}\n\\end{document}";

        assertEquals(control, canvas.toTikZ());
    }

/*    @Test
    void toTikz_shapeWithLabel() {
        int shapeId = canvas.getIdForNewShape();
        Shape toAdd1 = new Square(new Coordinates(0, 0), new Coordinates(0, 0), Thickness.VERY_THICK.toString(),shapeId);

        canvas.addShape(toAdd1);
        canvas.setShapeLabel(shapeId,);


        String control = "\\documentclass{article}\n\\usepackage[utf8]{inputenc}\n\\usepackage{tikz}\n\n\\begin{document}\n\\begin{tikzpicture}\n\n";
        control += toAdd1.print() + "\n";

        control += "\n\\end{tikzpicture}\n\\end{document}";

        assertEquals(control, canvas.toTikZ());
    }*/

    @Test
    void clear_expectedBehaviour() {
        canvas.clear();
        assertTrue(canvas.getShapes().isEmpty());
        assertEquals(canvas.getIdForNewShape(), 1);
    }
}