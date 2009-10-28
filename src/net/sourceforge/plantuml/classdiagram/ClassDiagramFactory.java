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
import net.sourceforge.plantuml.command.AbstractUmlSystemCommandFactory;

public class ClassDiagramFactory extends AbstractUmlSystemCommandFactory {

	private ClassDiagram system;

	public ClassDiagram getSystem() {
		return system;
	}

	@Override
	protected void initCommands() {
		system = new ClassDiagram();

		addCommand(new CommandPage(system));
		addCommand(new CommandLink(system));

		addCommand(new CommandCreateEntity(system));
		addCommand(new CommandCreateNote(system));
		addCommand(new CommandPackage(system));
		addCommand(new CommandStereotype(system));
		addCommand(new CommandImport(system));
		addCommand(new CommandNoteEntity(system));
		addCommand(new CommandAddMethod(system));

		addCommand(new CommandMultiple(system, this));
		addCommand(new CommandMultilinesNoteEntity(system));
		addCommand(new CommandMultilinesStandaloneNote(system));
		
		addCommand(new CommandNoopClass(system));
		
		addCommonCommands(system);
	}
}
