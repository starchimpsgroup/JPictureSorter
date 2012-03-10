package de.ernst.software.image;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cernst
 * Date: 14.02.12
 * Time: 10:55
 */
public class ImagePool {
    private static final Logger logger = Logger.getLogger(ImagePool.class);

    private final List<Image> images = new ArrayList<>();

    public ImagePool() {
    }

    public ImagePool(final String directoryPath, final boolean recursive) {
        addFolder(directoryPath, recursive);
    }

    public void addImage(final String imagePath) {
    }

    public void deleteImage(final int imageHash) {
    }

    public void deleteImage(final Image image) {
    }

    public void addFolder(final String directoryPath, final boolean recursive) {
        File dir = new File(directoryPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (final File file : files) {
                if (file.isFile()) {
                    final Image image = new Image(file.getPath());
                    if (image.isLoaded()) {
                        images.add(image);
                        if (logger.isDebugEnabled()) {
                            String type = (file.isDirectory() ? "dir " : "file");
                            logger.debug("read " + type + ": " + file);
                        }
                    }
                } else if (file.isDirectory() && recursive) {

                }
            }
        } else {
            logger.error("'" + directoryPath + "' does not exists or is no directory!");
        }
    }

    public void deleteFolder(final String directoryPath) {
    }

    public int getImageCount() {
        return images.size();
    }

    private class ColorComparator implements Comparator<Image> {
        @Override
        public int compare(final Image a, final Image b) {
            if (a.getAverageColor().getRed() > b.getAverageColor().getRed()) {
                return 1;
            } else if (a.getAverageColor().getRed() < b.getAverageColor().getRed()) {
                return -1;
            }

            if (a.getAverageColor().getGreen() > b.getAverageColor().getGreen()) {
                return 1;
            } else if (a.getAverageColor().getGreen() < b.getAverageColor().getGreen()) {
                return -1;
            }

            if (a.getAverageColor().getBlue() > b.getAverageColor().getBlue()) {
                return 1;
            } else if (a.getAverageColor().getBlue() < b.getAverageColor().getBlue()) {
                return -1;
            }

            return 0;
        }
    }

    public List<Image> getColorSortedImages() {
        final ArrayList<Image> list = new ArrayList<>(images);
        Collections.sort(list, new ColorComparator());
        return list;
    }
}
