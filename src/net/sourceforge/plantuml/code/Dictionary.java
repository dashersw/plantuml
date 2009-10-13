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
package net.sourceforge.plantuml.code;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Dictionary {

	private final Map<Character, String> words = new LinkedHashMap<Character, String>();

	private char next = '\u0001';

	public Dictionary(Collection<Character> forbidden) {
		add("actor", forbidden);
		add("participant", forbidden);
		add("skin", forbidden);
		add("else", forbidden);
		add("activate", forbidden);
		add("deactivate", forbidden);
		add("destroy", forbidden);
		add("class", forbidden);
		add("interface", forbidden);
		add("enum", forbidden);
		add("component", forbidden);
		add("abstract", forbidden);
		add("usecase", forbidden);
		add("note", forbidden);
		add("right", forbidden);
		add("left", forbidden);
		add("over", forbidden);
		add("newpage", forbidden);
		add("title", forbidden);
		add("bottom", forbidden);
		add("package", forbidden);

		// autonumber
		// add("opt", forbidden);
		// add("end", forbidden);

	}

	private void add(String s, Collection<Character> forbidden) {
		while (forbidden.contains(next)) {
			next++;
		}
		if (words.containsValue(s)) {
			throw new IllegalArgumentException();
		}
		words.put(next, s);
		next++;
	}

	public int getStartingWord(String stringToTest) {

		for (Map.Entry<Character, String> ent : words.entrySet()) {
			if (stringToTest.startsWith(ent.getValue())) {
				return ent.getKey();
			}
		}
		return -1;
	}

	public String getWord(int index) {
		return words.get(new Character((char) index));
	}

	public boolean isSpecial(char c) {
		return words.containsKey(new Character(c));
	}

}