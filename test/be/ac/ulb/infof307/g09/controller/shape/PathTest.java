package be.ac.ulb.infof307.g09.controller.shape;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PathTest {

    @Test
    void print_SimplePathArrayOfPoints() {
        ArrayList<Coordinates> coordinates = new ArrayList<>();
        coordinates.add(new Coordinates(0, 0));
        coordinates.add(new Coordinates(2, 2));
        coordinates.add(new Coordinates(0, 4));
        Path path = new Path(coordinates, Thickness.THIN, 1);
        assertEquals("\\draw [black, thin] (0.0,0.0) -- (2.0,2.0) -- (0.0,4.0) ;", path.print());
    }

    @Test
    void print_ComplexPath2Points() {
        ArrayList<Coordinates> coordinates = new ArrayList<>();
        coordinates.add(new Coordinates(0, 0));
        coordinates.add(new Coordinates(2, 2));
        Path path = new Path(new Coordinates(0, 0), new Coordinates(2, 2), Thickness.SEMI_THICK, 1, false, false);
        assertEquals("\\draw [black, semi thick] (0.0,0.0) -- (2.0,2.0) ;", path.print());
    }

    @Test
    void print_ComplexPathArrayOfPoints() {
        ArrayList<Coordinates> coordinates = new ArrayList<>();
        coordinates.add(new Coordinates(0, 0));
        coordinates.add(new Coordinates(2, 2));
        coordinates.add(new Coordinates(0, 4));
        Path path = new Path(coordinates, false, false, Color.RED, Thickness.ULTRA_THICK, 1);
        assertEquals("\\draw [red, ultra thick] (0.0,0.0) -- (2.0,2.0) -- (0.0,4.0) ;", path.print());
    }


}