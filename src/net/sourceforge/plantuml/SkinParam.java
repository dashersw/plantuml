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
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.graphic.HtmlColor;

public class SkinParam {

	private final Map<String, String> params = new HashMap<String, String>();

	public void setParam(String key, String value) {
		params.put(key.toLowerCase().trim(), value.trim());
	}

	public HtmlColor getBackgroundColor() {
		final HtmlColor result = getHtmlColor(ColorParam.background);
		if (result == null) {
			return new HtmlColor("white");
		}
		return result;
	}

	public String getValue(String key) {
		return params.get(key.toLowerCase().replaceAll("_", ""));
	}

	public HtmlColor getHtmlColor(ColorParam param) {
		final String value = getValue(param.name() + "color");
		if (value == null || HtmlColor.isValid(value) == false) {
			return null;
		}
		return new HtmlColor(value);
	}

	public int getFontSize(FontParam param) {
		final String value = getValue(param.name() + "fontsize");
		if (value == null || value.matches("\\d+") == false) {
			return param.getDefaultSize();
		}
		return Integer.parseInt(value);
	}

	public String getFontFamily(FontParam param) {
		// Aapex, Times, Helvetica, Courier or Symbol
		return getValue(param.name() + "fontname");
	}

	public HtmlColor getFontHtmlColor(FontParam param) {
		String value = getValue(param.name() + "fontcolor");
		if (value == null) {
			value = param.getDefaultColor();
		}
		return new HtmlColor(value);
	}

	public Font getFont(FontParam fontParam) {
		return new Font(getFontFamily(fontParam), fontParam.getFontType(), getFontSize(fontParam));
	}

}
