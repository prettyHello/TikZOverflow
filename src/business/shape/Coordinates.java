package business.shape;

/**
 * Pair of coordinates, with their respective units and options.
 * only work with default units for now (cm).
 * https://en.wikibooks.org/wiki/LaTeX/PGF/TikZ#Specifying_Coordinates
 *
 */
public class Coordinates {
    private float x = 0;
    private float y = 0;

    public Coordinates(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String print(){
        return "("+this.x+","+this.y+") ";
    }
}
