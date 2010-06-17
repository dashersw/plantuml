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
 * Revision $Revision: 4236 $
 * 
 */
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;

public class DecorInterfaceProvider implements Decor {

	private final double radius = 4;
	private final double distanceCircle = 16;

	public void drawLine(UGraphic ug, Point2D start, Point2D end) {
		ug.draw(start.getX(), start.getY(), new ULine(end.getX() - start.getX(), end.getY() - start.getY()));

		final double dist = start.distance(end);
		if (dist == 0) {
			throw new IllegalArgumentException();
		}
		final double dx = end.getX() - start.getX();
		final double dy = end.getY() - start.getY();

		final double delta = distanceCircle / dist;
		final double cornerX = start.getX() + delta * dx;
		final double cornerY = start.getY() + delta * dy;

		ug.draw(cornerX - radius, cornerY - radius, new UEllipse(2 * radius, 2 * radius));
	}

}
