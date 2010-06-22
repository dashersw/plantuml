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

import java.awt.Color;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

public class EntityImageBlock {

	// private final Entity entity;
	private final ISkinParam param;
	private final Rose rose;
	private final int margin = 6;
	final private TextBlock name;

	public EntityImageBlock(Entity entity, Rose rose, ISkinParam param) {
		// this.entity = entity;
		this.param = param;
		this.rose = rose;
		this.name = TextBlockUtils.create(StringUtils.getWithNewlines(entity.getDisplay()), param
				.getFont(FontParam.CLASS), Color.BLACK, HorizontalAlignement.CENTER);
	}

	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D dim = name.calculateDimension(stringBounder);
		return new Dimension2DDouble(dim.getWidth()+margin*2, dim.getHeight()+margin*2);
	}

	public void drawU(UGraphicG2d ug, double x, double y) {
		final Dimension2D dim = getDimension(ug.getStringBounder());
		ug.getParam().setBackcolor(rose.getHtmlColor(param, ColorParam.classBackground).getColor());
		ug.getParam().setColor(rose.getHtmlColor(param, ColorParam.classBorder).getColor());
		ug.draw(x, y, new URectangle(dim.getWidth(), dim.getHeight()));
				
		name.drawU(ug, x+margin, y+margin);
	}
}
