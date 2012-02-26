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

package de.ernst.software.marvin.histogram;

import java.awt.Color;

import marvin.gui.MarvinPluginWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.statistic.MarvinHistogram;
import marvin.statistic.MarvinHistogramEntry;
import marvin.util.MarvinAttributes;

/**
 * Color histogram is a representation of the RGB colors distribution.
 * @author Gabriel Ambr�sio Archanjo
 * @version 1.0 02/13/2008
 */
public class ColorHistogram extends MarvinAbstractImagePlugin
{
    public void load(){}

    public void show(){
      process(getImagePanel().getImage(), null, null, MarvinImageMask.NULL_MASK, false);
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
        MarvinPluginWindow l_pluginWindow = new MarvinPluginWindow("Color Histogram", 440,580);

        MarvinHistogram l_histoRed = new MarvinHistogram("Red Intensity");
        l_histoRed.setBarWidth(1);

        MarvinHistogram l_histoGreen = new MarvinHistogram("Green Intensity");
        l_histoGreen.setBarWidth(1);

        MarvinHistogram l_histoBlue = new MarvinHistogram("Blue Intensity");
        l_histoBlue.setBarWidth(1);

        int l_arrRed[] = new int[256];
        int l_arrGreen[] = new int[256];
        int l_arrBlue[] = new int[256];

        for (int x = 0; x < a_imageIn.getWidth(); x++) {
            for (int y = 0; y < a_imageIn.getHeight(); y++) {
                l_arrRed[a_imageIn.getIntComponent0(x, y)]++;
                l_arrGreen[a_imageIn.getIntComponent1(x, y)]++;
                l_arrBlue[a_imageIn.getIntComponent2(x, y)]++;
            }
        }

        for(int x=0; x<256; x++){
            l_histoRed.addEntry(new MarvinHistogramEntry(x, l_arrRed[x], new Color(x, 0, 0)));
            l_histoGreen.addEntry(new MarvinHistogramEntry(x, l_arrGreen[x], new Color(0, x, 0)));
            l_histoBlue.addEntry(new MarvinHistogramEntry(x, l_arrBlue[x], new Color(0, 0, x)));
        }

        l_pluginWindow.addImage("histoRed", l_histoRed.getImage(400,200));
        l_pluginWindow.newComponentRow();
        l_pluginWindow.addImage("histoGreen", l_histoGreen.getImage(400,200));
        l_pluginWindow.newComponentRow();
        l_pluginWindow.addImage("histoBlue", l_histoBlue.getImage(400,200));
        l_pluginWindow.setVisible(true);
    }
}