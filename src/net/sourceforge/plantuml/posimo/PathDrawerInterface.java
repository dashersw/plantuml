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
import java.util.Collection;
import java.util.Map;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkStyle;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;

public class PathDrawerInterface implements PathDrawer {

	private final Rose rose;
	private final ISkinParam param;
	private final LinkType linkType;

	public PathDrawerInterface(Rose rose, ISkinParam param, LinkType linkType) {
		this.rose = rose;
		this.param = param;
		this.linkType = linkType;
	}

	public void drawPathBefore(UGraphic ug, Positionable start, Positionable end, Path path) {
		//final DotPath dotPath = path.getDotPath().manageRect(start, end);
		final DotPath dotPath = path.getDotPath();
		ug.draw(0, 0, dotPath);

	}

	public void drawPathAfter(UGraphic ug, Positionable start, Positionable end, Path path) {
		//final DotPath dotPath = path.getDotPath().manageRect(start, end);
		final DotPath dotPath = path.getDotPath();

		final Point2D p1 = dotPath.getFrontierIntersection(start);
		final Point2D p2 = dotPath.getFrontierIntersection(end);
		if (linkType.getDecor1() == LinkDecor.SQUARRE) {
			drawSquare(ug, p1.getX(), p1.getY());
		}
		if (linkType.getDecor2() == LinkDecor.SQUARRE) {
			drawSquare(ug, p2.getX(), p2.getY());
		}

		final LinkStyle style = linkType.getStyle();
		if (style == LinkStyle.INTERFACE_PROVIDER || style == LinkStyle.INTERFACE_USER) {
			final Decor decor = new DecorInterfaceProvider(style);
			final Map<Point2D, Double> all = dotPath.somePoints();
			final Point2D p = getFarest(p1, p2, all.keySet());

			ug.getParam().setBackcolor(rose.getHtmlColor(param, ColorParam.background).getColor());
			ug.getParam().setColor(rose.getHtmlColor(param, ColorParam.classBorder).getColor());

			decor.drawDecor(ug, p, all.get(p));
		}
	}

	private static Point2D getFarest(Point2D p1, Point2D p2, Collection<Point2D> all) {
		Point2D result = null;
		double farest = 0;
		for (Point2D p : all) {
			if (result == null) {
				result = p;
				farest = p1.distanceSq(result) + p2.distanceSq(result);
				continue;
			}
			final double candidat = p1.distanceSq(p) + p2.distanceSq(p);
			if (candidat < farest) {
				result = p;
				farest = candidat;
			}
		}
		if (result == null) {
			throw new IllegalArgumentException();
		}
		return result;
	}

	private void drawSquare(UGraphic ug, double centerX, double centerY) {
		ug.getParam().setBackcolor(rose.getHtmlColor(param, ColorParam.classBackground).getColor());
		ug.getParam().setColor(rose.getHtmlColor(param, ColorParam.classBorder).getColor());
		final double width = 10;
		final double height = 10;
		ug.draw(centerX - width / 2, centerY - height / 2, new URectangle(width, height));
	}

	private Point2D nullIfContained(Point2D p, Positionable start, Positionable end) {
		if (PositionableUtils.contains(start, p)) {
			return null;
		}
		if (PositionableUtils.contains(end, p)) {
			return null;
		}
		return p;
	}

	// private void drawPath(UGraphic ug, PointList points, Positionable start,
	// Positionable end) {
	// Decor decor = new DecorInterfaceProvider();
	// Point2D last = null;
	// final int nb = 10;
	// final double t1 =
	// points.getIntersectionDouble(PositionableUtils.convert(start));
	// final double t2 =
	// points.getIntersectionDouble(PositionableUtils.convert(end));
	// for (int i = 0; i <= nb; i++) {
	// final double d = t1 + (t2 - t1) * i / nb;
	// final Point2D cur = nullIfContained(points.getPoint(d), start, end);
	// if (last != null && cur != null) {
	// ug.draw(last.getX(), last.getY(), new ULine(cur.getX() - last.getX(),
	// cur.getY() - last.getY()));
	// if (decor != null) {
	// decor.drawLine(ug, last, cur);
	// decor = null;
	// }
	// }
	// last = cur;
	// }
	//
	// for (Point2D p : points.getPoints()) {
	// ug.draw(p.getX() - 1, p.getY() - 1, new UEllipse(2, 2));
	// }
	// }

}
