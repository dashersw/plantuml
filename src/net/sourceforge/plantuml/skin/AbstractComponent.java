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

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;

public abstract class AbstractComponent implements Component {

	final protected void stroke(Graphics2D g2d, float dash, float thickness) {
		final float[] style = { dash, dash };
		g2d.setStroke(new BasicStroke(thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, style, 0));
	}

	final protected void stroke(Graphics2D g2d, float dash) {
		stroke(g2d, dash, 1);
	}

	abstract protected void drawInternal(Graphics2D g2d, Dimension2D dimensionToUse);

	protected void drawBackgroundInternal(Graphics2D g2d, Dimension2D dimensionToUse) {
	}

	public final void draw(Graphics2D g2d, Dimension2D dimensionToUse, Context2D context) {
		final AffineTransform t = g2d.getTransform();
		g2d.translate(getPaddingX(), getPaddingY());
		if (context.isBackground()) {
			drawBackgroundInternal(g2d, dimensionToUse);
		} else {
			drawInternal(g2d, dimensionToUse);
		}
		g2d.setTransform(t);
	}

	public double getPaddingX() {
		return 0;
	}

	public double getPaddingY() {
		return 0;
	}

	public abstract double getPreferredWidth(Graphics2D g2d);

	public abstract double getPreferredHeight(Graphics2D g2d);

}
