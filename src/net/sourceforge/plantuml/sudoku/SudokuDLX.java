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

import java.util.Random;

public class SudokuDLX implements ISudoku {

	private final String tab[];

	public SudokuDLX(Random rnd) {
		final DLXEngine engine = new DLXEngine(rnd);
		final String s = engine.generate(10000, 100000);
		tab = s.split("\\s");
	}

	public int getGiven(int x, int y) {
		final char c = tab[x].charAt(y);
		if (c == '.') {
			return 0;
		}
		return c - '0';
	}

	public void print() {
		for (String s : tab) {
			System.err.println(s);
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			final SudokuDLX sudoku = new SudokuDLX(new Random());
			sudoku.print();
		}
	}

}
