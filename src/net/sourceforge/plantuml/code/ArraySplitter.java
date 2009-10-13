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

import java.util.Arrays;
import java.util.List;

public class ArraySplitter {

	public List<byte[]> split(int[] data) {
		final byte b0[] = new byte[data.length];
		final byte b1[] = new byte[data.length];
		final byte b2[] = new byte[data.length];
		final byte b3[] = new byte[data.length];

		for (int i = 0; i < data.length; i++) {
			b0[i] = (byte) (data[i] & 0xFF);
			b1[i] = (byte) ((data[i] >> 8) & 0xFF);
			b2[i] = (byte) ((data[i] >> 16) & 0xFF);
			b3[i] = (byte) ((data[i] >> 24) & 0xFF);
		}
		return Arrays.asList(b0, b1, b2, b3);
	}

	public int[] merge(List<byte[]> list) {
		final int[] result = new int[list.get(0).length];
		for (int i = 0; i < result.length; i++) {
			result[i] = list.get(0)[i] & 0xFF;
			if (list.size() > 1) {
				result[i] += (list.get(1)[i] & 0xFF) << 8;
			}
			if (list.size() > 2) {
				result[i] += (list.get(2)[i] & 0xFF) << 16;
			}
			if (list.size() > 3) {
				result[i] += (list.get(3)[i] & 0xFF) << 24;
			}
		}
		return result;
	}

}