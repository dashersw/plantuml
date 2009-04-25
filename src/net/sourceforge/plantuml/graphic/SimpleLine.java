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

class SimpleLine implements Line {

	private final List<SimpleBlock> blocs = new ArrayList<SimpleBlock>();

	public SimpleLine(String text, Font font, Color paint) {
		final LineSplitter lineSplitter = new LineSplitter(text);
		for (String s : lineSplitter.getSplitted()) {
			blocs.add(new SimpleBlock(s, font, paint));
		}
	}

	public Dimension2D calculateDimensions(Graphics2D g2d) {
		double width = 0;
		double height = 0;
		for (SimpleBlock b : blocs) {
			final Dimension2D size2D = b.calculateDimensions(g2d);
			width += size2D.getWidth();
			height = Math.max(height, size2D.getHeight());
		}
		return new Dimension2DDouble(width, height);
	}

	public void draw(Graphics2D g2d, double x, double y) {
		for (SimpleBlock b : blocs) {
			b.draw(g2d, x, y);
			x += b.calculateDimensions(g2d).getWidth();
		}
	}
}
