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
 * Revision $Revision: 4762 $
 *
 */
package net.sourceforge.plantuml.activitydiagram.command;

import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;

public class CommandLinkActivity extends SingleLineCommand<ActivityDiagram> {

	public CommandLinkActivity(ActivityDiagram diagram) {
		super(
				diagram,
				"(?i)^([\\u00A7*]|\\<\\>|\\[\\]|==+)?\\s*(\\(\\*\\)|[\\p{L}0-9_.]+|\"([^\"]+)\"(?:\\s+as\\s+([\\p{L}0-9_.]+))?)?(?:\\s*=+)?"
						+ "\\s*(\\[[^\\]*]+[^\\]]*\\])?\\s*([=-]+\\>|\\<[=-]+)\\s*(\\[[^\\]*]+[^\\]]*\\])?\\s*"
						+ "([*\\u00A7]|\\<\\>|\\[\\]|==+)?\\s*(\\(\\*\\)|[\\p{L}0-9_.]+|\"([^\"]+)\"(?:\\s+as\\s+([\\p{L}0-9_.]+))?)?(?:\\s*=+)?\\s*(?::\\s*([^\"]+))?$");
	}

	@Override
	protected boolean isDeprecated(String line) {
		if (line.indexOf('\u00A7') != -1) {
			return true;
		}
		if (line.indexOf("*start") != -1) {
			return true;
		}
		if (line.indexOf("*end") != -1) {
			return true;
		}
		return false;
	}

	@Override
	public String getHelpMessageForDeprecated(List<String> lines) {
		String s = lines.get(0);
		s = s.replaceAll("[\\u00A7][\\p{L}0-9_.]+", "(*)");
		s = s.replaceAll("\\*start", "(*)");
		s = s.replaceAll("\\*end", "(*)");
		return s;
	}

	@Override
	protected CommandExecutionResult executeArg(List<String> arg) {
		final Entity lastEntityConsulted = getSystem().getLastEntityConsulted();

		final EntityType type1 = getTypeFromString(arg.get(0), EntityType.CIRCLE_START);
		final EntityType type2 = getTypeFromString(arg.get(7), EntityType.CIRCLE_END);

		final String entityLabel = getLabel(arg.get(6), arg.get(4));

		final Entity entity1;
		if ("(*)".equals(arg.get(1))) {
			entity1 = getSystem().getStart();
		} else {
			entity1 = getEntity(lastEntityConsulted, getSystem(), arg.get(1), arg.get(2), arg.get(3), type1, entityLabel);
		}

		final Entity entity2;
		if ("(*)".equals(arg.get(8))) {
			entity2 = getSystem().getEnd();
		} else {
			entity2 = getEntity(lastEntityConsulted, getSystem(), arg.get(8), arg.get(9), arg.get(10), type2, entityLabel);
		}

		if (entity1 == null || entity2 == null) {
			return CommandExecutionResult.error("No activity defined");
		}

		final String arrow = StringUtils.manageArrow(arg.get(5));
		final int lenght = arrow.length() - 1;
		
		final String linkLabel = getLabel(entityLabel, arg.get(11));

		final Link link = new Link(entity1, entity2, getLinkType(arrow), linkLabel, lenght);

		getSystem().addLink(link);

		return CommandExecutionResult.ok();

	}

	static LinkType getLinkType(String arrowSring) {
		if (arrowSring.indexOf('[') != -1 || arrowSring.indexOf(']') != -1 || arrowSring.indexOf('=') != -1) {
			throw new IllegalArgumentException(arrowSring);
		}
		if (arrowSring.endsWith(">")) {
			return new LinkType(LinkDecor.ARROW, LinkDecor.NONE);
		}
		return new LinkType(LinkDecor.NONE, LinkDecor.ARROW);
	}

	static Entity getEntity(final Entity lastEntityConsulted, ActivityDiagram system, String arg1, String arg2,
			String arg3, final EntityType type1, final String label) {
		if (arg1 == null) {
			return label == null ? lastEntityConsulted : system.getLastEntityBrancheConsulted();
		}

		if (arg1.equals("(*)")) {
			throw new IllegalArgumentException();
		}

		final String display;
		final String code;
		if (arg2 == null) {
			display = arg1;
			code = arg1;
		} else {
			display = arg2;
			code = arg3 != null ? arg3 : arg2;
		}
		return system.getOrCreate(code, display, type1);

	}

	static String getLabel(String label1, String ifOtherNull) {
		return label1 != null ? label1 : ifOtherNull;
	}

	static EntityType getTypeFromString(String type, final EntityType circle) {
		if (type == null) {
			return EntityType.ACTIVITY;
		}
		if (type.equals("*") || type.equals("\u00A7")) {
			return circle;
		}
		if (type.equals("<>")) {
			return EntityType.BRANCH;
		}
		if (type.equals("[]")) {
			return EntityType.BRANCH;
		}
		if (type.startsWith("=")) {
			return EntityType.SYNCHRO_BAR;
		}
		throw new IllegalArgumentException();
	}

}
