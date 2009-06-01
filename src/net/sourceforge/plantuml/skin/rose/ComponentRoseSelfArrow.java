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

public class ComponentRoseSelfArrow extends AbstractComponentRoseArrow {

	private final double arrowWidth = 45;

	public ComponentRoseSelfArrow(Color foregroundColor, Color colorFont, Font font, List<String> stringsToDisplay,
			boolean dotted) {
		super(foregroundColor, colorFont, font, stringsToDisplay, dotted);
	}

	@Override
	protected void drawInternal(Graphics2D g2d, Dimension2D dimensionToUse) {
		final int textHeight = (int) getTextHeight(g2d);

		g2d.setColor(getForegroundColor());
		final int x2 = (int) arrowWidth;

		if (isDotted()) {
			stroke(g2d, 2);
		}

		g2d.drawLine(0, textHeight, x2, textHeight);

		final int textAndArrowHeight = (int) (textHeight + getArrowOnlyHeight(g2d));

		g2d.drawLine(x2, textHeight, x2, textAndArrowHeight);
		g2d.drawLine(x2, textAndArrowHeight, 0, textAndArrowHeight);

		if (isDotted()) {
			g2d.setStroke(new BasicStroke());
		}

		g2d.drawLine(getArrowDeltaX(), textAndArrowHeight - getArrowDeltaY(), 0, textAndArrowHeight);
		g2d.drawLine(getArrowDeltaX(), textAndArrowHeight + getArrowDeltaY(), 0, textAndArrowHeight);

		getTextBlock().draw(g2d, getMarginX1(), 0);
	}

	public Point2D getStartPoint(Graphics2D g2d, Dimension2D dimensionToUse) {
		final int textHeight = (int) getTextHeight(g2d);
		return new Point2D.Double(getPaddingX(), textHeight + getPaddingY());
	}

	public Point2D getEndPoint(Graphics2D g2d, Dimension2D dimensionToUse) {
		final int textHeight = (int) getTextHeight(g2d);
		final int textAndArrowHeight = (int) (textHeight + getArrowOnlyHeight(g2d));
		return new Point2D.Double(getPaddingX(), textAndArrowHeight + getPaddingY());
	}

	@Override
	public double getPreferredHeight(Graphics2D g2d) {
		return getTextHeight(g2d) + getArrowDeltaY() + getArrowOnlyHeight(g2d) + 2 * getPaddingY();
	}

	private double getArrowOnlyHeight(Graphics2D g2d) {
		return 13;
	}

	@Override
	public double getPreferredWidth(Graphics2D g2d) {
		return Math.max(getTextWidth(g2d), arrowWidth);
	}

}
