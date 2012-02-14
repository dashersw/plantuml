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
 * Revision $Revision: 7637 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockLineBefore;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.TextBlockWidth;
import net.sourceforge.plantuml.graphic.TextBlockWidthVertical;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class BodyEnhanced implements TextBlockWidth {

	private TextBlockWidth area2;
	private final FontConfiguration titleConfig;
	private final List<String> rawBody;
	private final FontParam fontParam;
	private final ISkinParam skinParam;

	public BodyEnhanced(List<String> rawBody, FontParam fontParam, ISkinParam skinParam) {
		this.rawBody = new ArrayList<String>(rawBody);
		this.fontParam = fontParam;
		this.skinParam = skinParam;

		this.titleConfig = new FontConfiguration(skinParam.getFont(fontParam, null), new Rose().getFontColor(skinParam,
				fontParam));

	}

	private TextBlockWidth decorate(StringBounder stringBounder, TextBlockWidth b, char separator, TextBlock title) {
		if (title == null) {
			return new TextBlockLineBefore(TextBlockUtils.withMargin(b, 6, 4), separator);
		}
		final Dimension2D dimTitle = title.calculateDimension(stringBounder);
		final TextBlockWidth raw = new TextBlockLineBefore(
				TextBlockUtils.withMargin(b, 6, dimTitle.getHeight() / 2, 4), separator, title);
		return TextBlockUtils.withMargin(raw, 0, dimTitle.getHeight() / 2, 0);
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return getArea(stringBounder).calculateDimension(stringBounder);
	}

	private TextBlockWidth getArea(StringBounder stringBounder) {
		if (area2 != null) {
			return area2;
		}
		final List<TextBlockWidth> blocks = new ArrayList<TextBlockWidth>();

		char separator = '_';
		TextBlock title = null;
		List<Member> members = new ArrayList<Member>();
		for (String s : rawBody) {
			if (isBlockSeparator(s)) {
				blocks.add(decorate(stringBounder, new MethodsOrFieldsArea(members, fontParam, skinParam), separator,
						title));
				separator = s.charAt(0);
				title = getTitle(s);
				members = new ArrayList<Member>();
			} else {
				final Member m = new MemberImpl(s, StringUtils.isMethod(s));
				members.add(m);
			}
		}
		blocks.add(decorate(stringBounder, new MethodsOrFieldsArea(members, fontParam, skinParam), separator, title));

		this.area2 = new TextBlockWidthVertical(blocks);
		return area2;
	}

	public void drawU(UGraphic ug, double x, double y, double widthToUse) {
		getArea(ug.getStringBounder()).drawU(ug, x, y, widthToUse);
	}

	public static boolean isBlockSeparator(String s) {
		if (s.startsWith("--") && s.endsWith("--")) {
			return true;
		}
		if (s.startsWith("==") && s.endsWith("==")) {
			return true;
		}
		if (s.startsWith("..") && s.endsWith("..")) {
			return true;
		}
		if (s.startsWith("__") && s.endsWith("__")) {
			return true;
		}
		return false;
	}

	private TextBlock getTitle(String s) {
		if (s.length() <= 4) {
			return null;
		}
		s = s.substring(2, s.length() - 2).trim();
		return TextBlockUtils.create(StringUtils.getWithNewlines(s), titleConfig, HorizontalAlignement.LEFT);
	}

}
