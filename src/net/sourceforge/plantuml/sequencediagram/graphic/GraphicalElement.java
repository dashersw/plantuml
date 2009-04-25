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
import java.awt.geom.AffineTransform;

import net.sourceforge.plantuml.skin.Context2D;

abstract class GraphicalElement {

	private double startingY;

	GraphicalElement(double startingY) {
		this.startingY = startingY;
	}

	void pushToDown(double delta) {
		startingY += delta;
	}

	protected final double getStartingY() {
		return startingY;
	}

	public final void draw(Graphics2D g2d, double maxX, Context2D context) {
		final AffineTransform t = g2d.getTransform();
		drawInternal(g2d, maxX, context);
		g2d.setTransform(t);
	}

	protected abstract void drawInternal(Graphics2D g2d, double maxX, Context2D context);

	public abstract double getStartingX(Graphics2D g2d);

	public abstract double getPreferredWidth(Graphics2D g2d);

	public abstract double getPreferredHeight(Graphics2D g2d);

}
