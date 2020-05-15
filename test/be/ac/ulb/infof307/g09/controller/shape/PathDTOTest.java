package be.ac.ulb.infof307.g09.controller.shape;

import be.ac.ulb.infof307.g09.controller.DTO.shapes.ColorDTO;
import be.ac.ulb.infof307.g09.controller.DTO.shapes.CoordinatesDTO;
import be.ac.ulb.infof307.g09.controller.DTO.shapes.PathDTO;
import be.ac.ulb.infof307.g09.controller.DTO.shapes.Thickness;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PathDTOTest {

    @Test
    void print_SimplePathArrayOfPoints() {
        ArrayList<CoordinatesDTO> coordinates = new ArrayList<>();
        coordinates.add(new CoordinatesDTO(0,0));
        coordinates.add(new CoordinatesDTO(2,2));
        coordinates.add(new CoordinatesDTO(0,4));
        PathDTO path = new PathDTO(coordinates, Thickness.THIN,1);
        assertEquals("\\draw [black, thin] (0.0,0.0) -- (2.0,2.0) -- (0.0,4.0) ;",path.print());
    }

    @Test
    void print_ComplexPath2Points() {
        ArrayList<CoordinatesDTO> coordinates = new ArrayList<>();
        coordinates.add(new CoordinatesDTO(0,0));
        coordinates.add(new CoordinatesDTO(2,2));
        PathDTO path = new PathDTO(new CoordinatesDTO(0,0),new CoordinatesDTO(2,2),Thickness.SEMI_THICK,1,false, false);
        assertEquals("\\draw [black, semi thick] (0.0,0.0) -- (2.0,2.0) ;",path.print());
    }

    @Test
    void print_ComplexPathArrayOfPoints() {
        ArrayList<CoordinatesDTO> coordinates = new ArrayList<>();
        coordinates.add(new CoordinatesDTO(0,0));
        coordinates.add(new CoordinatesDTO(2,2));
        coordinates.add(new CoordinatesDTO(0,4));
        PathDTO path = new PathDTO(coordinates,false,false, ColorDTO.RED,Thickness.ULTRA_THICK, 1);
        assertEquals("\\draw [red, ultra thick] (0.0,0.0) -- (2.0,2.0) -- (0.0,4.0) ;",path.print());
    }


}