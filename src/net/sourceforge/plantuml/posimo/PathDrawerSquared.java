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

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

public class PathDrawerSquared implements PathDrawer {

	private final Rose rose;
	private final ISkinParam param;
	private final LinkType linkType;

	public PathDrawerSquared(Rose rose, ISkinParam param, LinkType linkType) {
		this.rose = rose;
		this.param = param;
		this.linkType = linkType;
	}

	public void drawPath(UGraphicG2d ug, Positionable start, Positionable end, Path path) {
		//drawPath(ug, path.getPoints(), start, end);
		final DotPath dotPath = path.getDotPath().manageRect(start, end);
		ug.draw(0, 0, dotPath);

		final Point2D p1 = path.getDotPath().getFrontierIntersection(start);
		final Point2D p2 = path.getDotPath().getFrontierIntersection(end);
		if (linkType.getDecor1() == LinkDecor.SQUARRE) {
			drawSquare(ug, p1.getX(), p1.getY());
		}
		if (linkType.getDecor2() == LinkDecor.SQUARRE) {
			drawSquare(ug, p2.getX(), p2.getY());
		}
	}

	private void drawSquare(UGraphicG2d ug, double centerX, double centerY) {
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

//	private void drawPath(UGraphic ug, PointList points, Positionable start, Positionable end) {
//		Point2D last = null;
//		final int nb = 10;
//		final double t1 = points.getIntersectionDouble(PositionableUtils.convert(start));
//		final double t2 = points.getIntersectionDouble(PositionableUtils.convert(end));
//		for (int i = 0; i <= nb; i++) {
//			final double d = t1 + (t2 - t1) * i / nb;
//			final Point2D cur = nullIfContained(points.getPoint(d), start, end);
//			if (last != null && cur != null) {
//				ug.draw(last.getX(), last.getY(), new ULine(cur.getX() - last.getX(), cur.getY() - last.getY()));
//			}
//			last = cur;
//		}
//
//		for (Point2D p : points.getPoints()) {
//			ug.draw(p.getX() - 1, p.getY() - 1, new UEllipse(2, 2));
//		}
//	}

}
