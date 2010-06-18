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

import java.awt.Graphics2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.ugraphic.UShape;

public class DotPath implements UShape {

	static class TriPoints {
		public TriPoints(String p1, String p2, String p, double deltaY) {
			final StringTokenizer st1 = new StringTokenizer(p1, ",");
			x1 = Double.parseDouble(st1.nextToken());
			y1 = Double.parseDouble(st1.nextToken()) + deltaY;
			final StringTokenizer st2 = new StringTokenizer(p2, ",");
			x2 = Double.parseDouble(st2.nextToken());
			y2 = Double.parseDouble(st2.nextToken()) + deltaY;
			final StringTokenizer st = new StringTokenizer(p, ",");
			x = Double.parseDouble(st.nextToken());
			y = Double.parseDouble(st.nextToken()) + deltaY;
		}

		private final double x1;
		private final double y1;
		private final double x2;
		private final double y2;
		private final double x;
		private final double y;

		@Override
		public String toString() {
			return "[" + x1 + "," + y1 + " " + x2 + "," + y2 + " " + x + "," + y + "]";
		}
	}

	private final double startX;
	private final double startY;

	private final List<CubicCurve2D.Double> beziers = new ArrayList<CubicCurve2D.Double>();
	private final List<TriPoints> triPoints = new ArrayList<TriPoints>();

	@Override
	public String toString() {
		return "" + startX + "," + startY + " " + triPoints;
	}

	public DotPath(String init, double deltaY) {
		if (init.startsWith("M") == false) {
			throw new IllegalArgumentException();
		}
		final int posC = init.indexOf("C");
		if (posC == -1) {
			throw new IllegalArgumentException();
		}
		final StringTokenizer st = new StringTokenizer(init.substring(1, posC), ",");
		startX = Double.parseDouble(st.nextToken());
		startY = Double.parseDouble(st.nextToken()) + deltaY;

		final StringTokenizer st2 = new StringTokenizer(init.substring(posC + 1), " ");
		while (st2.hasMoreTokens()) {
			final String p1 = st2.nextToken();
			final String p2 = st2.nextToken();
			final String p = st2.nextToken();
			triPoints.add(new TriPoints(p1, p2, p, deltaY));
		}
		computeBez();
	}

	private void computeBez() {
		double x = startX;
		double y = startY;
		for (TriPoints p : triPoints) {
			final CubicCurve2D.Double bezier = new CubicCurve2D.Double(x, y, p.x1, p.y1, p.x2, p.y2, p.x, p.y);
			beziers.add(bezier);
			x = p.x;
			y = p.y;
		}
	}

	public void draw(Graphics2D g2d) {
		for (CubicCurve2D.Double bez : beziers) {
			g2d.draw(bez);
		}
	}

	private CubicCurve2D.Double getCutting(Rectangle2D rect) {
		for (CubicCurve2D.Double bez : beziers) {
			if (isCutting(bez, rect)) {
				return bez;
			}
		}
		throw new IllegalArgumentException();
	}

	private boolean isCutting(CubicCurve2D.Double bez, Rectangle2D rect) {
		final boolean contains1 = rect.contains(bez.x1, bez.y1);
		final boolean contains2 = rect.contains(bez.x2, bez.y2);
		return contains1 ^ contains2;
	}

	private void shorten(CubicCurve2D.Double bez, Rectangle2D rect) {
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
		bez.subdivide(left, right);

		if (isCutting(left, rect) ^ isCutting(right, rect) == false) {
			throw new IllegalArgumentException();
		}

		if (isCutting(left, rect)) {
			bez.setCurve(left);
		} else {
			bez.setCurve(right);
		}

	}

	private double dist(CubicCurve2D.Double seg) {
		return Point2D.distance(seg.x1, seg.y1, seg.x2, seg.y2);
	}

	public Point2D getFrontierIntersection(Rectangle2D rect) {
		final CubicCurve2D.Double bez = new CubicCurve2D.Double();
		bez.setCurve(getCutting(rect));
		while (dist(bez) > 1.0) {
			shorten(bez, rect);
		}
		return new Point2D.Double((bez.x1 + bez.x2) / 2, (bez.y1 + bez.y2) / 2);
	}

	public Point2D getFrontierIntersection(Positionable p) {
		final Point2D pos = p.getPosition();
		final Dimension2D dim = p.getSize();
		return getFrontierIntersection(new Rectangle2D.Double(pos.getX(), pos.getY(), dim.getWidth(), dim.getHeight()));
	}
}
