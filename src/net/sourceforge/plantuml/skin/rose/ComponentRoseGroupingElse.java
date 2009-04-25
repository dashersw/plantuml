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
package net.sourceforge.plantuml.skin.rose;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.skin.AbstractTextualComponent;

public class ComponentRoseGroupingElse extends AbstractTextualComponent {

	public ComponentRoseGroupingElse(Color fontColor, Font smallFont, String comment) {
		super(comment == null ? null : "[" + comment + "]", fontColor, smallFont, 5, 5, 1);
	}

	@Override
	protected void drawInternal(Graphics2D g2d, Dimension2D dimensionToUse) {
		stroke(g2d, 2);
		g2d.setColor(getFontColor());

		g2d.drawLine(0, 1, (int) dimensionToUse.getWidth(), 1);

		g2d.setStroke(new BasicStroke());

		getTextBlock().draw(g2d, getMarginX1(), getMarginY());

	}

	@Override
	public double getPreferredHeight(Graphics2D g2d) {
		return 15;
	}

	@Override
	public double getPreferredWidth(Graphics2D g2d) {
		return getTextWidth(g2d);
	}

}
