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
 * Revision $Revision: 7558 $
 *
 */
package net.sourceforge.plantuml.command.note.sequence;

import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.note.SingleMultiFactoryCommand;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
import net.sourceforge.plantuml.sequencediagram.AbstractMessage;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public final class FactorySequenceNoteOnArrowCommand implements SingleMultiFactoryCommand<SequenceDiagram> {

	private RegexConcat getRegexConcatMultiLine() {
		return new RegexConcat(new RegexLeaf("^note\\s+"), //
				new RegexLeaf("POSITION", "(right|left)\\s*"), //
				new RegexLeaf("COLOR", "(#\\w+)?"), //
				new RegexLeaf("$"));
	}

	private RegexConcat getRegexConcatSingleLine() {
		return new RegexConcat(new RegexLeaf("^note\\s+"),//
				new RegexLeaf("POSITION", "(right|left)\\s*"), //
				new RegexLeaf("COLOR", "(#\\w+)?\\s*:\\s*"), //
				new RegexLeaf("NOTE", "(.*)"), //
				new RegexLeaf("$"));
	}

	public Command createSingleLine(final SequenceDiagram system) {
		return new SingleLineCommand2<SequenceDiagram>(system, getRegexConcatSingleLine()) {

			@Override
			protected CommandExecutionResult executeArg(Map<String, RegexPartialMatch> arg) {
				final List<String> strings = StringUtils.getWithNewlines(arg.get("NOTE").get(0));
				return executeInternal(getSystem(), arg, strings);
			}

		};
	}

	public Command createMultiLine(final SequenceDiagram system) {
		return new CommandMultilines2<SequenceDiagram>(system, getRegexConcatMultiLine()) {

			@Override
			public String getPatternEnd() {
				return "(?i)^end ?note$";
			}

			public CommandExecutionResult execute(List<String> lines) {
				final Map<String, RegexPartialMatch> line0 = getStartingPattern().matcher(lines.get(0).trim());
				final List<String> in = StringUtils.removeEmptyColumns(lines.subList(1, lines.size() - 1));

				return executeInternal(getSystem(), line0, in);
			}

		};
	}

	private CommandExecutionResult executeInternal(SequenceDiagram system,
			final Map<String, RegexPartialMatch> line0, final List<String> in) {
		final AbstractMessage m = system.getLastMessage();
		if (m != null) {
			final NotePosition position = NotePosition.valueOf(line0.get("POSITION").get(0).toUpperCase());
			final List<CharSequence> strings = StringUtils.manageEmbededDiagrams(in);
			m.setNote(strings, position, line0.get("COLOR").get(0), null);
		}

		return CommandExecutionResult.ok();
	}

}
