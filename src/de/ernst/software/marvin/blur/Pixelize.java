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

package de.ernst.software.marvin.blur;

import java.awt.Color;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Pixelize
 * @author Gabriel Ambrosio Archanjo
 * @version 1.0 02/28/2008
 */
public class Pixelize extends MarvinAbstractImagePlugin
{
	MarvinAttributes 	attributes;
	private boolean[][]	arrMask;
	
	public void load(){
		attributes = getAttributes();
		attributes.set("squareSide", 10);
	}

	public void show(){
		MarvinFilterWindow filterWindow = new MarvinFilterWindow("Pixelize", 400,350, getImagePanel(), this);
		filterWindow.addLabel("lblSquareSide", "Square Side");
		filterWindow.addTextField("txtSquareSide", "squareSide", attributes);
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
		int l_rgb;
		
		int squareSide = (Integer)attributes.get("squareSide");
		arrMask = mask.getMaskArray();
			
		for (int x = 0; x < imageIn.getWidth(); x+=squareSide) {
			for (int y = 0; y < imageIn.getHeight(); y+=squareSide) {				
				l_rgb = getPredominantRGB(imageIn, x,y,squareSide);
				fillRect(imageOut, x,y,squareSide, l_rgb);					
			}
		}
	}
	
	private int getPredominantRGB(MarvinImage a_image, int a_x, int a_y, int a_squareSide){
		int l_red=-1;
		int l_green=-1;
		int l_blue=-1;
		
		for(int x=a_x; x<a_x+a_squareSide; x++){
			for(int y=a_y; y<a_y+a_squareSide; y++){
				if(x < a_image.getWidth() && y < a_image.getHeight()){
					if(arrMask != null && !arrMask[x][y]){
						continue;
					}
					
					if(l_red == -1)		l_red = a_image.getIntComponent0(x,y);
					else				l_red = (l_red+a_image.getIntComponent0(x,y))/2;
					if(l_green == -1)	l_green = a_image.getIntComponent1(x,y);
					else				l_green = (l_green+a_image.getIntComponent1(x,y))/2;
					if(l_blue == -1)	l_blue = a_image.getIntComponent2(x,y);
					else				l_blue = (l_blue+a_image.getIntComponent2(x,y))/2;	
				}				
			} 
		}
		return (255<<24)+(l_red<<16)+(l_green<<8)+l_blue;
	}
	
	private void fillRect(MarvinImage a_image, int a_x, int a_y, int a_squareSide, int a_rgb){
		for(int x=a_x; x<a_x+a_squareSide; x++){
			for(int y=a_y; y<a_y+a_squareSide; y++){
				if(x < a_image.getWidth() && y < a_image.getHeight()){
					if(arrMask != null && !arrMask[x][y]){
						continue;
					}
					
					a_image.setIntColor(x,y,a_rgb);
				}
			}
		}					
	}
}

