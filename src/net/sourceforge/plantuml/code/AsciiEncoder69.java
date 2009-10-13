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
import java.math.BigInteger;

public class AsciiEncoder69 implements URLEncoder {

	private static final BigInteger BASE69 = new BigInteger("69");
	private static final BigInteger BASE256 = new BigInteger("256");

	public String encode(byte data[]) {
		BigInteger big = BigInteger.ZERO;
		for (int i = data.length-1; i>=0; i--) {
			final int v = data[i] & 0xFF;
			big = big.multiply(BASE256);
			big = big.add(BigInteger.valueOf(v));
		}
		final StringBuilder sb = new StringBuilder();
		while (big.signum() > 0) {
			final BigInteger tab[] = big.divideAndRemainder(BASE69);
			sb.insert(0, Base69.digitToChar(tab[1].intValue()));
			big = tab[0];
		}
		return sb.toString();
	}

	public byte[] decode(String s) {
		try {
			BigInteger big = BigInteger.ZERO;
			for (char c : s.toCharArray()) {
				final int v = Base69.charToDigit(c);
				big = big.multiply(BASE69);
				big = big.add(BigInteger.valueOf(v));
			}
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (big.signum() > 0) {
				final BigInteger tab[] = big.divideAndRemainder(BASE256);
				baos.write(tab[1].intValue());
				big = tab[0];
			}
			baos.close();
			return baos.toByteArray();
		} catch (IOException e) {
			throw new IllegalStateException();
		}

	}

}
