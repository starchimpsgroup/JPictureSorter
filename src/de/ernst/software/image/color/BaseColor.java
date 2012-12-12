package de.ernst.software.image.color;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: cernst
 * Date: 27.03.12
 * Time: 09:56
 */
public enum BaseColor {
    BLACK(0, 0, 0),
    BLUE(0, 0, 255),
    GREEN(0, 255, 0),
    AQUA(0, 255, 255),
    RED(255, 0, 0),
    FUCHSIA(255, 0, 255),
    YELLOW(255, 255, 0),
    WHITE(255, 255, 255);

    public final int red;
    public final int green;
    public final int blue;

    private BaseColor(final int red, final int green, final int blue) {
        this.red = getAllowedColorRange(red);
        this.green = getAllowedColorRange(green);
        this.blue = getAllowedColorRange(blue);
    }

    private int getAllowedColorRange(final int color) {
        return (color >= 0 && color <= 255 ? color : 0);
    }

    public static boolean isBaseColor(final Color color) {
        return isBaseColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static boolean isBaseColor(final int red, final int green, final int blue) {
        final BaseColor[] colors = BaseColor.values();
        for (final BaseColor color : colors) {
            if (color.red == red && color.green == green && color.blue == blue)
                return true;
        }
        return false;
    }

    public static BaseColor getBaseColor(final Color color) {
        return getBaseColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static BaseColor getBaseColor(final int red, final int green, final int blue) {
        final BaseColor[] colors = BaseColor.values();
        for (final BaseColor color : colors) {
            if (color.red == red && color.green == green && color.blue == blue)
                return color;
        }
        return null;
    }

    public static BaseColor[] getBaseColorRange(final Color color) {
        return getBaseColorRange(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static BaseColor[] getBaseColorRange(final int red, final int green, final int blue) {
        if (isBaseColor(red, green, blue)) {
            BaseColor baseColor[] = new BaseColor[1];
            baseColor[0] = getBaseColor(red, green, blue);
            return baseColor;
        }

        if (red == green && green == blue) {
            final BaseColor baseColor[] = new BaseColor[2];
            baseColor[0] = WHITE;
            baseColor[1] = BLACK;
            return baseColor;
        } else {
            BaseColor firstColor;
            if (red >= green) {
                if (red >= blue) {
                    firstColor = RED;
                } else {
                    firstColor = BLUE;
                }
            } else if (green >= blue) {
                firstColor = GREEN;
            } else {
                firstColor = BLUE;
            }
            return null;
        }

//        final BaseColor baseColor[] = new BaseColor[2];
//        final BaseColor[] colors = BaseColor.values();
//        for (final BaseColor fistColor : colors) {
//            for (final BaseColor secondColor : colors) {
//                if (isInRange(red, fistColor.red, secondColor.red) &&
//                        isInRange(green, fistColor.green, secondColor.green) &&
//                        isInRange(blue, fistColor.blue, secondColor.blue)) {
//                    baseColor[0] = fistColor;
//                    baseColor[1] = secondColor;
//                }
//            }
//        }
    }

    private static boolean isInRange(final int value, final int startRange, final int endRange) {
//        final int maxRange = (startRange > endRange ? startRange : endRange);
//        final int minRange = (startRange < endRange ? startRange : endRange);
//
//        return value <= maxRange && value >= minRange;
        return value <= startRange && value >= endRange;
    }

    @Override
    public String toString() {
        return "BaseColor{" +
                this.name() +
                '}';
    }
}
