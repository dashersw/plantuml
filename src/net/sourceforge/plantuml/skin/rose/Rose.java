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
package net.sourceforge.plantuml.skin.rose;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Skin;

public class Rose implements Skin {

	private final Font normalFont = new Font("SansSerif", Font.PLAIN, 13);
	private final Font bigFont = new Font("SansSerif", Font.BOLD, 13);
	private final Font smallFont = new Font("SansSerif", Font.BOLD, 11);

	private final Color yellowNote = new Color(Integer.parseInt("FBFB77", 16));
	private final Color yellow = new Color(Integer.parseInt("FEFECE", 16));
	private final Color red = new Color(Integer.parseInt("A80036", 16));

	private final Color fontColor = Color.BLACK;

	public Component createComponent(ComponentType type, List<String> stringsToDisplay) {
		if (type == ComponentType.SELF_ARROW) {
			return new ComponentRoseSelfArrow(red, fontColor, normalFont, stringsToDisplay, false);
		}
		if (type == ComponentType.DOTTED_SELF_ARROW) {
			return new ComponentRoseSelfArrow(red, fontColor, normalFont, stringsToDisplay, true);
		}
		if (type == ComponentType.ARROW) {
			return new ComponentRoseArrow(red, fontColor, normalFont, stringsToDisplay, 1, false);
		}
		if (type == ComponentType.RETURN_ARROW) {
			return new ComponentRoseArrow(red, fontColor, normalFont, stringsToDisplay, -1, false);
		}
		if (type == ComponentType.DOTTED_ARROW) {
			return new ComponentRoseArrow(red, fontColor, normalFont, stringsToDisplay, 1, true);
		}
		if (type == ComponentType.RETURN_DOTTED_ARROW) {
			return new ComponentRoseArrow(red, fontColor, normalFont, stringsToDisplay, -1, true);
		}
		if (type == ComponentType.PARTICIPANT_HEAD) {
			return new ComponentRoseParticipant(yellow, red, fontColor, normalFont, stringsToDisplay);
		}
		if (type == ComponentType.PARTICIPANT_TAIL) {
			return new ComponentRoseParticipant(yellow, red, fontColor, normalFont, stringsToDisplay);
		}
		if (type == ComponentType.PARTICIPANT_LINE) {
			return new ComponentRoseLine(red);
		}
		if (type == ComponentType.ACTOR_HEAD) {
			return new ComponentRoseActor(yellow, red, fontColor, normalFont, stringsToDisplay, true);
		}
		if (type == ComponentType.ACTOR_TAIL) {
			return new ComponentRoseActor(yellow, red, fontColor, normalFont, stringsToDisplay, false);
		}
		if (type == ComponentType.ACTOR_LINE) {
			return new ComponentRoseLine(red);
		}
		if (type == ComponentType.NOTE) {
			return new ComponentRoseNote(yellowNote, red, fontColor, normalFont, stringsToDisplay);
		}
		if (type == ComponentType.GROUPING_HEADER) {
			return new ComponentRoseGroupingHeader(fontColor, bigFont, smallFont, stringsToDisplay);
		}
		if (type == ComponentType.GROUPING_BODY) {
			return new ComponentRoseGroupingBody(fontColor);
		}
		if (type == ComponentType.GROUPING_TAIL) {
			return new ComponentRoseGroupingTail(fontColor);
		}
		if (type == ComponentType.GROUPING_ELSE) {
			return new ComponentRoseGroupingElse(fontColor, smallFont, stringsToDisplay.get(0));
		}
		if (type == ComponentType.ALIVE_LINE) {
			return new ComponentRoseActiveLine(red);
		}
		if (type == ComponentType.DESTROY) {
			return new ComponentRoseDestroy(red);
		}
		if (type == ComponentType.NEWPAGE) {
			return new ComponentRoseNewpage(fontColor);
		}
		if (type == ComponentType.TITLE) {
			return new ComponentRoseTitle(fontColor, bigFont, stringsToDisplay);
		}
		if (type == ComponentType.SIGNATURE) {
			return new ComponentRoseTitle(Color.BLACK, smallFont, Arrays.asList("This skin was created ", "in April 2009."));
		}
		return null;
	}

	public Object getProtocolVersion() {
		return 1;
	}

}
