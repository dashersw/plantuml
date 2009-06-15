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

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.Log;

public class PngRotation {

	public PngRotation(File pngFile, String source) throws IOException {

		Log.info("Rotation for " + pngFile);
		Thread.yield();
		final BufferedImage im = ImageIO.read(pngFile);
		Thread.yield();

		final BufferedImage newIm = new BufferedImage(im.getHeight(), im.getWidth(), BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2d = newIm.createGraphics();
		
		final AffineTransform at = new AffineTransform(0, 1, 1, 0, 0, 0);
		at.concatenate(new AffineTransform(-1, 0, 0, 1, im.getWidth(), 0));
		g2d.setTransform(at);

		g2d.drawImage(im, 0, 0, null);
		g2d.dispose();

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

	}

}
