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

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class TextBlockLineBefore implements TextBlockWidth {

	private final TextBlockWidth textBlock;
	private final char separator;
	private final TextBlock title;

	public TextBlockLineBefore(TextBlockWidth textBlock, char separator, TextBlock title) {
		this.textBlock = textBlock;
		this.separator = separator;
		this.title = title;
	}

	public TextBlockLineBefore(TextBlockWidth textBlock, char separator) {
		this(textBlock, separator, null);
	}

	public TextBlockLineBefore(TextBlockWidth textBlock) {
		this(textBlock, '_');
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim = textBlock.calculateDimension(stringBounder);
		if (title != null) {
			final Dimension2D dimTitle = title.calculateDimension(stringBounder);
			return Dimension2DDouble.atLeast(dim, dimTitle.getWidth() + 8, dimTitle.getHeight());
		}
		return dim;
	}

	public void drawU(UGraphic ug, double x, double y, double widthToUse) {
		final HtmlColor color = ug.getParam().getColor();
		if (title == null) {
			drawLine(ug, x, y, widthToUse);
		}
		textBlock.drawU(ug, x, y, widthToUse);
		ug.getParam().setColor(color);
		if (title != null) {
			final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
			final double space = (widthToUse - dimTitle.getWidth()) / 2;
			drawLine(ug, x, y, space - 1);
			title.drawU(ug, x + space, y - dimTitle.getHeight() / 2 - 0.5);
			ug.getParam().setColor(color);
			drawLine(ug, x + widthToUse - space + 1, y, space - 1);
		}
	}

	private void drawLine(UGraphic ug, double x, double y, double widthToUse) {
		if (separator == '=') {
			ug.draw(x, y, new ULine(widthToUse, 0));
			ug.draw(x, y + 2, new ULine(widthToUse, 0));
		} else if (separator == '.') {
			ug.getParam().setStroke(new UStroke(1, 2, 1));
			ug.draw(x, y, new ULine(widthToUse, 0));
			ug.getParam().setStroke(new UStroke());
		} else if (separator == '-') {
			ug.draw(x, y, new ULine(widthToUse, 0));
		} else {
			ug.getParam().setStroke(new UStroke(1.5));
			ug.draw(x, y, new ULine(widthToUse, 0));
			ug.getParam().setStroke(new UStroke());
		}
	}

}