package business.shape;

/**
 * Shape is the representation of a line of tikz code, eg :
 * \draw[thick,rounded corners=8pt] (0,0) -- (0,2) -- (1,3.25)
 *    -- (2,2) -- (2,0) -- (0,2) -- (2,2) -- (0,0) -- (2,0);
 * In order to avoid confusion with an actual line, it's called a shape here.
 *
 * Since it's an abstract class it's only intended to extend actual shape class, like Path.java,
 * that should be used to represent the code line above.
 */
public abstract class Shape {

    private boolean draw = true;
    private boolean fill = false;
    private Color fillColor = Color.BLACK;
    private Color drawColor = Color.BLACK;
    private Thickness thickness = Thickness.THIN;
}
