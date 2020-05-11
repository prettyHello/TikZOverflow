package be.ac.ulb.infof307.g09.controller.shape;


import java.util.HashMap;
import java.util.Map;

/**
 * All the colors in tikz.
 */
public enum Color {
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

    Color(String value) {
        this.value = value;
    }

    private static final Map<String, Color> lookup = new HashMap<>();

    static {
        for (Color c : Color.values()) {
            lookup.put(c.getValue(), c);
        }
    }

    public String getValue() {
        return value;
    }

    public static Color get(String color) {
        return lookup.get(color);
    }
}

