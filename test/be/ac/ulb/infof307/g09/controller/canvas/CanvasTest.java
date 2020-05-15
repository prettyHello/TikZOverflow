package be.ac.ulb.infof307.g09.controller.canvas;

import be.ac.ulb.infof307.g09.controller.Canvas.Canvas;
import be.ac.ulb.infof307.g09.controller.Canvas.CanvasImpl;
import be.ac.ulb.infof307.g09.controller.shape.*;
import be.ac.ulb.infof307.g09.exceptions.FatalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CanvasTest {
    Canvas canvas;
    int id;
    Shape shape;

    @BeforeEach
    void setUp() {
        canvas = new CanvasImpl();
        id = canvas.getIdForNewShape();
        shape = new Rectangle(new Coordinates(0, 0), new Coordinates(50, 50), Thickness.THIN, id);
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
        Shape newShape = new Rectangle(new Coordinates(0, 0), new Coordinates(0, 0), Thickness.VERY_THIN, id);
        canvas.updateShape(newShape);
        assertEquals(canvas.getShapeById(id), shape);
        assertEquals(newShape, shape);
    }

    @Test
    void updateShape_differentType() {
        canvas.addShape(shape);
        Shape newShape = new Circle(new Coordinates(0, 0), 50, Thickness.SEMI_THICK, id);
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
        canvas.addShape(new Square(new Coordinates(0, 0), new Coordinates(0, 0), Thickness.SEMI_THICK, canvas.getIdForNewShape()));
        canvas.addShape(new Square(new Coordinates(0, 0), new Coordinates(0, 0), Thickness.ULTRA_THICK, canvas.getIdForNewShape()));
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
        Shape toAdd = new Square(new Coordinates(0, 0), new Coordinates(0, 0), Thickness.ULTRA_THIN, canvas.getIdForNewShape());
        canvas.addShape(toAdd);

        String control = "\\documentclass{article}\n\\usepackage[utf8]{inputenc}\n\\usepackage{tikz}\n\n\\begin{document}\n\\begin{tikzpicture}\n\n";
        control += toAdd.print() + "\n";
        control += "\n\\end{tikzpicture}\n\\end{document}";
        assertEquals(canvas.toTikZ(), control);
    }

    @Test
    void toTikz_multipleShapes() {
        Shape toAdd1 = new Square(new Coordinates(0, 0), new Coordinates(0, 0), Thickness.VERY_THICK, canvas.getIdForNewShape());
        Shape toAdd2 = new Circle(new Coordinates(0, 0), 50, Thickness.VERY_THICK, canvas.getIdForNewShape());
        canvas.addShape(toAdd1);
        canvas.addShape(toAdd2);

        String control = "\\documentclass{article}\n\\usepackage[utf8]{inputenc}\n\\usepackage{tikz}\n\n\\begin{document}\n\\begin{tikzpicture}\n\n";
        control += toAdd1.print() + "\n";
        control += toAdd2.print() + "\n";
        control += "\n\\end{tikzpicture}\n\\end{document}";

        assertEquals(control, canvas.toTikZ());
    }

    @Test
    void clear_expectedBehaviour() {
        canvas.clear();
        assertTrue(canvas.getShapes().isEmpty());
        assertEquals(canvas.getIdForNewShape(), 1);
    }

    @Test
    void copyToClipboard_expectedBehaviour() {
        int id1 = canvas.getIdForNewShape();
        int id2 = canvas.getIdForNewShape();
        Shape circle = new Circle(new Coordinates(50, 50), 40, Thickness.THICK, id1);
        Shape line = new Line(new Coordinates(100, 100), new Coordinates(500, 500), Thickness.THIN, id2);
        canvas.addShape(circle);
        canvas.addShape(line);
        List<Integer> toCopy = new ArrayList<>();
        toCopy.add(1);

        canvas.copyToClipboard(toCopy);
        assertEquals(2, canvas.getShapes().size()); // unchanged ?
        assertEquals(circle, canvas.getShapeById(id1));
        assertEquals(line, canvas.getShapeById(id2));
    }

    @Test
    void copyToClipboard_copyNothing() {
        int id1 = canvas.getIdForNewShape();
        int id2 = canvas.getIdForNewShape();
        Shape circle = new Circle(new Coordinates(50, 50), 40, Thickness.THICK, id1);
        Shape line = new Line(new Coordinates(100, 100), new Coordinates(500, 500), Thickness.THIN, id2);
        canvas.addShape(circle);
        canvas.addShape(line);

        canvas.copyToClipboard(new ArrayList<>());
        assertEquals(2, canvas.getShapes().size()); // unchanged ?
        assertEquals(circle, canvas.getShapeById(id1));
        assertEquals(line, canvas.getShapeById(id2));
    }

    @Test
    void paste_expectedBehaviour() {
        int id1 = canvas.getIdForNewShape();
        int id2 = canvas.getIdForNewShape();
        Shape circle = new Circle(new Coordinates(50, 50), 40, Thickness.THICK, id1);
        Shape line = new Line(new Coordinates(100, 100), new Coordinates(500, 500), Thickness.THIN, id2);
        canvas.addShape(circle);
        canvas.addShape(line);
        List<Integer> toCopy = new ArrayList<>();
        toCopy.add(id1);

        canvas.copyToClipboard(toCopy);
        Coordinates pasteLoc = new Coordinates(300, 300);
        canvas.pasteClipBoard(pasteLoc);

        assertEquals(3, canvas.getShapes().size());
        for (Shape s : canvas.getShapes()) {
            if (s.getId() != id1 && s.getId() != id2) {
                Circle pastedCircle = (Circle) s;
                assertEquals(pasteLoc, pastedCircle.getCoordinates());
                return;
            }
        }

        fail("canvas didn't contain the pasted circle");
    }

    @Test
    void paste_emptyClipboard() {
        int id1 = canvas.getIdForNewShape();
        int id2 = canvas.getIdForNewShape();
        Shape circle = new Circle(new Coordinates(50, 50), 40, Thickness.THICK, id1);
        Shape line = new Line(new Coordinates(100, 100), new Coordinates(500, 500), Thickness.THIN, id2);
        canvas.addShape(circle);
        canvas.addShape(line);

        canvas.copyToClipboard(new ArrayList<>());

        canvas.pasteClipBoard(new Coordinates(100, 100));
        assertEquals(2, canvas.getShapes().size());
    }
}
