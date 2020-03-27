package business.shape;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {

    @Test
    void printSimpleTriangle() {
        Triangle triangle = new Triangle(new Coordinates(0,0),1);
        assertEquals(triangle.print(),"\\draw[draw=black] (0.0,0.0) -- (1.0,0.0) -- (0.0,1.0) -- cycle;");
    }

    @Test
    void printComplexComplex() {
        Triangle triangle = new Triangle(true,true,Color.BLUE,Color.RED,new Coordinates(0,0),new Coordinates(2,0),new Coordinates(0,3), 1);
        assertEquals(triangle.print(),"\\filldraw[fill=red, draw=blue] (0.0,0.0) -- (2.0,0.0) -- (0.0,3.0) -- cycle;");
    }
}