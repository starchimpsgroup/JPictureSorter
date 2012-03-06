package de.ernst.software.image;

import de.ernst.software.image.util.AverageColor;
import de.ernst.software.image.util.ImageSlicer;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


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
    private final List<Color> averageColorList;

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
            averageColorList = new ArrayList<>(9);
            final List<MarvinImage> images = ImageSlicer.slice(image, 3, 3);
            for (final MarvinImage img : images) {
                averageColorList.add(AverageColor.circleLineScan(img));
            }
            if (logger.isDebugEnabled()) {
                logger.debug(averageColor);
                logger.debug(averageColorList);
            }
        } else {
            averageColor = null;
            averageColorList = null;
        }
    }

    public Color getAverageColor() {
        return averageColor;
    }

    public List<Color> getAverageColorList() {
        return averageColorList;
    }
}
