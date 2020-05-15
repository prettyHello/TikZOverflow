package be.ac.ulb.infof307.g09.controller.shape;

import be.ac.ulb.infof307.g09.controller.DTO.shapes.ArrowDTO;
import be.ac.ulb.infof307.g09.controller.DTO.shapes.CoordinatesDTO;
import be.ac.ulb.infof307.g09.controller.DTO.shapes.Thickness;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrowDTOTest {

    @Test
    void print_expectedBehaviour() {
        ArrowDTO arrow = new ArrowDTO(new CoordinatesDTO(0,0),new CoordinatesDTO(2,2), Thickness.VERY_THIN, 1);
        assertEquals("\\draw [black,->, very thin] (0.0,0.0) -- (2.0,2.0) ;",arrow.print());
    }
}