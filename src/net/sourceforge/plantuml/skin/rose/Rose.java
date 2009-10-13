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

import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Skin;

public class Rose implements Skin {

	private final Font normalFont = new Font("SansSerif", Font.PLAIN, 13);
	private final Font bigFont = new Font("SansSerif", Font.BOLD, 13);
	private final Font smallFont = new Font("SansSerif", Font.BOLD, 11);

	public HtmlColor getFontHtmlColor(SkinParam param) {
		final HtmlColor result = param.getFontColor();
		if (result == null) {
			return new HtmlColor("black");
		}
		return result;
	}

	private Color getFontColor(SkinParam param) {
		return getFontHtmlColor(param).getColor();
	}

	public HtmlColor getBorderHtmlColor(SkinParam param) {
		final HtmlColor result = param.getBorderColor();
		if (result == null) {
			return new HtmlColor("#A80036");
		}
		return result;
	}

	private Color getBorderColor(SkinParam param) {
		return getBorderHtmlColor(param).getColor();
	}

	public HtmlColor getArrowHtmlColor(SkinParam param) {
		final HtmlColor result = param.getArrowColor();
		if (result == null) {
			return new HtmlColor("#A80036");
		}
		return result;
	}

	public Color getArrowColor(SkinParam param) {
		return getArrowHtmlColor(param).getColor();
	}

	public HtmlColor getBoxHtmlColor(SkinParam param) {
		final HtmlColor result = param.getBoxBackgroundColor();
		if (result == null) {
			return new HtmlColor("#FEFECE");
		}
		return result;
	}

	private Color getBoxColor(SkinParam param) {
		return getBoxHtmlColor(param).getColor();
	}

	public HtmlColor getNoteHtmlColor(SkinParam param) {
		final HtmlColor result = param.getNoteBackgroundColor();
		if (result == null) {
			return new HtmlColor("#FBFB77");
		}
		return result;
	}

	private Color getNoteColor(SkinParam param) {
		return getNoteHtmlColor(param).getColor();
	}

	public Component createComponent(ComponentType type, SkinParam param, List<? extends CharSequence> stringsToDisplay) {
		if (type == ComponentType.SELF_ARROW) {
			return new ComponentRoseSelfArrow(getArrowColor(param), getFontColor(param), normalFont, stringsToDisplay,
					false);
		}
		if (type == ComponentType.DOTTED_SELF_ARROW) {
			return new ComponentRoseSelfArrow(getArrowColor(param), getFontColor(param), normalFont, stringsToDisplay,
					true);
		}
		if (type == ComponentType.ARROW) {
			return new ComponentRoseArrow(getArrowColor(param), getFontColor(param), normalFont, stringsToDisplay, 1,
					false);
		}
		if (type == ComponentType.RETURN_ARROW) {
			return new ComponentRoseArrow(getArrowColor(param), getFontColor(param), normalFont, stringsToDisplay, -1,
					false);
		}
		if (type == ComponentType.DOTTED_ARROW) {
			return new ComponentRoseArrow(getArrowColor(param), getFontColor(param), normalFont, stringsToDisplay, 1,
					true);
		}
		if (type == ComponentType.RETURN_DOTTED_ARROW) {
			return new ComponentRoseArrow(getArrowColor(param), getFontColor(param), normalFont, stringsToDisplay, -1,
					true);
		}
		if (type == ComponentType.PARTICIPANT_HEAD) {
			return new ComponentRoseParticipant(getBoxColor(param), getBorderColor(param), getFontColor(param),
					normalFont, stringsToDisplay);
		}
		if (type == ComponentType.PARTICIPANT_TAIL) {
			return new ComponentRoseParticipant(getBoxColor(param), getBorderColor(param), getFontColor(param),
					normalFont, stringsToDisplay);
		}
		if (type == ComponentType.PARTICIPANT_LINE) {
			return new ComponentRoseLine(getBorderColor(param));
		}
		if (type == ComponentType.ACTOR_HEAD) {
			return new ComponentRoseActor(getBoxColor(param), getBorderColor(param), getFontColor(param), normalFont,
					stringsToDisplay, true);
		}
		if (type == ComponentType.ACTOR_TAIL) {
			return new ComponentRoseActor(getBoxColor(param), getBorderColor(param), getFontColor(param), normalFont,
					stringsToDisplay, false);
		}
		if (type == ComponentType.ACTOR_LINE) {
			return new ComponentRoseLine(getBorderColor(param));
		}
		if (type == ComponentType.NOTE) {
			return new ComponentRoseNote(getNoteColor(param), getBorderColor(param), getFontColor(param), normalFont,
					stringsToDisplay);
		}
		if (type == ComponentType.GROUPING_HEADER) {
			return new ComponentRoseGroupingHeader(getFontColor(param), bigFont, smallFont, stringsToDisplay);
		}
		if (type == ComponentType.GROUPING_BODY) {
			return new ComponentRoseGroupingBody(getFontColor(param));
		}
		if (type == ComponentType.GROUPING_TAIL) {
			return new ComponentRoseGroupingTail(getFontColor(param));
		}
		if (type == ComponentType.GROUPING_ELSE) {
			return new ComponentRoseGroupingElse(getFontColor(param), smallFont, stringsToDisplay.get(0));
		}
		if (type == ComponentType.ALIVE_LINE) {
			return new ComponentRoseActiveLine(getBorderColor(param));
		}
		if (type == ComponentType.DESTROY) {
			return new ComponentRoseDestroy(getBorderColor(param));
		}
		if (type == ComponentType.NEWPAGE) {
			return new ComponentRoseNewpage(getFontColor(param));
		}
		if (type == ComponentType.TITLE) {
			return new ComponentRoseTitle(getFontColor(param), bigFont, stringsToDisplay);
		}
		if (type == ComponentType.SIGNATURE) {
			return new ComponentRoseTitle(Color.BLACK, smallFont, Arrays.asList("This skin was created ",
					"in April 2009."));
		}
		return null;
	}

	public Object getProtocolVersion() {
		return 1;
	}

}
