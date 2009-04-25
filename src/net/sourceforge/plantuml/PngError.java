/* ========================================================================
 * Plantuml : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques (for Atos Origin).
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of Plantuml.
 *
 * Plantuml is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Plantuml distributed in the hope that it will be useful, but
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
package net.sourceforge.plantuml;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.graphic.TextBlock;

public class PngError {

	private static final Font FONT1 = new Font("SansSerif", Font.BOLD, 14);
	
	private static final Color green = new Color(Integer.parseInt("33FF02", 16));


	private final List<String> strings;

	public PngError(List<String> strings) {
		this.strings = strings;
	}

	public void writeError(File png) throws IOException {
		
		final EmptyImageBuilder builder = new EmptyImageBuilder(640, 400, Color.BLACK);
		
		BufferedImage im = builder.getBufferedImage();
		final Graphics2D g2d = builder.getGraphics2D();
		final TextBlock textBlock = new TextBlock(strings, FONT1, green);
		final Dimension2D size = textBlock.calculateDimension(g2d);
		textBlock.draw(g2d, 0, 0);
		im = im.getSubimage(0, 0, (int) size.getWidth(), (int) size.getHeight());

		ImageIO.write(im, "png", png);
		g2d.dispose();
	}

}
