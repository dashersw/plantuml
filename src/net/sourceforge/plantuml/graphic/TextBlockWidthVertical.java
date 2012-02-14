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
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class TextBlockWidthVertical implements TextBlockWidth {

	private final List<TextBlockWidth> blocks = new ArrayList<TextBlockWidth>();

	public TextBlockWidthVertical(TextBlockWidth b1, TextBlockWidth b2) {
		this.blocks.add(b1);
		this.blocks.add(b2);
	}

	public TextBlockWidthVertical(List<TextBlockWidth> all) {
		if (all.size() < 2) {
			throw new IllegalArgumentException();
		}
		this.blocks.addAll(all);
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		Dimension2D dim = blocks.get(0).calculateDimension(stringBounder);
		for (int i = 1; i < blocks.size(); i++) {
			dim = Dimension2DDouble.mergeTB(dim, blocks.get(i).calculateDimension(stringBounder));
		}
		return dim;
	}

	public void drawU(UGraphic ug, double x, double y, double widthToUse) {
		for (TextBlockWidth b : blocks) {
			b.drawU(ug, x, y, widthToUse);
			final Dimension2D dim = b.calculateDimension(ug.getStringBounder());
			y += dim.getHeight();
		}

	}

}