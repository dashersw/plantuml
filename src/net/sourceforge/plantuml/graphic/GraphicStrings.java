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
package net.sourceforge.plantuml.graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.png.PngIO;

public class GraphicStrings {

	private final Color background;

	private final Font font;

	private final Color green;

	private final List<String> strings;

	public GraphicStrings(List<String> strings) {
		this(strings, new Font("SansSerif", Font.BOLD, 14), new Color(Integer.parseInt("33FF02", 16)), Color.BLACK);
	}

	public GraphicStrings(List<String> strings, Font font, Color green, Color background) {
		this.strings = strings;
		this.font = font;
		this.green = green;
		this.background = background;
	}

	public void writeImage(OutputStream os) throws IOException {
		final BufferedImage im = createImage();
		PngIO.write(im, os);
	}

	private BufferedImage createImage() {
		final EmptyImageBuilder builder = new EmptyImageBuilder(640, 400, background);
		BufferedImage im = builder.getBufferedImage();
		final Graphics2D g2d = builder.getGraphics2D();
		final Dimension2D size = draw(g2d);
		im = im.getSubimage(0, 0, (int) size.getWidth(), (int) size.getHeight());
		g2d.dispose();
		return im;
	}

	public Dimension2D draw(final Graphics2D g2d) {
		final TextBlock textBlock = TextBlockUtils.create(strings, font, green, HorizontalAlignement.LEFT);
		final Dimension2D size = textBlock.calculateDimension(g2d);
		textBlock.draw(g2d, 0, 0);
		return size;
	}

}
