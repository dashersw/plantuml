/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 7559 $
 *
 */
package net.sourceforge.plantuml.classdiagram;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.classdiagram.command.CommandAddMethod;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateEntityClass2;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateEntityClassMultilines2;
import net.sourceforge.plantuml.classdiagram.command.CommandDiamondAssociation;
import net.sourceforge.plantuml.classdiagram.command.CommandHideShow;
import net.sourceforge.plantuml.classdiagram.command.CommandHideShow3;
import net.sourceforge.plantuml.classdiagram.command.CommandImport;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkClass3;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkLollipop2;
import net.sourceforge.plantuml.classdiagram.command.CommandMultilinesClassNote;
import net.sourceforge.plantuml.classdiagram.command.CommandStereotype;
import net.sourceforge.plantuml.classdiagram.command.CommandUrl;
import net.sourceforge.plantuml.command.AbstractUmlSystemCommandFactory;
import net.sourceforge.plantuml.command.CommandEndNamespace;
import net.sourceforge.plantuml.command.CommandEndPackage;
import net.sourceforge.plantuml.command.CommandNamespace;
import net.sourceforge.plantuml.command.CommandPackage;
import net.sourceforge.plantuml.command.CommandPackageEmpty;
import net.sourceforge.plantuml.command.CommandPage;
import net.sourceforge.plantuml.command.note.CommandCreateNote;
import net.sourceforge.plantuml.command.note.CommandMultilinesNoteOnStateLink;
import net.sourceforge.plantuml.command.note.CommandMultilinesStandaloneNote;
import net.sourceforge.plantuml.command.note.CommandNoteEntityOld;
import net.sourceforge.plantuml.command.note.CommandNoteOnStateLink;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;

public class ClassDiagramFactory extends AbstractUmlSystemCommandFactory {

	private ClassDiagram system;

	public ClassDiagram getSystem() {
		return system;
	}

	@Override
	protected void initCommands() {
		system = new ClassDiagram();

		addCommonCommands(system);

		addCommand(new CommandPage(system));
		addCommand(new CommandAddMethod(system));

		addCommand(new CommandCreateEntityClass2(system));
		addCommand(new CommandCreateNote(system));

		addCommand(new CommandPackage(system));
		addCommand(new CommandEndPackage(system));
		addCommand(new CommandPackageEmpty(system));
		
		addCommand(new CommandNamespace(system));
		addCommand(new CommandEndNamespace(system));
		addCommand(new CommandStereotype(system));

		addCommand(new CommandLinkClass3(system));
		addCommand(new CommandLinkLollipop2(system));

		addCommand(new CommandImport(system));
		addCommand(new CommandNoteEntityOld(system));
		addCommand(new CommandUrl(system));

		addCommand(new CommandMultilinesClassNote(system));
		addCommand(new CommandMultilinesStandaloneNote(system));
		addCommand(new CommandCreateEntityClassMultilines2(system));

		addCommand(new CommandNoteOnStateLink(system));
		addCommand(new CommandMultilinesNoteOnStateLink(system));

		addCommand(new CommandDiamondAssociation(system));

		addCommand(new CommandHideShow3(system));
		addCommand(new CommandHideShow(system));

	}

	@Override
	public String checkFinalError() {
		if (system.getSkinParam().isSvek()) {
			for (Group g : system.getGroups(true)) {
				final List<IEntity> standalones = new ArrayList<IEntity>();
				for (IEntity ent : g.entities().values()) {
					if (system.isStandalone(ent)) {
						standalones.add(ent);
					}
				}
				if (standalones.size() < 3) {
					continue;
				}
				putInSquare(standalones);
			}
		}
		return super.checkFinalError();
	}

	private void putInSquare(List<IEntity> standalones) {
		final LinkType linkType = new LinkType(LinkDecor.NONE, LinkDecor.NONE).getInvisible();
		final int branch = computeBranch(standalones.size());
		int headBranch = 0;
		for (int i = 1; i < standalones.size(); i++) {
			final int dist = i - headBranch;
			final IEntity ent2 = standalones.get(i);
			final Link link;
			if (dist == branch) {
				final IEntity ent1 = standalones.get(headBranch);
				link = new Link(ent1, ent2, linkType, null, 2);
				headBranch = i;
			} else {
				final IEntity ent1 = standalones.get(i - 1);
				link = new Link(ent1, ent2, linkType, null, 1);
			}
			system.addLink(link);
		}

	}

	static int computeBranch(int size) {
		final double sqrt = Math.sqrt(size);
		final int r = (int) sqrt;
		if (r * r == size) {
			return r;
		}
		return r + 1;
	}
}
