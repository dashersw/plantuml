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
package net.sourceforge.plantuml.sequencediagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Command;
import net.sourceforge.plantuml.CommandControl;
import net.sourceforge.plantuml.PSystemFactory;
import net.sourceforge.plantuml.sequencediagram.command.CommandActivate;
import net.sourceforge.plantuml.sequencediagram.command.CommandArrow;
import net.sourceforge.plantuml.sequencediagram.command.CommandGrouping;
import net.sourceforge.plantuml.sequencediagram.command.CommandMultilinesNote;
import net.sourceforge.plantuml.sequencediagram.command.CommandMultilinesNoteOnArrow;
import net.sourceforge.plantuml.sequencediagram.command.CommandMultilinesNoteOverSeveral;
import net.sourceforge.plantuml.sequencediagram.command.CommandNewpage;
import net.sourceforge.plantuml.sequencediagram.command.CommandNote;
import net.sourceforge.plantuml.sequencediagram.command.CommandNoteOnArrow;
import net.sourceforge.plantuml.sequencediagram.command.CommandNoteOverSeveral;
import net.sourceforge.plantuml.sequencediagram.command.CommandParticipant;
import net.sourceforge.plantuml.sequencediagram.command.CommandSkin;
import net.sourceforge.plantuml.sequencediagram.command.CommandTitle;

public class SequenceDiagramFactory implements PSystemFactory {

	private SequenceDiagram system;

	private List<Command> cmds;

	public SequenceDiagramFactory() {
		reset();
	}

	public void reset() {
		system = new SequenceDiagram();

		cmds = new ArrayList<Command>();

		cmds.add(new CommandParticipant(system));
		cmds.add(new CommandArrow(system));
		cmds.add(new CommandNote(system));
		cmds.add(new CommandNoteOverSeveral(system));
		cmds.add(new CommandGrouping(system));
		cmds.add(new CommandActivate(system));
		
		cmds.add(new CommandNoteOnArrow(system));
		
		cmds.add(new CommandMultilinesNote(system));
		cmds.add(new CommandMultilinesNoteOverSeveral(system));
		cmds.add(new CommandMultilinesNoteOnArrow(system));
		
		cmds.add(new CommandNewpage(system));
		cmds.add(new CommandTitle(system));
		cmds.add(new CommandSkin(system));
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

	public SequenceDiagram getSystem() {
		return system;
	}

}
