/**
Marvin Project <2007-2009>

Initial version by:

Danilo Rosetto Munoz
Fabio Andrijauskas
Gabriel Ambrosio Archanjo

site: http://marvinproject.sourceforge.net

GPL
Copyright (C) <2007>  

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/

package de.ernst.software.marvin.edgeDetection;

import java.awt.Color;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Edge detection plug-in. Reference: {@link http://www.cs.princeton.edu/introcs/31datatype/EdgeDetector.java.html}
 * @authors Danilo Rosetto Mu�oz, Ivan Francisco Coutinho Costa
 * @version 1.0 02/28/2008
 */

public class EdgeDetector extends MarvinAbstractImagePlugin {

    private MarvinPerformanceMeter performanceMeter;
    private Color grayMatrix[] = new Color[256];

    public void load() {
        performanceMeter = new MarvinPerformanceMeter();
    }

    public void show() {
        MarvinFilterWindow l_filterWindow = new MarvinFilterWindow(
                "EdgeDetector", 400, 350, getImagePanel(),
                this);
        l_filterWindow.setVisible(true);
    }

    public void process
	(
		MarvinImage a_imageIn, 
		MarvinImage a_imageOut,
		MarvinAttributes a_attributesOut,
		MarvinImageMask a_mask, 
		boolean a_previewMode
	)
    {
    	// Image size
        int width = a_imageIn.getWidth();
        int height = a_imageIn.getHeight();
        boolean[][] mask = a_mask.getMaskArray();
        //System.out.println("time:"+(System.currentTimeMillis()-time));

        performanceMeter.start("EdgeDetector");
        performanceMeter.enableProgressBar("EdgeDetector",
                ((height - 2) * (width - 2)));
        performanceMeter.startEvent("Sobel filter");

        //MarvinImage img2 = new MarvinImage(width, height);
       
        // Init gray matrix
        for (int i = 0; i <= 255; i++) {
            grayMatrix[i] = new Color(i, i, i);
        }
       
        int [][] luminance = new int[width][height];
        
        for (int y = 0; y < height ; y++) {
            for (int x = 0; x < width ; x++) {
            	
            	if(mask != null && !mask[x][y]){
            		continue;
            	}
            	
            	luminance[x][y] = (int) luminance(a_imageIn.getIntComponent0(x, y),
	                		a_imageIn.getIntComponent1(x, y), a_imageIn.getIntComponent2(x, y));
            	
            }
        }
        
        //System.out.println("time:"+(System.currentTimeMillis()-time));
       
        int grayX, grayY;
        int magnitude;
         //for (int y = 1; y < height - 1; y++) {
            //for (int x = 1; x < width - 1; x++) {
        for (int y = 1; y < height-1; y++) {
        	for (int x = 1; x < width-1; x++) {

            	if(mask != null && !mask[x][y]){
            		continue;
            	}
            	
            	grayX = - luminance[x-1][y-1] + luminance[x-1][y-1+2]
                        - 2* luminance[x-1+1][y-1] + 2* luminance[x-1+1][y-1+2]
                        - luminance[x-1+2][y-1]+ luminance[x-1+2][y-1+2];
                grayY = luminance[x-1][y-1] + 2* luminance[x-1][y-1+1] + luminance[x-1][y-1+2]
                        - luminance[x-1+2][y-1] - 2* luminance[x-1+2][y-1+1] - luminance[x-1+2][y-1+2];
                
                // Magnitudes sum
                magnitude = 255 - truncate(Math.abs(grayX)
                        + Math.abs(grayY));
              
                Color grayscaleColor = grayMatrix[magnitude];

                // Apply the color into a new image
                a_imageOut.setIntColor(x, y, grayscaleColor.getRGB());
            }
            performanceMeter.incProgressBar(width);
        }
         
        performanceMeter.finishEvent();
        performanceMeter.finish();
    }

    /**
     * Sets the RGB between 0 and 255
     *
     * @param a
     * @return
     */
    private int truncate(int a) {
        if (a < 0)
            return 0;
        else if (a > 255)
            return 255;
        else
            return a;
    }

    /**
     * Apply the luminance
     *
     * @param r
     * @param g
     * @param b
     * @return
     */
    private int luminance(int r, int g, int b) {
        // Y = 0.299 r + 0.587g + 0.114b
        return (int) ((0.299 * r) + (0.58 * g) + (0.11 * b));
    }
}