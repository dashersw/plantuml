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
 * Revision $Revision: 3977 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.Log;

public class DrawFile {

	private final File png;
	private final String svg;
	private final File eps;

	private int widthPng = -1;
	private int heightPng = -1;

	public DrawFile(File png) {
		this(png, null, null);
	}

	public DrawFile(File png, String svg) {
		this(png, svg, null);
	}

	public DrawFile(File png, String svg, File eps) {
		this.png = png;
		this.svg = svg;
		this.eps = eps;
	}

	public File getPngOrEps(boolean isEps) {
		if (isEps) {
			if (eps == null) {
				throw new UnsupportedOperationException();
			}
			return eps;
		} else {
			return png;
		}
	}

	public File getPng() {
		return png;
	}

	public String getSvg() {
		return svg;
	}

	public File getEps() {
		return eps;
	}

	private void initSize() throws IOException {
		final BufferedImage im = ImageIO.read(png);
		widthPng = im.getWidth();
		heightPng = im.getHeight();
	}

	public void delete() {
		Thread.yield();
		Log.info("Deleting temporary file " + png);
		final boolean ok = png.delete();
		if (ok == false) {
			Log.error("Cannot delete: " + png);
		}
		if (eps != null) {
			Log.info("Deleting temporary file " + eps);
			final boolean ok2 = eps.delete();
			if (ok2 == false) {
				Log.error("Cannot delete: " + eps);
			}

		}
	}

	public final int getWidthPng() throws IOException {
		if (widthPng == -1) {
			initSize();
		}
		return widthPng;
	}

	public final int getHeightPng() throws IOException {
		if (widthPng == -1) {
			initSize();
		}
		return heightPng;
	}

	@Override
	public String toString() {
		if (svg == null) {
			return png.toString();
		}
		return png + " " + svg.length();
	}

}
