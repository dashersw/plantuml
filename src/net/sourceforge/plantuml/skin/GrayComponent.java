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
package net.sourceforge.plantuml.skin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.graphic.TextBlock;

public class GrayComponent extends AbstractComponent {

	private static final Font NORMAL = new Font("SansSerif", Font.PLAIN, 7);
	
	private final ComponentType type;

	public GrayComponent(ComponentType type) {
		this.type = type;
	}

	@Override
	protected void drawInternal(Graphics2D g2d, Dimension2D dimensionToUse) {
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(0, 0, (int) getPreferredWidth(g2d), (int) getPreferredHeight(g2d));
		g2d.setColor(Color.BLACK);
		g2d.drawRect(0, 0, (int) getPreferredWidth(g2d), (int) getPreferredHeight(g2d));

		final String n = type.name();
		final int split = 9;
		final List<String> strings = new ArrayList<String>();
		for (int i = 0; i < n.length(); i += split) {
			strings.add(n.substring(i, Math.min(i + split, n.length())));
		}

		final TextBlock textBlock = new TextBlock(strings, NORMAL, Color.BLACK);
		textBlock.draw(g2d, 0, 0);
	}

	@Override
	public double getPreferredHeight(Graphics2D g2d) {
		return 42;
	}

	@Override
	public double getPreferredWidth(Graphics2D g2d) {
		return 42;
	}

}
