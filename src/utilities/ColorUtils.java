package utilities;

import java.awt.*;
import java.util.ArrayList;

/**
 * This class is to be removed during the next refactor
 * Java Code to get a color name from rgb/hex value/awt color
 * This is basically a slightly modified copy paste of stack overflow https://stackoverflow.com/questions/4126029/convert-rgb-values-to-color-name
 */
//TODO remove this class during the next refactor
public class ColorUtils {

    static final ArrayList<ColorUtils.ColorName> colorList = new ArrayList<>();

    // Initialize the color list that we have.
    static {
        colorList.add(new ColorUtils.ColorName(controller.shape.Color.RED, 0xFF, 0x00, 0x00));
        colorList.add(new ColorUtils.ColorName(controller.shape.Color.GREEN, 0x00, 0x80, 0x00));
        colorList.add(new ColorUtils.ColorName(controller.shape.Color.BLUE, 0x00, 0x00, 0xFF));
        colorList.add(new ColorUtils.ColorName(controller.shape.Color.CYAN, 0x00, 0xFF, 0xFF));
        colorList.add(new ColorUtils.ColorName(controller.shape.Color.MAGENTA, 0xFF, 0x00, 0xFF));
        colorList.add(new ColorUtils.ColorName(controller.shape.Color.YELLOW, 0xFF, 0xFF, 0x00));
        colorList.add(new ColorUtils.ColorName(controller.shape.Color.BLACK, 0x00, 0x00, 0x00));
        colorList.add(new ColorUtils.ColorName(controller.shape.Color.GRAY, 0x80, 0x80, 0x80));
        colorList.add(new ColorUtils.ColorName(controller.shape.Color.DARKGRAY, 0xA9, 0xA9, 0xA9));
        colorList.add(new ColorUtils.ColorName(controller.shape.Color.LIGHTGRAY, 0xD3, 0xD3, 0xD3));
        colorList.add(new ColorUtils.ColorName(controller.shape.Color.BROWN, 0xA5, 0x2A, 0x2A));
        colorList.add(new ColorUtils.ColorName(controller.shape.Color.LIME, 0x00, 0xFF, 0x00));
        colorList.add(new ColorUtils.ColorName(controller.shape.Color.OLIVE, 0x80, 0x80, 0x00));
        colorList.add(new ColorUtils.ColorName(controller.shape.Color.ORANGE, 0xFF, 0xA5, 0x00));
        colorList.add(new ColorUtils.ColorName(controller.shape.Color.PINK, 0xFF, 0xC0, 0xCB));
        colorList.add(new ColorUtils.ColorName(controller.shape.Color.PURPLE, 0x80, 0x00, 0x80));
        colorList.add(new ColorUtils.ColorName(controller.shape.Color.TEAL, 0x00, 0x80, 0x80));
        colorList.add(new ColorUtils.ColorName(controller.shape.Color.VIOLET, 0xEE, 0x82, 0xEE));
        colorList.add(new ColorUtils.ColorName(controller.shape.Color.WHITE, 0xFF, 0xFF, 0xFF));
    }

    /**
     * Get the closest color name from our list
     *
     * @param red   red component
     * @param green green component
     * @param blue  blue component
     * @return the closest color
     */
    public static controller.shape.Color getColorNameFromRgb(double red, double green, double blue) {
        int r = (int) Math.floor(red * 255 + 0.5d);
        int g = (int) Math.floor(green * 255 + 0.5d);
        int b = (int) Math.floor(blue * 255 + 0.5d);

        ColorName closestMatch = null;
        int minMSE = Integer.MAX_VALUE;
        int mse;
        for (ColorName c : colorList) {
            mse = c.computeMSE(r, g, b);
            if (mse < minMSE) {
                minMSE = mse;
                closestMatch = c;
            }
        }
        return closestMatch.getName();
    }

    /**
     * Convert hexColor to rgb, then call getColorNameFromRgb(r, g, b)
     *
     * @param hexColor
     * @return
     */
    public controller.shape.Color getColorNameFromHex(int hexColor) {
        int r = (hexColor & 0xFF0000) >> 16;
        int g = (hexColor & 0xFF00) >> 8;
        int b = (hexColor & 0xFF);
        return getColorNameFromRgb(r, g, b);
    }

    public int colorToHex(Color c) {
        return Integer.decode("0x" + Integer.toHexString(c.getRGB()).substring(2));
    }

    public controller.shape.Color getColorNameFromColor(Color color) {
        return getColorNameFromRgb(color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * SubClass of ColorUtils. In order to lookup color name
     *
     * @author Xiaoxiao Li
     */
    public static class ColorName {
        public int r, g, b;
        public final controller.shape.Color name;

        public ColorName(controller.shape.Color name, int r, int g, int b) {
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

        public controller.shape.Color getName() {
            return name;
        }
    }
}
