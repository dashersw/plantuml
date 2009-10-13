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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	static private final Pattern multiLines = Pattern.compile("((?:\\\\\\\\|[^\\\\])+)(\\\\n)?");

	public static String getPlateformDependentAbsolutePath(File file) {
		// assert file.getAbsolutePath().replace('/',
		// File.separatorChar).equals(file.getAbsolutePath());
		// return file.getAbsolutePath().replace('/', File.separatorChar);
		return file.getAbsolutePath();

	}

	public static List<String> getWithNewlines(String s) {
		if (s == null) {
			throw new IllegalArgumentException();
		}
		final Matcher matcher = multiLines.matcher(s);
		final List<String> strings = new ArrayList<String>();

		while (matcher.find()) {
			strings.add(matcher.group(1).replace("\\\\", "\\"));
		}
		return strings;
	}

	public static String getMergedLines(List<String> strings) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strings.size(); i++) {
			sb.append(strings.get(i));
			if (i < strings.size() - 1) {
				sb.append("\\n");
			}
		}
		return sb.toString();
	}

	final static public List<String> getSplit(Pattern pattern, String line) {
		final Matcher m = pattern.matcher(line);
		if (m.find() == false) {
			return null;
		}
		final List<String> result = new ArrayList<String>();
		for (int i = 1; i <= m.groupCount(); i++) {
			result.add(m.group(i));
		}
		return result;

	}

	public static boolean isNotEmpty(String input) {
		return input != null && input.trim().length() > 0;
	}

	public static String manageHtml(String s) {
		s = s.replace("<", "&lt;");
		s = s.replace(">", "&gt;");
		return s;
	}
	
	public static String manageArrow(String s) {
		s = s.replace('=', '-');
		s = s.replace('[', '<');
		s = s.replace(']', '>');
		return s;
	}

	public static String eventuallyRemoveStartingAndEndingDoubleQuote(String s) {
		if (s.startsWith("\"") && s.endsWith("\"")) {
			return s.substring(1, s.length() - 1);
		}
		return s;
	}

}
