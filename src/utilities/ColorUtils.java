package utilities;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Java Code to get a color name from rgb/hex value/awt color
 *
 * The part of looking up a color name from the rgb values is edited from
 * https://gist.github.com/nightlark/6482130#file-gistfile1-java (that has some errors) by Ryan Mast (nightlark)
 *
 * @author Xiaoxiao Li
 * https://stackoverflow.com/questions/4126029/convert-rgb-values-to-color-name
 *
 */
public class ColorUtils {

    static ArrayList<ColorUtils.ColorName> colorList = new ArrayList<ColorUtils.ColorName>();

    /**
     * Initialize the color list that we have.
     */
    static  {
        colorList.add(new ColorUtils.ColorName(business.shape.Color.RED, 0xFF, 0x00, 0x00));
        colorList.add(new ColorUtils.ColorName(business.shape.Color.GREEN, 0x00, 0x80, 0x00));
        colorList.add(new ColorUtils.ColorName(business.shape.Color.BLUE, 0x00, 0x00, 0xFF));
        colorList.add(new ColorUtils.ColorName(business.shape.Color.CYAN, 0x00, 0xFF, 0xFF));
        colorList.add(new ColorUtils.ColorName(business.shape.Color.MAGENTA, 0xFF, 0x00, 0xFF));
        colorList.add(new ColorUtils.ColorName(business.shape.Color.YELLOW, 0xFF, 0xFF, 0x00));
        colorList.add(new ColorUtils.ColorName(business.shape.Color.BLACK, 0x00, 0x00, 0x00));
        colorList.add(new ColorUtils.ColorName(business.shape.Color.GRAY, 0x80, 0x80, 0x80));
        colorList.add(new ColorUtils.ColorName(business.shape.Color.DARKGRAY, 0xA9, 0xA9, 0xA9));
        colorList.add(new ColorUtils.ColorName(business.shape.Color.LIGHTGRAY, 0xD3, 0xD3, 0xD3));
        colorList.add(new ColorUtils.ColorName(business.shape.Color.BROWN, 0xA5, 0x2A, 0x2A));
        colorList.add(new ColorUtils.ColorName(business.shape.Color.LIME, 0x00, 0xFF, 0x00));
        colorList.add(new ColorUtils.ColorName(business.shape.Color.OLIVE, 0x80, 0x80, 0x00));
        colorList.add(new ColorUtils.ColorName(business.shape.Color.ORANGE, 0xFF, 0xA5, 0x00));
        colorList.add(new ColorUtils.ColorName(business.shape.Color.PINK, 0xFF, 0xC0, 0xCB));
        colorList.add(new ColorUtils.ColorName(business.shape.Color.PURPLE, 0x80, 0x00, 0x80));
        colorList.add(new ColorUtils.ColorName(business.shape.Color.TEAL, 0x00, 0x80, 0x80));
        colorList.add(new ColorUtils.ColorName(business.shape.Color.VIOLET, 0xEE, 0x82, 0xEE));
        colorList.add(new ColorUtils.ColorName(business.shape.Color.WHITE, 0xFF, 0xFF, 0xFF));
    }

    /**
     * Get the closest color name from our list
     *
     * @param r
     * @param g
     * @param b
     * @return
     */
    public static business.shape.Color getColorNameFromRgb(double r, double g, double b) {
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
    public business.shape.Color getColorNameFromHex(int hexColor) {
        int r = (hexColor & 0xFF0000) >> 16;
        int g = (hexColor & 0xFF00) >> 8;
        int b = (hexColor & 0xFF);
        return getColorNameFromRgb(r, g, b);
    }

    public int colorToHex(Color c) {
        return Integer.decode("0x"
                + Integer.toHexString(c.getRGB()).substring(2));
    }

    public business.shape.Color getColorNameFromColor(Color color) {
        return getColorNameFromRgb(color.getRed(), color.getGreen(),
                color.getBlue());
    }

    /**
     * SubClass of ColorUtils. In order to lookup color name
     *
     * @author Xiaoxiao Li
     */
    public static class ColorName {
        public int r, g, b;
        public business.shape.Color name;

        public ColorName(business.shape.Color name, int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.name = name;
        }

        public int computeMSE(double pixR, double pixG, double pixB) {
            return (int) (((pixR - r) * (pixR - r) + (pixG - g) * (pixG - g) + (pixB - b)
                    * (pixB - b)) / 3);
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

        public business.shape.Color getName() {
            return name;
        }
    }
}

