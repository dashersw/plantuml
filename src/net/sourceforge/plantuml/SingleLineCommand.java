/* ========================================================================
 * Plantuml : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques (for Atos Origin).
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of Plantuml.
 *
 * Plantuml is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Plantuml distributed in the hope that it will be useful, but
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SingleLineCommand<S extends PSystem> implements Command {

	private static final Set<String> printed = new HashSet<String>();

	private final S system;
	private final Pattern pattern;

	public SingleLineCommand(S system, String pattern) {
		if (system == null) {
			throw new IllegalArgumentException();
		}
		if (pattern == null) {
			throw new IllegalArgumentException();
		}
		if (pattern.startsWith("(?i)^") == false || pattern.endsWith("$") == false) {
			throw new IllegalArgumentException("Bad pattern " + pattern);
		}

		if (printed.add(pattern) == true) {
			// System.out.println(pattern);
		}

		this.system = system;
		this.pattern = Pattern.compile(pattern);
	}

	final protected S getSystem() {
		return system;
	}

	final public CommandControl isValid(List<String> lines) {
		if (lines.size() != 1) {
			return CommandControl.NOT_OK;
		}
		final String line = lines.get(0).trim();
		final Matcher m = pattern.matcher(line);
		final boolean result = m.find();
		return result ? CommandControl.OK : CommandControl.NOT_OK;
	}

	public final boolean execute(List<String> lines) {
		if (lines.size() != 1) {
			throw new IllegalArgumentException();
		}
		final String line = lines.get(0).trim();
		final List<String> arg = getSplit(line);
		if (arg == null) {
			return false;
		}
		return executeArg(arg);
	}

	protected abstract boolean executeArg(List<String> arg);

	final public List<String> getSplit(String line) {
		return StringUtils.getSplit(pattern, line);
	}

}
