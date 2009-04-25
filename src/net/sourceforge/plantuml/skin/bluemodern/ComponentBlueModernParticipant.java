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
import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.skin.AbstractTextualComponent;

public class ComponentBlueModernParticipant extends AbstractTextualComponent {

	private final int shadowview = 3;
	private final int outMargin = 5;
	private final Color blue1;
	private final Color blue2;

	public ComponentBlueModernParticipant(Color blue1, Color blue2, Color fontColor, Font font,
			List<String> stringsToDisplay) {
		super(stringsToDisplay, fontColor, font, 7, 7, 7);
		this.blue1 = blue1;
		this.blue2 = blue2;
	}

	@Override
	protected void drawInternal(Graphics2D g2d, Dimension2D dimensionToUse) {

		final ShadowShape shadowShape = new ShadowShape(getTextWidth(g2d), getTextHeight(g2d), 10);
		g2d.translate(shadowview + outMargin, shadowview);
		shadowShape.draw(g2d);
		g2d.translate(-shadowview - outMargin, -shadowview);

		final FillRoundShape shape = new FillRoundShape(getTextWidth(g2d), getTextHeight(g2d), blue1, blue2, 10);
		g2d.translate(outMargin, 0);
		shape.draw(g2d);
		g2d.translate(-outMargin, 0);

		getTextBlock().draw(g2d, (int) (outMargin + getMarginX1()), getMarginY());
	}

	@Override
	public double getPreferredHeight(Graphics2D g2d) {
		return getTextHeight(g2d) + shadowview;
	}

	@Override
	public double getPreferredWidth(Graphics2D g2d) {
		return getTextWidth(g2d) + outMargin * 2;
	}

}
