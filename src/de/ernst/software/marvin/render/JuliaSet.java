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
 * @author Gabriel Ambr�sio Archanjo
 */
public class JuliaSet extends MarvinAbstractImagePlugin{

	private final static String MODEL_0 = "Model 0";
	private final static String MODEL_1 = "Model 1";
	
	private MarvinAttributes 	attributes;
	private int					colorModel;
	int width;
	int height;
	
	public void load() {
		attributes = getAttributes();
		attributes.set("cReal", -0.4);
		attributes.set("cImag", 0.6);
		attributes.set("zoom", 1.0);
		attributes.set("xCenter", 0.0);
		attributes.set("yCenter", 0.0);
		attributes.set("iterations", 500);
		attributes.set("colorModel", MODEL_0);
	}

	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut, 
		MarvinAttributes out2,
		MarvinImageMask a_mask, 
		boolean previewMode
	)
	{
		width = imageOut.getWidth();
		height = imageOut.getHeight();
		
		double cRe 		= (Double)attributes.get("cReal");
		double cIm 		= (Double)attributes.get("cImag");
		double zoom 	= (Double)attributes.get("zoom");
		double xc   	= (Double)attributes.get("xCenter");
		double yc   	= (Double)attributes.get("yCenter");
		int iterations 	= (Integer)attributes.get("iterations");
		
		if(((String)attributes.get("colorModel")).equals(MODEL_0)){
			colorModel = 0;
		} else{
			colorModel = 1;
		}
		
		
		double factor = 5.0/zoom;
		int iter;

		double x0,y0;
		double nx;
		double ny;
		double nx1;
		double ny1;
		
		boolean[][] mask = a_mask.getMaskArray();
		
		for (int i=0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				
				if(mask != null && !mask[j][i]){	continue;	}
				
				x0 = xc - factor/2 + factor*j/width;
				y0 = yc - factor/2 + factor*i/height;

				nx=x0;
				ny=y0;

				iter=0;
				while(Math.sqrt(nx*nx + ny*ny) < 2.0 && iter < iterations){
					nx1 = nx*nx-ny*ny+cRe;
					ny1 = 2*(nx*ny)+cIm;

					nx = nx1;
					ny = ny1;						 
					iter++;
				}
				imageOut.setIntColor(j,height-1-i, getColor(iter, iterations, colorModel));
			}
		}
	}
	
	private int getColor(int iter, int max, int colorModel){
		if(colorModel == 0){
			return getColor0(iter, max);
		}
		return getColor1(iter, max);
	}
	
	 private int getColor1(int iter, int max){
		int red, green, blue;
		
		red = (int)(iter*(255.0/100));
		green = (int)(iter*(255.0/200));
		blue = (int)(iter*(255.0/300));
	 
		return blue + (green << 8) + (red << 16);
	 }
	 
	 private int getColor0(int iter, int max) {
			int red = (int) ((Math.cos(iter / 10.0f) + 1.0f) * 127.0f);
			int green = (int) ((Math.cos(iter / 20.0f) + 1.0f) * 127.0f);
	        int blue = (int) ((Math.cos(iter / 300.0f) + 1.0f) * 127.0f);
	        return blue + (green << 8) + (red << 16);
		}

	 MarvinFilterWindow filterWindow;
	 
	public void show() {
		filterWindow = new MarvinFilterWindow("Mandelbrot", 450,550, getImagePanel(), this);
		
		filterWindow.addLabel("lblCReal", "c Real:");
		filterWindow.addTextField("txtCReal", "cReal", attributes);
		filterWindow.newComponentRow();
		
		filterWindow.addLabel("lblCImaginary", "c Imaginary:");
		filterWindow.addTextField("txtCImaginary", "cImag", attributes);
		filterWindow.newComponentRow();
		
		filterWindow.addLabel("lblXCenter", "X Center:");
		filterWindow.addTextField("txtXCenter", "xCenter", attributes);
		filterWindow.newComponentRow();
		
		filterWindow.addLabel("lblYCenter", "Y Center:");
		filterWindow.addTextField("txtYCenter", "yCenter", attributes);
		filterWindow.newComponentRow();
		
		filterWindow.addLabel("lblZoom", "Zoom:");
		filterWindow.addTextField("txtZoom", "zoom", attributes);
		filterWindow.newComponentRow();
		
		filterWindow.addLabel("lblIterations", "Iterations:");
		filterWindow.addTextField("txtIterations", "iterations", attributes);
		filterWindow.newComponentRow();
		
		filterWindow.addLabel("lblColorModel", "Color Model:");
		filterWindow.addComboBox("combColorModel", "colorModel", new Object[]{MODEL_0, MODEL_1}, attributes);
		
		filterWindow.setVisible(true);
	}

}
