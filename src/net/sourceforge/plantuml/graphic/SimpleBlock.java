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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.sequencediagram.graphic.Dimension2DDouble;

class SimpleBlock {

	private final Pattern pattern = Pattern.compile("^(?:\\<[buiBUI]\\>)*(.*?)(?:\\</[buiBUI]\\>)*$");

	private final String text;
	private final FontStyle fontStyle;
	private final Font font;
	private final Color paint;

	public SimpleBlock(String text, Font font, Color paint) {
		this.fontStyle = FontStyle.getStyle(text);
		final Matcher matcher = pattern.matcher(text);
		if (matcher.find() == false) {
			throw new IllegalArgumentException(text);
		}
		this.text = matcher.group(1);
		this.font = this.fontStyle.mutateFont(font);
		this.paint = paint;
	}

	public Dimension2D calculateDimensions(Graphics2D g2d) {
		final FontMetrics fm = g2d.getFontMetrics(font);
		final Rectangle2D rect = fm.getStringBounds(text, g2d);
		return new Dimension2DDouble(rect.getWidth(), rect.getHeight());
	}

	public void draw(Graphics2D g2d, double x, double y) {
		g2d.setFont(font);
		g2d.setPaint(paint);
		final double d = font.getSize2D();
		g2d.drawString(text, (float) x, (float) (y + d));

		if (fontStyle == FontStyle.UNDERLINE) {
			final Dimension2D dim = calculateDimensions(g2d);
			final FontMetrics fm = g2d.getFontMetrics(font);
			final int ypos = (int) (y + fm.getAscent() + 1);
			g2d.drawLine((int) x, ypos, (int) (x + dim.getWidth()), ypos);

		}
	}
}
