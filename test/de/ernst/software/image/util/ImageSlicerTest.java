package de.ernst.software.image.util;

import de.ernst.software.image.Global;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import org.junit.Test;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cernst
 * Date: 26.02.12
 * Time: 22:09
 */
public class ImageSlicerTest {
    public MarvinImage image = MarvinImageIO.loadImage(Global.imagePath + "1283837294038.jpg");
    public MarvinImage image901x601 = MarvinImageIO.loadImage(Global.imagePath + "901x601.jpg");
    public MarvinImage image1x1 = MarvinImageIO.loadImage(Global.imagePath + "1x1.jpg");

    @Test
    public void testSlice() throws Exception {
//        saveImages(ImageSlicer.slice(image, 3, 2));
//        saveImages(ImageSlicer.slice(image901x601, 2, 2));
//        saveImages(ImageSlicer.slice(image901x601, 4, 2));
//        saveImages(ImageSlicer.slice(image1x1, 2, 2));
        saveImages(ImageSlicer.slice(image, 3, 3));
//        saveImages(ImageSlicer.slice(image, 32, 20));
    }

    public void saveImages(List<MarvinImage> images) {
        if (images == null)
            return;

        int i = 0;
        for (MarvinImage image : images) {
            MarvinImageIO.saveImage(image, Global.testPath +
                    String.valueOf(/*image.hashCode()*/i) + "." + image.getFormatName());
            i++;
        }
    }
}
