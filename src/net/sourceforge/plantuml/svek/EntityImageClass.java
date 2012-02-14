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
 * Revision $Revision: 5183 $
 *
 */
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlockWidth;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class EntityImageClass extends AbstractEntityImage {

	// final private TextBlockWidth methods2;
	// final private TextBlockWidth fields2;
	final private TextBlockWidth body;
	final private int shield;
	final private EntityImageClassHeader2 header;

	// final private IEntity entity;
	// final private ISkinParam skinParam;
	// final private PortionShower portionShower;
	// final private boolean showMethods;
	// final private boolean showFields;

	public EntityImageClass(IEntity entity, ISkinParam skinParam, PortionShower portionShower) {
		super(entity, skinParam);

		this.shield = entity.hasNearDecoration() ? 16 : 0;
		// this.entity = entity;
		// this.skinParam = skinParam;
		// this.portionShower = portionShower;
		//
		// showMethods = portionShower.showPortion(EntityPortion.METHOD, getEntity());
		// showFields = portionShower.showPortion(EntityPortion.FIELD, getEntity());
		//
		// if (showMethods) {
		// this.methods2 = entity.getMethodsToDisplay().asTextBlock(FontParam.CLASS_ATTRIBUTE, skinParam);
		// } else {
		// this.methods2 = null;
		// }
		//
		// if (showFields) {
		// this.fields2 = entity.getFieldsToDisplay().asTextBlock(FontParam.CLASS_ATTRIBUTE, skinParam);
		// } else {
		// this.fields2 = null;
		// }

		this.body = entity.getBody(portionShower).asTextBlock(FontParam.CLASS_ATTRIBUTE, skinParam);

		// if (showFields && showMethods) {
		// this.body = new TextBlockVertical(fields, methods);
		// } else if (showFields) {
		// this.body = fields;
		// } else if (showMethods) {
		// this.body = methods;
		// } else {
		// this.body = null;
		// }

		header = new EntityImageClassHeader2(entity, skinParam, portionShower);

	}

	// private int marginEmptyFieldsOrMethod = 13;

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		// final Dimension2D dimTitle = getTitleDimension(stringBounder);
		final Dimension2D dimHeader = header.getDimension(stringBounder);
		final Dimension2D dimBody = body == null ? new Dimension2DDouble(0, 0) : body.calculateDimension(stringBounder);
		final double width = Math.max(dimBody.getWidth(), dimHeader.getWidth());
		final double height = dimBody.getHeight() + dimHeader.getHeight();
		return new Dimension2DDouble(width, height);
	}

	// private double getMethodOrFieldHeight(final Dimension2D dim, EntityPortion portion) {
	// if (methods2 == null && portion == EntityPortion.METHOD) {
	// return 0;
	// }
	// if (fields2 == null && portion == EntityPortion.FIELD) {
	// return 0;
	// }
	// final double fieldsHeight = dim.getHeight();
	// if (fieldsHeight == 0) {
	// return marginEmptyFieldsOrMethod;
	// }
	// return fieldsHeight;
	// }

	public void drawU(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = getDimension(stringBounder);
		final Dimension2D dimHeader = header.getDimension(stringBounder);

		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();
		// final URectangle rect = new URectangle(widthTotal, heightTotal);
		final Shadowable rect = new URectangle(widthTotal, heightTotal);
		if (getSkinParam().shadowing()) {
			rect.setDeltaShadow(4);
		}

		final HtmlColor classBorder = getColor(ColorParam.classBorder, getStereo());
		ug.getParam().setColor(classBorder);
		ug.getParam().setBackcolor(getColor(ColorParam.classBackground, getStereo()));

		double x = xTheoricalPosition;
		double y = yTheoricalPosition;
		ug.getParam().setStroke(new UStroke(1.5));
		ug.draw(x, y, rect);
		ug.getParam().setStroke(new UStroke());

		header.drawU(ug, x, y, dimTotal.getWidth(), dimHeader.getHeight());

		y += dimHeader.getHeight();

		x = xTheoricalPosition;
		// if (fields2 != null) {
		// ug.getParam().setColor(classBorder);
		// fields2.drawU(ug, x, y, widthTotal);
		// y += getMethodOrFieldHeight(fields2.calculateDimension(stringBounder), EntityPortion.FIELD);
		// }
		//
		// if (methods2 != null) {
		// ug.getParam().setColor(classBorder);
		// methods2.drawU(ug, x, y, widthTotal);
		// }
		if (body != null) {
			ug.getParam().setColor(classBorder);
			body.drawU(ug, x, y, widthTotal);
		}
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public int getShield() {
		return shield;
	}

}
