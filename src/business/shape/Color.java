package business.shape;


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
    public final String value;


    Color(String value) {
        this.value = value;
    }

}

