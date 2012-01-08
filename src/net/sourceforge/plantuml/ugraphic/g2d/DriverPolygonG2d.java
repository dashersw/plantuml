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
 * Revision $Revision: 7526 $
 *
 */
package net.sourceforge.plantuml.ugraphic.g2d;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UShape;

public class DriverPolygonG2d extends DriverShadowedG2d implements UDriver<Graphics2D> {

	private final double dpiFactor;

	public DriverPolygonG2d(double dpiFactor) {
		this.dpiFactor = dpiFactor;
	}

	public void draw(UShape ushape, double x, double y, ColorMapper mapper, UParam param, Graphics2D g2d) {
		final UPolygon shape = (UPolygon) ushape;

		g2d.setStroke(new BasicStroke((float) param.getStroke().getThickness()));

		final GeneralPath path = new GeneralPath();

		boolean first = true;
		for (Point2D pt : shape.getPoints()) {
			final double xp = pt.getX() + x;
			final double yp = pt.getY() + y;
			if (first) {
				path.moveTo((float) xp, (float) yp);
			} else {
				path.lineTo((float) xp, (float) yp);
			}
			first = false;
		}

		if (first == false) {
			path.closePath();
		}

		if (shape.getDeltaShadow() != 0) {
			drawShadow(g2d, path, shape.getDeltaShadow(), dpiFactor);
		}

		if (param.getBackcolor() != null) {
			g2d.setColor(mapper.getMappedColor(param.getBackcolor()));
			g2d.fill(path);
		}
		if (param.getColor() != null) {
			g2d.setColor(mapper.getMappedColor(param.getColor()));
			g2d.draw(path);
		}
	}
}
