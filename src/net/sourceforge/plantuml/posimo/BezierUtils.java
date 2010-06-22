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

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class BezierUtils {

	static double getEndingAngle(final CubicCurve2D.Double left) {
		if (left.getCtrlP2().equals(left.getP2())) {
			// assert left.getP1().equals(left.getCtrlP1());
			return getAngle(left.getP1(), left.getP2());
		}
		return getAngle(left.getCtrlP2(), left.getP2());
	}

	static double getStartingAngle(final CubicCurve2D.Double left) {
		if (left.getP1().equals(left.getCtrlP1())) {
			// assert left.getCtrlP2().equals(left.getP2());
			return getAngle(left.getP1(), left.getP2());
		}
		return getAngle(left.getP1(), left.getCtrlP1());
	}

	static double getAngle(Point2D p1, Point2D p2) {
		if (p1.equals(p2)) {
			throw new IllegalArgumentException();
		}
		double a = -Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX());
		a = a * 180.0 / Math.PI;
		a -= 90;
		if (a >= 360.0) {
			a -= 360.0;
		}
		if (a < 0.0) {
			a += 360.0;
		}
		return a;
	}

	static boolean isCutting(CubicCurve2D.Double bez, Rectangle2D rect) {
		final boolean contains1 = rect.contains(bez.x1, bez.y1);
		final boolean contains2 = rect.contains(bez.x2, bez.y2);
		return contains1 ^ contains2;
	}

	static void shorten(CubicCurve2D.Double bez, Rectangle2D rect) {
		final boolean contains1 = rect.contains(bez.x1, bez.y1);
		final boolean contains2 = rect.contains(bez.x2, bez.y2);
		if (contains1 ^ contains2 == false) {
			throw new IllegalArgumentException();
		}
		if (contains1 == false) {
			bez.setCurve(bez.x2, bez.y2, bez.ctrlx2, bez.ctrly2, bez.ctrlx1, bez.ctrly1, bez.x1, bez.y1);
		}
		assert rect.contains(bez.x1, bez.y1) && rect.contains(bez.x2, bez.y2) == false;
		final CubicCurve2D.Double left = new CubicCurve2D.Double();
		final CubicCurve2D.Double right = new CubicCurve2D.Double();
		subdivide(bez, left, right, 0.5);

		if (isCutting(left, rect) ^ isCutting(right, rect) == false) {
			throw new IllegalArgumentException();
		}

		if (isCutting(left, rect)) {
			bez.setCurve(left);
		} else {
			bez.setCurve(right);
		}

	}

	public static void subdivide(CubicCurve2D src, CubicCurve2D left, CubicCurve2D right, final double coef) {
		final double coef1 = coef;
		final double coef2 = 1 - coef;
		final double centerxA = src.getCtrlX1() * coef1 + src.getCtrlX2() * coef2;
		final double centeryA = src.getCtrlY1() * coef1 + src.getCtrlY2() * coef2;

		final double ctrlx1 = src.getX1() * coef1 + src.getCtrlX1() * coef1;
		final double ctrly1 = src.getY1() * coef1 + src.getCtrlY1() * coef1;
		final double ctrlx2 = src.getX2() * coef1 + src.getCtrlX2() * coef1;
		final double ctrly2 = src.getY2() * coef1 + src.getCtrlY2() * coef1;

		final double ctrlx12 = ctrlx1 * coef1 + centerxA * coef1;
		final double ctrly12 = ctrly1 * coef1 + centeryA * coef1;
		final double ctrlx21 = ctrlx2 * coef1 + centerxA * coef1;
		final double ctrly21 = ctrly2 * coef1 + centeryA * coef1;
		final double centerxB = ctrlx12 * coef1 + ctrlx21 * coef1;
		final double centeryB = ctrly12 * coef1 + ctrly21 * coef1;
		left.setCurve(src.getX1(), src.getY1(), ctrlx1, ctrly1, ctrlx12, ctrly12, centerxB, centeryB);
		right.setCurve(centerxB, centeryB, ctrlx21, ctrly21, ctrlx2, ctrly2, src.getX2(), src.getY2());
	}

	static double dist(CubicCurve2D.Double seg) {
		return Point2D.distance(seg.x1, seg.y1, seg.x2, seg.y2);
	}

}
