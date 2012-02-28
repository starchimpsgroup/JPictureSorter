package de.ernst.software.image.util;

import marvin.image.MarvinImage;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cernst
 * Date: 26.02.12
 * Time: 18:31
 */
public class ImageSlicer {
    private static final Logger logger = Logger.getLogger(ImageSlicer.class);

    private static int getPart(final int partCount, final int maxParts) {
        if (partCount >= maxParts) {
            return maxParts;
        }
        return partCount + 1;
    }

    public static List<MarvinImage> slice(final MarvinImage image, int rows, int cols) {
        if (rows <= 0)
            rows = 1;

        if (cols <= 0)
            cols = 1;

        if (image == null)
            return null;

        final int rowPartSize = image.getHeight() / rows;
        final int colPartSize = image.getWidth() / cols;

        if (rowPartSize == 0 || colPartSize == 0) {
            logger.error("Can not split the image, the image is maybe to small.");
            final List<MarvinImage> images = new ArrayList<>(1);
            images.add(image);
            return images;
        }

        final List<MarvinImage> images = new ArrayList<>(rows * cols);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                final int rowPartCount = getPart(y / rowPartSize, rows);
                final int colPartCount = getPart(x / colPartSize, cols);
                final int partCount = cols * (rowPartCount - 1) + colPartCount;

                if (images.size() < partCount) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Count: " + String.valueOf(rowPartCount) + " " + String.valueOf(colPartCount));
                        logger.debug("Part: " + String.valueOf(partCount));
                    }
                    int addCol = 0, addRow = 0;
                    if (rowPartCount == rows)
                        addRow = image.getHeight() % rowPartSize;
                    if (colPartCount == cols)
                        addCol = image.getWidth() % colPartSize;
                    images.add(new MarvinImage(colPartSize + addCol, rowPartSize + addRow));
                }
                images.get(partCount - 1).setIntColor(x - (colPartCount - 1) * colPartSize,
                        y - (rowPartCount - 1) * rowPartSize, image.getIntColor(x, y));
            }
        }
        for (MarvinImage img : images) {
            img.update();
        }
        return images;
    }
}
