package be.ac.ulb.infof307.g09.controller.shape;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LineTest {

    @Test
    void print_expectedBehaviour() {
        Line line = new Line(new Coordinates(0,0),new Coordinates(2,2),Thickness.ULTRA_THIN.toString(),1);
        assertEquals("\\draw [black, ultra thin] (0.0,0.0) -- (2.0,2.0) ;",line.print());
    }
}