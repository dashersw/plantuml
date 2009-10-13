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
package net.sourceforge.plantuml.classdiagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.classdiagram.command.CommandAddMethod;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateEntity;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateNote;
import net.sourceforge.plantuml.classdiagram.command.CommandImport;
import net.sourceforge.plantuml.classdiagram.command.CommandLink;
import net.sourceforge.plantuml.classdiagram.command.CommandMultilinesNoteEntity;
import net.sourceforge.plantuml.classdiagram.command.CommandMultilinesStandaloneNote;
import net.sourceforge.plantuml.classdiagram.command.CommandMultiple;
import net.sourceforge.plantuml.classdiagram.command.CommandNoopClass;
import net.sourceforge.plantuml.classdiagram.command.CommandNoteEntity;
import net.sourceforge.plantuml.classdiagram.command.CommandPackage;
import net.sourceforge.plantuml.classdiagram.command.CommandPage;
import net.sourceforge.plantuml.classdiagram.command.CommandStereotype;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandControl;
import net.sourceforge.plantuml.command.CommandMinwidth;
import net.sourceforge.plantuml.command.CommandMultilinesTitle;
import net.sourceforge.plantuml.command.CommandRotate;
import net.sourceforge.plantuml.command.CommandTitle;
import net.sourceforge.plantuml.command.PSystemCommandFactory;

public class ClassDiagramFactory implements PSystemCommandFactory {

	private ClassDiagram system;

	private List<Command> cmds;

	public ClassDiagramFactory() {
		reset();
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

	public ClassDiagram getSystem() {
		return system;
	}

	public void reset() {
		system = new ClassDiagram();
		cmds = new ArrayList<Command>();

		cmds.add(new CommandRotate(system));
		cmds.add(new CommandPage(system));
		cmds.add(new CommandLink(system));

		cmds.add(new CommandCreateEntity(system));
		cmds.add(new CommandCreateNote(system));
		cmds.add(new CommandPackage(system));
		cmds.add(new CommandStereotype(system));
		cmds.add(new CommandImport(system));
		cmds.add(new CommandNoteEntity(system));
		cmds.add(new CommandAddMethod(system));

		cmds.add(new CommandMultiple(system, this));
		cmds.add(new CommandMultilinesNoteEntity(system));
		cmds.add(new CommandMultilinesStandaloneNote(system));
		cmds.add(new CommandMinwidth(system));
		
		cmds.add(new CommandTitle(system));
		cmds.add(new CommandMultilinesTitle(system));
		cmds.add(new CommandNoopClass(system));
	}
}
