package de.ernst.software;

import de.ernst.software.image.ImagePool;

/**
 * Created by IntelliJ IDEA.
 * User: cernst
 * Date: 13.02.12
 * Time: 18:12
 */
public class Main {
    public static void main(String[] args) {
        final ImagePool imagePool = new ImagePool();
        imagePool.addFolder("/data/images", false);
    }
}
