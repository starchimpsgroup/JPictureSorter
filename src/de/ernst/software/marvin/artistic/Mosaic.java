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

package de.ernst.software.marvin.artistic;


import java.awt.Color;
import java.awt.Graphics;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Mosaic implementation
 * @author Gabriel Ambr�sio Archanjo
 * @version 1.0 03/01/2008
 */
public class Mosaic extends MarvinAbstractImagePlugin
{
	private final static String SQUARES = "squares";
	private final static String TRIANGLES = "triangles";

	private int width;
	private String shape;
	private boolean border;

	private MarvinAttributes attributes;
	private MarvinPerformanceMeter performanceMeter;

	public void load()
	{
		attributes = getAttributes();
		attributes.set("width", 6);
		attributes.set("shape", SQUARES);
		attributes.set("border", true);
		performanceMeter = new MarvinPerformanceMeter();
	}

	public void show(){
		MarvinFilterWindow filterWindow = new MarvinFilterWindow("Halftone - Circles", 420,350, getImagePanel(), this);
		filterWindow.addLabel("lblWidth", "Tile witdh:");
		filterWindow.addTextField("txtwidth", "width", attributes);
		filterWindow.newComponentRow();
		filterWindow.addLabel("lblWidth", "Format:");
		filterWindow.addComboBox("combShape", "shape", new Object[]{SQUARES, TRIANGLES}, attributes);
		filterWindow.newComponentRow();
		filterWindow.addLabel("lblWidth", "Edge:");
		filterWindow.addComboBox("combBorder", "border", new Object[]{true, false}, attributes);
		filterWindow.setVisible(true);		
	}

	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut,
		MarvinAttributes attributesOut,
		MarvinImageMask mask, 
		boolean a_previewMode
	)
	{		
		width = (Integer)attributes.get("width");
		shape = (String)attributes.get("shape");
		border = (Boolean)attributes.get("border");

		Graphics l_graphics = imageOut.getBufferedImage().getGraphics();

		performanceMeter.enableProgressBar("Mosaic", imageIn.getHeight()*width);

		if(shape.equals(SQUARES)){
			squaresMosaic(width, border, l_graphics, imageIn);
		}
		else if(shape.equals(TRIANGLES)){
			trianglesMosaic(width, border, l_graphics, imageIn);
		}

		imageOut.updateColorArray();
		performanceMeter.finish();
	}

	private void squaresMosaic(int width, boolean border, Graphics graphics, MarvinImage image){
		Color l_color;

		for (int y = 0; y < image.getHeight(); y+=width) {
			for (int x = 0; x < image.getWidth(); x+=width) {			
				l_color = getSquareColor(x,y,image);
				graphics.setColor(l_color);
				graphics.fillRect((int)(x), (int)(y), (int)((width)), (int)((width)));

				if(border){
					graphics.setColor(Color.black);
					graphics.drawRect((int)(x), (int)(y), (int)((width)), (int)((width)));
				}
			}
			performanceMeter.stepsFinished(image.getWidth());
		}
	}

	private void trianglesMosaic(int width, boolean border, Graphics graphics, MarvinImage image){
		Color l_colorT1;
		Color l_colorT2;
		int t=-1;
		boolean l_aux=true;

		if
		(
			((image.getWidth()/width)%2 == 0 && image.getWidth()%width==0) ||
			((image.getWidth()/width)%2 == 1 && image.getWidth()%width!=0)
		)
		{
			l_aux=false;
		}
		
		for (int y = 0; y < image.getHeight(); y+=width) {
			for (int x = 0; x < image.getWidth(); x+=width) {
				if(t ==-1)
				{
					l_colorT1 = getTriangleColor(x,y,0, image);
					l_colorT2 = getTriangleColor(x,y,1, image);

					graphics.setColor(l_colorT1);
					graphics.fillPolygon(new int[]{x,x+width,x}, new int[]{y,y,y+width},3);
					if(border){
						graphics.setColor(Color.black);
						graphics.drawPolygon(new int[]{x,x+width,x}, new int[]{y,y,y+width},3);
					}


					graphics.setColor(l_colorT2);
					graphics.fillPolygon(new int[]{x+width,x+width,x}, new int[]{y,y+width,y+width},3);
					if(border){
						graphics.setColor(Color.black);
						graphics.drawPolygon(new int[]{x+width,x+width,x}, new int[]{y,y+width,y+width},3);
					}
				}
				else{
					l_colorT1 = getTriangleColor(x,y,2, image);
					l_colorT2 = getTriangleColor(x,y,3, image);


					graphics.setColor(l_colorT1);
					graphics.fillPolygon(new int[]{x,x+width,x+width}, new int[]{y,y,y+width},3);
					if(border){
						graphics.setColor(Color.black);
						graphics.drawPolygon(new int[]{x,x+width,x+width}, new int[]{y,y,y+width},3);
					}	



					graphics.setColor(l_colorT2);
					graphics.fillPolygon(new int[]{x, x+width,x}, new int[]{y,y+width,y+width},3);
					if(border){
						graphics.setColor(Color.black);
						graphics.drawPolygon(new int[]{x, x+width,x}, new int[]{y,y+width,y+width},3);
					}
				}
				performanceMeter.stepsFinished(image.getWidth());	
				t*=-1;
			}
			if(l_aux){
				t*=-1;
			}
		}
	}

	private Color getSquareColor(int aX, int aY, MarvinImage image){
		int l_red=-1;
		int l_green=-1;
		int l_blue=-1;

		for(int y=0; y<width; y++){
			for(int x=0; x<width; x++)
			{
				if(aX+x > 0 && aX+x < image.getWidth() &&  aY+y> 0 && aY+y < image.getHeight()){
					if(l_red == -1){
						l_red = image.getIntComponent0(aX+x,aY+y);
						l_green = image.getIntComponent1(aX+x,aY+y);
						l_blue = image.getIntComponent2(aX+x,aY+y);
					}
					else{
						l_red = (l_red+image.getIntComponent0(aX+x,aY+y))/2;
						l_green = (l_green+image.getIntComponent1(aX+x,aY+y))/2;
						l_blue = (l_blue+image.getIntComponent2(aX+x,aY+y))/2;
					}
				}
			}
		}
		return new Color(l_red,l_green,l_blue);
	}

	private Color getTriangleColor(int aX, int aY, int tringlePos, MarvinImage image){
		int l_red=-1;
		int l_green=-1;
		int l_blue=-1;

		int l_xInitial=0;
		int l_xOffSet=0;
		int l_xOffSetInc=0;
		int l_xInitalInc=0;

		switch(tringlePos){
			case 0:
				l_xInitial=1;
				l_xOffSet = width;
				l_xOffSetInc=-1;
				l_xInitalInc = 0;
				break;
			case 1:
				l_xInitial = width-1;
				l_xOffSet = width;
				l_xOffSetInc=0;
				l_xInitalInc = -1;
				break;
			case 2:
				l_xInitial=1;
				l_xOffSet = width;
				l_xOffSetInc=0;
				l_xInitalInc = 1;
				break;
			case 3:
				l_xInitial = 1;
				l_xOffSet = 1;
				l_xOffSetInc=1;
				l_xInitalInc = 0;
				break;
			
		}
		
		int x = l_xInitial;
		int y = 0;

		for(int w=0; w< width-1; w++){ 
			while(x < l_xOffSet){
				if(aX+x > 0 && aX+x < image.getWidth() &&  aY+y> 0 && aY+y < image.getHeight()){
					if(l_red == -1){
						l_red = image.getIntComponent0(aX+x,aY+y);
						l_green = image.getIntComponent1(aX+x,aY+y);
						l_blue = image.getIntComponent2(aX+x,aY+y);
					}
					else{
						l_red = (l_red+image.getIntComponent0(aX+x,aY+y))/2;
						l_green = (l_green+image.getIntComponent1(aX+x,aY+y))/2;
						l_blue = (l_blue+image.getIntComponent2(aX+x,aY+y))/2;
					}
				}
				x++;
			}
			l_xInitial+=l_xInitalInc;
			l_xOffSet+=l_xOffSetInc;
			x = l_xInitial;
			y++;
			
		}
		if(l_red == -1) l_red = 0;
		if(l_green == -1) l_green = 0;
		if(l_blue == -1) l_blue = 0;
		return new Color(l_red,l_green,l_blue);
	}
}