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
package net.sourceforge.plantuml.skin.bluemodern;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.skin.AbstractTextualComponent;

final public class ComponentBlueModernNote extends AbstractTextualComponent {

	private final int shadowview = 4;
	private final int cornersize = 10;
	private final Color back;
	private final Color foregroundColor;

	public ComponentBlueModernNote(Color back, Color foregroundColor, Color fontColor, Font font, List<String> strings) {
		super(strings, fontColor, font, 6, 15, 5);
		this.back = back;
		this.foregroundColor = foregroundColor;
	}

	final public double getPreferredWidth(Graphics2D g2d) {
		final double result = getTextWidth(g2d) + 2 * getPaddingX();
		return result;
	}

	final public double getPreferredHeight(Graphics2D g2d) {
		return getTextHeight(g2d) + 2 * getPaddingY();
	}

	@Override
	public double getPaddingX() {
		return 9;
	}

	@Override
	public double getPaddingY() {
		return 9;
	}

	@Override
	protected void drawInternal(Graphics2D g2d, Dimension2D dimensionToUse) {
		final int textHeight = (int) getTextHeight(g2d);

		final int textWidth = (int) getTextWidth(g2d);

		final ShadowShape shadowShape = new ShadowShape(textWidth, textHeight, 3);
		g2d.translate(shadowview, shadowview);
		shadowShape.draw(g2d);
		g2d.translate(-shadowview, -shadowview);

		final Polygon polygon = new Polygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(0, textHeight);
		polygon.addPoint(textWidth, textHeight);
		polygon.addPoint(textWidth, cornersize);
		polygon.addPoint(textWidth - cornersize, 0);
		polygon.addPoint(0, 0);

		g2d.setColor(back);
		g2d.fill(polygon);
		g2d.setColor(foregroundColor);
		g2d.draw(polygon);

		g2d.drawLine(textWidth - cornersize, 0, textWidth - cornersize, cornersize);
		g2d.drawLine(textWidth, cornersize, textWidth - cornersize, cornersize);

		getTextBlock().draw(g2d, getMarginX1(), getMarginY());
	}

}
