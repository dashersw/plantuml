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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordListProducer {

	private final List<String> words = new ArrayList<String>();

	private StringBuilder sb = null;

	public void addChar(char c) {
		if (Character.isLetter(c)) {
			addLetter(c);
		} else {
			addNonLetter(c);
		}
	}

	private void addNonLetter(char c) {
		if (sb != null) {
			addWord(sb.toString());
			sb = null;
		}
	}

	private void addWord(String word) {
		if (word.length() > 50 && words.contains(word) == false) {
			words.add(word);
		}
	}

	private void addLetter(char c) {
		if (sb == null) {
			sb = new StringBuilder();
		}
		sb.append(c);

	}

	public final List<String> getWords() {
		return Collections.unmodifiableList(words);
	}

	public int getWordStarting(String s) {
		for (int i = 0; i < words.size(); i++) {
			if (s.startsWith(words.get(i))) {
				return i;
			}
		}
		return -1;
	}

}