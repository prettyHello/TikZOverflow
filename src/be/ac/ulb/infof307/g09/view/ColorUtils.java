package be.ac.ulb.infof307.g09.view;

import be.ac.ulb.infof307.g09.controller.DTO.shapes.ColorDTO;

import java.util.ArrayList;

/**
 * This class allow to transform a rgb value into a tikz supported color
 * Java Code to get a color name from rgb/hex value/awt color
 * This code was heavily inspired by the following solution: stack overflow https://stackoverflow.com/questions/4126029/convert-rgb-values-to-color-name
 */
public class ColorUtils {

    static final ArrayList<ColorUtils.ColorName> COLOR_LIST = new ArrayList<>();

    // Initialize the color list that we have.

    static {
        COLOR_LIST.add(new ColorUtils.ColorName(ColorDTO.RED, 0xFF, 0x00, 0x00));
        COLOR_LIST.add(new ColorUtils.ColorName(ColorDTO.GREEN, 0x00, 0x80, 0x00));
        COLOR_LIST.add(new ColorUtils.ColorName(ColorDTO.BLUE, 0x00, 0x00, 0xFF));
        COLOR_LIST.add(new ColorUtils.ColorName(ColorDTO.CYAN, 0x00, 0xFF, 0xFF));
        COLOR_LIST.add(new ColorUtils.ColorName(ColorDTO.MAGENTA, 0xFF, 0x00, 0xFF));
        COLOR_LIST.add(new ColorUtils.ColorName(ColorDTO.YELLOW, 0xFF, 0xFF, 0x00));
        COLOR_LIST.add(new ColorUtils.ColorName(ColorDTO.BLACK, 0x00, 0x00, 0x00));
        COLOR_LIST.add(new ColorUtils.ColorName(ColorDTO.GRAY, 0x80, 0x80, 0x80));
        COLOR_LIST.add(new ColorUtils.ColorName(ColorDTO.DARKGRAY, 0xA9, 0xA9, 0xA9));
        COLOR_LIST.add(new ColorUtils.ColorName(ColorDTO.LIGHTGRAY, 0xD3, 0xD3, 0xD3));
        COLOR_LIST.add(new ColorUtils.ColorName(ColorDTO.BROWN, 0xA5, 0x2A, 0x2A));
        COLOR_LIST.add(new ColorUtils.ColorName(ColorDTO.LIME, 0x00, 0xFF, 0x00));
        COLOR_LIST.add(new ColorUtils.ColorName(ColorDTO.OLIVE, 0x80, 0x80, 0x00));
        COLOR_LIST.add(new ColorUtils.ColorName(ColorDTO.ORANGE, 0xFF, 0xA5, 0x00));
        COLOR_LIST.add(new ColorUtils.ColorName(ColorDTO.PINK, 0xFF, 0xC0, 0xCB));
        COLOR_LIST.add(new ColorUtils.ColorName(ColorDTO.PURPLE, 0x80, 0x00, 0x80));
        COLOR_LIST.add(new ColorUtils.ColorName(ColorDTO.TEAL, 0x00, 0x80, 0x80));
        COLOR_LIST.add(new ColorUtils.ColorName(ColorDTO.VIOLET, 0xEE, 0x82, 0xEE));
        COLOR_LIST.add(new ColorUtils.ColorName(ColorDTO.WHITE, 0xFF, 0xFF, 0xFF));
    }

    /**
     * Get the closest color name from our list of tikz color
     *
     * @param red   red component
     * @param green green component
     * @param blue  blue component
     * @return the closest color
     */
    public static ColorDTO getColorNameFromRgb(double red, double green, double blue) {
        int r = (int) Math.floor(red * 255 + 0.5d);
        int g = (int) Math.floor(green * 255 + 0.5d);
        int b = (int) Math.floor(blue * 255 + 0.5d);

        ColorName closestMatch = null;
        int minMSE = Integer.MAX_VALUE;
        int mse;
        for (ColorName c : COLOR_LIST) {
            mse = c.computeMSE(r, g, b);
            if (mse < minMSE) {
                minMSE = mse;
                closestMatch = c;
            }
        }
        return closestMatch.getName();
    }

    /**
     * This is a subClass of ColorUtils. It is used to store the different color properties in a list.
     *
     * @author Xiaoxiao Li
     */
    public static class ColorName {
        private final int r, g, b;
        private final ColorDTO name;

        public ColorName(ColorDTO name, int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.name = name;
        }

        public int computeMSE(double pixR, double pixG, double pixB) {
            return (int) (((pixR - r) * (pixR - r) + (pixG - g) * (pixG - g) + (pixB - b) * (pixB - b)) / 3);
        }

        public int getR() {
            return r;
        }

        public int getG() {
            return g;
        }

        public int getB() {
            return b;
        }

        public ColorDTO getName() {
            return name;
        }
    }
}
