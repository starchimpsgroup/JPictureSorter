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

package de.ernst.software.marvin.imageCombination;

import java.awt.Color;

import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Combine two images using a mask color.
 * @author Gabriel Ambrosio Archanjo
 */
public class CombineByMask extends MarvinAbstractImagePlugin{
	
	MarvinAttributes 	attributes;
	
	MarvinImage 		combinationImage;
	
	Color 				colorMask;
	
	private int 		xi=0,
						yi=0;
	
	public void load(){
		attributes = getAttributes();
		attributes.set("xi", xi);
		attributes.set("yi", yi);
	}
	
	public void show(){
		
	}
	
	public void process(MarvinImage imageIn, MarvinImage imageOut, MarvinAttributes attributesOut, MarvinImageMask mask, boolean previewMode){
		xi = (Integer)attributes.get("xi");
		yi = (Integer)attributes.get("yi");		
		colorMask = (Color)attributes.get("colorMask");
		combinationImage = (MarvinImage)attributes.get("combinationImage");
		
		int l_xCI,
			l_yCI;
		
		int l_widthCI = combinationImage.getWidth(),
			l_heightCI = combinationImage.getHeight();
			
			
		for(int y=0; y<imageIn.getHeight(); y++){
    		for(int x=0; x<imageIn.getWidth(); x++){
    			
    			l_xCI = x-xi;
    			l_yCI = y-yi;
    			
    			if(l_xCI >= 0 && l_xCI < l_widthCI && l_yCI >= 0 && l_yCI < l_heightCI){
    				if(imageIn.getIntColor(x, y) == colorMask.getRGB()){
    					imageOut.setIntColor(x, y, combinationImage.getIntColor(x, y));
    				}
    				else{
    					imageOut.setIntColor(x, y, imageIn.getIntColor(x, y));
    				}
    			}
    			else{
    				imageOut.setIntColor(x, y, imageIn.getIntColor(x, y));
    			}
    		}
		}
	}
}
