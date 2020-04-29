package be.ac.ulb.infof307.g09.controller.shape;

import java.io.Serializable;

/**
 * Pair of coordinates, with their respective units and options.
 * only work with default units for now (cm).
 * https://en.wikibooks.org/wiki/LaTeX/PGF/TikZ#Specifying_Coordinates
 */
public class Coordinates implements Serializable {
    private double x = 0;
    private double y = 0;

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Copy constructor
     * @param other
     */
    public Coordinates(Coordinates other){
        this.x = other.x;
        this.y = other.y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String print() {
        return "(" + this.x + "," + this.y + ") ";
    }

    /**
     * Computes the vector addition of this Coordinates and the provided one
     * @param other the Coordinates to add from this one
     * @return the addition, this object remains unchanged
     */
    public Coordinates add(Coordinates other) {
        return new Coordinates(this.x + other.x, this.y + other.y);
    }

    /**
     * Computes the vector substraction of this Coordinates and the provided one
     * @param other the Coordinates to subtract
     * @return the result of the other vector subtracted from this one, this object remains unchanged
     */
    public Coordinates sub(Coordinates other){
        return new Coordinates(this.x - other.x, this.y - other.y);
    }
}
