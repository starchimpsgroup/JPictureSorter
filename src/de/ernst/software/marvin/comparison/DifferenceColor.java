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

package de.ernst.software.marvin.comparison;

import java.awt.Color;
import java.util.Vector;

import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Find the difference between two images considering the pixel�s color.
 * @author Gabriel
 *
 */
public class DifferenceColor extends MarvinAbstractImagePlugin{
	
	private MarvinAttributes 	attributes;
	private MarvinImage 		comparisonImage; 
	
	private int					colorRGB;
	private int					colorRange;
	
	public void load(){
		attributes = getAttributes();
		attributes.set("colorRange", 30);
		attributes.set("differenceColor", Color.green);
	}
	
	public void show(){}
	
	public void process(MarvinImage a_imageIn, MarvinImage a_imageOut, MarvinAttributes a_attributesOut, MarvinImageMask a_mask, boolean a_previewMode){
		int l_redA,
		l_redB,
		l_greenA,
		l_greenB,
		l_blueA,
		l_blueB;
		
		comparisonImage = (MarvinImage)attributes.get("comparisonImage");
		colorRange = (Integer)attributes.get("colorRange");
		colorRGB = ((Color)attributes.get("differenceColor")).getRGB();
		
		boolean[][] l_arrMask = a_mask.getMaskArray();
		
		for(int y=0; y<a_imageIn.getHeight(); y++){
			for(int x=0; x<a_imageIn.getWidth(); x++){
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				
				l_redA = a_imageIn.getIntComponent0(x, y);
				l_greenA = a_imageIn.getIntComponent1(x, y);
				l_blueA = a_imageIn.getIntComponent2(x, y);
				
				l_redB = comparisonImage.getIntComponent0(x, y);
				l_greenB = comparisonImage.getIntComponent1(x, y);
				l_blueB = comparisonImage.getIntComponent2(x, y);
				
				if
				(
					Math.abs(l_redA-l_redB)> colorRange ||
					Math.abs(l_greenA-l_greenB)> colorRange ||
					Math.abs(l_blueA-l_blueB)> colorRange
				)
				{
					a_imageOut.setIntColor(x, y, colorRGB);
				}
				else{
					a_imageOut.setIntColor(x, y, a_imageIn.getIntColor(x,y));
				}
			}
		}		
	}
}
