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

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.command.CommandMultilines;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Link;

public class CommandLinkLongActivity extends CommandMultilines<ActivityDiagram> {

	public CommandLinkLongActivity(final ActivityDiagram diagram) {
		super(
				diagram,
				"(?i)^([*\\u00A7]|\\<\\>|\\[\\]|==+)?\\s*(\\(\\*\\)|\\w+|\"([^\"]+)\"(?:\\s+as\\s+(\\w+))?)?(?:\\s*=+)?"
						+ "\\s*(\\[[^\\]]+\\])?\\s*([=-]+[\\>\\]]|[\\<\\[][=-]+)\\s*(\\[[^\\]]+\\])?\\s*\"([^\"]*?)\\s*$",
				"(?i)^\\s*([^\"]*)\"(?:\\s+as\\s+(\\w+))?\\s*$");
	}

	@Override
	public boolean isDeprecated(List<String> line) {
		if (line.get(0).indexOf('\u00A7') != -1) {
			return true;
		}
		if (line.get(0).indexOf("*start") != -1) {
			return true;
		}
		if (line.get(0).indexOf("*end") != -1) {
			return true;
		}
		return false;
	}

	@Override
	public String getHelpMessageForDeprecated(List<String> lines) {
		String s = lines.get(0);
		s = s.replaceAll("[\\u00A7]\\w+", "(*)");
		s = s.replaceAll("\\*start", "(*)");
		s = s.replaceAll("\\*end", "(*)");
		return s;
	}

	public boolean execute(List<String> lines) {

		final Entity lastEntityConsulted = getSystem().getLastEntityConsulted();

		final List<String> line0 = StringUtils.getSplit(getStartingPattern(), lines.get(0));

		final EntityType type1 = CommandLinkActivity.getTypeFromString(line0.get(0), EntityType.CIRCLE_START);
		final String label = CommandLinkActivity.getLabel(line0.get(4), line0.get(6));

		final Entity entity1;
		if ("(*)".equals(line0.get(1))) {
			entity1 = getSystem().getStart();
		} else {
			entity1 = CommandLinkActivity.getEntity(lastEntityConsulted, getSystem(), line0.get(1), line0.get(2), line0
					.get(3), type1, label);
		}

		final StringBuilder sb = new StringBuilder();

		if (StringUtils.isNotEmpty(line0.get(7))) {
			sb.append(line0.get(7));
			sb.append("\\n");
		}
		for (int i = 1; i < lines.size() - 1; i++) {
			sb.append(lines.get(i));
			if (i < lines.size() - 2) {
				sb.append("\\n");
			}
		}
		final List<String> lineLast = StringUtils.getSplit(getEnding(), lines.get(lines.size() - 1));
		if (StringUtils.isNotEmpty(lineLast.get(0))) {
			sb.append("\\n");
			sb.append(lineLast.get(0));
		}

		final String display = sb.toString();
		final String code = lineLast.get(1) == null ? display : lineLast.get(1);

		final Entity entity2 = getSystem().createEntity(code, display, EntityType.ACTIVITY);

		if (entity1 == null || entity2 == null) {
			return false;
		}

		final String arrow = StringUtils.manageArrow(line0.get(5));
		final int lenght = arrow.length() - 1;

		final Link link = new Link(entity1, entity2, CommandLinkActivity.getLinkType(arrow), label, lenght, null, null);

		getSystem().addLink(link);

		return true;
	}

}
