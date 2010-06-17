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

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class Path {

	private final Label label;
	private final Block start;
	private final Block end;
	// private final List<Point2D.Double> points = new
	// ArrayList<Point2D.Double>();
	private final PointList points2 = new PointList();

	public Path(Block start, Block end, Label label) {
		this.start = start;
		this.end = end;
		this.label = label;
	}

	public final Label getLabel() {
		return label;
	}

	public final Block getStart() {
		return start;
	}

	public final Block getEnd() {
		return end;
	}

	public void addPoint(double x, double y) {
		points2.addPoint(x, y);
	}

	public void setLabelPositionCenter(double labelX, double labelY) {
		label.setCenterX(labelX);
		label.setCenterY(labelY);
	}

	public final PointList getPoints() {
		return points2;
	}

	public Point2D getIntersection(Positionable positionable) {
		final Point2D position = positionable.getPosition();
		final Dimension2D dim = positionable.getSize();
		final Rectangle2D rect = new Rectangle2D.Double(position.getX(),
				position.getY(), dim.getWidth(), dim.getHeight());
		return points2.getPoint(points2.getIntersectionDouble(rect));
	}

}
