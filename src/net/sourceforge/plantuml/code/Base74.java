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

class Base74 {

	// private static final String cars =
	// "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_:;@.,[]{}()=$#*!^";
	private static final String cars = "0123456789-_.,*()/~@$=abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	static char digitToChar(int digit) {
		if (digit < 0 || digit >= 74) {
			throw new IllegalArgumentException("d=" + digit);
		}
		return cars.charAt(digit);
	}

	static int charToDigit(char c) {
		final int idx = cars.indexOf(c, 0);
		if (idx == -1) {
			throw new IllegalArgumentException();
		}
		return idx;
	}

	static public long decode(String s) {
		if (s.length() != 5) {
			throw new IllegalArgumentException();
		}
		final int v1 = charToDigit(s.charAt(0));
		final int v2 = charToDigit(s.charAt(1));
		final int v3 = charToDigit(s.charAt(2));
		final int v4 = charToDigit(s.charAt(3));
		final int v5 = charToDigit(s.charAt(4));
		return ((((v1 * 74L) + v2) * 74L + v3) * 74L + v4) * 74L + v5;
	}

	static public String code(final long x) {
		if (x < 0 || x >= 74L * 74L * 74L * 74L * 74L) {
			throw new IllegalArgumentException("x=" + x);
		}
		long r = x;
		final int v1 = (int) (r / 74L / 74L / 74L / 74L);
		r = r - v1 * 74L * 74L * 74L * 74L;
		final int v2 = (int) (r / 74L / 74L / 74L);
		r = r - v2 * 74L * 74L * 74L;
		final int v3 = (int) (r / 74L / 74L);
		r = r - v3 * 74L * 74L;
		final int v4 = (int) (r / 74L);
		r = r - v4 * 74L;
		final int v5 = (int) r;

		final StringBuilder sb = new StringBuilder();
		sb.append(digitToChar(v1));
		sb.append(digitToChar(v2));
		sb.append(digitToChar(v3));
		sb.append(digitToChar(v4));
		sb.append(digitToChar(v5));
		return sb.toString();
	}

}
