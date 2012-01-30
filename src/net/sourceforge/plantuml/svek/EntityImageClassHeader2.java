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
import java.util.Arrays;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.CircledCharacter;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockGeneric;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class EntityImageClassHeader2 extends AbstractEntityImage {

	final private HeaderLayout headerLayout;

	public EntityImageClassHeader2(IEntity entity, ISkinParam skinParam, PortionShower portionShower) {
		super(entity, skinParam);

		final boolean italic = entity.getType() == EntityType.ABSTRACT_CLASS
				|| entity.getType() == EntityType.INTERFACE;

		final HtmlColor color = getFontColor(FontParam.CLASS, getStereo());
		final Stereotype stereotype = entity.getStereotype();
		final String generic = entity.getGeneric();
		FontConfiguration fontConfigurationName = new FontConfiguration(getFont(FontParam.CLASS, stereotype), color);
		if (italic) {
			fontConfigurationName = fontConfigurationName.italic();
		}
		final TextBlock name = TextBlockUtils.withMargin(
				TextBlockUtils.create(entity.getDisplay2(), fontConfigurationName, HorizontalAlignement.CENTER), 3, 3,
				0, 0);

		final TextBlock stereo;
		if (stereotype == null || stereotype.getLabel() == null
				|| portionShower.showPortion(EntityPortion.STEREOTYPE, entity) == false) {
			stereo = null;
		} else {
			stereo = TextBlockUtils.withMargin(TextBlockUtils.create(
					stereotype.getLabels(),
					new FontConfiguration(getFont(FontParam.CLASS_STEREOTYPE, stereotype), getFontColor(
							FontParam.CLASS_STEREOTYPE, stereotype)), HorizontalAlignement.CENTER), 1, 0);
		}

		TextBlock genericBlock;
		if (generic == null) {
			genericBlock = null;
		} else {
			genericBlock = TextBlockUtils.create(
					Arrays.asList(generic),
					new FontConfiguration(getFont(FontParam.CLASS_STEREOTYPE, stereotype), getFontColor(
							FontParam.CLASS_STEREOTYPE, stereotype)), HorizontalAlignement.CENTER);
			genericBlock = TextBlockUtils.withMargin(genericBlock, 1, 1);
			final HtmlColor classBackground = getColor(ColorParam.background, stereotype);
			// final HtmlColor classBorder = getColor(ColorParam.classBorder, stereotype);
			final HtmlColor classBorder = getFontColor(FontParam.CLASS_STEREOTYPE, stereotype);
			genericBlock = new TextBlockGeneric(genericBlock, classBackground, classBorder);
			genericBlock = TextBlockUtils.withMargin(genericBlock, 1, 1);
		}

		final TextBlock circledCharacter;
		if (portionShower.showPortion(EntityPortion.CIRCLED_CHARACTER, getEntity())) {
			circledCharacter = TextBlockUtils.withMargin(getCircledCharacter(entity), 4, 0, 5, 5);
		} else {
			circledCharacter = null;
		}
		this.headerLayout = new HeaderLayout(circledCharacter, stereo, name, genericBlock);
	}

	private CircledCharacter getCircledCharacter(IEntity entity) {
		final Stereotype stereotype = entity.getStereotype();
		if (stereotype != null && stereotype.getCharacter() != 0) {
			final HtmlColor classBorder = getColor(ColorParam.classBorder, stereotype);
			final UFont font = getFont(FontParam.CIRCLED_CHARACTER, null);
			return new CircledCharacter(stereotype.getCharacter(), getSkinParam().getCircledCharacterRadius(), font,
					stereotype.getHtmlColor(), classBorder, getFontColor(FontParam.CIRCLED_CHARACTER, null));
		}
		if (entity.getType() == EntityType.ABSTRACT_CLASS) {
			return new CircledCharacter('A', getSkinParam().getCircledCharacterRadius(), getFont(
					FontParam.CIRCLED_CHARACTER, null), getColor(ColorParam.stereotypeABackground, stereotype),
					getColor(ColorParam.classBorder, stereotype), getFontColor(FontParam.CIRCLED_CHARACTER, null));
		}
		if (entity.getType() == EntityType.CLASS) {
			return new CircledCharacter('C', getSkinParam().getCircledCharacterRadius(), getFont(
					FontParam.CIRCLED_CHARACTER, null), getColor(ColorParam.stereotypeCBackground, stereotype),
					getColor(ColorParam.classBorder, stereotype), getFontColor(FontParam.CIRCLED_CHARACTER, null));
		}
		if (entity.getType() == EntityType.INTERFACE) {
			return new CircledCharacter('I', getSkinParam().getCircledCharacterRadius(), getFont(
					FontParam.CIRCLED_CHARACTER, null), getColor(ColorParam.stereotypeIBackground, stereotype),
					getColor(ColorParam.classBorder, stereotype), getFontColor(FontParam.CIRCLED_CHARACTER, null));
		}
		if (entity.getType() == EntityType.ENUM) {
			return new CircledCharacter('E', getSkinParam().getCircledCharacterRadius(), getFont(
					FontParam.CIRCLED_CHARACTER, null), getColor(ColorParam.stereotypeEBackground, stereotype),
					getColor(ColorParam.classBorder, stereotype), getFontColor(FontParam.CIRCLED_CHARACTER, null));
		}
		assert false;
		return null;
	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		return headerLayout.getDimension(stringBounder);
	}

	public void drawU(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
		// final StringBounder stringBounder = ug.getStringBounder();
		// final Dimension2D dimTotal = getDimension(stringBounder);
		// final Dimension2D dimTitle = getTitleDimension(stringBounder);
		//
		// final UGroup header = createHeader(ug);
		// header.drawU(ug, xTheoricalPosition, yTheoricalPosition,
		// dimTotal.getWidth(), dimTitle.getHeight());
		throw new UnsupportedOperationException();
	}

	public void drawU(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition, double width, double height) {
		headerLayout.drawU(ug, xTheoricalPosition, yTheoricalPosition, width, height);
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public int getShield() {
		return 0;
	}

}
