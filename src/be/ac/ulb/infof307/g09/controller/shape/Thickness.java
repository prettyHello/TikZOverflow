package be.ac.ulb.infof307.g09.controller.shape;

/**
 * All different shape thickness in TikZ.
 */
public enum Thickness {
    ULTRA_THIN(0.4),
    VERY_THIN(0.6),
    THIN(0.8),
    SEMI_THICK(1.2),
    THICK(1.6),
    VERY_THICK(2.4),
    ULTRA_THICK(3.2);
    public final double value;

    Thickness(double value) {
        this.value = value;
    }

    public double thicknessValue() {
        return this.value;
    }
}
