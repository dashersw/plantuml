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
package net.sourceforge.plantuml.classdiagram.command;

import java.util.List;

import net.sourceforge.plantuml.SingleLineCommand;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkType;

public class CommandNoteEntity extends SingleLineCommand<ClassDiagram> {

	private static int cpt = 1;

	public CommandNoteEntity(ClassDiagram classDiagram) {
		super(classDiagram, "(?i)^note\\s+(right|left|top|bottom)\\s+of\\s+(\\w+)\\s*:\\s*(.*)$");
	}

	protected boolean executeArg(List<String> arg) {
		final String pos = arg.get(0);
		final Entity cl1 = getSystem().getOrCreateClass(arg.get(1));
		final Entity note = getSystem().createEntity("GN" + cpt, arg.get(2), EntityType.NOTE, null);
		cpt++;

		final Link link;

		if (pos.equals("right")) {
			link = new Link(cl1, note, LinkType.ASSOCIED_DASHED, null, 1, null, null);
		} else if (pos.equals("left")) {
			link = new Link(note, cl1, LinkType.ASSOCIED_DASHED, null, 1, null, null);
		} else if (pos.equals("bottom")) {
			link = new Link(cl1, note, LinkType.ASSOCIED_DASHED, null, 2, null, null);
		} else if (pos.equals("top")) {
			link = new Link(note, cl1, LinkType.ASSOCIED_DASHED, null, 2, null, null);
		} else {
			throw new IllegalArgumentException();
		}
		getSystem().addLink(link);
		return true;

	}

}
