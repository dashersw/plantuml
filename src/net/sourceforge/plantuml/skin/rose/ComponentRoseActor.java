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
import net.sourceforge.plantuml.skin.StickMan;

public class ComponentRoseActor extends AbstractTextualComponent {

	private final StickMan stickman;
	private final int outMargin = 5;
	private final boolean head;

	public ComponentRoseActor(Color yellow, Color red, Color fontColor, Font font, List<String> stringsToDisplay,
			boolean head) {
		super(stringsToDisplay, fontColor, font, 3, 3, 0);
		this.head = head;
		stickman = new StickMan(yellow, red);
	}

	@Override
	protected void drawInternal(Graphics2D g2d, Dimension2D dimensionToUse) {
		g2d.setColor(getFontColor());
		final TextBlock textBlock = getTextBlock();
		final double delta = (getPreferredWidth(g2d) - stickman.getPreferredWidth(g2d)) / 2;

		if (head) {
			textBlock.draw(g2d, getTextMiddlePostion(g2d), stickman.getPreferredHeight(g2d));
			g2d.translate(delta, 0);
		} else {
			textBlock.draw(g2d, getTextMiddlePostion(g2d), 0);
			g2d.translate(delta, getTextHeight(g2d));
		}
		stickman.draw(g2d);
	}

	private double getTextMiddlePostion(Graphics2D g2d) {
		return (getPreferredWidth(g2d) - getTextWidth(g2d)) / 2.0;
	}

	@Override
	public double getPreferredHeight(Graphics2D g2d) {
		return stickman.getPreferredHeight(g2d) + getTextHeight(g2d);
	}

	@Override
	public double getPreferredWidth(Graphics2D g2d) {
		return Math.max(stickman.getPreferredWidth(g2d), getTextWidth(g2d)) + outMargin * 2;
	}

}
