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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import net.sourceforge.plantuml.cucadiagram.dot.GraphvizUtils;
import net.sourceforge.plantuml.version.Version;

public class OptionPrint {

	static public void printTestDot() throws InterruptedException {
		for (String s : getTestDotStrings()) {
			System.err.println(s);
		}
		exit();
	}

	static public List<String> getTestDotStrings() {
		final List<String> result = new ArrayList<String>();
		final String ent = GraphvizUtils.getenvGraphvizDot();
		if (ent == null) {
			result.add("The environment variable GRAPHVIZ_DOT has not been set");
		} else {
			result.add("The environment variable GRAPHVIZ_DOT has been set to " + ent);
		}
		result.add("Dot executable is " + GraphvizUtils.getDotExe());

		boolean ok = true;
		if (GraphvizUtils.getDotExe() == null) {
			result.add("Error: Not dot executable");
			ok = false;
		} else if (GraphvizUtils.getDotExe() != null && GraphvizUtils.getDotExe().exists() == false) {
			result.add("Error: the dot executable does not exists at specified location");
			ok = false;
		}

		if (ok) {
			result.add("Installation seems OK");
			try {
				final String version = GraphvizUtils.dotVersion();
				result.add("Dot version: " + version);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			result.add("Error: only sequence diagrams will be generated");
		}

		return Collections.unmodifiableList(result);
	}

	static public void printHelp() throws InterruptedException {
		System.err.println("Usage: java -jar plantuml.jar [options]");
		System.err.println("\t(to execute the GUI)");
		System.err.println("    or java -jar plantuml.jar [options] [files/dirs]");
		System.err.println("\t(to process files or directories)");
		System.err.println();
		System.err.println("You can use the following wildcards in files/dirs:");
		System.err.println("\t*\tmeans any characters but '" + File.separator + "'");
		System.err.println("\t?\tone and only one character but '" + File.separator + "'");
		System.err.println("\t**\tmeans any characters (used to recurse through directories)");
		System.err.println();
		System.err.println("where options include:");
		System.err.println("    -o[utput] \"dir\"\tTo generate images in the specified directory");
		System.err.println("    -config \"file\"\tTo read the provided config file before each diagram");
		System.err.println("    -e[x]clude pattern\tTo exclude files that matche the provided pattern");
		System.err.println("    -metadata\t\tTo retrieve PlantUML sources from PNG images");
		System.err.println("    -version\t\tTo display information about PlantUML and Java versions");
		System.err.println("    -v[erbose]\t\tTo have log information");
		System.err.println("    -keepfiles\t\tTo NOT delete temporary files after process");
		System.err.println("    -h[elp]\t\tTo display this help message");
		System.err.println("    -testdot\t\tTo test the installation of graphviz");
		System.err.println();
		System.err.println("If needed, you can setup the environment variable GRAPHVIZ_DOT.");
		exit();
	}


	static private void exit() throws InterruptedException {
		if (OptionFlags.getInstance().isSystemExit()) {
			System.exit(0);
		}
		throw new InterruptedException("exit");
	}

	public static void printVersion() throws InterruptedException {
		System.err.println("PlantUML version " + Version.version() + "(" + new Date(Version.compileTime()) + ")");
		final Properties p = System.getProperties();
		System.err.println(p.getProperty("java.runtime.name"));
		System.err.println(p.getProperty("java.vm.name"));
		System.err.println(p.getProperty("java.runtime.version"));
		System.err.println(p.getProperty("os.name"));
		exit();
	}

}
