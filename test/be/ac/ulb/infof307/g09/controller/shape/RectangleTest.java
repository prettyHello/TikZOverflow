package be.ac.ulb.infof307.g09.controller.shape;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RectangleTest {

    @Test
    void print_SimpleRectangle() {
        Rectangle rectangle = new Rectangle(new Coordinates(0,0), new Coordinates(1,2),Thickness.ULTRA_THIN.toString(),1);
        assertEquals("\\draw[draw=black, ultra thin] (0.0,0.0) rectangle (1.0,2.0) ;",rectangle.print());
    }

    @Test
    void print_ComplexRectangle() {
        Rectangle rectangle = new Rectangle(true,true,Color.BLUE,Color.RED,Thickness.VERY_THICK.toString(), new Coordinates(0,0), new Coordinates(1,2),1);
        assertEquals("\\filldraw[fill=red, draw=blue, very thick] (0.0,0.0) rectangle (1.0,2.0) ;",rectangle.print());
    }


}