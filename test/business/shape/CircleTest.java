package business.shape;

import exceptions.BizzException;
import exceptions.FatalException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CircleTest {

    @Test
    void printSimpleCircle() {
        Circle circle = new Circle(new Coordinates(1,2),3,1);
        assertEquals(circle.print(),"\\draw[draw=black] (1.0,2.0) circle [radius=3.0];","Error if a negative radius is passed");
    }

    @Test
    void printComplexCircle() {
        Circle circle = new Circle(true,true,Color.BLUE,Color.RED,new Coordinates(1,2),3,1);
        assertEquals(circle.print(),"\\filldraw[fill=red, draw=blue] (1.0,2.0) circle [radius=3.0];","Error if a negative radius is passed");
    }

    @Test
    void constructorNegativeRadius() {
        assertThrows(BizzException.class, () -> {
            new Circle(new Coordinates(1,1),-1,1);
        }, "Error if a negative radius is passed");
    }

    @Test
    void constructorZeroRadius() {
        assertThrows(BizzException.class, () -> {
            new Circle(new Coordinates(1,1),0,1);
        }, "Error if a zero value radius is passed");
    }
}