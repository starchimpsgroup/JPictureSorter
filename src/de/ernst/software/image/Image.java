package de.ernst.software.image;

import de.ernst.software.image.util.AverageColor;
import de.ernst.software.image.util.ImageSlicer;
import de.ernst.software.thread.AutoThread;
import de.ernst.software.thread.Thread;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by IntelliJ IDEA.
 * User: cernst
 * Date: 14.02.12
 * Time: 10:55
 */
public class Image extends AutoThread {
    private static final Logger logger = Logger.getLogger(Image.class);

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(8);
    private static final Map<String, String> formats = new HashMap<>();

    static {
        formats.put("JPEG", "jpg");
    }

    private final boolean loaded;
    private final String path;
    private final String name;

    private Color averageColor;
    private List<Color> averageColorList;
    private MarvinImage smallImage;
    private String format;

    private MarvinImage loadImage(final String imagePath) {
        MarvinImage image = null;
        try {
            image = MarvinImageIO.loadImage(imagePath);
        } catch (Exception e) {
            logger.error("'" + imagePath + "' is not a valid image format");
        }
        return image;
    }

    private int getSize(final int sizeA, final int sizeB, final int maxSize) {
        final float percent = maxSize / (float) sizeA;
        if (logger.isDebugEnabled()) {
            logger.debug("percent: " + String.valueOf(percent));
            logger.debug("size: " + String.valueOf(sizeB * percent));
        }
        final int size = (int) (sizeB * percent);
        return (size != 0 ? size : 1);
    }

    private String getName(final String path) {
        final String[] split = path.split("\\\\");
        return split[split.length - 1].split("\\.")[0];
    }

    private String getFormat(final MarvinImage image) {
        final String format = image.getFormatName();
        return (formats.containsKey(format) ? formats.get(format) : format);
    }

    private MarvinImage resizeImage(final MarvinImage image, final int maxSize) {
        int width, height;
        if (image.getWidth() > image.getHeight()) {
            width = maxSize;
            height = getSize(image.getWidth(), image.getHeight(), maxSize);
        } else {
            width = getSize(image.getHeight(), image.getWidth(), maxSize);
            height = maxSize;
        }
        final MarvinImage smallImage = new MarvinImage(image.getBufferedImage());
        smallImage.resize(width, height);
        return smallImage;
    }

    public boolean isLoaded() {
        return loaded;
    }

    @Thread("init")
    private void init(final MarvinImage image) {
        averageColor = AverageColor.circleLineScan(image);
        averageColorList = new ArrayList<>(9);
        final List<MarvinImage> images = ImageSlicer.slice(image, 3, 3);
        for (final MarvinImage img : images) {
            averageColorList.add(AverageColor.circleLineScan(img));
        }
        smallImage = resizeImage(image, 200);
        format = getFormat(image);
        if (logger.isDebugEnabled()) {
            logger.debug(averageColor);
            logger.debug(averageColorList);
            logger.debug(path);
            logger.debug(name);
            logger.debug(format);
        }
    }

    public Image(final String imagePath) {
        super(threadPool);
        final MarvinImage image = loadImage(imagePath);
        loaded = image != null;
        if (loaded) {
            path = imagePath;
            name = getName(imagePath);
            run("init", image);
        } else {
            path = null;
            name = null;

            averageColor = null;
            averageColorList = null;
            smallImage = null;
            format = null;
        }
    }

    public Color getAverageColor() {
        waitFor("init");
        return averageColor;
    }

    public List<Color> getAverageColorList() {
        waitFor("init");
        return averageColorList;
    }

    public String getPath() {
        return path;
    }

    public MarvinImage getSmallImage() {
        waitFor("init");
        return smallImage;
    }

    public String getFormat() {
        waitFor("init");
        return format;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Image{" +
                "averageColor=" + averageColor +
                '}';
    }
}
