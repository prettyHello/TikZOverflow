package be.ac.ulb.infof307.g09.controller.DTO.shapes;

import be.ac.ulb.infof307.g09.controller.DTO.shapes.CoordinatesDTO;
import be.ac.ulb.infof307.g09.controller.DTO.shapes.LineDTO;
import be.ac.ulb.infof307.g09.controller.DTO.shapes.Thickness;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LineDTOTest {

    @Test
    void print_expectedBehaviour() {
        LineDTO line = new LineDTO(new CoordinatesDTO(0,0),new CoordinatesDTO(2,2), Thickness.ULTRA_THIN,1);
        assertEquals("\\draw [black, ultra thin] (0.0,0.0) -- (2.0,2.0) ;",line.print());
    }
}