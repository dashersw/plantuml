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
 * Revision $Revision: 6577 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class TextBlockGeneric implements TextBlock {

	private final TextBlock textBlock;
	private final HtmlColor background;
	private final HtmlColor border;

	public TextBlockGeneric(TextBlock textBlock, HtmlColor background, HtmlColor border) {
		this.textBlock = textBlock;
		this.border = border;
		this.background = background;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim = textBlock.calculateDimension(stringBounder);
		return dim;
	}

	public void drawTOBEREMOVED(ColorMapper colorMapper, Graphics2D g2d, double x, double y) {
		throw new UnsupportedOperationException();
	}

	public void drawU(UGraphic ug, double x, double y) {
		
		ug.getParam().setBackcolor(background);
		ug.getParam().setColor(border);
		ug.getParam().setStroke(new UStroke(2, 2, 1));
		final Dimension2D dim = calculateDimension(ug.getStringBounder());
		ug.draw(x, y, new URectangle(dim.getWidth(), dim.getHeight()));
		ug.getParam().setStroke(new UStroke());

		textBlock.drawU(ug, x, y);
	}

}