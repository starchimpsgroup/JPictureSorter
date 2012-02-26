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

import java.util.HashMap;

/**
 * @author Gabriel Ambr�sio Archanjo
 */
public class Grammar {
	HashMap<String, String> rules;
	
	public Grammar(){
		rules = new HashMap<String, String>();
	}
	
	public void addRule(String predecessor, String sucessor){
		rules.put(predecessor, sucessor);
	}
	
	public String derive(String text){
		StringBuffer result = new StringBuffer();
		String temp;
		for(int i=0; i<text.length(); i++){
			temp = rules.get(""+text.charAt(i));
			if(temp == null){
				result.append(text.charAt(i)); 
			}
			else{
				result.append(temp);
			}
		}
		return result.toString();
	}
}
