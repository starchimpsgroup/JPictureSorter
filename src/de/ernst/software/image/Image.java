package de.ernst.software.image;

import de.ernst.software.image.util.AverageColor;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import org.apache.log4j.Logger;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: cernst
 * Date: 14.02.12
 * Time: 10:55
 */
public class Image {
    private static final Logger logger = Logger.getLogger(Image.class);
    private final boolean loaded;
    private final Color averageColor;
    private final Color[] averageColorVector;

    private MarvinImage loadImage(final String imagePath) {
        MarvinImage image = null;
        try {
            image = MarvinImageIO.loadImage(imagePath);
        } catch (Exception e) {
            logger.error("'" + imagePath + "' is not a valid image format");
        }
        return image;
    }

    private Color calculateAverageColor(final MarvinImage image) {
        float averageRed = 0;
        float averageGreen = 0;
        float averageBlue = 0;
        final int[] colorArray = image.getIntColorArray();
        for (int color : colorArray) {
            final Color c = new Color(color);
            averageRed = (averageRed + c.getRed());
            averageGreen = (averageGreen + c.getGreen());
            averageBlue = (averageBlue + c.getBlue());
        }
        return new Color((int) averageRed / colorArray.length, (int) averageGreen / colorArray.length, (int) averageBlue / colorArray.length);
    }

    public boolean isLoaded() {
        return loaded;
    }

    public Image(final String imagePath) {
        final MarvinImage image = loadImage(imagePath);
        loaded = image != null;
        if (loaded) {
            averageColor = AverageColor.circleLineScan(image);
            averageColorVector = new Color[9];
            if (logger.isDebugEnabled()) {
                logger.debug(averageColor);
            }
        } else {
            averageColor = null;
            averageColorVector = null;
        }
    }

    public Color getAverageColor() {
        return averageColor;
    }
}
