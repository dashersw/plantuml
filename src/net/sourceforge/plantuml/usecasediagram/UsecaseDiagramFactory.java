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
package net.sourceforge.plantuml.usecasediagram;

import net.sourceforge.plantuml.classdiagram.command.CommandCreateNote;
import net.sourceforge.plantuml.classdiagram.command.CommandMultilinesStandaloneNote;
import net.sourceforge.plantuml.classdiagram.command.CommandNoteEntity;
import net.sourceforge.plantuml.classdiagram.command.CommandPackage;
import net.sourceforge.plantuml.classdiagram.command.CommandPage;
import net.sourceforge.plantuml.command.AbstractUmlSystemCommandFactory;
import net.sourceforge.plantuml.command.CommandMinwidth;
import net.sourceforge.plantuml.command.CommandMultilinesTitle;
import net.sourceforge.plantuml.command.CommandRotate;
import net.sourceforge.plantuml.command.CommandSkinParam;
import net.sourceforge.plantuml.command.CommandTitle;
import net.sourceforge.plantuml.usecasediagram.command.CommandCreateActor;
import net.sourceforge.plantuml.usecasediagram.command.CommandCreateUsecase;
import net.sourceforge.plantuml.usecasediagram.command.CommandLinkUsecase;
import net.sourceforge.plantuml.usecasediagram.command.CommandMultilinesUsecaseNoteEntity;
import net.sourceforge.plantuml.usecasediagram.command.CommandNoopUsecase;

public class UsecaseDiagramFactory extends AbstractUmlSystemCommandFactory {

	private UsecaseDiagram system;

	public UsecaseDiagram getSystem() {
		return system;
	}

	@Override
	protected void initCommands() {
		system = new UsecaseDiagram();

		addCommand(new CommandRotate(system));
		addCommand(new CommandPage(system));
		addCommand(new CommandLinkUsecase(system));

		addCommand(new CommandPackage(system));
		// cmds.add(new CommandStereotype(system));
		addCommand(new CommandNoteEntity(system));

		addCommand(new CommandCreateNote(system));
		addCommand(new CommandCreateActor(system));
		addCommand(new CommandCreateUsecase(system));

		addCommand(new CommandMultilinesUsecaseNoteEntity(system));
		addCommand(new CommandMultilinesStandaloneNote(system));
		addCommand(new CommandMinwidth(system));
		addCommand(new CommandNoopUsecase(system));

		addCommand(new CommandTitle(system));
		addCommand(new CommandMultilinesTitle(system));

		addCommand(new CommandSkinParam(system));
	}
}
