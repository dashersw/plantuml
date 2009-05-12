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
import java.io.IOException;
import java.util.Arrays;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.PngError;
import net.sourceforge.plantuml.StringUtils;

public class Graphviz {

	private static File dotExe;
	static {
		final String getenv = getenvGraphvizDot();

		if (getenv == null) {
			if (isWindows()) {
				final File programFile = new File("c:/Program Files");
				if (programFile.exists()) {
					for (File f : programFile.listFiles(new FileFilter() {
						public boolean accept(File pathname) {
							return pathname.isDirectory() && pathname.getName().startsWith("Graphviz");
						}
					})) {
						final File binDir = new File(f, "bin");
						dotExe = new File(binDir, "dot.exe");
					}
				}
			} else {
				dotExe = new File("/usr/bin/dot");
			}
		} else {
			dotExe = new File(getenv);
		}
	}

	private static boolean isWindows() {
		return File.separatorChar == '\\';
	}

	public static String getenvGraphvizDot() {
		return System.getenv("GRAPHVIZ_DOT");
	}

	static public File getDotExe() {
		return dotExe;
	}

	private final File dotFile;

	public Graphviz(File dotFile) {
		this.dotFile = dotFile;
	}

	public void createPng(File pngFile) throws IOException, InterruptedException {
		if (dotExe == null) {
			createPngNoGraphviz(pngFile);
			return;
		}
		final String cmd = getCommandLine(pngFile);
		Log.debug(cmd);
		try {
			final Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
		} catch (Throwable e) {
			e.printStackTrace();
			Log.error("Error: " + e);
			Log.error("The command was " + cmd);
			Log.error("");
			Log.error("Try java -jar plantuml.jar -testdot to figure out the issue");
			Log.error("");
		}
		if (pngFile.exists() == false) {
			Log.error("Error: The file .png " + pngFile + " was not created by dot.");
			Log.error("The command was " + cmd);
			Log.error("");
			Log.error("Try java -jar plantuml.jar -testdot to figure out the issue");
			Log.error("");
		}
	}

	public void createPositionFile(File positionFile) throws IOException, InterruptedException {
		final String cmd2 = getCommandLineDotFile(positionFile);
		final Process process2 = Runtime.getRuntime().exec(cmd2);
		process2.waitFor();
	}

	public static String dotVersion() throws IOException, InterruptedException {
		final String cmd = getCommandLineVersion();
		final ProcessRunner p = new ProcessRunner(cmd);
		p.run();
		final StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotEmpty(p.getInput())) {
			sb.append(p.getInput());
		}
		if (StringUtils.isNotEmpty(p.getError())) {
			if (sb.length() > 0) {
				sb.append(' ');
			}
			sb.append(p.getError());
		}
		return sb.toString().replace('\n', ' ').trim();
	}

	private void createPngNoGraphviz(File pngFile) throws IOException {
		final PngError errorResult = new PngError(Arrays
				.asList("Cannot find Graphviz: try 'java -jar plantuml.jar -testdot'"));
		errorResult.writeError(pngFile);
	}

	static String getCommandLineVersion() {
		final StringBuilder sb = new StringBuilder();
		appendDoubleQuoteOnWindows(sb);
		sb.append(dotExe.getAbsolutePath());
		appendDoubleQuoteOnWindows(sb);
		sb.append(" -V");
		return sb.toString();
	}

	String getCommandLine(File pngFile) {
		final StringBuilder sb = new StringBuilder();
		appendDoubleQuoteOnWindows(sb);
		sb.append(dotExe.getAbsolutePath());
		appendDoubleQuoteOnWindows(sb);
		sb.append(" -Tpng ");
		appendDoubleQuoteOnWindows(sb);
		sb.append(dotFile.getAbsolutePath());
		appendDoubleQuoteOnWindows(sb);
		sb.append(" -o ");
		appendDoubleQuoteOnWindows(sb);
		sb.append(pngFile.getAbsolutePath());
		appendDoubleQuoteOnWindows(sb);
		return sb.toString();
	}

	private static void appendDoubleQuoteOnWindows(final StringBuilder sb) {
		if (isWindows()) {
			sb.append('\"');
		}
	}

	private String getCommandLineDotFile(File out) {
		final StringBuilder sb = new StringBuilder();
		appendDoubleQuoteOnWindows(sb);
		sb.append(dotExe.getAbsolutePath());
		appendDoubleQuoteOnWindows(sb);
		sb.append(" -Tdot ");
		appendDoubleQuoteOnWindows(sb);
		sb.append(dotFile.getAbsolutePath());
		appendDoubleQuoteOnWindows(sb);
		sb.append(" -o ");
		appendDoubleQuoteOnWindows(sb);
		sb.append(out.getAbsolutePath());
		appendDoubleQuoteOnWindows(sb);
		return sb.toString();
	}
}
