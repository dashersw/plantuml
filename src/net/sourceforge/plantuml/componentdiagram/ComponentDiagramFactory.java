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
package net.sourceforge.plantuml.componentdiagram;

import net.sourceforge.plantuml.classdiagram.command.CommandCreateNote;
import net.sourceforge.plantuml.classdiagram.command.CommandMultilinesStandaloneNote;
import net.sourceforge.plantuml.classdiagram.command.CommandNoteEntity;
import net.sourceforge.plantuml.classdiagram.command.CommandPackage;
import net.sourceforge.plantuml.classdiagram.command.CommandPage;
import net.sourceforge.plantuml.command.AbstractUmlSystemCommandFactory;
import net.sourceforge.plantuml.componentdiagram.command.CommandCreateCircleInterface;
import net.sourceforge.plantuml.componentdiagram.command.CommandCreateComponent;
import net.sourceforge.plantuml.componentdiagram.command.CommandLinkComponent;
import net.sourceforge.plantuml.componentdiagram.command.CommandMultilinesComponentNoteEntity;
import net.sourceforge.plantuml.componentdiagram.command.CommandNoopComponent;

public class ComponentDiagramFactory extends AbstractUmlSystemCommandFactory {

	private ComponentDiagram system;

	public ComponentDiagram getSystem() {
		return system;
	}

	@Override
	protected void initCommands() {
		system = new ComponentDiagram();

		addCommand(new CommandPage(system));
		addCommand(new CommandLinkComponent(system));

		addCommand(new CommandPackage(system));
		addCommand(new CommandNoteEntity(system));

		addCommand(new CommandCreateNote(system));
		addCommand(new CommandCreateComponent(system));
		addCommand(new CommandCreateCircleInterface(system));

		addCommand(new CommandMultilinesComponentNoteEntity(system));
		addCommand(new CommandMultilinesStandaloneNote(system));

		addCommand(new CommandNoopComponent(system));
		addCommonCommands(system);

	}
}
