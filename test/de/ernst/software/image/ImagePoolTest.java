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
    private static final ImagePool imagePool = new ImagePool(Global.imagePath, true);

    @Test
    public void testAddImage() throws Exception {

    }

    @Test
    public void testDeleteImage() throws Exception {

    }

    @Test
    public void testAddFolder() throws Exception {
//        assertEquals(imagePool.getImageCount(), 68); // 68
    }

    @Test
    public void testDeleteFolder() throws Exception {

    }

    @Test
    public void testGetColorSortedImages() throws Exception {
    imagePool.addFolder("C:\\Users\\cernst\\Pictures",true);
    imagePool.addFolder("D:\\Workspace\\Wals",true);
        final List<Image> colorSortedImages = imagePool.getColorSortedImages();
        System.out.println(colorSortedImages);
        for (int i = 0; i < colorSortedImages.size(); i++) {
            final MarvinImage smallImage = colorSortedImages.get(i).getSmallImage();
            smallImage.fillRect(0, 0, smallImage.getWidth(), smallImage.getHeight(), colorSortedImages.get(i).getAverageColor());
            smallImage.update();
            MarvinImageIO.saveImage(smallImage, Global.testPath +
                    String.valueOf(i) + "." + colorSortedImages.get(i).getFormat());
            System.out.println(String.valueOf(i) + " " + colorSortedImages.get(i));
        }
    }
}
