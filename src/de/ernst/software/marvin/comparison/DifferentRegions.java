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

import java.util.Vector;

import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

import org.w3c.dom.css.Rect;

/**
 * Find the different regions between two images.
 * @author Gabriel Ambrosio Archanjo
 */
public class DifferentRegions extends MarvinAbstractImagePlugin{
	
	private MarvinAttributes 	attributes;
	private MarvinImage 		comparisonImage; 
	
	private int[][] 			arrPixelsPerSubRegion;
	private boolean[][] 		arrRegionMask;
	
	private int 				width;
	private int 				height;
	private int					colorRange;
	
	private int 				subRegionSide=10;	
	private boolean 			initialized = false;
	
	public void load(){
		attributes = getAttributes();
		attributes.set("colorRange", 30);
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
		width = a_imageIn.getWidth();
		height = a_imageIn.getHeight();
		
		if(!initialized){
			arrPixelsPerSubRegion = new int[width/subRegionSide][height/subRegionSide];
			arrRegionMask = new boolean[width/subRegionSide][height/subRegionSide];
			initialized = true;
		}
			
		clearRegions();
		
		for(int y=0; y<a_imageIn.getHeight(); y++){
			for(int x=0; x<a_imageIn.getWidth(); x++){
				
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
					arrPixelsPerSubRegion[x/subRegionSide][y/subRegionSide]++;
				}
			}
		}		
		
		Vector<int[]> l_vecRegions = new Vector<int[]>();
		int[] l_rect;
	
		while(true){
			l_rect = new int[4];
			l_rect[0] = -1;
			
			JoinRegions(l_rect);
			if(l_rect[0] != -1){
				l_vecRegions.add(l_rect);
			}
			else{
				break;
			}
		}			
		a_attributesOut.set("regions", l_vecRegions);		
	}
	
	private boolean JoinRegions(int[] a_rect){
		for(int x=0; x<width/subRegionSide; x++){
			for(int y=0; y<height/subRegionSide; y++){
				if(arrPixelsPerSubRegion[x][y] > (subRegionSide*subRegionSide/2) && !arrRegionMask[x][y]){
					arrRegionMask[x][y] = true;
					a_rect[0] = x*subRegionSide;
					a_rect[1] = y*subRegionSide;
					a_rect[2] = x*subRegionSide;
					a_rect[3] = y*subRegionSide;
					
					testNeighbors(a_rect, x,y);
					return true;
				}
			}
		}
		return false;
	}
	
	private void testNeighbors(int[] a_rect, int a_x, int a_y){
		for(int x=a_x-5; x<a_x+5; x++){
			for(int y=a_y-5; y<a_y+5; y++){
				if
				(
					(x > 0 && x<width/subRegionSide) &&
					(y > 0 && y<height/subRegionSide)
				)
				{
					if(arrPixelsPerSubRegion[x][y] > (subRegionSide*subRegionSide/2) && !arrRegionMask[x][y]){						
						if(x*subRegionSide < a_rect[0]){
							a_rect[0] = x*subRegionSide;
						}
						if(x*subRegionSide > a_rect[2]){
							a_rect[2] = x*subRegionSide;
						}
						
						if(y*subRegionSide < a_rect[1]){
							a_rect[1] = y*subRegionSide;
						}
						if(y*subRegionSide > a_rect[3]){
							a_rect[3] = y*subRegionSide;
						}
						
						arrRegionMask[x][y] = true;
						testNeighbors(a_rect, x,y);
					}
				}
			}				
		}
	}
	
	private void clearRegions(){
		for(int x=0; x<width/subRegionSide; x++){
			for(int y=0; y<height/subRegionSide; y++){
				arrPixelsPerSubRegion[x][y]=0;
				arrRegionMask[x][y]=false;
			}
		}
	}
}
