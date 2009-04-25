/* ========================================================================
 * Plantuml : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques (for Atos Origin).
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of Plantuml.
 *
 * Plantuml is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Plantuml distributed in the hope that it will be useful, but
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
package net.sourceforge.plantuml.graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.sequencediagram.graphic.Dimension2DDouble;

public class TextBlock {

	private final List<Line> lines = new ArrayList<Line>();

	private static boolean USE_SWING = false;

	public TextBlock(List<String> texts, Font font, Color paint) {
		for (String s : texts) {
			if (USE_SWING) {
				lines.add(new HtmlLine(s, font, paint));
			} else {
				lines.add(new SimpleLine(s, font, paint));
			}
		}
	}

	public Dimension2D calculateDimension(Graphics2D g2d) {
		double width = 0;
		double height = 0;
		for (Line line : lines) {
			final Dimension2D size2D = line.calculateDimensions(g2d);
			height += size2D.getHeight();
			width = Math.max(width, size2D.getWidth());
		}
		return new Dimension2DDouble(width, height);
	}

	public void draw(Graphics2D g2d, double x, double y) {
		for (Line line : lines) {
			line.draw(g2d, x, y);
			y += line.calculateDimensions(g2d).getHeight();
		}
	}

}
