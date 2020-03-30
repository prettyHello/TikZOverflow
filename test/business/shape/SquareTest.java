package business.shape;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SquareTest {

    @Test
    void printSimpleSquare() {
        Square square = new Square(new Coordinates(0,0), new Coordinates(1,2),1);
        assertEquals("\\draw[draw=black] (0.0,0.0) rectangle (1.0,2.0) ;",square.print());
    }

    @Test
    void printComplexRectangle() {
        Square square = new Square(new Coordinates(0,0),4,1);
        assertEquals("\\draw[draw=black] (0.0,0.0) rectangle (4.0,4.0) ;",square.print());
    }


}