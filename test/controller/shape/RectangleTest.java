package controller.shape;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RectangleTest {

    @Test
    void printSimpleRectangle() {
//        Rectangle rectangle = new Rectangle(new Coordinates(0,0), new Coordinates(1,2),1);
//        assertEquals("\\draw[draw=black] (0.0,0.0) rectangle (1.0,2.0) ;",rectangle.print());
    }

    @Test
    void printComplexRectangle() {
        Rectangle rectangle = new Rectangle(true,true,Color.BLUE,Color.RED,"THIN", new Coordinates(0,0), new Coordinates(1,2),1);
        assertEquals("\\filldraw[fill=red, draw=blue] (0.0,0.0) rectangle (1.0,2.0) ;",rectangle.print());
    }


}