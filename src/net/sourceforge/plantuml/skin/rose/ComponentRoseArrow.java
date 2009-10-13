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
package net.sourceforge.plantuml.skin.rose;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.List;

public class ComponentRoseArrow extends AbstractComponentRoseArrow {

	private final int direction;

	public ComponentRoseArrow(Color foregroundColor, Color fontColor, Font font, List<? extends CharSequence> stringsToDisplay,
			int direction, boolean dotted) {
		super(foregroundColor, fontColor, font, stringsToDisplay, dotted);
		this.direction = direction;
		if (direction != 1 && direction != -1) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	protected void drawInternal(Graphics2D g2d, Dimension2D dimensionToUse) {
		final int textHeight = (int) getTextHeight(g2d);
		g2d.setColor(getForegroundColor());

		final int x2 = (int) dimensionToUse.getWidth();

		if (isDotted()) {
			stroke(g2d, 2);
		}

		g2d.drawLine(0, textHeight, x2, textHeight);
		if (isDotted()) {
			g2d.setStroke(new BasicStroke());
		}
		final int direction = getDirection(g2d);
		if (direction == 1) {
			g2d.drawLine(x2 - getArrowDeltaX(), textHeight - getArrowDeltaY(), x2, textHeight);
			g2d.drawLine(x2 - getArrowDeltaX(), textHeight + getArrowDeltaY(), x2, textHeight);
		} else {
			g2d.drawLine(getArrowDeltaX(), textHeight - getArrowDeltaY(), 0, textHeight);
			g2d.drawLine(getArrowDeltaX(), textHeight + getArrowDeltaY(), 0, textHeight);

		}
		getTextBlock().draw(g2d, getMarginX1(), 0);
	}

	public Point2D getStartPoint(Graphics2D g2d, Dimension2D dimensionToUse) {
		final int textHeight = (int) getTextHeight(g2d);
		if (direction == 1) {
			return new Point2D.Double(getPaddingX(), textHeight + getPaddingY());
		}
		return new Point2D.Double(dimensionToUse.getWidth() + getPaddingX(), textHeight + getPaddingY());
	}

	public Point2D getEndPoint(Graphics2D g2d, Dimension2D dimensionToUse) {
		final int textHeight = (int) getTextHeight(g2d);
		if (direction == 1) {
			return new Point2D.Double(dimensionToUse.getWidth() + getPaddingX(), textHeight + getPaddingY());
		}
		return new Point2D.Double(getPaddingX(), textHeight + getPaddingY());
	}

	protected int getDirection(Graphics2D g2d) {
		return direction;
	}

	@Override
	public double getPreferredHeight(Graphics2D g2d) {
		return getTextHeight(g2d) + getArrowDeltaY() + 2 * getPaddingY();
	}

	@Override
	public double getPreferredWidth(Graphics2D g2d) {
		return getTextWidth(g2d);
	}

}
