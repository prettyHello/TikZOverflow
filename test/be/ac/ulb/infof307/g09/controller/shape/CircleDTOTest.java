package be.ac.ulb.infof307.g09.controller.shape;

import be.ac.ulb.infof307.g09.controller.DTO.shapes.CircleDTO;
import be.ac.ulb.infof307.g09.controller.DTO.shapes.ColorDTO;
import be.ac.ulb.infof307.g09.controller.DTO.shapes.CoordinatesDTO;
import be.ac.ulb.infof307.g09.controller.DTO.shapes.Thickness;
import org.junit.jupiter.api.Test;
import be.ac.ulb.infof307.g09.exceptions.BizzException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CircleDTOTest {

    @Test
    void print_simpleCircle() {
        CircleDTO circle = new CircleDTO(new CoordinatesDTO(1,2),3, Thickness.ULTRA_THIN.toString(),1);
        assertEquals("\\draw[draw=black, ultra thin] (1.0,2.0) circle [radius=3.0] ;",circle.print());
    }

    @Test
    void print_complexCircle() {
        CircleDTO circle = new CircleDTO(true,true, ColorDTO.BLUE, ColorDTO.RED, Thickness.VERY_THICK.toString(), new CoordinatesDTO(1,2),3,1);
        assertEquals("\\filldraw[fill=red, draw=blue, very thick] (1.0,2.0) circle [radius=3.0] ;",circle.print());
    }

    @Test
    void constructor_NegativeRadius() {
        assertThrows(BizzException.class, () -> {
            new CircleDTO(new CoordinatesDTO(1,1),-1,"THIN",1);
        }, "Error if a negative radius is passed");
    }

    @Test
    void constructor_ZeroRadius() {
        assertThrows(BizzException.class, () -> {
            new CircleDTO(new CoordinatesDTO(1,1),0,"THIN",1);
        }, "Error if a zero value radius is passed");
    }
}