package de.ernst.software.image;

import de.ernst.software.image.util.AverageColor;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;
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
    private final Color[] averageColorArray;

    private MarvinImage loadImage(final String imagePath) {
        MarvinImage image = null;
        try {
            image = MarvinImageIO.loadImage(imagePath);
        } catch (Exception e) {
            logger.error("'" + imagePath + "' is not a valid image format");
        }
        return image;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public Image(final String imagePath) {
        final MarvinImage image = loadImage(imagePath);
        loaded = image != null;
        if (loaded) {
            averageColor = AverageColor.circleLineScan(image);
            averageColorArray = new Color[9];

//            MarvinImagePlugin plugin = new ImageSlicer();
//            plugin.load();
//            MarvinImage slicedImage = new MarvinImage(image.getBufferedImage());
//            plugin.process(image, slicedImage, null, null, false);
            if (logger.isDebugEnabled()) {
                logger.debug(averageColor);
                logger.debug(averageColorArray);
            }
        } else {
            averageColor = null;
            averageColorArray = null;
        }
    }

    public Color getAverageColor() {
        return averageColor;
    }
}
