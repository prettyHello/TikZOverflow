package be.ac.ulb.infof307.g09.controller.DTO.shapes;


import java.util.HashMap;
import java.util.Map;

/**
 * All the colors in tikz.
 */
public enum ColorDTO {
    RED("red"),
    GREEN("green"),
    BLUE("blue"),
    CYAN("cyan"),
    MAGENTA("magenta"),
    YELLOW("yellow"),
    BLACK("black"),
    GRAY("gray"),
    DARKGRAY("darkgray"),
    LIGHTGRAY("lightgray"),
    BROWN("brown"),
    LIME("lime"),
    OLIVE("olive"),
    ORANGE("orange"),
    PINK("pink"),
    PURPLE("purple"),
    TEAL("teal"),
    VIOLET("violet"),
    WHITE("white");

    private final String value;

    ColorDTO(String value) {
        this.value = value;
    }

    private static final Map<String, ColorDTO> LOOKUP = new HashMap<>();

    static {
        for (ColorDTO c : ColorDTO.values()) {
            LOOKUP.put(c.getValue(), c);
        }
    }

    public String getValue() {
        return value;
    }

    public static ColorDTO get(String color) {
        return LOOKUP.get(color);
    }
}

