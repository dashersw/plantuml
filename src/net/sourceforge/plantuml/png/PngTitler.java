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
package net.sourceforge.plantuml.png;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.util.List;

import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.VerticalPosition;

public class PngTitler {

	static public BufferedImage process(BufferedImage im, Color background, Color textColor, List<String> text,
			int fontSize, String fontFamily, HorizontalAlignement horizontalAlignement,
			VerticalPosition verticalPosition) {
		if (text != null && text.size() > 0) {
			im = addTitle(im, background, textColor, text, fontSize, fontFamily, horizontalAlignement, verticalPosition);
		}
		return im;

	}

	static private BufferedImage addTitle(BufferedImage im, Color background, Color textColor, List<String> text,
			int fontSize, String fontFamily, HorizontalAlignement horizontalAlignement,
			VerticalPosition verticalPosition) {

		final Font normalFont = new Font(fontFamily, Font.PLAIN, fontSize);
		final Graphics2D oldg2d = im.createGraphics();
		final TextBlock textBloc = TextBlockUtils.create(text, normalFont, textColor, horizontalAlignement);
		final Dimension2D dimText = textBloc.calculateDimension(oldg2d);
		oldg2d.dispose();

		final double width = Math.max(im.getWidth(), dimText.getWidth());
		final double height = im.getHeight() + dimText.getHeight();

		final BufferedImage newIm = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2d = newIm.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2d.setColor(background);
		g2d.fillRect(0, 0, newIm.getWidth(), newIm.getHeight());
		final double xText;
		if (horizontalAlignement == HorizontalAlignement.LEFT) {
			xText = 2;
		} else if (horizontalAlignement == HorizontalAlignement.RIGHT) {
			xText = width - dimText.getWidth() - 2;
		} else if (horizontalAlignement == HorizontalAlignement.CENTER) {
			xText = (width - dimText.getWidth()) / 2;
		} else {
			xText = 0;
			assert false;
		}

		final int yText;
		final int yImage;

		if (verticalPosition == VerticalPosition.TOP) {
			yText = 0;
			yImage = (int) dimText.getHeight();
		} else {
			yText = im.getHeight();
			yImage = 0;
		}

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		textBloc.draw(g2d, xText, yText);

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		final double delta2 = (width - im.getWidth()) / 2;

		g2d.drawImage(im, (int) delta2, yImage, null);
		g2d.dispose();
		return newIm;

	}
}
