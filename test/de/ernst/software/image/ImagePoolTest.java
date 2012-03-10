package de.ernst.software.image;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: cernst
 * Date: 14.02.12
 * Time: 14:31
 */
public class ImagePoolTest {
    public ImagePool createImagePool() {
        return new ImagePool();
    }

    public ImagePool createFilledImagePool() {
        final ImagePool imagePool = createImagePool();
        imagePool.addFolder(Global.imagePath, false);
        return imagePool;
    }

    @Test
    public void testAddImage() throws Exception {

    }

    @Test
    public void testDeleteImage() throws Exception {

    }

    @Test
    public void testAddFolder() throws Exception {
        final ImagePool imagePool = createFilledImagePool();
        assertEquals(imagePool.getImageCount(), 18); // 68
    }

    @Test
    public void testDeleteFolder() throws Exception {

    }

    @Test
    public void testGetColorSortedImages() throws Exception {
        final ImagePool imagePool = createFilledImagePool();
        final List<Image> colorSortedImages = imagePool.getColorSortedImages();
        System.out.println(colorSortedImages);
        for (int i = 0; i < colorSortedImages.size(); i++)
            MarvinImageIO.saveImage(colorSortedImages.get(i).getSmallImage(), Global.testPath +
                    String.valueOf(i) + "." + colorSortedImages.get(i).getFormat());
    }
}
