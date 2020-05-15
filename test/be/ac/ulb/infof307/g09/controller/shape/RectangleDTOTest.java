package be.ac.ulb.infof307.g09.controller.shape;

import be.ac.ulb.infof307.g09.controller.DTO.shapes.ColorDTO;
import be.ac.ulb.infof307.g09.controller.DTO.shapes.CoordinatesDTO;
import be.ac.ulb.infof307.g09.controller.DTO.shapes.RectangleDTO;
import be.ac.ulb.infof307.g09.controller.DTO.shapes.Thickness;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RectangleDTOTest {

    @Test
    void print_SimpleRectangle() {
        RectangleDTO rectangle = new RectangleDTO(new CoordinatesDTO(0,0), new CoordinatesDTO(1,2), Thickness.ULTRA_THIN.toString(),1);
        assertEquals("\\draw[draw=black, ultra thin] (0.0,0.0) rectangle (1.0,2.0) ;",rectangle.print());
    }

    @Test
    void print_ComplexRectangle() {
        RectangleDTO rectangle = new RectangleDTO(true,true, ColorDTO.BLUE, ColorDTO.RED,Thickness.VERY_THICK.toString(), new CoordinatesDTO(0,0), new CoordinatesDTO(1,2),1);
        assertEquals("\\filldraw[fill=red, draw=blue, very thick] (0.0,0.0) rectangle (1.0,2.0) ;",rectangle.print());
    }


}