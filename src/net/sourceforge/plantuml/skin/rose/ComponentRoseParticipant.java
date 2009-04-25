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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;

public class ComponentRoseParticipant extends AbstractTextualComponent {

	private final int outMargin = 5;
	private final Color back;
	private final Color foregroundColor;

	public ComponentRoseParticipant(Color back, Color foregroundColor, Color fontColor, Font font, List<String> stringsToDisplay) {
		super(stringsToDisplay, fontColor, font, 7, 7, 7);
		this.back = back;
		this.foregroundColor = foregroundColor;
	}

	@Override
	protected void drawInternal(Graphics2D g2d, Dimension2D dimensionToUse) {
		g2d.setColor(back);
		g2d.fillRect(outMargin, 0, (int) getTextWidth(g2d), (int) getTextHeight(g2d));
		g2d.setColor(foregroundColor);
		g2d.drawRect(outMargin, 0, (int) getTextWidth(g2d), (int) getTextHeight(g2d));
		final TextBlock textBlock = getTextBlock();
		textBlock.draw(g2d, (int) (outMargin + getMarginX1()), getMarginY());
	}

	@Override
	public double getPreferredHeight(Graphics2D g2d) {
		return getTextHeight(g2d);
	}

	@Override
	public double getPreferredWidth(Graphics2D g2d) {
		return getTextWidth(g2d) + outMargin * 2;
	}

}
