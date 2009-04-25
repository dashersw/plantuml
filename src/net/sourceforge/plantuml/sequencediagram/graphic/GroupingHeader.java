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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;

class GroupingHeader extends GraphicalElement {

	private final Component comp;
	private final double xpos;

	public GroupingHeader(double currentY, Component comp, double xpos) {
		super(currentY);
		this.xpos = xpos;
		this.comp = comp;
	}

	final public double getPreferredWidth(Graphics2D g2d) {
		return comp.getPreferredWidth(g2d);
	}

	final public double getPreferredHeight(Graphics2D g2d) {
		return comp.getPreferredHeight(g2d);
	}
	
	public double getStartingX(Graphics2D g2d) {
		return xpos;
	}

	@Override
	protected void drawInternal(Graphics2D g2d, double maxX, Context2D context) {
		final double x = g2d.getTransform().getTranslateX();
		g2d.translate(xpos - x, getStartingY());
		final Dimension2D dim = new Dimension2DDouble(maxX - 2 * xpos, comp.getPreferredHeight(g2d));
		comp.draw(g2d, dim, context);
	}

}
