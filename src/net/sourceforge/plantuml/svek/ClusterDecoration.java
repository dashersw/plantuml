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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class ClusterDecoration {

	final private PackageStyle style;
	final private TextBlock title;
	final private HtmlColor stateBack;

	final private double minX;
	final private double minY;
	final private double maxX;
	final private double maxY;

	public ClusterDecoration(PackageStyle style, TextBlock title, HtmlColor stateBack, double minX, double minY,
			double maxX, double maxY) {
		this.style = style;
		this.title = title;
		this.stateBack = stateBack;
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}

	public void drawU(UGraphic ug, double x, double y, HtmlColor borderColor, boolean shadowing) {
		if (style == PackageStyle.RECT) {
			drawWithTitleRect(ug, x, y, borderColor, shadowing);
		} else {
			drawWithTitle(ug, x, y, borderColor, shadowing);
		}

	}

	private UPolygon getSpecificFrontier(StringBounder stringBounder) {
		final double width = maxX - minX;
		final double height = maxY - minY;
		final Dimension2D dimTitle = title.calculateDimension(stringBounder);
		final double wtitle = dimTitle.getWidth() + marginTitleX1 + marginTitleX2;
		final double htitle = dimTitle.getHeight() + marginTitleY1 + marginTitleY2;
		final UPolygon shape = new UPolygon();
		shape.addPoint(0, 0);
		shape.addPoint(wtitle, 0);
		shape.addPoint(wtitle + marginTitleX3, htitle);
		shape.addPoint(width, htitle);
		shape.addPoint(width, height);
		shape.addPoint(0, height);
		shape.addPoint(0, 0);
		return shape;
	}

	private void drawWithTitle(UGraphic ug, double x, double y, HtmlColor borderColor, boolean shadowing) {
		final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
		final double wtitle = dimTitle.getWidth() + marginTitleX1 + marginTitleX2;
		final double htitle = dimTitle.getHeight() + marginTitleY1 + marginTitleY2;
		final UPolygon shape = getSpecificFrontier(ug.getStringBounder());
		if (shadowing) {
			shape.setDeltaShadow(3.0);
		}

		ug.getParam().setBackcolor(stateBack);
		ug.getParam().setColor(borderColor);
		ug.getParam().setStroke(new UStroke(2));
		ug.draw(x + minX, y + minY, shape);
		ug.draw(x + minX, y + minY + htitle, new ULine(wtitle + marginTitleX3, 0));
		ug.getParam().setStroke(new UStroke());
		title.drawU(ug, x + minX + marginTitleX1, y + minY + marginTitleY1);
	}

	private void drawWithTitleRect(UGraphic ug, double x, double y, HtmlColor borderColor, boolean shadowing) {
		final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
		final double width = maxX - minX;
		final double height = maxY - minY;
		final URectangle shape = new URectangle(width, height);
		if (shadowing) {
			shape.setDeltaShadow(3.0);
		}

		ug.getParam().setBackcolor(stateBack);
		ug.getParam().setColor(borderColor);
		ug.getParam().setStroke(new UStroke(2));

		ug.draw(x + minX, y + minY, shape);
		ug.getParam().setStroke(new UStroke());
		final double deltax = width - dimTitle.getWidth();
		title.drawU(ug, x + minX + deltax / 2, y + minY + 5);
	}

	public final static int marginTitleX1 = 3;
	public final static int marginTitleX2 = 3;
	public final static int marginTitleX3 = 7;
	public final static int marginTitleY0 = 0;
	public final static int marginTitleY1 = 3;
	public final static int marginTitleY2 = 3;

	static HtmlColor getStateBackColor(HtmlColor stateBack, ISkinParam skinParam, String stereotype) {
		if (stateBack == null) {
			stateBack = skinParam.getHtmlColor(ColorParam.packageBackground, stereotype);
		}
		if (stateBack == null) {
			stateBack = skinParam.getHtmlColor(ColorParam.background, stereotype);
		}
		if (stateBack == null) {
			stateBack = HtmlColor.WHITE;
		}
		return stateBack;
	}

}
