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

package de.ernst.software.marvin.color;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;


/**
 * Brightness and Constrast manipulation plug-in.
 * @author Gabriel Ambr�sio Archanjo
 * @version 1.0 02/28/2008
 */
public class BrightnessAndContrast extends MarvinAbstractImagePlugin
{
	private MarvinAttributes attributes;

	public void load(){		
		attributes = getAttributes();
		attributes.set("brightness", 0);
		attributes.set("contrast", 0);
	}

	public void show(){
		MarvinFilterWindow filterWindow = new MarvinFilterWindow("Brightness and Contrast", 400,400, getImagePanel(), this);
		filterWindow.addLabel("lblBrightness", "Brightness");
		filterWindow.addHorizontalSlider("sliderBrightness", "brightness", -127, 127, 0, attributes);
		filterWindow.newComponentRow();
		filterWindow.addLabel("lblContrast", "Contrast");
		filterWindow.addHorizontalSlider("sliderContrast", "contrast", -127, 127, 0, attributes);
		filterWindow.setVisible(true);
	}

	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut,
		MarvinAttributes attributesOut,
		MarvinImageMask mask, 
		boolean previewMode
	)
	{
		double r,g,b;
		double l_brightness = (Integer)attributes.get("brightness");
		double l_contrast = (Integer)attributes.get("contrast");
		l_contrast = Math.pow((127 + l_contrast)/127, 2);

		// Brightness
		for (int x = 0; x < imageIn.getWidth(); x++) {
			for (int y = 0; y < imageIn.getHeight(); y++) {
				r = imageIn.getIntComponent0(x, y);
				g = imageIn.getIntComponent1(x, y);
				b = imageIn.getIntComponent2(x, y);

				r+= (1-(r/255))*l_brightness;
				g+= (1-(g/255))*l_brightness;
				b+= (1-(b/255))*l_brightness;
				if(r < 0) r=0;
				if(r > 255) r=255;
				if(g < 0) g=0;
				if(g > 255) g=255;
				if(b < 0) b=0;
				if(b > 255) b=255;

				imageOut.setIntColor(x,y,(int)r,(int)g,(int)b);
			}
		}

		// Contrast
		for (int x = 0; x < imageIn.getWidth(); x++) {
			for (int y = 0; y < imageIn.getHeight(); y++) {
				r = imageOut.getIntComponent0(x, y);
				g = imageOut.getIntComponent1(x, y);
				b = imageOut.getIntComponent2(x, y);

				
				r /= 255.0;
				r -= 0.5;
				r *= l_contrast;
				r += 0.5;
				r *= 255.0;

				g /= 255.0;
				g -= 0.5;
				g *= l_contrast;
				g += 0.5;
				g *= 255.0;

				b /= 255.0;
				b -= 0.5;
				b *= l_contrast;
				b += 0.5;
				b *= 255.0;
				

				if(r < 0) r=0;
				if(r > 255) r=255;
				if(g < 0) g=0;
				if(g > 255) g=255;
				if(b < 0) b=0;
				if(b > 255) b=255;

				imageOut.setIntColor(x,y,(int)r,(int)g,(int)b);
			}
		}
	}
}