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

package de.ernst.software.marvin.pattern;

import java.io.File;
import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinPlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

import org.marvinproject.image.pattern.correlation.Correlation;
import org.marvinproject.image.pattern.harrisPlessey.HarrisPlessey;

public class PatternRecognition extends MarvinAbstractImagePlugin {

	public void load() {

		process(getImagePanel().getImage(), null, null, MarvinImageMask.NULL_MASK, true);
	}

	@SuppressWarnings("unchecked")
	public void process
	(
		MarvinImage a_imageIn, 
		MarvinImage a_imageOut,
		MarvinAttributes a_attributesOut,
		MarvinImageMask a_mask, 
		boolean a_previewMode
	)
	{
		MarvinPlugin l_filter = (MarvinPlugin)MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.pattern.harrisPlessey.jar");

		HarrisPlessey hr = (HarrisPlessey) l_filter;
		hr.load();

		hr.setModoAnalise(true);
		hr.process(a_imageIn, null, null, MarvinImageMask.NULL_MASK, false);


		File dir = new File("./pattern/");
		File pt[] = dir.listFiles();

		File dirTmp = new File("pattern/tmp/");
		File imgsTmp[] = dirTmp.listFiles();


		SortedMap<String,Integer> map = new TreeMap<String, Integer>(); 
		MarvinImage mv = null;

		for(int b= 0;b < imgsTmp.length;b++)
		{	
			for(int a = 0; a < pt.length; a++)
			{
				if(pt[a].isDirectory() && !pt[a].getName().equals("tmp") )
				{
					File tmp[] = pt[a].listFiles();
					for(int i = 0; i < tmp.length;i++)
					{

						try {
							mv = new MarvinImage(ImageIO.read(imgsTmp[b]));
						} catch (IOException e) {
							e.printStackTrace();
						}
						Correlation cr = new Correlation();
						cr.setModoAnalise(true);
						cr.load();
						try {
							cr.setNovaImagem(tmp[i].getCanonicalPath());
						} catch (IOException e) {							
							e.printStackTrace();
						}
						cr.process(mv, null, null, MarvinImageMask.NULL_MASK, false);
					
						if(cr.getCr() > 0.98){

							if(map.containsKey(pt[a].getName())){
								Integer valor =  (Integer)map.get(pt[a].getName());
								valor++;
								map.put(pt[a].getName(), valor);

							}else{
								Integer valor = 1;
								map.put(pt[a].getName(), valor);

							}
						}
					}

				}

			}
		}

		String all = "";

		for(String a : map.keySet())
		{
			all += "The class is: " + a + " with: " + map.get(a).toString();
			break;
		}
		JOptionPane.showMessageDialog(null, all);	
	}

	public void show() {
		//process(getApplication().getCurrentImage(),true);

	}

}
