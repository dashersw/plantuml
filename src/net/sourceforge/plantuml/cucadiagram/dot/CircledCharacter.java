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
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class CircledCharacter {

	private final String c;
	private final Font font;
	private final Color innerCircle;
	private final Color circle;
	private final Color fontColor;

	public CircledCharacter(char c, Font font, Color innerCircle, Color circle, Color fontColor) {
		this.c = "" + c;
		this.font = font;
		this.innerCircle = innerCircle;
		this.circle = circle;
		this.fontColor = fontColor;
	}

	public void draw(Graphics2D g2d) {
		g2d.setColor(innerCircle);
		g2d.fillOval(0, 0, (int) getPreferredWidth(g2d), (int) getPreferredHeight(g2d));
		g2d.setColor(circle);
		g2d.drawOval(0, 0, (int) getPreferredWidth(g2d), (int) getPreferredHeight(g2d));

		g2d.setColor(fontColor);
		g2d.setFont(font);
		final Rectangle2D stringDimension = getStringDimension(g2d);
		final int descent = g2d.getFontMetrics(font).getDescent();
		final double deltaX = (getPreferredWidth(g2d) - stringDimension.getWidth()) / 2;
		final double deltaY = getPreferredHeight(g2d) - (getPreferredHeight(g2d) - stringDimension.getHeight()) / 2
				- descent;
		g2d.drawString(c, (float) (deltaX + 0.5), (float) deltaY);
		// g2d.drawString(c, (float) (deltaX + 0.5), (float) deltaY);
		// g2d.drawString(c, (float) (deltaX - 0.5), (float) deltaY);
	}

	private Rectangle2D getStringDimension(Graphics2D g2d) {
		return g2d.getFontMetrics(font).getStringBounds(c, g2d);
	}

	final public double getPreferredWidth(Graphics2D g2d) {
		final Rectangle2D dim = getStringDimension(g2d);
		return Math.max(dim.getWidth(), dim.getHeight());
	}

	final public double getPreferredHeight(Graphics2D g2d) {
		return getPreferredWidth(g2d);
	}

}
