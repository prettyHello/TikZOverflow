package be.ac.ulb.infof307.g09.controller.shape;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TriangleTest {

    @Test
    void printSimpleTriangle() {
        Triangle triangle = new Triangle(new Coordinates(0,0),"SEMI_THICK",1);
        assertEquals("\\draw[draw=black, semi thick] (0.0,0.0) -- (1.0,0.0) -- (0.0,1.0) -- cycle;",triangle.print());
    }

    @Test
    void printComplexComplex() {
        Triangle triangle = new Triangle(true,true,Color.BLUE,Color.RED,"VERY_THIN", new Coordinates(0,0),new Coordinates(2,0),new Coordinates(0,3), 1);
        assertEquals("\\filldraw[fill=red, draw=blue, very thin] (0.0,0.0) -- (2.0,0.0) -- (0.0,3.0) -- cycle;",triangle.print());
    }
}