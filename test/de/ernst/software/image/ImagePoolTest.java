package de.ernst.software.image;

import org.junit.Test;

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

    @Test
    public void testAddImage() throws Exception {

    }

    @Test
    public void testDeleteImage() throws Exception {

    }

    @Test
    public void testAddFolder() throws Exception {
        final ImagePool imagePool = createImagePool();
        imagePool.addFolder(/*System.getProperty("user.home")*/ "D:\\Workspace\\JPictureSorter\\data\\images", false);
        assertEquals(imagePool.getImageCount(), 66);
    }

    @Test
    public void testDeleteFolder() throws Exception {

    }
}