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
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.Color;
import java.awt.Font;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.graphic.FontChange;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.FontStyle;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlCommand;
import net.sourceforge.plantuml.graphic.Splitter;
import net.sourceforge.plantuml.graphic.Text;

class DotExpression {

	private final StringBuilder sb = new StringBuilder();

	private final Font normalFont;
	private static final Color COLOR = Color.BLACK;

	private FontConfiguration fontConfiguration;

	private final boolean underline;

	DotExpression(String html, int defaultFontSize) {
		this.normalFont = new Font("SansSerif", Font.PLAIN, defaultFontSize);
		this.fontConfiguration = new FontConfiguration(normalFont, COLOR);
		String html2 = html.replaceAll(" \\<[uU]\\>", "<u>");
		html2 = html2.replaceAll("\\</[uU]\\> ", "</u>");
		underline = html.equals(html2) == false;
		final Splitter splitter = new Splitter(html2);
		for (HtmlCommand command : splitter.getHtmlCommands()) {
			if (command instanceof Text) {
				manage((Text) command);
			} else if (command instanceof FontChange) {
				manage((FontChange) command);
			} else {
				Log.error("Cannot manage " + command);
			}

		}
	}

	private void manage(FontChange command) {
		fontConfiguration = command.apply(fontConfiguration);
	}

	private void manage(Text command) {
		sb.append(getFontTag());

		String text = command.getText();
		text = text.replace("<", "&lt;");
		text = text.replace(">", "&gt;");
		text = text.replace("\\n", "<BR/>");
		
		sb.append(text);
		sb.append("</FONT>");
		underline();
	}

	private String getFontTag() {
		underline();
		final int size = fontConfiguration.getFont().getSize();
		final StringBuilder sb = new StringBuilder("<FONT POINT-SIZE=\"");
		sb.append(size);
		sb.append("\"");
		if (fontConfiguration.containsStyle(FontStyle.ITALIC)) {
			sb.append(" FACE=\"italic\"");
		} else if (fontConfiguration.containsStyle(FontStyle.BOLD)) {
			sb.append(" FACE=\"bold\"");
		} else {
			// sb.append(" FACE=\"normal\"");
		}
		final Color col = fontConfiguration.getColor();
		sb.append(" COLOR=\"").append(HtmlColor.getAsHtml(col)).append("\"");
		sb.append(">");
		return sb.toString();
	}

	private void underline() {
		if (fontConfiguration.containsStyle(FontStyle.UNDERLINE)) {
			sb.append("<FONT COLOR=\"#FEFECF\">_</FONT>");
		}
	}

	public String getDotHtml() {
		return sb.toString();

	}

	protected final boolean isUnderline() {
		return underline;
	}

}
