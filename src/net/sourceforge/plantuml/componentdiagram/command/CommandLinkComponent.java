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
package net.sourceforge.plantuml.componentdiagram.command;

import java.util.List;

import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.componentdiagram.ComponentDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkType;

public class CommandLinkComponent extends SingleLineCommand<ComponentDiagram> {

	public CommandLinkComponent(ComponentDiagram diagram) {
		super(diagram, "(?i)^(\\[[^\\]]+\\]|\\u00B0[^\\u00B0]+\\u00B0|\\(\\)\\s*\\w+)\\s*"
				+ "(?:(([=-]+|\\.+)([\\]>]|\\|[>\\]])?)|(([\\[<]|[<\\[]\\|)?([=-]+|\\.+)))"
				+ "\\s*(\\[[^\\]]+\\]|\\u00B0[^\\u00B0]+\\u00B0|\\(\\)\\s*\\w+)\\s*(?::\\s*([^\"]*))?$");
	}

	@Override
	protected boolean executeArg(List<String> arg) {
		final Entity cl1 = getSystem().getOrCreateClass(arg.get(0));
		final Entity cl2 = getSystem().getOrCreateClass(arg.get(7));

		final LinkType linkType = arg.get(1) != null ? getLinkTypeNormal(arg) : getLinkTypeInv(arg);
		final String queue = arg.get(1) != null ? arg.get(2) : arg.get(6);

		final Link link = new Link(cl1, cl2, linkType, arg.get(8), queue.length(), null, null);
		getSystem().addLink(link);
		return true;
	}


	private LinkType getLinkTypeNormal(List<String> arg) {
		final String queue = arg.get(2);
		final String key = arg.get(3);
		LinkType linkType = getLinkTypeFromKey(key);

		if (queue.startsWith(".")) {
			linkType = linkType.getDashed();
		}
		return linkType;
	}

	private LinkType getLinkTypeInv(List<String> arg) {
		final String queue = arg.get(6);
		final String key = arg.get(5);
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
		if (k.equals("<") || k.equals(">")) {
			return LinkType.NAVASSOC;
		}
//		if (k.equals("<|") || k.equals("|>")) {
//			return LinkType.EXTENDS;
//		}
		return null;
	}

}
