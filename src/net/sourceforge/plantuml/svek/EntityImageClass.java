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
import net.sourceforge.plantuml.MathUtils;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class EntityImageClass extends AbstractEntityImage {

	final private TextBlock methods;
	final private TextBlock fields;
	final private int shield;
	final private EntityImageClassHeader2 header;

	public EntityImageClass(IEntity entity, ISkinParam skinParam, PortionShower portionShower) {
		super(entity, skinParam);

		this.shield = entity.hasNearDecoration() ? 16 : 0;

		final boolean showMethods = portionShower.showPortion(EntityPortion.METHOD, getEntity());
		if (showMethods) {
			this.methods = TextBlockUtils.withMargin(
					entity.getMethodsToDisplay().asTextBlock(FontParam.CLASS_ATTRIBUTE, skinParam), 6, 4);
		} else {
			this.methods = null;
		}

		final boolean showFields = portionShower.showPortion(EntityPortion.FIELD, getEntity());
		if (showFields) {
			this.fields = TextBlockUtils.withMargin(
					entity.getFieldsToDisplay().asTextBlock(FontParam.CLASS_ATTRIBUTE, skinParam), 6, 4);
		} else {
			this.fields = null;
		}

		header = new EntityImageClassHeader2(entity, skinParam, portionShower);

	}

	private int marginEmptyFieldsOrMethod = 13;

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		// final Dimension2D dimTitle = getTitleDimension(stringBounder);
		final Dimension2D dimHeader = header.getDimension(stringBounder);
		final Dimension2D dimMethods = methods == null ? new Dimension2DDouble(0, 0) : methods
				.calculateDimension(stringBounder);
		final Dimension2D dimFields = fields == null ? new Dimension2DDouble(0, 0) : fields
				.calculateDimension(stringBounder);
		final double width = MathUtils.max(dimMethods.getWidth(), dimFields.getWidth(), dimHeader.getWidth());
		final double height = getMethodOrFieldHeight(dimMethods, EntityPortion.METHOD)
				+ getMethodOrFieldHeight(dimFields, EntityPortion.FIELD) + dimHeader.getHeight();
		return new Dimension2DDouble(width, height);
	}

	private double getMethodOrFieldHeight(final Dimension2D dim, EntityPortion portion) {
		if (methods == null && portion == EntityPortion.METHOD) {
			return 0;
		}
		if (fields == null && portion == EntityPortion.FIELD) {
			return 0;
		}
		final double fieldsHeight = dim.getHeight();
		if (fieldsHeight == 0) {
			return marginEmptyFieldsOrMethod;
		}
		return fieldsHeight;
	}

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

		ug.getParam().setColor(getColor(ColorParam.classBorder, getStereo()));
		ug.getParam().setBackcolor(getColor(ColorParam.classBackground, getStereo()));

		double x = xTheoricalPosition;
		double y = yTheoricalPosition;
		ug.getParam().setStroke(new UStroke(1.5));
		ug.draw(x, y, rect);
		ug.getParam().setStroke(new UStroke());

		// final UGroup header = createHeader(ug);
		header.drawU(ug, x, y, dimTotal.getWidth(), dimHeader.getHeight());
		// header.drawU(ug, x, y);

		y += dimHeader.getHeight();

		x = xTheoricalPosition;
		if (fields != null) {
			ug.getParam().setColor(getColor(ColorParam.classBorder, getStereo()));
			ug.getParam().setStroke(new UStroke(1.5));
			ug.draw(x, y, new ULine(widthTotal, 0));
			ug.getParam().setStroke(new UStroke());
			fields.drawU(ug, x, y);
			y += getMethodOrFieldHeight(fields.calculateDimension(stringBounder), EntityPortion.FIELD);
		}

		if (methods != null) {
			ug.getParam().setColor(getColor(ColorParam.classBorder, getStereo()));
			ug.getParam().setStroke(new UStroke(1.5));
			ug.draw(x, y, new ULine(widthTotal, 0));
			ug.getParam().setStroke(new UStroke());
			methods.drawU(ug, x, y);
		}
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public int getShield() {
		return shield;
	}

}
