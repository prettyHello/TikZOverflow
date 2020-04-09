package controller.shape;

/**
 * All different shape thickness in Tikz.
 */
//TODO right value
public enum Thickness {
    ULTRA_THIN(0.1),
    VERY_THIN(0.2),
    THIN(0.4),
    SEMI_THICK(0.6),
    THICK(0.8),
    VERY_THICK(1.2),
    ULTRA_THICK(1.6);
    public final double value;

    Thickness(double value) {
        this.value = value;
    }
    public double thicknessValue() { return this.value; }
}
