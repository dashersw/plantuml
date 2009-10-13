/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques (for Atos Origin).
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
 * Original Author:  Arnaud Roques (for Atos Origin).
 *
 */
package net.sourceforge.plantuml.graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.util.Arrays;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;

class EntityImageDefault extends AbstractEntityImage {

	final private TextBlock textBlock;

	public EntityImageDefault(Entity entity) {
		super(entity);
		this.textBlock = TextBlockUtils.create(Arrays.asList(entity.getDisplay()), getFont14(), Color.BLACK,
				HorizontalAlignement.CENTER);
	}

	public Dimension2D getDimension(Graphics2D g2d) {
		final Dimension2D dim = textBlock.calculateDimension(g2d);
		return new Dimension2DDouble(dim.getWidth(), dim.getHeight());
	}

	public void draw(Graphics2D g2d) {
		final Dimension2D dim = textBlock.calculateDimension(g2d);
		final int width = (int) dim.getWidth();
		final int height = (int) dim.getHeight();
		g2d.setColor(Color.BLACK);
		g2d.drawRect(0, 0, width, height);
		textBlock.draw(g2d, 0, 0);
	}
}
