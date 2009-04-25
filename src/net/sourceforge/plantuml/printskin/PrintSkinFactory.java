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
package net.sourceforge.plantuml.printskin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Command;
import net.sourceforge.plantuml.CommandControl;
import net.sourceforge.plantuml.PSystemFactory;

public class PrintSkinFactory implements PSystemFactory {

	private PrintSkin system;

	private List<Command> cmds;

	public PrintSkinFactory() {
		reset();
	}

	public void reset() {
		system = new PrintSkin();

		cmds = new ArrayList<Command>();

		cmds.add(new CommandTestSkin(system));
	}

	public List<Command> create(List<String> lines) {
		for (Command cmd : cmds) {
			final CommandControl result = cmd.isValid(lines);
			if (result == CommandControl.OK) {
				return Arrays.asList(cmd);
			} else if (result == CommandControl.OK_PARTIAL) {
				return Collections.emptyList();
			}
		}
		return null;
	}

	public PrintSkin getSystem() {
		return system;
	}

}
