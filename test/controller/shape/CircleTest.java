package controller.shape;

import org.junit.jupiter.api.Test;
import utilities.exceptions.BizzException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CircleTest {

    @Test
    void printSimpleCircle() {
//        Circle circle = new Circle(new Coordinates(1,2),3,1);
//        assertEquals("\\draw[draw=black] (1.0,2.0) circle [radius=3.0];",circle.print());
    }

    @Test
    void printComplexCircle() {
        Circle circle = new Circle(true,true,Color.BLUE,Color.RED, "THIN", new Coordinates(1,2),3,1);
        assertEquals("\\filldraw[fill=red, draw=blue] (1.0,2.0) circle [radius=3.0];",circle.print());
    }

    @Test
    void constructorNegativeRadius() {
//        assertThrows(BizzException.class, () -> {
//            new Circle(new Coordinates(1,1),-1,1);
//        }, "Error if a negative radius is passed");
    }

    @Test
    void constructorZeroRadius() {
//        assertThrows(BizzException.class, () -> {
//            new Circle(new Coordinates(1,1),0,1);
//        }, "Error if a zero value radius is passed");
    }
}