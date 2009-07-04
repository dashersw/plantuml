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
import java.util.List;

public class Multiplexer {

	public byte[] mix(List<byte[]> list) {
		int len = 1 + list.size() * 2;
		for (byte b[] : list) {
			len += b.length;
		}
		final byte result[] = new byte[len];

		int pos = 0;
		result[pos++] = (byte) list.size();
		for (byte tab[] : list) {
			result[pos++] = (byte) ((tab.length >> 8) & 0xFF);
			result[pos++] = (byte) (tab.length & 0xFF);
			for (byte b : tab) {
				result[pos++] = b;
			}
		}
		return result;
	}

	public List<byte[]> unmix(byte data[]) {
		final List<byte[]> result = new ArrayList<byte[]>();
		int pos = 0;
		final int nb = data[pos++];
		for (int nbTab=0; nbTab<nb; nbTab++) {
			final int len = ((0xFF & data[pos++]) << 8) | (0xFF & data[pos++]);
			final byte tab[] = new byte[len];
			result.add(tab);
			for (int i = 0; i < tab.length; i++) {
				tab[i] = data[pos++];
			}
		}
		return result;
	}

}