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
package net.sourceforge.plantuml.classdiagram.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.classdiagram.ClassDiagramFactory;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.SingleLineCommand;

public class CommandMultiple extends SingleLineCommand<ClassDiagram> {

	private final static Pattern p = Pattern.compile("((?:\"[^\"]+\"\\s*){0,1}[-|*<.>o]{2,}(?:\\s*\"[^\"]+\")?|\\w+)");

	private final ClassDiagramFactory factory;

	public CommandMultiple(ClassDiagram classDiagram, ClassDiagramFactory classDiagramFactory) {
		super(classDiagram, "(?i)^(\\w+(?:\\s*(?:\"[^\"]+\")?\\s*[-|*<.>o]{2,}\\s*(?:\"[^\"]+\")?\\s*\\w+){2,})$");
		this.factory = classDiagramFactory;
	}

	@Override
	protected boolean executeArg(List<String> arg) {
		if (arg.size() != 1) {
			throw new IllegalArgumentException();
		}
		arg = analyse(arg.get(0));
		if (arg.size() % 2 != 1) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < arg.size() - 2; i += 2) {
			final String simpleLine = arg.get(i) + " " + arg.get(i + 1) + " " + arg.get(i + 2);
			final List<Command> simpleCmd = factory.create(Arrays.asList(simpleLine));
			if (simpleCmd == null) {
				throw new IllegalArgumentException(simpleLine);
			}
			final boolean ok = simpleCmd.get(0).execute(Arrays.asList(simpleLine));
			if (ok == false) {
				return false;
			}
		}

		return true;
	}

	List<String> analyse(String arg) {
		final Matcher m = p.matcher(arg);
		final List<String> result = new ArrayList<String>();
		while (m.find()) {
			result.add(m.group(1));
		}
		return result;
	}

}
