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
package net.sourceforge.plantuml;

import java.awt.Font;

public enum FontParam {

	ACTIVITY(14, Font.PLAIN, "black"),
	ACTIVITY_ARROW(13, Font.PLAIN, "black"),
	CLASS_ARROW(10, Font.PLAIN, "black"),
	CLASS_ATTRIBUTE(10, Font.PLAIN, "black"),
	CLASS(12, Font.PLAIN, "black"),
	COMPONENT(14, Font.PLAIN, "black"),
	COMPONENT_ARROW(13, Font.PLAIN, "black"),
	NOTE(13, Font.PLAIN, "black"),
	PACKAGE(14, Font.PLAIN, "black"),
	SEQUENCE_ACTOR(13, Font.PLAIN, "black"),
	SEQUENCE_ARROW(13, Font.PLAIN, "black"),
	SEQUENCE_GROUPING(11, Font.BOLD, "black"),
	SEQUENCE_GROUPING_HEADER(13, Font.BOLD, "black"),
	SEQUENCE_PARTICIPANT(13, Font.PLAIN, "black"),
	SEQUENCE_TITLE(13, Font.BOLD, "black"),
	TITLE(18, Font.PLAIN, "black"),
	FOOTER(10, Font.PLAIN, "#888888"),
	HEADER(10, Font.PLAIN, "#888888"),
	USECASE(14, Font.PLAIN, "black"),
	USECASE_ACTOR(14, Font.PLAIN, "black"),
	USECASE_ARROW(13, Font.PLAIN, "black");
	
	private final int defaultSize;
	private final int fontType;
	private final String defaultColor;

	private FontParam(int defaultSize, int fontType, String defaultColor) {
		this.defaultSize = defaultSize;
		this.fontType = fontType;
		this.defaultColor = defaultColor;
	}

	public final int getDefaultSize() {
		return defaultSize;
	}

	public final int getFontType() {
		return fontType;
	}

	public final String getDefaultColor() {
		return defaultColor;
	}
	
	
}
