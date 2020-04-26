package be.ac.ulb.infof307.g09.controller.shape;

public class Label {
    private String id;
    private final String idPrefix = "labelOfShape";
    private String title;
    private Coordinates coords;
    private Color color = Color.BLACK;

    public Label(int id, String title, Coordinates coords, Color color) {
        this.id = this.idPrefix + id;
        this.title = title;
        this.coords = coords;
        this.color = color;
    }

    public String getId() { return id;}

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Coordinates getCoords() {
        return coords;
    }

    public void setCoords(Coordinates coords) {
        this.coords = coords;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
