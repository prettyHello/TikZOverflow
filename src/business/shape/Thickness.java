package business.shape;
//TODO right value
public enum Thickness {
    ULTRA_THIN(0.1),
    VERY_THIN(0.1),
    THIN(0.1),
    SEMI_THICK(0.1),
    THICK(0.1),
    VERY_THICK(0.1),
    ULTRA_THICK(0.1);
    public final double value;
    private Thickness(double value){
        this.value = value;
    }
}
