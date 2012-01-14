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
 * Revision $Revision: 7559 $
 *
 */
package net.sourceforge.plantuml.command.note;

import java.util.Map;

import net.sourceforge.plantuml.UniqueSequence;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.graphic.HtmlColor;

public abstract class AbstractCommandNoteEntity extends SingleLineCommand2<AbstractEntityDiagram> implements
		CommandNote {

	public AbstractCommandNoteEntity(AbstractEntityDiagram classDiagram, IRegex partialPattern) {
		super(classDiagram, getRegexConcat(partialPattern));
	}

	static RegexConcat getRegexConcat(IRegex partialPattern) {
		return new RegexConcat(new RegexLeaf("^note\\s+"), //
				new RegexLeaf("POSITION", "(right|left|top|bottom)\\s+of\\s+"), //
				partialPattern, // 
				new RegexLeaf("COLOR", "\\s*(#\\w+)?\\s*:\\s*"), //
				new RegexLeaf("NOTE", "(.*)"), //
				new RegexLeaf("$") //
		);
	}

	@Override
	protected CommandExecutionResult executeArg(Map<String, RegexPartialMatch> arg) {
		final String pos = arg.get("POSITION").get(0);
		final IEntity cl1 = getSystem().getOrCreateClass(arg.get("ENTITY").get(0));
		final Entity note = getSystem().createEntity("GN" + UniqueSequence.getValue(), arg.get("NOTE").get(0),
				EntityType.NOTE);
		note.setSpecificBackcolor(HtmlColor.getColorIfValid(arg.get("COLOR").get(0)));

		final Link link;
		final Position position = Position.valueOf(pos.toUpperCase()).withRankdir(getSystem().getRankdir());

		final LinkType type = new LinkType(LinkDecor.NONE, LinkDecor.NONE).getDashed();
		if (position == Position.RIGHT) {
			link = new Link(cl1, note, type, null, 1);
			link.setHorizontalSolitary(true);
		} else if (position == Position.LEFT) {
			link = new Link(note, cl1, type, null, 1);
			link.setHorizontalSolitary(true);
		} else if (position == Position.BOTTOM) {
			link = new Link(cl1, note, type, null, 2);
		} else if (position == Position.TOP) {
			link = new Link(note, cl1, type, null, 2);
		} else {
			throw new IllegalArgumentException();
		}
		getSystem().addLink(link);
		return CommandExecutionResult.ok();

	}

}
