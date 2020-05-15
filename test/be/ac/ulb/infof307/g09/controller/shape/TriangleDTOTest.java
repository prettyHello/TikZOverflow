package be.ac.ulb.infof307.g09.controller.shape;

import be.ac.ulb.infof307.g09.controller.DTO.shapes.ColorDTO;
import be.ac.ulb.infof307.g09.controller.DTO.shapes.CoordinatesDTO;
import be.ac.ulb.infof307.g09.controller.DTO.shapes.Thickness;
import be.ac.ulb.infof307.g09.controller.DTO.shapes.TriangleDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TriangleDTOTest {

    @Test
    void print_SimpleTriangle() {
        TriangleDTO triangle = new TriangleDTO(new CoordinatesDTO(0,0), Thickness.SEMI_THICK,1);
        assertEquals("\\draw[draw=black, semi thick] (0.0,0.0) -- (1.0,0.0) -- (0.0,1.0) -- cycle;",triangle.print());
    }

    @Test
    void print_ComplexComplex() {
        TriangleDTO triangle = new TriangleDTO(true,true, ColorDTO.BLUE, ColorDTO.RED,Thickness.VERY_THIN, new CoordinatesDTO(0,0),new CoordinatesDTO(2,0),new CoordinatesDTO(0,3), 1);
        assertEquals("\\filldraw[fill=red, draw=blue, very thin] (0.0,0.0) -- (2.0,0.0) -- (0.0,3.0) -- cycle;",triangle.print());
    }
}