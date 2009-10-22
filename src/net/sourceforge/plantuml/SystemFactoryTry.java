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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandControl;
import net.sourceforge.plantuml.command.PSystemCommandFactory;

final public class SystemFactoryTry {

	final private SortedMap<Integer, StartUml> result = new TreeMap<Integer, StartUml>();

	private int nb = 0;
	private final Iterator<String> it;

	public SystemFactoryTry(List<String> strings, PSystemFactory systemFactory) throws IOException {

		it = strings.iterator();
		while (hasNext()) {
			final String s = next();
			if (isIgnoredLine(s)) {
				continue;
			}
			if (isArobaseStartuml(s)) {
				final int line = nb;
				PSystem system = null;
				if (systemFactory instanceof PSystemCommandFactory) {
					system = executeUmlCommand(s, (PSystemCommandFactory) systemFactory);
				} else if (systemFactory instanceof PSystemBasicFactory) {
					system = executeUmlBasic(s, (PSystemBasicFactory) systemFactory);
				}
				if (system != null) {
					result.put(line, new StartUml(system, s));
				}
			}
		}
	}

	public static boolean isArobaseStartuml(final String s) {
		return s.equals("@startuml") || s.startsWith("@startuml ");
	}

	private boolean hasNext() {
		return it.hasNext();
	}

	private String next() {
		nb++;
		return it.next();
	}

	public SortedMap<Integer, StartUml> getAllStartUml() {
		return Collections.unmodifiableSortedMap(result);
	}

	private PSystem executeUmlBasic(String start, PSystemBasicFactory systemFactory) throws IOException {
		systemFactory.reset();
		final StringBuilder source = new StringBuilder(start);
		source.append('\n');
		while (hasNext()) {
			final String s = next();
			if (isIgnoredLine(s)) {
				continue;
			}
			source.append(s);
			source.append('\n');
			if (s.equals("@enduml")) {
				final PSystem sys = systemFactory.getSystem();
				sys.setSource(source.toString());
				return sys;
			}
			final boolean ok = systemFactory.executeLine(s);
			if (ok == false) {
				return new PSystemError(s);
			}
		}
		final PSystem sys = systemFactory.getSystem();
		sys.setSource(source.toString());
		return sys;
	}

	private boolean isIgnoredLine(final String s) {
		return s.length() == 0 || s.startsWith("#") || s.startsWith("'");
	}

	private PSystem executeUmlCommand(String start, PSystemCommandFactory systemFactory) throws IOException {
		systemFactory.reset();
		final StringBuilder source = new StringBuilder(start);
		source.append('\n');
		while (hasNext()) {
			final String s = next();
			if (isIgnoredLine(s)) {
				continue;
			}
			source.append(s);
			source.append('\n');
			if (s.equals("@enduml")) {
				final PSystem sys = systemFactory.getSystem();
				sys.setSource(source.toString());
				return sys;
			}
			final CommandControl commandControl = systemFactory.isValid(Arrays.asList(s));
			if (commandControl == CommandControl.NOT_OK) {
				return new PSystemError(s);
			} else if (commandControl == CommandControl.OK_PARTIAL) {
				final boolean ok = manageMultiline(systemFactory, s);
				if (ok == false) {
					return new PSystemError(s);
				}
			} else if (commandControl == CommandControl.OK) {
				final Command cmd = systemFactory.createCommand(Arrays.asList(s));
				final boolean ok = cmd.execute(Arrays.asList(s));
				if (ok == false) {
					return new PSystemError(s);
				}
				testDeprecated(Arrays.asList(s), cmd);
			} else {
				assert false;
			}
		}
		final PSystem sys = systemFactory.getSystem();
		sys.setSource(source.toString());
		return sys;
	}

	private void testDeprecated(final List<String> lines, final Command cmd) {
		if (cmd.isDeprecated(lines)) {
			Log.error("The following syntax is deprecated :");
			for (String s : lines) {
				Log.error(s);
			}
			final String msg = cmd.getHelpMessageForDeprecated(lines);
			if (msg != null) {
				Log.error("Use instead :");
				Log.error(msg);
			}
		}
	}

	private boolean manageMultiline(PSystemCommandFactory systemFactory, final String init) throws IOException {
		final List<String> lines = new ArrayList<String>();
		lines.add(init);
		while (hasNext()) {
			final String s = next();
			if (isIgnoredLine(s)) {
				continue;
			}
			if (s.equals("@enduml")) {
				return false;
			}
			lines.add(s);
			// final List<Command> cmd = systemFactory.create(lines);
			final CommandControl commandControl = systemFactory.isValid(lines);
			if (commandControl == CommandControl.NOT_OK) {
				throw new IllegalStateException();
			}
			if (commandControl == CommandControl.OK) {
				final Command cmd = systemFactory.createCommand(lines);
				testDeprecated(lines, cmd);
				return cmd.execute(lines);
			}
		}
		return false;

	}

}
