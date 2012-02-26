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
import marvin.util.MarvinAttributes;

/**
 * Invert the pixels color.
 * @author Gabriel Ambrosio Archanjo
 * @version 1.0 02/28/2008
 */
public class Invert extends MarvinAbstractImagePlugin
{
	public void load(){}

	public void show(){
		MarvinFilterWindow filterWindow = new MarvinFilterWindow("Negative", 400,350, getImagePanel(), this);
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
		boolean[][] l_arrMask = mask.getMaskArray();
		
		int r, g, b;
		for (int x = 0; x < imageIn.getWidth(); x++) {
			for (int y = 0; y < imageIn.getHeight(); y++) {
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				r = (255-(int)imageIn.getIntComponent0(x, y));
				g = (255-(int)imageIn.getIntComponent1(x, y));
				b = (255-(int)imageIn.getIntComponent2(x, y));

				imageOut.setIntColor(x,y,r,g,b);
			}
		}
	}
}