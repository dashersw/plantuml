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

import java.io.File;
import java.io.FileFilter;

class GraphvizWindows extends AbstractGraphviz {

	private static File exeOnWindows;
	static {
		final String getenv = getenvGraphvizDot();

		if (getenv == null) {
			final File programFile = new File("c:/Program Files");
			if (programFile.exists()) {
				for (File f : programFile.listFiles(new FileFilter() {
					public boolean accept(File pathname) {
						return pathname.isDirectory() && pathname.getName().startsWith("Graphviz");
					}
				})) {
					final File binDir = new File(f, "bin");
					// exeOnWindows = new File(binDir, "neato.exe");
					exeOnWindows = new File(binDir, "dot.exe");
				}
			}
		} else {
			exeOnWindows = new File(getenv);
		}
	}

	GraphvizWindows(String dotString) {
		super(exeOnWindows, dotString);
	}

	@Override
	String getCommandLine() {
		final StringBuilder sb = new StringBuilder();
		appendDoubleQuoteOnWindows(sb);
		sb.append(getDotExe().getAbsolutePath());
		appendDoubleQuoteOnWindows(sb);
		sb.append(" -Tpng ");
		return sb.toString();
	}

	private static void appendDoubleQuoteOnWindows(final StringBuilder sb) {
		sb.append('\"');
	}

	@Override
	String getCommandLineVersion() {
		final StringBuilder sb = new StringBuilder();
		appendDoubleQuoteOnWindows(sb);
		sb.append(getDotExe().getAbsolutePath());
		appendDoubleQuoteOnWindows(sb);
		sb.append(" -V");
		return sb.toString();
	}

}
