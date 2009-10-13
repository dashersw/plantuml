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

import java.util.List;

import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.command.SingleLineCommand;

public class CommandPage extends SingleLineCommand<AbstractEntityDiagram> {

	public CommandPage(AbstractEntityDiagram classDiagram) {
		super(classDiagram, "(?i)^page\\s+(\\d+)\\s*x\\s*(\\d+)$");
	}

	@Override
	protected boolean executeArg(List<String> arg) {

		final int horizontal = Integer.parseInt(arg.get(0));
		final int vertical = Integer.parseInt(arg.get(1));
		if (horizontal <= 0 || vertical <= 0) {
			return false;
		}
		getSystem().setHorizontalPages(horizontal);
		getSystem().setVerticalPages(vertical);
		return true;
	}

}
