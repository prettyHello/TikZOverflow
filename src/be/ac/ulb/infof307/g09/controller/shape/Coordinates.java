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
}
