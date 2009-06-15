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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.Log;

public class PngSizer {

	public PngSizer(File pngFile, int minsize, String source) throws IOException {

		if (minsize != Integer.MAX_VALUE) {
			resize(pngFile, minsize, source);
		}

	}

	private void resize(File pngFile, int minsize, String source) throws IOException {
		Log.info("Resizing file to " + minsize);
		Thread.yield();
		final BufferedImage im = ImageIO.read(pngFile);
		Thread.yield();

		if (im.getWidth() >= minsize) {
			return;
		}

		final BufferedImage newIm = new BufferedImage(minsize, im.getHeight(), BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2d = newIm.createGraphics();
		// g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, newIm.getWidth(), newIm.getHeight());
		final int delta = (minsize - im.getWidth()) / 2;
		g2d.drawImage(im, delta, 0, null);

		final File tmp = new File(pngFile.getParentFile(), pngFile.getName() + ".tmp");
		Thread.yield();
		tmp.delete();
		Thread.yield();
		PngIO.write(newIm, tmp, source);
		Thread.yield();

		Thread.yield();
		boolean ok = pngFile.delete();
		Thread.yield();
		if (ok == false) {
			throw new IOException("Cannot delete " + pngFile);
		}
		Thread.yield();
		ok = tmp.renameTo(pngFile);
		Thread.yield();
		if (ok == false) {
			throw new IOException("Cannot rename to " + pngFile);
		}
		g2d.dispose();

	}

}
