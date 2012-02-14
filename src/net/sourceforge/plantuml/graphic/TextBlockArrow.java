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
 * Revision $Revision: 7163 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;

public class TextBlockArrow implements TextBlock {

	private final double size;
	private final char arrow;
	private final HtmlColor color;

	public TextBlockArrow(char arrow, FontConfiguration fontConfiguration) {
		if (arrow != '<' && arrow != '>') {
			throw new IllegalArgumentException();
		}
		this.arrow = arrow;
		this.size = fontConfiguration.getFont().getSize2D() * 0 + 30;
		// this.size = fontConfiguration.getFont().getSize2D();
		System.err.println("size=" + size);
		this.color = fontConfiguration.getColor();

	}

	public void drawU(UGraphic ug, double x, double y) {
		//ug.draw(x, y, new URectangle(size, size));
		ug.getParam().setBackcolor(color);
		ug.getParam().setColor(color);
		final UPolygon triangle = new UPolygon();
		int x1 = (int) (size * .8 - 3);
		if (x1 % 2 == 1) {
			x1--;
		}
		int y1 = (int) (size * .8 - 3);
		if (y1 % 2 == 1) {
			y1--;
		}
		triangle.addPoint(0, 0);
		triangle.addPoint(x1, y1 / 2);
		triangle.addPoint(0, y1);
		triangle.addPoint(0, 0);
		ug.draw(x + 2, y + (size - y1) - 2, triangle);
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(size, size);
	}

	public void drawTOBEREMOVED(ColorMapper colorMapper, Graphics2D g2d, double x, double y) {
		throw new UnsupportedOperationException();
	}

}