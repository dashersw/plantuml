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

import net.sourceforge.plantuml.command.AbstractUmlSystemCommandFactory;
import net.sourceforge.plantuml.sequencediagram.command.CommandActivate;
import net.sourceforge.plantuml.sequencediagram.command.CommandArrow;
import net.sourceforge.plantuml.sequencediagram.command.CommandAutonumber;
import net.sourceforge.plantuml.sequencediagram.command.CommandFootbox;
import net.sourceforge.plantuml.sequencediagram.command.CommandGrouping;
import net.sourceforge.plantuml.sequencediagram.command.CommandMultilinesNote;
import net.sourceforge.plantuml.sequencediagram.command.CommandMultilinesNoteOnArrow;
import net.sourceforge.plantuml.sequencediagram.command.CommandMultilinesNoteOverSeveral;
import net.sourceforge.plantuml.sequencediagram.command.CommandNewpage;
import net.sourceforge.plantuml.sequencediagram.command.CommandNoopSequence;
import net.sourceforge.plantuml.sequencediagram.command.CommandNote;
import net.sourceforge.plantuml.sequencediagram.command.CommandNoteOnArrow;
import net.sourceforge.plantuml.sequencediagram.command.CommandNoteOverSeveral;
import net.sourceforge.plantuml.sequencediagram.command.CommandParticipant;
import net.sourceforge.plantuml.sequencediagram.command.CommandSkin;

public class SequenceDiagramFactory extends AbstractUmlSystemCommandFactory {

	private SequenceDiagram system;

	@Override
	protected void initCommands() {
		system = new SequenceDiagram();

		addCommand(new CommandParticipant(system));
		addCommand(new CommandArrow(system));
		addCommand(new CommandNote(system));
		addCommand(new CommandNoteOverSeveral(system));
		addCommand(new CommandGrouping(system));
		addCommand(new CommandActivate(system));

		addCommand(new CommandNoteOnArrow(system));

		addCommand(new CommandMultilinesNote(system));
		addCommand(new CommandMultilinesNoteOverSeveral(system));
		addCommand(new CommandMultilinesNoteOnArrow(system));

		addCommand(new CommandNewpage(system));
		addCommand(new CommandSkin(system));
		addCommand(new CommandAutonumber(system));
		addCommand(new CommandFootbox(system));
		addCommand(new CommandNoopSequence(system));

		addCommonCommands(system);
	}

	public SequenceDiagram getSystem() {
		return system;
	}

}
