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
package net.sourceforge.plantuml.command.note;

import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.graphic.HtmlColor;

public final class FactoryNoteOnLinkCommand implements SingleMultiFactoryCommand<CucaDiagram> {

	private RegexConcat getRegexConcatSingleLine() {
		return new RegexConcat(new RegexLeaf("^note\\s+"),//
				new RegexLeaf("POSITION", "(right|left|top|bottom)?\\s*on\\s+link"),//
				new RegexLeaf("COLOR", "\\s*(#\\w+)?\\s*:\\s*"), //
				new RegexLeaf("NOTE", "(.*)"),//
				new RegexLeaf("$"));
	}

	private RegexConcat getRegexConcatMultiLine() {
		return new RegexConcat(new RegexLeaf("^note\\s+"),//
				new RegexLeaf("POSITION", "(right|left|top|bottom)?\\s*on\\s+link"),//
				new RegexLeaf("COLOR", "\\s*(#\\w+)?"), //
				new RegexLeaf("$"));
	}

	public Command createMultiLine(final CucaDiagram system) {
		return new CommandMultilines2<CucaDiagram>(system, getRegexConcatMultiLine()) {

			@Override
			public String getPatternEnd() {
				return "(?i)^end ?note$";
			}

			public CommandExecutionResult execute(List<String> lines) {
				final List<String> strings = StringUtils.removeEmptyColumns(lines.subList(1, lines.size() - 1));
				if (strings.size() > 0) {
					final List<CharSequence> note = StringUtils.manageEmbededDiagrams(strings);
					final Map<String, RegexPartialMatch> arg = getStartingPattern().matcher(lines.get(0));
					return executeInternal(getSystem(), note, arg);
				}
				return CommandExecutionResult.error("No note defined");
			}

		};
	}

	public Command createSingleLine(final CucaDiagram system) {
		return new SingleLineCommand2<CucaDiagram>(system, getRegexConcatSingleLine()) {

			@Override
			protected CommandExecutionResult executeArg(Map<String, RegexPartialMatch> arg) {
				final List<String> note = StringUtils.getWithNewlines(arg.get("NOTE").get(0));
				return executeInternal(getSystem(), note, arg);
			}
		};
	}

	private CommandExecutionResult executeInternal(CucaDiagram system, final List<? extends CharSequence> note,
			final Map<String, RegexPartialMatch> arg) {
		final Link link = system.getLastLink();
		if (link == null) {
			return CommandExecutionResult.error("No link defined");
		}
		Position position = Position.BOTTOM;
		if (arg.get("POSITION").get(0) != null) {
			position = Position.valueOf(arg.get("POSITION").get(0).toUpperCase());
		}
		link.addNote(note, position, HtmlColor.getColorIfValid(arg.get("COLOR").get(0)));
		return CommandExecutionResult.ok();
	}

}
