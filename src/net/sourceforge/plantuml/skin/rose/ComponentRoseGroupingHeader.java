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
package net.sourceforge.plantuml.skin.rose;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Dimension2D;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;

public class ComponentRoseGroupingHeader extends AbstractTextualComponent {

	private final int cornersize = 10;
	private final int commentMargin = 0; // 8;

	private final TextBlock commentTextBlock;

	private final Color background = new Color(Integer.parseInt("EEEEEE", 16));

	public ComponentRoseGroupingHeader(Color fontColor, Font bigFont, Font smallFont, List<? extends CharSequence> strings) {
		super(strings.get(0), fontColor, bigFont, HorizontalAlignement.LEFT, 15, 30, 1);
		if (strings.size() == 1 || strings.get(1) == null) {
			this.commentTextBlock = null;
		} else {
			this.commentTextBlock = TextBlockUtils.create(Arrays.asList("[" + strings.get(1) + "]"), smallFont, fontColor, HorizontalAlignement.LEFT);
		}
	}

	@Override
	public double getPaddingY() {
		return 6;
	}

	@Override
	final public double getPreferredWidth(Graphics2D g2d) {
		final double sup;
		if (commentTextBlock == null) {
			sup = commentMargin * 2;
		} else {
			final Dimension2D size = commentTextBlock.calculateDimension(g2d);
			sup = getMarginX1() + commentMargin + size.getWidth();

		}
		return getTextWidth(g2d) + sup;
	}

	@Override
	final public double getPreferredHeight(Graphics2D g2d) {
		return getTextHeight(g2d) + 2 * getPaddingY();
	}

	@Override
	protected void drawInternal(Graphics2D g2d, Dimension2D dimensionToUse) {
		final int textWidth = (int) getTextWidth(g2d);
		final int textHeight = (int) getTextHeight(g2d);

		final Polygon polygon = new Polygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(textWidth, 0);

		polygon.addPoint(textWidth, textHeight - cornersize);
		polygon.addPoint(textWidth - cornersize, textHeight);

		polygon.addPoint(0, textHeight);
		polygon.addPoint(0, 0);

		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(this.background);
		g2d.fill(polygon);
		g2d.setColor(getFontColor());
		g2d.draw(polygon);
		g2d.drawLine(0, 0, (int) (dimensionToUse.getWidth()), 0);
		g2d.drawLine((int) dimensionToUse.getWidth(), 0, (int) dimensionToUse.getWidth(), textHeight);
		g2d.setStroke(new BasicStroke());

		getTextBlock().draw(g2d, getMarginX1(), getMarginY());

		if (commentTextBlock != null) {
			final Dimension2D size = commentTextBlock.calculateDimension(g2d);
			g2d.setColor(Color.WHITE);
			final int x1 = getMarginX1() + textWidth;
			final int y2 = getMarginY() + 1;
			g2d.fillRect(x1, y2, (int) size.getWidth() + 2 * commentMargin, (int) size.getHeight());

			commentTextBlock.draw(g2d, x1 + commentMargin, y2);
		}

	}

}
