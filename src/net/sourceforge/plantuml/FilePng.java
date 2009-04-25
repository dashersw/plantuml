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

import java.io.File;

public class FilePng implements Comparable<FilePng> {

	private final File sourceFile;
	private final File pngFile;

	public FilePng(File sourceFile, File pngFile) {
		if (sourceFile == null || pngFile == null) {
			throw new IllegalArgumentException();
		}
		this.sourceFile = sourceFile;
		this.pngFile = pngFile;
	}

	public File getSourceFile() {
		return sourceFile;
	}

	public File getPngFile() {
		return pngFile;
	}

	@Override
	public String toString() {
		return sourceFile.getAbsolutePath() + " " + pngFile.getAbsolutePath();
	}

	public int compareTo(FilePng this2) {
		final int cmp = this.sourceFile.compareTo(this2.sourceFile);
		if (cmp != 0) {
			return cmp;
		}
		return this.pngFile.compareTo(this2.pngFile);
	}

	@Override
	public int hashCode() {
		return sourceFile.hashCode() + pngFile.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		final FilePng this2 = (FilePng) obj;
		return this2.sourceFile.equals(this.sourceFile) && this2.pngFile.equals(this.pngFile);
	}

}
