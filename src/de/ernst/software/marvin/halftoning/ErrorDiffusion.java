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

package de.ernst.software.marvin.halftoning;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

/**
 * Halftone Error Diffusion implementation.
 * @author Gabriel Ambr�sio Archanjo
 * @version 1.0 02/28/2008

 * @author danilo
 *
 */
public class ErrorDiffusion extends MarvinAbstractImagePlugin
{
	int threshold;
	private MarvinPerformanceMeter performanceMeter;

	public void load(){
		performanceMeter = new MarvinPerformanceMeter();
	}

	public void show()
	{
		MarvinFilterWindow l_filterWindow = new MarvinFilterWindow("Halftone - Error Diffusion ", 400,350, getImagePanel(), this);
		l_filterWindow.setVisible(true);
		threshold = 128;
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
		int color;
		double dif;

		// Gray
		MarvinImagePlugin l_filter = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.grayScale.jar");
		l_filter.process(a_imageIn, a_imageOut, a_attributesOut, a_mask, a_previewMode);
		
		performanceMeter.start("Halftone - Error Diffusion");
		performanceMeter.enableProgressBar("Halftone - Error Diffusion", a_imageOut.getHeight()*a_imageOut.getWidth());
		performanceMeter.startEvent("Error Diffusion");

		boolean[][] l_arrMask = a_mask.getMaskArray();
		
		for (int y = 0; y < a_imageOut.getHeight(); y++) {
			for (int x = 0; x < a_imageOut.getWidth(); x++) {
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				
				color = a_imageOut.getIntComponent0(x, y);
				if(color > threshold){
					a_imageOut.setIntColor(x,y,255,255,255);
					dif = -(255-color);
				}
				else{
					a_imageOut.setIntColor(x,y,0,0,0);
					dif = color;
				}

				// Pixel Right
				if(x+1 < a_imageOut.getWidth()){
					color = a_imageOut.getIntComponent0(x+1,y);
					color+=(int)(0.4375*dif);
					color = getValidGray(color); 
					a_imageOut.setIntColor(x+1,y,color,color,color);

					// Pixel Right Down
					if(y+1 < a_imageOut.getHeight()){
						color = a_imageOut.getIntComponent0(x+1,y+1);
						color+=(int)(0.0625*dif);
						color = getValidGray(color); 
						a_imageOut.setIntColor(x+1,y+1,color,color,color);
					}
				}

				// Pixel Down
				if(y+1 < a_imageOut.getHeight()){
					color = a_imageOut.getIntComponent0(x,y+1);
					color+=(int)(0.3125*dif);
					color = getValidGray(color); 
					a_imageOut.setIntColor(x,y+1,color,color,color);

					// Pixel Down Left
					if(x-1 >= 0){
						color = a_imageOut.getIntComponent0(x-1,y+1);
						color+=(int)(0.1875*dif);
						color = getValidGray(color); 
						a_imageOut.setIntColor(x-1,y+1,color,color,color);
					}
				}
			}
			performanceMeter.stepsFinished(a_imageOut.getWidth());
			performanceMeter.incProgressBar(a_imageOut.getWidth());
		}
		performanceMeter.finishEvent();
		performanceMeter.finish();
	}

	private int getValidGray(int a_value){
		if(a_value < 0) return 0;
		if(a_value > 255) return 255;
		return a_value;
	}
}