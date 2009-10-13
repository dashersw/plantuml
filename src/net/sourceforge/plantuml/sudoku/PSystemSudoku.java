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
package net.sourceforge.plantuml.sudoku;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;

public class PSystemSudoku extends AbstractPSystem {

	final private ISudoku sudoku;

	public List<File> createPng(File pngFile) throws IOException, InterruptedException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(pngFile);
			new GraphicsSudoku(sudoku).writeImage(os);
		} finally {
			if (os != null) {
				os.close();
			}
		}
		return Arrays.asList(pngFile);
	}

	public void createPng(OutputStream os) throws IOException {
		new GraphicsSudoku(sudoku).writeImage(os);
	}

	public String getDescription() {
		return "(Sudoku)";
	}

	public PSystemSudoku(Long seed) {
		sudoku = new SudokuDLX(seed);
	}

}
