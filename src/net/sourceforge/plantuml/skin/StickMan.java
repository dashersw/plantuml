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
package net.sourceforge.plantuml.skin;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class StickMan {

	private final float thickness = 2;

	private final double armsY = 8;
	private final double armsLenght = 13;
	private final double bodyLenght = 27;
	private final double legsX = 13;
	private final double legsY = 15;
	private final double headDiam = 16;

	private final Color backgroundColor;
	private final Color foregroundColor;

	public StickMan(Color backgroundColor, Color foregroundColor) {
		this.backgroundColor = backgroundColor;
		this.foregroundColor = foregroundColor;
	}

	public void draw(Graphics2D g2d) {

		g2d.setStroke(new BasicStroke(thickness));

		final double startX = Math.max(armsLenght, legsX) - headDiam / 2.0 + thickness;

		final Shape head = new Ellipse2D.Double(startX, thickness, headDiam, headDiam);
		final Rectangle2D headBound = head.getBounds2D();

		final double centerX = headBound.getCenterX();

		final Shape body = new Line2D.Double(centerX, headBound.getMaxY(), centerX, headBound.getMaxY() + bodyLenght);

		final Shape arms = new Line2D.Double(centerX - armsLenght, headBound.getMaxY() + armsY, centerX + armsLenght,
				headBound.getMaxY() + armsY);

		final double y = body.getBounds2D().getMaxY();

		final Shape legs1 = new Line2D.Double(centerX, y, centerX - legsX, y + legsY);
		final Shape legs2 = new Line2D.Double(centerX, y, centerX + legsX, y + legsY);

		g2d.setColor(backgroundColor);
		g2d.fill(head);

		g2d.setColor(foregroundColor);
		g2d.draw(head);
		g2d.draw(body);
		g2d.draw(arms);
		g2d.draw(legs1);
		g2d.draw(legs2);

		g2d.setStroke(new BasicStroke());
	}

	public double getPreferredWidth(Graphics2D g2d) {
		return Math.max(armsLenght, legsX) * 2 + 2 * thickness;
	}

	public double getPreferredHeight(Graphics2D g2d) {
		return headDiam + bodyLenght + legsY + 2 * thickness;
	}

}
