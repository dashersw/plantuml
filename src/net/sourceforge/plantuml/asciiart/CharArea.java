/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 3826 $
 *
 */
package net.sourceforge.plantuml.asciiart;

public class CharArea extends BasicCharArea {

	public void drawBoxSimple(int x, int y, int width, int height) {
		this.drawHLine('-', y, x + 1, x + width - 1);
		this.drawHLine('-', y + height - 1, x + 1, x + width - 1);

		this.drawVLine('|', x, y + 1, y + height - 1);
		this.drawVLine('|', x + width - 1, y + 1, y + height - 1);

		this.drawChar('+', x, y);
		this.drawChar('+', x + width - 1, y);
		this.drawChar('+', x, y + height - 1);
		this.drawChar('+', x + width - 1, y + height - 1);
	}

	public void drawBoxSimpleUnicode(int x, int y, int width, int height) {
		this.drawHLine('\u2500', y, x + 1, x + width - 1);
		this.drawHLine('\u2500', y + height - 1, x + 1, x + width - 1);

		this.drawVLine('\u2502', x, y + 1, y + height - 1);
		this.drawVLine('\u2502', x + width - 1, y + 1, y + height - 1);

		this.drawChar('\u250c', x, y);
		this.drawChar('\u2510', x + width - 1, y);
		this.drawChar('\u2514', x, y + height - 1);
		this.drawChar('\u2518', x + width - 1, y + height - 1);
	}

	public void drawStickMan(int x, int y) {
		this.drawStringLR(",-.", x, y++);
		this.drawStringLR("`-'", x, y++);
		this.drawStringLR("/|\\", x, y++);
		this.drawStringLR(" | ", x, y++);
		this.drawStringLR("/|\\", x, y++);
	}
}
