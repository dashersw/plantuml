/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 6859 $
 *
 */
package net.sourceforge.plantuml.ugraphic.g2d;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class DriverShadowedG2d {

	private ConvolveOp getConvolveOp(int blurRadius, double dpiFactor) {
		blurRadius = (int) (blurRadius * dpiFactor);
		final int blurRadius2 = blurRadius * blurRadius;
		final float blurRadius2F = blurRadius2;
		// final float weight = (float) (1.0 / blurRadius2F / dpiFactor);
		final float weight = (float) (1.0 / blurRadius2F);
		final float[] elements = new float[blurRadius2];
		for (int k = 0; k < blurRadius2; k++) {
			elements[k] = weight;
		}
		final Kernel myKernel = new Kernel(blurRadius, blurRadius, elements);

		// if EDGE_NO_OP is not selected, EDGE_ZERO_FILL is the default which
		// creates a black border
		return new ConvolveOp(myKernel, ConvolveOp.EDGE_NO_OP, null);
	}

	private final Color color = new Color(170, 170, 170);

	protected void drawShadow(Graphics2D g2d, Shape shape, double deltaShadow, double dpiFactor) {
		if (dpiFactor < 1) {
			dpiFactor = 1;
		}
		// dpiFactor = 1;
		// Shadow
		final Rectangle2D bounds = shape.getBounds2D();
		final double w = (bounds.getMaxX() + deltaShadow * 2 + 6) * dpiFactor;
		final double h = (bounds.getMaxY() + deltaShadow * 2 + 6) * dpiFactor;
		BufferedImage destination = new BufferedImage((int) w, (int) h, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D gg = destination.createGraphics();
		gg.setColor(color);
		gg.scale(dpiFactor, dpiFactor);
		gg.translate(deltaShadow - bounds.getMinX(), deltaShadow - bounds.getMinY());
		if (shape instanceof Line2D.Double) {
			gg.draw(shape);
		} else {
			gg.fill(shape);
		}
		gg.dispose();
		final ConvolveOp simpleBlur = getConvolveOp(6, dpiFactor);
		destination = simpleBlur.filter(destination, null);
		final AffineTransform at = g2d.getTransform();
		g2d.scale(1 / dpiFactor, 1 / dpiFactor);
		g2d.drawImage(destination, (int) (bounds.getMinX() * dpiFactor), (int) (bounds.getMinY() * dpiFactor), null);
		g2d.setTransform(at);
	}
}
