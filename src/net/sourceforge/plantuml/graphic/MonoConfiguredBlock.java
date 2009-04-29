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
package net.sourceforge.plantuml.graphic;

import java.awt.BasicStroke;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.Dimension2DDouble;

class MonoConfiguredBlock {

	private final String text;
	private final FontConfiguration fontConfiguration;

	public MonoConfiguredBlock(String text, FontConfiguration fontConfiguration) {
		this.fontConfiguration = fontConfiguration;
		this.text = text;
	}

	public Dimension2D calculateDimensions(Graphics2D g2d) {
		final FontMetrics fm = g2d.getFontMetrics(fontConfiguration.getFont());
		final Rectangle2D rect = fm.getStringBounds(text, g2d);
		return new Dimension2DDouble(rect.getWidth(), rect.getHeight());
	}

	void draw(Graphics2D g2d, double x, double y) {
		g2d.setFont(fontConfiguration.getFont());
		g2d.setPaint(fontConfiguration.getColor());
		final double d = fontConfiguration.getFont().getSize2D();
		g2d.drawString(text, (float) x, (float) (y + d));

		if (fontConfiguration.containsStyle(FontStyle.UNDERLINE)) {
			final Dimension2D dim = calculateDimensions(g2d);
			final FontMetrics fm = g2d.getFontMetrics(fontConfiguration.getFont());
			final int ypos = (int) (y + fm.getAscent() + 1);
			g2d.setStroke(new BasicStroke((float) 1.3));
			g2d.drawLine((int) x, ypos, (int) (x + dim.getWidth()), ypos);
			g2d.setStroke(new BasicStroke());
		}
		if (fontConfiguration.containsStyle(FontStyle.STRIKE)) {
			final Dimension2D dim = calculateDimensions(g2d);
			// final FontMetrics fm =
			// g2d.getFontMetrics(fontConfiguration.getFont());
			final int ypos = (int) (y + dim.getHeight() / 2 + 1);
			g2d.setStroke(new BasicStroke((float) 1.5));
			g2d.drawLine((int) x, ypos, (int) (x + dim.getWidth()), ypos);
			g2d.setStroke(new BasicStroke());
		}
	}

	public double getAscent(Graphics2D g2d) {
		final FontMetrics fm = g2d.getFontMetrics(fontConfiguration.getFont());
		return fm.getAscent();
	}

	public double getDescent(Graphics2D g2d) {
		final FontMetrics fm = g2d.getFontMetrics(fontConfiguration.getFont());
		return fm.getDescent();
	}
}
