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
package net.sourceforge.plantuml.version;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.OptionPrint;
import net.sourceforge.plantuml.graphic.GraphicStrings;

public class PSystemVersion extends AbstractPSystem {

	private final List<String> strings = new ArrayList<String>();

	private PSystemVersion(List<String> args) {
		strings.addAll(args);
	}

	public List<File> createPng(File pngFile) throws IOException, InterruptedException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(pngFile);
			getGraphicStrings().writeImage(os);
		} finally {
			if (os != null) {
				os.close();
			}
		}
		return Arrays.asList(pngFile);
	}

	public void createPng(OutputStream os) throws IOException {
		getGraphicStrings().writeImage(os);
	}

	public static PSystemVersion createShowVersion() {
		final List<String> strings = new ArrayList<String>();
		strings.add("<b>PlantUML version " + Version.version() + "</b> (" + new Date(Version.compileTime()) + ")");
		strings.add(" ");

		strings.addAll(OptionPrint.getTestDotStrings());
		strings.add(" ");
		final Properties p = System.getProperties();
		strings.add(p.getProperty("java.runtime.name"));
		strings.add(p.getProperty("java.vm.name"));
		strings.add(p.getProperty("java.runtime.version"));
		strings.add(p.getProperty("os.name"));
		return new PSystemVersion(strings);
	}

	public static PSystemVersion createShowAuthors() {
		final List<String> strings = new ArrayList<String>();
		strings.add("<b>PlantUML version " + Version.version() + "</b> (" + new Date(Version.compileTime()) + ")");
		strings.add(" ");
		strings.add("<u>Original idea</u>: Arnaud Roques");
		strings.add("<u>Word Macro</u>: Alain Bertucat & Matthieu Sabatier");
		strings.add("<u>Eclipse Plugin</u>: Claude Durif & Anne Pecoil");
		strings.add("<u>Site design</u>: Raphael Cotisson");

		strings.add(" ");
		strings.add("http://plantuml.sourceforge.net");
		strings.add(" ");
		return new PSystemVersion(strings);
	}

	private GraphicStrings getGraphicStrings() throws IOException {
		final Font font = new Font("SansSerif", Font.PLAIN, 12);
		return new GraphicStrings(strings, font, Color.BLACK, Color.WHITE);
	}

	public String getDescription() {
		return "(Version)";
	}

}
