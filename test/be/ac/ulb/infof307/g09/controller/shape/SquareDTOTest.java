package be.ac.ulb.infof307.g09.controller.shape;

import be.ac.ulb.infof307.g09.controller.DTO.shapes.CoordinatesDTO;
import be.ac.ulb.infof307.g09.controller.DTO.shapes.SquareDTO;
import be.ac.ulb.infof307.g09.controller.DTO.shapes.Thickness;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SquareDTOTest {

    @Test
    void print_SimpleSquare() {
        SquareDTO square = new SquareDTO(new CoordinatesDTO(0,0), new CoordinatesDTO(1,2), Thickness.THIN,1);
        assertEquals("\\draw[draw=black, thin] (0.0,0.0) rectangle (1.0,2.0) ;",square.print());
    }

    @Test
    void print_ComplexSquare() {
        SquareDTO square = new SquareDTO(new CoordinatesDTO(0,0),4,Thickness.THICK,1);
        assertEquals("\\draw[draw=black, thick] (0.0,0.0) rectangle (4.0,4.0) ;",square.print());
    }


}