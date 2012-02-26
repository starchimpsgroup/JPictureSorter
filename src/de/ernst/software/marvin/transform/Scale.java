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

package de.ernst.software.marvin.transform;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Simple and fast scale based on nearest neighbors
 * @author Gabriel Ambr�sio Archanjo
 */
public class Scale extends MarvinAbstractImagePlugin{
	
	private int 				width;
	private int 				height;
	private int 				newWidth;
	private int 				newHeight;
	
	private MarvinAttributes 	attributes;
	
	public void load(){
		attributes = getAttributes();
		newWidth = 0;
		newHeight = 0;
		attributes.set("newWidth", newWidth);
		attributes.set("newHeight", newHeight);
	}
	
	public void show(){
		MarvinFilterWindow l_filterWindow = new MarvinFilterWindow("Scale - Nearest Neighbor", 270,100, getImagePanel(), this);
		l_filterWindow.disablePreview();
		l_filterWindow.addLabel("lblWidth", "Width:");
		l_filterWindow.addTextField("txtWidth", "newWidth", attributes);
		l_filterWindow.newComponentRow();
		l_filterWindow.addLabel("lblHeight", "Height:");
		l_filterWindow.addTextField("txtHeight", "newHeight", attributes);

		l_filterWindow.setVisible(true);
	}
		
	public void process
	(
		MarvinImage a_imageIn, 
		MarvinImage a_imageOut,
		MarvinAttributes a_attributesOut,
		MarvinImageMask a_mask, 
		boolean a_previewMode
	){
		
		width = a_imageIn.getWidth();
		height = a_imageIn.getHeight();
		newWidth = (Integer)attributes.get("newWidth");
		newHeight = (Integer)attributes.get("newHeight");
		
		a_imageOut.setDimension(newWidth, newHeight);
		
	    int x_ratio = (int)((width<<16)/newWidth) ;
	    int y_ratio = (int)((height<<16)/newHeight) ;
	    int x2, y2 ;
	    for (int i=0;i<newHeight;i++) {
	        for (int j=0;j<newWidth;j++) {
	            x2 = ((j*x_ratio)>>16) ;
	            y2 = ((i*y_ratio)>>16) ;
	            a_imageOut.setIntColor(j,i, a_imageIn.getIntColor(x2,y2));
	        }                
	    }	    
	}
}