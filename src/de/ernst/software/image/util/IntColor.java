package de.ernst.software.image.util;

/**
 * Created by IntelliJ IDEA.
 * User: cernst
 * Date: 26.02.12
 * Time: 20:21
 */
public class IntColor {
    public static int getRed(int color) {
        return color >>> 16 & 0xFF;
    }

    public static int getGreen(int color) {
        return color >>> 8 & 0xFF;
    }

    public static int getBlue(int color) {
        return color & 0xFF;
    }
}
