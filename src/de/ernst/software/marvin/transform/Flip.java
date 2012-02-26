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
 * Flip image horintally and vertically.
 * @author Gabriel Ambrosio Archanjo
 * @version 02/28/2008
 */
public class Flip extends MarvinAbstractImagePlugin
{
	private final static String HORIZONTAL = "horizontal";
	private final static String VERTICAL = "vertical";

	private MarvinAttributes 	attributes;
	private boolean[][]			arrMask;
	

	public void load(){
		attributes = getAttributes();
		attributes.set("flip", "horizontal");
	}

	public void show(){
		MarvinFilterWindow l_filterWindow = new MarvinFilterWindow("Flip", 400,350, getImagePanel(), this);
		l_filterWindow.addLabel("labelTipo", "Type:");
		l_filterWindow.addComboBox("combpFlip", "flip", new Object[]{HORIZONTAL, VERTICAL}, attributes);
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
		String l_operation = (String)attributes.get("flip");
		arrMask = a_mask.getMaskArray();
		
		if(l_operation.equals(HORIZONTAL)){
			flipHorizontal(a_imageIn, a_imageOut);
		}
		else{
			flipVertical(a_imageIn, a_imageOut);
		}
	}

	private void flipHorizontal(MarvinImage a_imageIn, MarvinImage a_imageOut){
		int r,g,b;
		for (int y = 0; y < a_imageIn.getHeight(); y++) {
			for (int x = 0; x < (a_imageIn.getWidth()/2)+1; x++) {	
				if(arrMask != null && !arrMask[x][y]){
					continue;
				}
				//Get Y points and change the positions 
				r = a_imageIn.getIntComponent0(x, y);
				g = a_imageIn.getIntComponent1(x, y);
				b = a_imageIn.getIntComponent2(x, y);

				a_imageOut.setIntColor(x,y,
						a_imageIn.getIntComponent0((a_imageIn.getWidth()-1)-x, y),
						a_imageIn.getIntComponent1((a_imageIn.getWidth()-1)-x, y),
						a_imageIn.getIntComponent2((a_imageIn.getWidth()-1)-x, y)
				);

				a_imageOut.setIntColor((a_imageIn.getWidth() - 1) - x, y, r, g, b);
			}
		}
	}

	private void flipVertical(MarvinImage a_imageIn, MarvinImage a_imageOut){
		int r,g,b;
		for (int x = 0; x < a_imageIn.getWidth(); x++) {
			for (int y = 0; y < (a_imageIn.getHeight()/2)+1; y++) {
				if(arrMask != null && arrMask[x][y]){
					continue;
				}
			
				//Get X points and change the positions 
				r = a_imageIn.getIntComponent0(x, y);
				g = a_imageIn.getIntComponent1(x, y);
				b = a_imageIn.getIntComponent2(x, y);

				a_imageOut.setIntColor(x,y,
						a_imageIn.getIntComponent0(x, (a_imageIn.getHeight()-1)-y),
						a_imageIn.getIntComponent1(x, (a_imageIn.getHeight()-1)-y),
						a_imageIn.getIntComponent2(x, (a_imageIn.getHeight()-1)-y)
				);

				a_imageOut.setIntColor(x, (a_imageIn.getHeight() - 1) - y, r, g, b);
			}
		}
	}
}