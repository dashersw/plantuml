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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;

class GroupingTail extends GraphicalElement {

	private final double initY;
	private final double xpos;
	private final Component body;
	private final Component tail;

	public GroupingTail(double currentY, double initY, double xpos, Component body, Component tail) {
		super(currentY);
		if (currentY <= initY) {
			throw new IllegalArgumentException();
		}
		this.body = body;
		this.tail = tail;
		this.initY = initY;
		this.xpos = xpos;
	}

	public double getStartingX(Graphics2D g2d) {
		return xpos;
	}

	@Override
	protected void drawInternal(Graphics2D g2d, double maxX, Context2D context) {
		final double x = g2d.getTransform().getTranslateX();
		g2d.translate(-x + xpos, initY);
		final Dimension2D dimBody = new Dimension2DDouble(maxX - 2 * xpos, getPreferredHeight(g2d));
		body.draw(g2d, dimBody, context);
		tail.draw(g2d, dimBody, context);

	}

	@Override
	public double getPreferredHeight(Graphics2D g2d) {
		return getStartingY() - initY;
	}

	@Override
	public double getPreferredWidth(Graphics2D g2d) {
		return 0;
	}


}
