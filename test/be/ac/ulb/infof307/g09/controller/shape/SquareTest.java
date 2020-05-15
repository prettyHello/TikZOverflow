package be.ac.ulb.infof307.g09.controller.shape;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SquareTest {

    @Test
    void print_SimpleSquare() {
        Square square = new Square(new Coordinates(0, 0), new Coordinates(1, 2), Thickness.THIN, 1);
        assertEquals("\\draw[draw=black, thin] (0.0,0.0) rectangle (1.0,2.0) ;", square.print());
    }

    @Test
    void print_ComplexSquare() {
        Square square = new Square(new Coordinates(0, 0), 4, Thickness.THICK, 1);
        assertEquals("\\draw[draw=black, thick] (0.0,0.0) rectangle (4.0,4.0) ;", square.print());
    }
}