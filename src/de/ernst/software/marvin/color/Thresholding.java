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
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

/**
 * Thresholding
 * @author Gabriel Ambrosio Archanjo
 */
public class Thresholding extends MarvinAbstractImagePlugin{

	private MarvinAttributes attributes;
	private int threshold;
	private MarvinImagePlugin pluginGray;
	
	public void load(){
		
		attributes = getAttributes();
		attributes.set("threshold", 125);
		pluginGray = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.grayScale.jar");
	}
	
	public void show(){
		MarvinFilterWindow filterWindow = new MarvinFilterWindow("Thresholding", 400,350, getImagePanel(), this);
		filterWindow.addLabel("lblThreshold", "Threshold");
		filterWindow.addTextField("txtThreshold", "threshold", attributes);		
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
		threshold = (Integer)attributes.get("threshold");
		
		pluginGray.process(imageIn, imageOut, attributesOut, mask, previewMode);
		
		boolean[][] l_arrMask = mask.getMaskArray();
		
		for(int y=0; y<imageIn.getHeight(); y++){
			for(int x=0; x<imageIn.getWidth(); x++){
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				
				if(imageIn.getIntComponent0(x,y) < threshold){
					imageOut.setIntColor(x, y, 0,0,0);
				}
				else{
					imageOut.setIntColor(x, y, 255,255,255);
				}				
			}
		}		
	}
}
