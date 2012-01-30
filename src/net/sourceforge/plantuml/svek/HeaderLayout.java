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

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.MathUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockEmpty;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class HeaderLayout {

	final private TextBlock name;
	final private TextBlock stereo;
	final private TextBlock generic;
	final private TextBlock circledCharacter;

	public HeaderLayout(TextBlock circledCharacter, TextBlock stereo, TextBlock name, TextBlock generic) {
		this.circledCharacter = protectAgaintNull(circledCharacter);
		this.stereo = protectAgaintNull(stereo);
		this.name = protectAgaintNull(name);
		this.generic = protectAgaintNull(generic);
	}

	private static TextBlock protectAgaintNull(TextBlock block) {
		if (block == null) {
			return new TextBlockEmpty();
		}
		return block;
	}

	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D nameDim = name.calculateDimension(stringBounder);
		final Dimension2D genericDim = generic.calculateDimension(stringBounder);
		final Dimension2D stereoDim = stereo.calculateDimension(stringBounder);
		final Dimension2D circleDim = circledCharacter.calculateDimension(stringBounder);
		// final Dimension2D circleDim = getCircleDimension(stringBounder);

		final double width = circleDim.getWidth() + Math.max(stereoDim.getWidth(), nameDim.getWidth())
				+ genericDim.getWidth();
		final double height = MathUtils.max(circleDim.getHeight(), stereoDim.getHeight() + nameDim.getHeight() + 10,
				genericDim.getHeight());
		return new Dimension2DDouble(width, height);
	}

	// private final int xMarginCircle = 5;
	// private final int yMarginCircle = 5;

	public void drawU(UGraphic ug, final double x, final double y, double width, double height) {

		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D nameDim = name.calculateDimension(stringBounder);
		final Dimension2D genericDim = generic.calculateDimension(stringBounder);
		final Dimension2D stereoDim = stereo.calculateDimension(stringBounder);
		final Dimension2D circleDim = circledCharacter.calculateDimension(stringBounder);

		final double widthStereoAndName = Math.max(stereoDim.getWidth(), nameDim.getWidth());
		final double suppWith = width - circleDim.getWidth() - widthStereoAndName - genericDim.getWidth();
		assert suppWith >= 0;

		final double h2 = Math.min(circleDim.getWidth() / 4, suppWith * 0.1);
		// final double h2 = 0;
		final double h1 = (suppWith - h2) / 2;
		assert h1 >= 0;
		assert h2 >= 0;

		final double xCircle = x + h1;
		final double yCircle = y + (height - circleDim.getHeight()) / 2;
		circledCharacter.drawU(ug, xCircle, yCircle);

		final double diffHeight = height - stereoDim.getHeight() - nameDim.getHeight();
		final double xStereo = x + circleDim.getWidth() + (widthStereoAndName - stereoDim.getWidth()) / 2 + h1 + h2;
		final double yStereo = y + diffHeight / 2;
		// final double yStereo = y;
		stereo.drawU(ug, xStereo, yStereo);

		final double xName = x + circleDim.getWidth() + (widthStereoAndName - nameDim.getWidth()) / 2 + h1 + h2;
		final double yName = y + diffHeight / 2 + stereoDim.getHeight();
		// final double yName = y + stereoDim.getHeight();
		name.drawU(ug, xName, yName);

		if (genericDim.getWidth() > 0) {
			final double delta = 4;
			final double xGeneric = x + width - genericDim.getWidth() + delta;
			final double yGeneric = y - delta;
//			ug.getParam().setBackcolor(HtmlColor.WHITE);
//			ug.getParam().setColor(HtmlColor.BLACK);
//			ug.getParam().setStroke(new UStroke(2, 2, 1));
//			ug.draw(xGeneric, yGeneric, new URectangle(genericDim.getWidth(), genericDim.getHeight()));
//			ug.getParam().setStroke(new UStroke());
			generic.drawU(ug, xGeneric, yGeneric);
		}

		// for (Map.Entry<TextBlock, Point2D> ent :
		// placementStrategy.getPositions(width, height).entrySet()) {
		// final TextBlock block = ent.getKey();
		// final Point2D pos = ent.getValue();
		// block.drawU(ug, x + pos.getX(), y + pos.getY());
		// }
	}

	// public void drawU(UGraphic ug, double xTheoricalPosition, double
	// yTheoricalPosition, double width, double height)
	// {
	// final UGroup header = createHeader(ug);
	// header.drawU(ug, xTheoricalPosition, yTheoricalPosition, width, height);
	// }

	// private UGroup createHeader(UGraphic ug) {
	// final UGroup header;
	// if (circledCharacter == null) {
	// header = new UGroup(new PlacementStrategyY1Y2(ug.getStringBounder()));
	// } else {
	// header = new UGroup(new PlacementStrategyX1Y2Y3(ug.getStringBounder()));
	// header.add(circledCharacter);
	// }
	// if (stereo != null) {
	// header.add(stereo);
	// }
	// header.add(name);
	// return header;
	// }

}
