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
package net.sourceforge.plantuml.activitydiagram.command;

import java.util.List;

import net.sourceforge.plantuml.SingleLineCommand;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkType;

public class CommandLinkActivityShort extends SingleLineCommand<ActivityDiagram> {

	public CommandLinkActivityShort(ActivityDiagram diagram) {
		super(diagram, "(?i)^(-+)\\>\\s*(\\*|\\<\\>|==+)?\\s*(\\w+)(?:\\s*=+)?$");
	}

	protected boolean executeArg(List<String> arg) {
		
		final Entity entity1 = getSystem().getLastEntityConsulted();
		final EntityType type2 = getTypeFromString(arg.get(1), EntityType.CIRCLE_END);
		final Entity entity2 = getSystem().getOrCreate(arg.get(2), type2);

		if (entity1 == null || entity2 == null) {
			return false;
		}

		final int lenght = arg.get(0).length();
		final Link link = new Link(entity1, entity2, LinkType.NAVASSOC, null, lenght, null, null);

		getSystem().addLink(link);
		return true;

	}

	private EntityType getTypeFromString(String type, final EntityType circle) {
		if (type == null) {
			return EntityType.ACTIVITY;
		}
		if (type.equals("*")) {
			return circle;
		}
		if (type.equals("<>")) {
			return EntityType.BRANCH;
		}
		if (type.startsWith("=")) {
			return EntityType.SYNCHRO_BAR;
		}
		throw new IllegalArgumentException(type);
	}

}
