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

import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.SingleLineCommand;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public class CommandArrow extends SingleLineCommand<SequenceDiagram> {

	public CommandArrow(SequenceDiagram sequenceDiagram) {
		super(sequenceDiagram, "(?i)^(\\w+)\\s*(--?[\\>\\]]|[\\<\\[]--?)\\s*(\\w+)\\s*(?::\\s*(.*))?$");
	}

	protected boolean executeArg(List<String> arg) {
		Participant p1;
		Participant p2;

		if (arg.get(1).endsWith("->") || arg.get(1).endsWith("-]")) {
			p1 = getSystem().getOrCreateParticipant(arg.get(0));
			p2 = getSystem().getOrCreateParticipant(arg.get(2));
		} else if (arg.get(1).startsWith("<-") || arg.get(1).startsWith("[-")) {
			p2 = getSystem().getOrCreateParticipant(arg.get(0));
			p1 = getSystem().getOrCreateParticipant(arg.get(2));
		} else {
			throw new IllegalStateException();
		}

		final boolean dotted = arg.get(1).length() == 3;

		final List<String> labels;
		if (arg.get(3) == null) {
			labels = Arrays.asList("");
		} else {
			labels = StringUtils.getWithNewlines(arg.get(3));
		}

		getSystem().addMessage(new Message(p1, p2, labels, dotted));
		return true;
	}

}
