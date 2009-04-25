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
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkType;

public class CommandLink extends SingleLineCommand<ClassDiagram> {

	public CommandLink(ClassDiagram classDiagram) {
		super(
				classDiagram,
				"(?i)^(\\w+)\\s*(?:\"([^\"]+)\")?\\s*(?:((-+|\\.+(?!o)(?!\\*))(\\>|o|\\*|\\|\\>)?)|((\\<|o|\\*|\\<\\|)?(-+|(?<!o)(?<!\\*)\\.+)))\\s*(?:\"([^\"]+)\")?\\s*(\\w+)\\s*(?::\\s*([^\"]*))?$");
	}

	protected boolean executeArg(List<String> arg) {

		final Entity cl1 = getSystem().getOrCreateClass(arg.get(0));
		final Entity cl2 = getSystem().getOrCreateClass(arg.get(9));

		final LinkType linkType = arg.get(2) != null ? getLinkTypeNormal(arg) : getLinkTypeInv(arg);
		final String queue = arg.get(2) != null ? arg.get(3) : arg.get(7);

		final Link link = new Link(cl1, cl2, linkType, arg.get(10), queue.length(), arg.get(1), arg.get(8));
		getSystem().addLink(link);
		return true;
	}

	private LinkType getLinkTypeNormal(List<String> arg) {
		final String queue = arg.get(3);
		final String key = arg.get(4);
		LinkType linkType = getLinkTypeFromKey(key);

		if (queue.startsWith(".")) {
			linkType = linkType.getDashed();
		}
		return linkType;
	}

	private LinkType getLinkTypeInv(List<String> arg) {
		final String queue = arg.get(7);
		final String key = arg.get(6);
		LinkType linkType = getLinkTypeFromKey(key);

		if (queue.startsWith(".")) {
			linkType = linkType.getDashed();
		}
		return linkType.getInv();
	}

	private LinkType getLinkTypeFromKey(String k) {
		if (k == null) {
			return LinkType.ASSOCIED;
		}
		if (k.equals("*")) {
			return LinkType.COMPOSITION;
		}
		if (k.equals("o")) {
			return LinkType.AGREGATION;
		}
		if (k.equals("<") || k.equals(">")) {
			return LinkType.NAVASSOC;
		}
		if (k.equals("<|") || k.equals("|>")) {
			return LinkType.EXTENDS;
		}
		throw new IllegalArgumentException(k);
	}

}
