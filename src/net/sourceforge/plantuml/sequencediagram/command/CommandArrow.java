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
 * Revision $Revision: 5424 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.command;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDecoration;
import net.sourceforge.plantuml.skin.ArrowDirection;
import net.sourceforge.plantuml.skin.ArrowHead;
import net.sourceforge.plantuml.skin.ArrowPart;

public class CommandArrow extends SingleLineCommand2<SequenceDiagram> {

	public CommandArrow(SequenceDiagram sequenceDiagram) {
		super(sequenceDiagram, getRegexConcat());
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexOr("PART1", //
						new RegexLeaf("PART1CODE", "([\\p{L}0-9_.@]+)"), //
						new RegexLeaf("PART1LONG", "\"([^\"]+)\""), //
						new RegexLeaf("PART1LONGCODE", "\"([^\"]+)\"\\s*as\\s+([\\p{L}0-9_.@]+)"), //
						new RegexLeaf("PART1CODELONG", "([\\p{L}0-9_.@]+)\\s+as\\s*\"([^\"]+)\"")), new RegexLeaf(
						"\\s*"), //
				new RegexOr("ARROW", //
						new RegexLeaf("ARROW_DIRECT", "( o)?[=-]+(>>?|//?|\\\\\\\\?|x )([ox] )?"), //
						new RegexLeaf("ARROW_REVERSE", "( [xo])?( x|<<?|//?|\\\\\\\\?)[=-]+(o )?")), //
				new RegexLeaf("\\s*"), //
				new RegexOr("PART2", //
						new RegexLeaf("PART2CODE", "([\\p{L}0-9_.@]+)"), //
						new RegexLeaf("PART2LONG", "\"([^\"]+)\""), //
						new RegexLeaf("PART2LONGCODE", "\"([^\"]+)\"\\s*as\\s+([\\p{L}0-9_.@]+)"), //
						new RegexLeaf("PART2CODELONG", "([\\p{L}0-9_.@]+)\\s+as\\s*\"([^\"]+)\"")), new RegexLeaf(
						"\\s*"), //
				new RegexLeaf("ACTIVATION", "(?:([+*!-]+)?)"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("LIFECOLOR", "(?:(#\\w+)?)"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("MESSAGE", "(?::\\s*(.*))?$"));
	}

	private Participant getOrCreateParticipant(Map<String, RegexPartialMatch> arg2, String n) {
		final String code;
		final List<String> display;
		if (arg2.get(n + "CODE").get(0) != null) {
			code = arg2.get(n + "CODE").get(0);
			display = StringUtils.getWithNewlines(code);
		} else if (arg2.get(n + "LONG").get(0) != null) {
			code = arg2.get(n + "LONG").get(0);
			display = StringUtils.getWithNewlines(code);
		} else if (arg2.get(n + "LONGCODE").get(0) != null) {
			display = StringUtils.getWithNewlines(arg2.get(n + "LONGCODE").get(0));
			code = arg2.get(n + "LONGCODE").get(1);
		} else if (arg2.get(n + "CODELONG").get(0) != null) {
			code = arg2.get(n + "CODELONG").get(0);
			display = StringUtils.getWithNewlines(arg2.get(n + "CODELONG").get(1));
			return getSystem().getOrCreateParticipant(code, display);
		} else {
			throw new IllegalStateException();
		}
		return getSystem().getOrCreateParticipant(code, display);
	}

	private boolean decorationAtStart(Map<String, RegexPartialMatch> arg2, String decoration) {
		return decorationAtPosition(arg2, 0, 2, decoration);
	}

	private boolean decorationAtEnd(Map<String, RegexPartialMatch> arg2, String decoration) {
		return decorationAtPosition(arg2, 2, 0, decoration);
	}

	private boolean decorationAtPosition(Map<String, RegexPartialMatch> arg2, int posDirect, int posReverse,
			String decoration) {
		final String s1 = arg2.get("ARROW_DIRECT").get(posDirect);
		if (s1 != null && s1.contains(decoration)) {
			return true;
		}
		final String s2 = arg2.get("ARROW_REVERSE").get(posReverse);
		if (s2 != null && s2.contains(decoration)) {
			return true;
		}
		return false;
	}

	@Override
	protected CommandExecutionResult executeArg(Map<String, RegexPartialMatch> arg2) {

		String fullArrow = StringUtils.manageArrowForSequence(arg2.get("ARROW").get(0));
		final String arrowWithX = fullArrow.replaceAll("[ o]", "");
		final String arrow = fullArrow.replaceAll("[ ox]", "");
		final boolean circleAtStart = decorationAtStart(arg2, "o");
		final boolean circleAtEnd = decorationAtEnd(arg2, "o");
		final boolean crossAtEnd = decorationAtEnd(arg2, "x") || decorationAtPosition(arg2, 1, 1, "x");
		Participant p1;
		Participant p2;

		if (arrowWithX.endsWith(">") || arrowWithX.endsWith("\\") || arrowWithX.endsWith("/")
				|| arrowWithX.endsWith("x")) {
			p1 = getOrCreateParticipant(arg2, "PART1");
			p2 = getOrCreateParticipant(arg2, "PART2");
		} else if (arrowWithX.startsWith("x") || arrowWithX.startsWith("<") || arrowWithX.startsWith("\\")
				|| arrowWithX.startsWith("/")) {
			p2 = getOrCreateParticipant(arg2, "PART1");
			p1 = getOrCreateParticipant(arg2, "PART2");
		} else {
			throw new IllegalStateException(fullArrow);
		}

		final boolean sync = arrow.endsWith(">>") || arrow.startsWith("<<") || arrow.contains("\\\\")
				|| arrow.contains("//");

		final boolean dotted = arrow.contains("--");

		final List<String> labels;
		if (arg2.get("MESSAGE").get(0) == null) {
			labels = Arrays.asList("");
		} else {
			labels = StringUtils.getWithNewlines(arg2.get("MESSAGE").get(0));
		}

		ArrowConfiguration config = ArrowConfiguration.withDirection(ArrowDirection.LEFT_TO_RIGHT_NORMAL);
		if (dotted) {
			config = config.withDotted();
		}
		if (sync) {
			config = config.withHead(ArrowHead.ASYNC);
		}
		if (arrow.endsWith("\\") || arrow.startsWith("/")) {
			config = config.withPart(ArrowPart.TOP_PART);
		}
		if (arrow.endsWith("/") || arrow.startsWith("\\")) {
			config = config.withPart(ArrowPart.BOTTOM_PART);
		}
		if (circleAtEnd) {
			config = config.withDecorationEnd(ArrowDecoration.CIRCLE);
		}
		if (circleAtStart) {
			config = config.withDecorationStart(ArrowDecoration.CIRCLE);
		}
		if (crossAtEnd) {
			config = config.withDecorationEnd(ArrowDecoration.CROSSX);
		}
		// if (crossAtStart) {
		// config = config.withDecorationStart(ArrowDecoration.CROSSX);
		// }

		final String activationSpec = arg2.get("ACTIVATION").get(0);

		if (activationSpec != null && activationSpec.charAt(0) == '*') {
			getSystem().activate(p2, LifeEventType.CREATE, null);
		}

		final String error = getSystem().addMessage(
				new Message(p1, p2, labels, config, getSystem().getNextMessageNumber()));
		if (error != null) {
			return CommandExecutionResult.error(error);
		}

		final HtmlColor activationColor = HtmlColor.getColorIfValid(arg2.get("LIFECOLOR").get(0));

		if (activationSpec != null) {
			switch (activationSpec.charAt(0)) {
			case '+':
				getSystem().activate(p2, LifeEventType.ACTIVATE, activationColor);
				break;
			case '-':
				getSystem().activate(p1, LifeEventType.DEACTIVATE, null);
				break;
			case '!':
				getSystem().activate(p2, LifeEventType.DESTROY, null);
				break;
			default:
				break;
			}
		} else if (getSystem().isAutoactivate()) {
			if (config.getHead() == ArrowHead.NORMAL) {
				if (config.isDotted()) {
					getSystem().activate(p1, LifeEventType.DEACTIVATE, null);
				} else {
					getSystem().activate(p2, LifeEventType.ACTIVATE, activationColor);
				}
			}
		}
		return CommandExecutionResult.ok();
	}

}
