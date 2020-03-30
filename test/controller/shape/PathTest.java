package controller.shape;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PathTest {

    @Test
    void printSimplePathArrayOfPoints() {
        ArrayList<Coordinates> coordinates = new ArrayList<Coordinates>();
        coordinates.add(new Coordinates(0,0));
        coordinates.add(new Coordinates(2,2));
        coordinates.add(new Coordinates(0,4));
        Path path = new Path(coordinates,1);
        assertEquals("\\draw [black] (0.0,0.0) -- (2.0,2.0) -- (0.0,4.0) ;",path.print());
    }

    @Test
    void printComplexPath2Points() {
        ArrayList<Coordinates> coordinates = new ArrayList<Coordinates>();
        coordinates.add(new Coordinates(0,0));
        coordinates.add(new Coordinates(2,2));
        Path path = new Path(new Coordinates(0,0),new Coordinates(2,2),1,false, false);
        assertEquals("\\draw [black] (0.0,0.0) -- (2.0,2.0) ;",path.print());
    }

    @Test
    void printComplexPathArrayOfPoints() {
        ArrayList<Coordinates> coordinates = new ArrayList<Coordinates>();
        coordinates.add(new Coordinates(0,0));
        coordinates.add(new Coordinates(2,2));
        coordinates.add(new Coordinates(0,4));
        Path path = new Path(coordinates,false,false,Color.RED,1);
        assertEquals("\\draw [red] (0.0,0.0) -- (2.0,2.0) -- (0.0,4.0) ;",path.print());
    }


}