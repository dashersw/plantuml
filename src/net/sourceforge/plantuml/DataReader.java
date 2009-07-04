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

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class DataReader {

	final private SortedMap<Integer, StartUml> result = new TreeMap<Integer, StartUml>();

	public DataReader(Reader reader, PSystemFactory systemFactory) throws IOException {

		LineNumberReaderIncluder br = null;
		try {
			br = new LineNumberReaderIncluder(reader);
			String s;
			while ((s = br.readLine()) != null) {
				s = cleanLine(s);
				if (s.length() == 0 || s.startsWith("#")) {
					continue;
				}
				if (s.equals("@startuml") || s.startsWith("@startuml ")) {
					final int line = br.getLineNumber();
					final PSystem system = executeUml(s, br, systemFactory);
					if (system != null) {
						result.put(line, new StartUml(system, s));
					}
				}
			}

		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	public SortedMap<Integer, StartUml> getAllStartUml() {
		return Collections.unmodifiableSortedMap(result);
	}

	private PSystem executeUml(String start, LineNumberReaderIncluder br, PSystemFactory systemFactory)
			throws IOException {

		systemFactory.reset();
		final StringBuilder source = new StringBuilder(start);
		source.append('\n');
		String s;
		while ((s = br.readLine()) != null) {
			s = cleanLine(s);
			if (s.length() == 0 || s.startsWith("#")) {
				continue;
			}
			source.append(s);
			source.append('\n');
			if (s.equals("@enduml")) {
				return systemFactory.getSystem(source.toString());
			}
			final List<Command> cmd = systemFactory.create(Arrays.asList(s));
			if (cmd == null) {
				return new PSystemError(s);
			}
			if (cmd.size() == 0) {
				final boolean ok = manageMultiline(br, systemFactory, s);
				if (ok == false) {
					return new PSystemError(s);
				}
			} else if (cmd.size() == 1) {
				final boolean ok = cmd.get(0).execute(Arrays.asList(s));
				if (ok == false) {
					return new PSystemError(s);
				}
			}
		}
		return systemFactory.getSystem(source.toString());
	}

	private boolean manageMultiline(LineNumberReaderIncluder br, PSystemFactory systemFactory, final String init)
			throws IOException {
		final List<String> lines = new ArrayList<String>();
		lines.add(init);
		String s;
		while ((s = br.readLine()) != null) {
			s = cleanLine(s);
			if (s.length() == 0 || s.startsWith("#")) {
				continue;
			}
			if (s.equals("@enduml")) {
				return false;
			}
			lines.add(s);
			final List<Command> cmd = systemFactory.create(lines);
			if (cmd.size() == 1) {
				return cmd.get(0).execute(lines);
			}
		}
		return false;

	}

	private String cleanLine(String s) {
		if (s.startsWith(" * ")) {
			s = s.substring(" * ".length());
		}
		if (s.equals(" *")) {
			s = "";
		}
		s = s.trim();
		while (s.startsWith(" ") || s.startsWith("/") || s.startsWith("\t") || s.startsWith("%")) {
			s = s.substring(1).trim();
		}
		return s;
	}

}
