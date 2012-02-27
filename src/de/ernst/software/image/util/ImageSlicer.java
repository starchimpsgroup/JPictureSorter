package de.ernst.software.image.util;

import marvin.image.MarvinImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cernst
 * Date: 26.02.12
 * Time: 18:31
 */
public class ImageSlicer {
    public static List<MarvinImage> slice(final MarvinImage image, int rows, int cols) {
        if (rows <= 0)
            rows = 1;

        if (cols <= 0)
            cols = 1;

        if (image == null)
            return null;

        final List<MarvinImage> images = new ArrayList<>(rows * cols);
        System.out.println("Size: " + String.valueOf(rows * cols));
        final int rowPartSize = image.getHeight() / rows;
        final int colPartSize = image.getWidth() / cols;
        System.out.println("Size: " + String.valueOf(rowPartSize) + " " + String.valueOf(colPartSize));

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                final int rowPartCount = y / rowPartSize + 1;
                final int colPartCount = x / colPartSize + 1;
                final int partCount = cols * (rowPartCount - 1) + colPartCount;
                if (images.size() < partCount) {
                    System.out.println("Count: " + String.valueOf(rowPartCount) + " " + String.valueOf(colPartCount));
                    System.out.println("Part: " + String.valueOf(partCount));
                    images.add(new MarvinImage(colPartSize, rowPartSize));
                }
                images.get(partCount - 1).setIntColor(x % colPartSize, y % rowPartSize, image.getIntColor(x, y));
            }
        }
        for (MarvinImage img : images) {
            img.update();
        }
        return images;
    }
}
