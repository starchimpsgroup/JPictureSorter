/**
Marvin Project <2007-2010>

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

package de.ernst.software.marvin.render;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Render white to black degrade. The Marvin framework do not supports 
 * color choosing component yet, but when it's supported, it will be 
 * used in this filter for degrade color selection.
 * 
 * @version 1.0 02/28/2008
 * @author Gabriel Ambr�sio Archanjo
 */

/**
 * @author Gabriel Ambr�sio Archanjo
 */
public class GrayGradient extends MarvinAbstractImagePlugin
{
	
	public void load(){}

	public void show(){
		MarvinFilterWindow l_filterWindow = new MarvinFilterWindow("Gradient", 400,350, getImagePanel(), this);
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
		//Start with white color.
		int r = 255,g = 255,b = 255;
		int aux=0;
		double factor;
		
		if(a_imageIn.getWidth() > 255){
			factor = a_imageIn.getWidth()/255.0;
		}
		else{
			factor = 255/a_imageIn.getWidth();
		}

		for (int x = 0; x < a_imageIn.getWidth(); x++) {
			for (int y = 0; y < a_imageIn.getHeight(); y++) {
				try{
					a_imageOut.setIntColor(x,y,r,g,b);
				}
				catch(Exception e){System.out.println("Error"); e.printStackTrace();}				
			}
		
			if(a_imageIn.getWidth() > 255){
				aux++;
				
				if(aux > factor){
					r--;
					g--;
					b--;
					aux=0;
				}
			}
			else if(255 > a_imageIn.getWidth()){
				aux+=factor;
				r=255-aux;
				g=255-aux;
				b=255-aux;
			}
			
			if(r<0){r=0;}
			if(g<0){g=0;}
			if(b<0){b=0;}
		}
	}
}