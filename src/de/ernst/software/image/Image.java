package de.ernst.software.image;

import de.ernst.software.image.color.AverageColor;
import de.ernst.software.image.color.BaseColor;
import de.ernst.software.image.util.ImageSlicer;
import de.ernst.software.thread.AutoThread;
import de.ernst.software.thread.Thread;
import de.ernst.software.thread.ThreadPool;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: cernst
 * Date: 14.02.12
 * Time: 10:55
 */
public class Image extends AutoThread {
    private static final Logger logger = Logger.getLogger(Image.class);

    private static final String INIT = "init";

    private static final Map<String, String> formats = new HashMap<>();

    static {
        formats.put("JPEG", "jpg");
    }

    private final boolean loaded;
    private final String path;
    private final String name;

    private Color averageColor = null;
    //    private float[] hsb = null;
    private BaseColor[] baseColor = null;
    private List<Color> averageColorList = null;
    private MarvinImage smallImage = null;
    private String format = null;

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

//    private float[] getHSB(final Color color) {
//        return Color.RGBtoHSB(color.getRed(), color.getRed(), color.getBlue(), null);
//    }

    @Thread(INIT)
    private void init(final MarvinImage image) {
        averageColor = AverageColor.circleLineScan(image);
//        hsb = getHSB(averageColor);
        baseColor = BaseColor.getBaseColorRange(averageColor);
        averageColorList = new ArrayList<>(9);
        final List<MarvinImage> images = ImageSlicer.slice(image, 3, 3);
        for (final MarvinImage img : images) {
            averageColorList.add(AverageColor.circleLineScan(img));
        }
        smallImage = resizeImage(image, 200);
        format = getFormat(image);
        if (logger.isDebugEnabled()) {
            logger.debug(averageColor);
//            logger.debug("H: " + String.valueOf(hsb[0]) + " S: " + String.valueOf(hsb[1]) + " B: " + String.valueOf(hsb[2]));
            for (BaseColor color : baseColor)
                logger.debug(color);
            logger.debug(averageColorList);
            logger.debug(path);
            logger.debug(name);
            logger.debug(format);
        }
    }

    public Image(final String imagePath) {
        super(ThreadPool.getInstance());
        final MarvinImage image = loadImage(imagePath);
        loaded = image != null;
        if (loaded) {
            path = imagePath;
            name = getName(imagePath);
            run(INIT, image);
        } else {
            path = null;
            name = null;
        }
    }

    public Color getAverageColor() {
        waitFor(INIT);
        return averageColor;
    }

//    public float[] getHSB() {
//        waitFor(INIT);
//        return hsb;
//    }

//    public float getHue() {
//        waitFor(INIT);
//        return hsb[0];
//    }
//
//    public float getSaturation() {
//        waitFor(INIT);
//        return hsb[1];
//    }
//
//    public float getBrightness() {
//        waitFor(INIT);
//        return hsb[2];
//    }

    public List<Color> getAverageColorList() {
        waitFor(INIT);
        return averageColorList;
    }

    public String getPath() {
        return path;
    }

    public MarvinImage getSmallImage() {
        waitFor(INIT);
        return smallImage;
    }

    public String getFormat() {
        waitFor(INIT);
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
