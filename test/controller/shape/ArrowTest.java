package controller.shape;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrowTest {

    @Test
    void printLine() {
        Arrow arrow = new Arrow(new Coordinates(0,0),new Coordinates(2,2),"THIN", 1);
        assertEquals("\\draw [black,->, thin] (0.0,0.0) -- (2.0,2.0) ;",arrow.print());
    }
}