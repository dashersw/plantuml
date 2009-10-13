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

public class Base74Encoder implements URLEncoder {

	private static final int GROUP_BIT = 31;
	private static final int GROUP_CHAR = 5;

	public byte[] decode(final String s) {
		if (s.length() % GROUP_CHAR != 0) {
			throw new IllegalArgumentException();
		}
		final int len = s.length() / GROUP_CHAR * GROUP_BIT;
		final byte[] result = new byte[(len + 7) / 8];
		for (int k = 0; k < result.length; k++) {
			for (int i = 0; i < 8; i++) {
				final int bn = k * 8 + i;
				final int bn1 = bn / GROUP_BIT;
				final int bn2 = bn % GROUP_BIT;

				boolean test = false;
				if (bn1 * GROUP_CHAR < s.length()) {
					final String sub = s.substring(bn1 * GROUP_CHAR, bn1 * GROUP_CHAR + GROUP_CHAR);
					final long x = Base74.decode(sub);
					test = (x & (1L << bn2)) != 0;
				}

				if (test) {
					result[k] += 1 << i;
				}

			}

		}
		return result;
	}

	public String encode(byte[] data) {
		final int len = nbBitsForBytes(data.length);
		if (len % GROUP_BIT != 0) {
			throw new AssertionError();
		}
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i += GROUP_BIT) {
			long v = 0;
			for (int j = 0; j < GROUP_BIT; j++) {
				final int bn = i + j;
				final int bn1 = bn / 8;
				final int bn2 = bn % 8;
				final boolean test = bn1 < data.length ? (data[bn1] & (1 << bn2)) != 0 : false;
				if (test) {
					v += 1L << j;
				}
			}
			sb.append(Base74.code(v));

		}
		return sb.toString();
	}

	static int nbBitsForBytes(int nbByte) {
		final int nbBit = nbByte * 8;
		return ((nbBit + (GROUP_BIT - 1)) / GROUP_BIT) * GROUP_BIT;
	}

}
