package business.shape;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {

    @Test
    void printSimpleRectangle() {
        Rectangle rectangle = new Rectangle(new Coordinates(0,0), new Coordinates(1,2),1);
        assertEquals(rectangle.print(),"\\draw[draw=black] (0.0,0.0) rectangle (1.0,2.0) ;");
    }

    @Test
    void printComplexRectangle() {
        Rectangle rectangle = new Rectangle(true,true,Color.BLUE,Color.RED,new Coordinates(0,0), new Coordinates(1,2),1);
        assertEquals(rectangle.print(),"\\filldraw[fill=red, draw=blue] (0.0,0.0) rectangle (1.0,2.0) ;");
    }


}