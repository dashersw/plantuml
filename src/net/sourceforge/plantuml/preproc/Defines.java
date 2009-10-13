/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques (for Atos Origin).
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques (for Atos Origin).
 *
 */
package net.sourceforge.plantuml.preproc;

import java.util.LinkedHashMap;
import java.util.Map;

public class Defines {

	private final Map<String, String> defines = new LinkedHashMap<String, String>();

	public void define(String name, String value) {
		defines.put(name, value);
	}

	public boolean isDefine(String name) {
		return defines.containsKey(name);
	}

	public void undefine(String name) {
		defines.remove(name);
	}

	public String applyDefines(String line) {
		for (Map.Entry<String, String> ent : defines.entrySet()) {
			final String key = ent.getKey();
			final String value = ent.getValue();
			if (value == null) {
				continue;
			}
			final String regex = "\\b" + key + "\\b";
			line = line.replaceAll(regex, value);
		}
		return line;
	}

	public String getValue(String name) {
		final String result = defines.get(name);
		if (result == null) {
			throw new IllegalArgumentException();
		}
		return result;
	}

}
