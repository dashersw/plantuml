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
package net.sourceforge.plantuml.skin.bluemodern;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

public class FillRoundShape {

	final private double width;
	final private double height;
	final private double corner;
	final private Color c1;
	final private Color c2;

	public FillRoundShape(double width, double height, Color c1, Color c2, double corner) {
		this.width = width;
		this.height = height;
		this.c1 = c1;
		this.c2 = c2;
		this.corner = corner;

	}

	public void draw(Graphics2D g2d) {
		final GradientPaint paint = new GradientPaint(0, 0, c1, (float) width, (float) height, c2);
		final RoundRectangle2D r = new RoundRectangle2D.Double(0, 0, width, height, corner * 2, corner * 2);
		g2d.setPaint(paint);

		g2d.fill(r);
	}
}
