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
package net.sourceforge.plantuml.sequencediagram.command;

import java.util.List;

import net.sourceforge.plantuml.CommandMultilines;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public class CommandMultilinesNoteOverSeveral extends CommandMultilines<SequenceDiagram> {

	public CommandMultilinesNoteOverSeveral(final SequenceDiagram sequenceDiagram) {
		super(sequenceDiagram, "(?i)^note\\s+over\\s+(\\w+)\\s*\\,\\s*(\\w+)$", "(?i)^end ?note$");
	}

	public boolean execute(List<String> lines) {
		final List<String> line0 = StringUtils.getSplit(getStartingPattern(), lines.get(0));

		final Participant p1 = getSystem().getOrCreateParticipant(line0.get(0));
		final Participant p2 = getSystem().getOrCreateParticipant(line0.get(1));

		final List<String> strings = lines.subList(1, lines.size() - 1);
		if (strings.size() > 0) {
			final Note note = new Note(p1, p2, strings);
			getSystem().addNote(note);
		}
		return true;
	}

}
