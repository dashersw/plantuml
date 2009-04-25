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

import net.sourceforge.plantuml.CommandMultilines;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.cucadiagram.EntityType;

public class CommandMultilinesStandaloneNote extends CommandMultilines<ClassDiagram> {

	public CommandMultilinesStandaloneNote(final ClassDiagram system) {
		super(system, "(?i)^(note)\\s+as\\s+(\\w+)$", "(?i)^end ?note$");
	}

	public boolean execute(List<String> lines) {

		final List<String> line0 = StringUtils.getSplit(getStartingPattern(), lines.get(0));

		final List<String> strings = lines.subList(1, lines.size() - 1);
		final String display = StringUtils.getMergedLines(strings);

		final EntityType type = EntityType.NOTE;
		final String code = line0.get(1);
		final String stereotype = null;
		getSystem().createEntity(code, display, type, stereotype);

		return true;
	}

}
