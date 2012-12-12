package de.ernst.software.image.color;

import marvin.image.MarvinImage;
import org.apache.log4j.Logger;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: cernst
 * Date: 20.02.12
 * Time: 13:04
 */
public class AverageColor {
    private static final Logger logger = Logger.getLogger(AverageColor.class);

    private static boolean isBiggerThanMaxInt(final int count, final int nextValue, final String name) {
        if (Integer.MAX_VALUE - count - nextValue < 0) {
            logger.error("The count of " + name + " is bigger than Integer.MAX!");
            return true;
        }
        return false;
    }

    /**
     * Used as reference.
     *
     * @param image The Picture.
     * @return The average color.
     */
    public static Color allPixel(final MarvinImage image) {
        int averageRed = 0;
        int averageGreen = 0;
        int averageBlue = 0;
        final int[] colorArray = image.getIntColorArray();
        for (int color : colorArray) {
            final int red = IntToRgbColor.getRed(color);
            final int green = IntToRgbColor.getGreen(color);
            final int blue = IntToRgbColor.getBlue(color);

            if (isBiggerThanMaxInt(averageRed, red, "RED"))
                return null;
            averageRed += red;

            if (isBiggerThanMaxInt(averageGreen, green, "GREEN"))
                return null;
            averageGreen += green;

            if (isBiggerThanMaxInt(averageBlue, blue, "BLUE"))
                return null;
            averageBlue += blue;
        }
        return new Color(averageRed / colorArray.length, averageGreen / colorArray.length, averageBlue / colorArray.length);
    }

    /**
     * In use, fast and acceptable difference.
     *
     * @param image The Picture.
     * @return The average color.
     */
    public static Color circleLineScan(final MarvinImage image) {
        int averageRed = 0;
        int averageGreen = 0;
        int averageBlue = 0;

        final int xM = image.getWidth() / 2;
        final int yM = image.getHeight() / 2;
        final int xR = (int) (xM * 0.9);
        final int yR = (int) (yM * 0.9);
        final int parts = 360;
        final float phiPart = 360f / parts;

        float phi = 0f;
        for (int i = 0; i < parts; i++) {
            final int x = (int) (xM + xR * Math.cos(phi));
            final int y = (int) (yM + yR * Math.sin(phi));

            final int color = image.getIntColor(x, y);
            averageRed += IntToRgbColor.getRed(color);
            averageGreen += IntToRgbColor.getGreen(color);
            averageBlue += IntToRgbColor.getBlue(color);

            phi += phiPart;
        }

//        if (logger.isDebugEnabled()) {
//            MarvinImage outImage = new MarvinImage(image.getBufferedImage());
//
//            phi = 0f;
//            for (int i = 0; i < parts; i++) {
//                final int x = (int) (xM + xR * Math.cos(phi));
//                final int y = (int) (yM + yR * Math.sin(phi));
//                outImage.setIntColor(x, y, 255, 0, 0);
//                phi += phiPart;
//            }
//
//            outImage.fillRect(0, 0, 50, 50, new Color(averageRed / parts, averageGreen / parts, averageBlue / parts));
//            outImage.update();
//            MarvinImageIO.saveImage(outImage, "D:\\Workspace\\JPictureSorter\\data\\test_images\\" +
//                    String.valueOf(image.hashCode()) + "." + image.getFormatName());
//        }
        return new Color(averageRed / parts, averageGreen / parts, averageBlue / parts);
    }

    /**
     * Not in use, to big difference.
     *
     * @param image The Picture.
     * @return The average color.
     */
    public static Color bitShift(final MarvinImage image) {
        int averageRed = 0;
        int averageGreen = 0;
        int averageBlue = 0;
        final int[] colorArray = image.getIntColorArray();
        for (int color : colorArray) {
            averageRed = (averageRed + IntToRgbColor.getRed(color)) >> 1;
            averageGreen = (averageGreen + IntToRgbColor.getGreen(color)) >> 1;
            averageBlue = (averageBlue + IntToRgbColor.getBlue(color)) >> 1;
        }
        return new Color(averageRed, averageGreen, averageBlue);
    }

    /**
     * Not in use, to big difference.
     *
     * @param image The Picture.
     * @return The average color.
     */
    public static Color division(final MarvinImage image) {
        int averageRed = 0;
        int averageGreen = 0;
        int averageBlue = 0;
        final int[] colorArray = image.getIntColorArray();
        for (int color : colorArray) {
            averageRed = (averageRed + IntToRgbColor.getRed(color)) / 2;
            averageGreen = (averageGreen + IntToRgbColor.getGreen(color)) / 2;
            averageBlue = (averageBlue + IntToRgbColor.getBlue(color)) / 2;
        }
        return new Color(averageRed, averageGreen, averageBlue);
    }
}
