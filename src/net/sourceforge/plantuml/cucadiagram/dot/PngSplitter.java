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
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.sequencediagram.graphic.SequenceDiagramPngMaker;

class PngSplitter {

	private final List<File> files = new ArrayList<File>();

	public PngSplitter(File pngFile, int horizontalPages, int verticalPages) throws IOException {
		if (horizontalPages == 1 && verticalPages == 1) {
			this.files.add(pngFile);
			return;
		}

		final File full = new File(pngFile.getParentFile(), pngFile.getName() + ".tmp");
		full.delete();
		final boolean ok = pngFile.renameTo(full);
		if (ok == false) {
			throw new IOException("Cannot rename");
		}

		final BufferedImage im = ImageIO.read(full);
		final PngSegment horizontalSegment = new PngSegment(im.getWidth(), horizontalPages);
		final PngSegment verticalSegment = new PngSegment(im.getHeight(), verticalPages);

		int x = 0;
		for (int i = 0; i < horizontalPages; i++) {
			for (int j = 0; j < verticalPages; j++) {
				final File f = SequenceDiagramPngMaker.computeFilename(pngFile, x++);
				this.files.add(f);
				final BufferedImage imPiece = im.getSubimage(horizontalSegment.getStart(i),
						verticalSegment.getStart(j), horizontalSegment.getLen(i), verticalSegment.getLen(j));
				ImageIO.write(imPiece, "png", f);
			}
		}
		
		full.delete();
	}

	public List<File> getFiles() {
		return Collections.unmodifiableList(files);
	}

}
