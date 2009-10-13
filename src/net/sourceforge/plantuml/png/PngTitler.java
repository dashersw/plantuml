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
import java.io.IOException;
import java.util.List;

import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;

public class PngTitler {

	static private final Font normalFont = new Font("SansSerif", Font.PLAIN, 18);

	static public BufferedImage process(BufferedImage im, Color color, List<String> titles) throws IOException {
		if (titles != null && titles.size() > 0) {
			im = addTitle(im, color, titles);
		}
		return im;

	}

	static private BufferedImage addTitle(BufferedImage im, Color color, List<String> titles) throws IOException {

		final Graphics2D oldg2d = im.createGraphics();
		final TextBlock title = TextBlockUtils.create(titles, normalFont, color, HorizontalAlignement.CENTER);
		final Dimension2D dimTitle = title.calculateDimension(oldg2d);
		oldg2d.dispose();

		final double width = Math.max(im.getWidth(), dimTitle.getWidth());
		final double height = im.getHeight() + dimTitle.getHeight();

		final BufferedImage newIm = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2d = newIm.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, newIm.getWidth(), newIm.getHeight());
		double delta = (width - dimTitle.getWidth()) / 2;
		title.draw(g2d, delta, 0);

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		delta = (width - im.getWidth()) / 2;
		g2d.drawImage(im, (int) delta, (int) dimTitle.getHeight(), null);
		g2d.dispose();
		return newIm;

	}
}
