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
package net.sourceforge.plantuml.usecasediagram.command;

import java.util.List;

import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.usecasediagram.UsecaseDiagram;

public class CommandCreateActor extends SingleLineCommand<UsecaseDiagram> {

	public CommandCreateActor(UsecaseDiagram usecaseDiagram) {
		super(usecaseDiagram, "(?i)^(?:\"([^\"]+)\"\\s+as\\s+)?:?(\\w+):?(?:\\s*([\\<\\[]{2}.*[\\>\\]]{2}))?$");
	}

	@Override
	protected boolean isForbidden(String line) {
		if (line.matches("^\\w+$")) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean executeArg(List<String> arg) {
		final EntityType type = EntityType.ACTOR;
		final String code = arg.get(1);
		final String display = arg.get(0);
		final String stereotype = arg.get(2);
		final Entity entity = getSystem().createEntity(code, display, type);
		if (stereotype != null) {
			entity.setStereotype(new Stereotype(stereotype));
		}
		return true;
	}

}
