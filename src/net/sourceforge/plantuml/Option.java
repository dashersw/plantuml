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
package net.sourceforge.plantuml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import net.sourceforge.plantuml.cucadiagram.dot.Graphviz;

public class Option {

	private static final Option singleton = new Option();

	private boolean keepTmpFiles = false;
	private boolean verbose = false;

	private Option() {
		reset();
	}

	public static Option getInstance() {
		return singleton;
	}

	public List<String> manageOption(String arg[]) {
		final List<String> result = new ArrayList<String>();
		for (String s : arg) {
			if (s.equalsIgnoreCase("-verbose") || s.equalsIgnoreCase("-v")) {
				verbose = true;
			} else if (s.equalsIgnoreCase("-keepfiles") || s.equalsIgnoreCase("-keepfile")) {
				keepTmpFiles = true;
			} else if (s.equalsIgnoreCase("-version")) {
				displayVersion();
			} else if (s.equalsIgnoreCase("-testdot")) {
				testDot();
			} else if (s.equalsIgnoreCase("-help") || s.equalsIgnoreCase("-h")) {
				System.err.println("Usage: java -jar plantuml.jar [options]");
				System.err.println("\t(to execute the GUI)");
				System.err.println("    or java -jar plantuml.jar [options] [files/dirs]");
				System.err.println("\t(to process files or directories)");
				System.err.println();
				System.err.println("where options include:");
				System.err.println("    -version\tTo display information about PlantUML and Java versions");
				System.err.println("    -v[erbose]\tTo have log information");
				System.err.println("    -keepfiles\tTo NOT delete temporary files after process");
				System.err.println("    -h[elp]\tTo display this help message");
				System.err.println("    -testdot\tTo test the installation of graphviz");
				System.err.println();
				System.err.println("If needed, you can setup the environment variable GRAPHVIZ_DOT.");
				System.exit(0);
			} else {
				result.add(s);
			}
		}
		return Collections.unmodifiableList(result);
	}

	private void testDot() {
		final String ent = Graphviz.getenvGraphvizDot();
		if (ent == null) {
			System.err.println("The environment variable GRAPHVIZ_DOT has not been set");
		} else {
			System.err.println("The environment variable GRAPHVIZ_DOT has been set to " + ent);
		}
		System.err.println("Dot executable is " + Graphviz.getDotExe());

		boolean ok = true;
		if (Graphviz.getDotExe() == null) {
			System.err.println("Error: Not dot executable");
			ok = false;
		} else if (Graphviz.getDotExe() != null && Graphviz.getDotExe().exists() == false) {
			System.err.println("Error: the dot executable does not exists at specified location");
			ok = false;
		}

		if (ok) {
			System.err.println("Installation seems OK");
			try {
				final String version = Graphviz.dotVersion();
				System.err.println("Dot version: " + version);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("Error: only sequence diagrams will be generated");
		}

		System.exit(0);
	}

	private void displayVersion() {
		System.err.println("PlantUML version " + Version.version());
		final Properties p = System.getProperties();
		System.err.println(p.getProperty("java.runtime.name"));
		System.err.println(p.getProperty("java.vm.name"));
		System.err.println(p.getProperty("java.runtime.version"));
		System.err.println(p.getProperty("os.name"));
		System.exit(0);
	}

	void reset() {
		keepTmpFiles = false;
		verbose = false;
	}

	public boolean isKeepFiles() {
		return keepTmpFiles;
	}

	public boolean isVerbose() {
		return verbose;
	}

}
