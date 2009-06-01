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
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineNumberReaderIncluder {

	private static final Pattern includePattern = Pattern.compile("^include\\s+\"?([^\"]+)\"?$");
	private static final Pattern toSkip = Pattern.compile("^@startuml.*|@enduml$");

	private final LineNumberReader lineNumberReader;

	private LineNumberReaderIncluder included = null;

	public LineNumberReaderIncluder(Reader reader) {
		this.lineNumberReader = new LineNumberReader(reader);
	}

	public String readLine() throws IOException {
		if (included != null) {
			String s;
			do {
				s = included.readLine();
			} while (s != null && toSkip.matcher(s).matches());
			if (s != null) {
				return s;
			}
			included.close();
			included = null;
		}
		final String s = lineNumberReader.readLine();
		if (s != null) {
			final Matcher m = includePattern.matcher(s);
			if (m.find()) {
				final String fileName = m.group(1);
				final File f = FileSystem.getInstance().getFile(fileName);
				if (f.exists() == false) {
					throw new IOException("Cannot include " + f);
				}
				included = new LineNumberReaderIncluder(new FileReader(f));
				return this.readLine();
			}
		}
		return s;

	}

	public int getLineNumber() {
		return lineNumberReader.getLineNumber();
	}

	public void close() throws IOException {
		lineNumberReader.close();
	}

}
