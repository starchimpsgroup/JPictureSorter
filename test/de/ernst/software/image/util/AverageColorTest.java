package de.ernst.software.image.util;

import de.ernst.software.image.Global;
import de.ernst.software.image.color.AverageColor;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import org.junit.Test;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: cernst
 * Date: 21.02.12
 * Time: 14:32
 */
public class AverageColorTest {
    private final static MarvinImage image = MarvinImageIO.loadImage(Global.imagePath + "1283837294038.jpg");

    @Test
    public void testAllPixel() throws Exception {
        long time = System.currentTimeMillis();
        final Color color = AverageColor.allPixel(image);
        System.out.println(String.valueOf(System.currentTimeMillis() - time));
        System.out.println("allPixel       " + color);
    }

    @Test
    public void testCircleLineScan() throws Exception {
        long time = System.currentTimeMillis();
        final Color color = AverageColor.circleLineScan(image);
        System.out.println(String.valueOf(System.currentTimeMillis() - time));
        System.out.println("circleLineScan " + color);
    }

    @Test
    public void testBitShift() throws Exception {
        long time = System.currentTimeMillis();
        final Color color = AverageColor.bitShift(image);
        System.out.println(String.valueOf(System.currentTimeMillis() - time));
        System.out.println("bitShift       " + color);
    }

    @Test
    public void testDivision() throws Exception {
        long time = System.currentTimeMillis();
        final Color color = AverageColor.division(image);
        System.out.println(String.valueOf(System.currentTimeMillis() - time));
        System.out.println("division       " + color);
    }
}
