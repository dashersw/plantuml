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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartUml {

	private final PSystem system;
	private final String startuml;

	private static final Pattern pattern1 = Pattern.compile("^@startuml\\s+\"?(.*?)\"?$");

	StartUml(PSystem system, String startuml) {
		this.system = system;
		this.startuml = startuml;
	}

	@Override
	public String toString() {
		return startuml + " " + system;
	}

	public PSystem getSystem() {
		return system;
	}

	public String getFilename() {
		if (Option.getInstance().isWord()) {
			return null;
		}
		final Matcher m = pattern1.matcher(startuml);
		final boolean ok = m.find();
		if (ok == false) {
			return null;
		}
		return m.group(1);
	}

	public String getStartuml() {
		return startuml;
	}

}
