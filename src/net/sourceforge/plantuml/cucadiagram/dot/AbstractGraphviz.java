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
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.graphic.GraphicStrings;

abstract class AbstractGraphviz implements Graphviz {

	private final File dotExe;
	private final String dotString;

	static boolean isWindows() {
		return File.separatorChar == '\\';
	}

	public static String getenvGraphvizDot() {
		return System.getenv("GRAPHVIZ_DOT");
	}

	AbstractGraphviz(File dotExe, String dotString) {
		this.dotExe = dotExe;
		this.dotString = dotString;
	}

	final public void createPng(OutputStream os) throws IOException, InterruptedException {
		if (dotString == null) {
			throw new IllegalArgumentException();
		}

		if (dotExe == null) {
			createPngNoGraphviz(os);
			return;
		}
		final String cmd = getCommandLine();
		try {
			Log.info("Starting Graphviz process " + cmd);
			Log.info("DotString size: " + dotString.length());
			final ProcessRunner p = new ProcessRunner(cmd);
			p.run(dotString.getBytes(), os);
		} catch (Throwable e) {
			e.printStackTrace();
			Log.error("Error: " + e);
			Log.error("The command was " + cmd);
			Log.error("");
			Log.error("Try java -jar plantuml.jar -testdot to figure out the issue");
			Log.error("");
		} finally {
			Log.info("Ending Graphviz process");

		}
	}

	final public String dotVersion() throws IOException, InterruptedException {
		final String cmd = getCommandLineVersion();
		final ProcessRunner p = new ProcessRunner(cmd);
		p.run(null, null);
		final StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotEmpty(p.getOut())) {
			sb.append(p.getOut());
		}
		if (StringUtils.isNotEmpty(p.getError())) {
			if (sb.length() > 0) {
				sb.append(' ');
			}
			sb.append(p.getError());
		}
		return sb.toString().replace('\n', ' ').trim();
	}

	final private void createPngNoGraphviz(OutputStream os) throws IOException {
		final GraphicStrings errorResult = new GraphicStrings(Arrays
				.asList("Cannot find Graphviz: try 'java -jar plantuml.jar -testdot'"));
		errorResult.writeImage(os);
	}

	abstract String getCommandLine();

	abstract String getCommandLineVersion();

	public final File getDotExe() {
		return dotExe;
	}
}
