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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class DictionaryStringCompressor implements StringCompressor {

	private final Dictionary dictionnary = new Dictionary(Arrays.asList('\t', '\r', '\n'));

	public String compress(String s) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final StringBuilder sb = new StringBuilder();

		for (int i = 0; i < s.length(); i++) {
			final int index = dictionnary.getStartingWord(s.substring(i));
			if (index == -1) {
				sb.append(s.charAt(i));
			} else {
				i += dictionnary.getWord(index).length() - 1;
				sb.append(getSpecialChar(index));
			}
		}
		baos.close();
		return sb.toString();
	}

	private char getSpecialChar(int index) {
		return (char) index;
	}

	private int getIndex(char c) {
		return c;
	}

	private boolean isSpecial(char c) {
		return dictionnary.isSpecial(c);
	}

	public String decompress(String stringAnnoted) throws IOException {
		final StringBuilder sb = new StringBuilder();

		for (int i = 0; i < stringAnnoted.length(); i++) {
			final char c = stringAnnoted.charAt(i);
			if (isSpecial(c)) {
				sb.append(dictionnary.getWord(getIndex(c)));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}