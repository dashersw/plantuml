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

public class CommandLinkActivity extends SingleLineCommand<ActivityDiagram> {

	public CommandLinkActivity(ActivityDiagram diagram) {
		super(diagram, "(?i)^(\\*|\\<\\>|==+)?\\s*(\\w+|\"([^\"]+)\"(?:\\s+as\\s+(\\w+))?)?(?:\\s*=+)?"
				+ "\\s*(\\[[^\\]]+\\])?\\s*(-+\\>|-+\\]|\\<-+)\\s*(\\[[^\\]]+\\])?\\s*"
				+ "(\\*|\\<\\>|==+)?\\s*(\\w+|\"([^\"]+)\"(?:\\s+as\\s+(\\w+))?)?(?:\\s*=+)?$");
	}

	@Override
	protected boolean executeArg(List<String> arg) {
		final Entity lastEntityConsulted = getSystem().getLastEntityConsulted();

		final EntityType type1 = getTypeFromString(arg.get(0), EntityType.CIRCLE_START);
		final EntityType type2 = getTypeFromString(arg.get(7), EntityType.CIRCLE_END);

		final String label = getLabel(arg);

		final Entity entity1 = getEntity1(lastEntityConsulted, getSystem(), arg, type1, label);
		final Entity entity2 = getEntity2(lastEntityConsulted, getSystem(), arg, type2, label);

		if (entity1 == null || entity2 == null) {
			return false;
		}

		final int lenght = arg.get(5).length() - 1;

		final Link link = new Link(entity1, entity2, getLinkType(arg.get(5)), label, lenght, null, null);

		getSystem().addLink(link);

		return true;

	}

	static Entity getEntity2(final Entity lastEntityConsulted, ActivityDiagram system, List<String> arg,
			final EntityType type2, final String label) {
		if (arg.get(8) == null) {
			return label == null ? lastEntityConsulted : system.getLastEntityBrancheConsulted();
		}
		final String display;
		final String code;
		if (arg.get(9) == null) {
			display = arg.get(8);
			code = arg.get(8);
		} else {
			display = arg.get(9);
			code = arg.get(10) != null ? arg.get(10) : arg.get(9);
		}
		return system.getOrCreate(code, display, type2);
	}

	static LinkType getLinkType(String arrowSring) {
		if (arrowSring.endsWith(">") || arrowSring.endsWith("]")) {
			return LinkType.NAVASSOC;
		}
		return LinkType.NAVASSOC_INV;
	}

	static Entity getEntity1(final Entity lastEntityConsulted, ActivityDiagram system, List<String> arg,
			final EntityType type1, final String label) {
		if (arg.get(1) == null) {
			return label == null ? lastEntityConsulted : system.getLastEntityBrancheConsulted();
		}
		final String display;
		final String code;
		if (arg.get(2) == null) {
			display = arg.get(1);
			code = arg.get(1);
		} else {
			display = arg.get(2);
			code = arg.get(3) != null ? arg.get(3) : arg.get(2);
		}
		return system.getOrCreate(code, display, type1);

	}

	static String getLabel(List<String> arg) {
		final String label1 = arg.get(6);
		final String label2 = arg.get(4);
		final String label = label1 != null ? label1 : label2;
		return label;
	}

	static EntityType getTypeFromString(String type, final EntityType circle) {
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
		throw new IllegalArgumentException();
	}

}
