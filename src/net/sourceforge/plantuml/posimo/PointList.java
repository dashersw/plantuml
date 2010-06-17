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
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PointList {

	private final List<Point2D.Double> points = new ArrayList<Point2D.Double>();

	// http://en.wikipedia.org/wiki/B-spline
	// http://en.wikipedia.org/wiki/B%C3%A9zier_curve

//	public PointList muteStartAndEnd(Point2D start, Point2D end) {
//		final PointList result = new PointList();
//		result.points.addAll(this.points);
//		result.points.set(0, new Point2D.Double(start.getX(), start.getY()));
//		final int last = this.points.size() - 1;
//		result.points.set(last, new Point2D.Double(end.getX(), end.getY()));
//		return result;
//	}

	public void addPoint(double x, double y) {
		points.add(new Point2D.Double(x, y));
	}

	public final Collection<? extends Point2D> getPoints() {
		return Collections.unmodifiableCollection(points);
	}

	int getN() {
		return points.size() - 1;
	}

	public Point2D getPoint(double t) {
		if (t < 0 || t > 1) {
			throw new IllegalArgumentException();
		}
		return new Point2D.Double(bx(t), by(t));
	}

//	public Point2D getIntersection(Rectangle2D rect) {
//		return getPoint(getIntersectionDouble(rect));
//	}

	public double getIntersectionDouble(Rectangle2D rect) {
		final boolean containsStart = rect.contains(getPoint(0));
		final boolean containsEnd = rect.contains(getPoint(1));
		if (containsStart ^ containsEnd == false) {
			throw new IllegalArgumentException();
		}
		final Segment seg;
		if (containsStart) {
			seg = new Segment(0, 1);
		} else {
			seg = new Segment(1, 0);
		}
		while (seg.length(this) > .0001) {
			dichotomy(rect, seg);
		}
		return seg.getMiddle();
	}

	private void dichotomy(Rectangle2D rect, Segment seg) {
		assert rect.contains(getPoint(seg.inside));
		assert rect.contains(getPoint(seg.outside)) == false;
		if (rect.contains(getPoint(seg.getMiddle()))) {
			seg.inside = seg.getMiddle();
		} else {
			seg.outside = seg.getMiddle();
		}
	}

	static class Segment {
		private double inside;
		private double outside;

		public Segment(double inside, double outside) {
			this.inside = inside;
			this.outside = outside;
		}

		public double getMiddle() {
			return (inside + outside) / 2;
		}

		public double length(PointList list) {
			final Point2D p1 = list.getPoint(inside);
			final Point2D p2 = list.getPoint(outside);
			return p1.distance(p2);
		}
	}

	private double bx(double t) {
		double result = 0;
		for (int j = 0; j <= getN(); j++) {
			result += Math.pow(t, j) * cx(j);
		}
		return result;
	}

	private double by(double t) {
		double result = 0;
		for (int j = 0; j <= getN(); j++) {
			result += Math.pow(t, j) * cy(j);
		}
		return result;
	}

	private double cx(int j) {
		double result = 0;
		for (int i = 0; i <= j; i++) {
			result += dx(i, j);
		}
		return result * fact(getN()) / fact(getN() - j);
	}

	private double cy(int j) {
		double result = 0;
		for (int i = 0; i <= j; i++) {
			result += dy(i, j);
		}
		return result * fact(getN()) / fact(getN() - j);
	}

	private double dx(int i, int j) {
		final double result = points.get(i).getX() / fact(i) / fact(j - i);
		if ((i + j) % 2 == 0) {
			return result;
		}
		return -result;
	}

	private double dy(int i, int j) {
		final double result = points.get(i).getY() / fact(i) / fact(j - i);
		if ((i + j) % 2 == 0) {
			return result;
		}
		return -result;
	}

	private long fact(int nb) {
		if (nb < 0) {
			throw new IllegalArgumentException();
		}
		long result = 1;
		for (int i = 2; i <= nb; i++) {
			result *= i;
		}
		return result;
	}

}
