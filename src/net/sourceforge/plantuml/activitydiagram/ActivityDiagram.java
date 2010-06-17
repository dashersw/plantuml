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
 * Revision $Revision: 3851 $
 *
 */
package net.sourceforge.plantuml.activitydiagram;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Link;

public class ActivityDiagram extends CucaDiagram {

	private Entity lastEntityConsulted;
	private Entity lastEntityBrancheConsulted;

	public Entity getOrCreate(String code, String display, EntityType type) {
		final Entity result;
		if (entityExist(code)) {
			result = super.getOrCreateEntity(code, type);
			if (result.getType() != type) {
				throw new IllegalArgumentException("Already known: " + code);
				// return null;
			}
		} else {
			result = createEntity(code, display, type);
		}
		updateLasts(result);
		return result;
	}

	public Entity getStart() {
		return getOrCreate("start", "start", EntityType.CIRCLE_START);
	}

	public Entity getEnd() {
		return getOrCreate("end", "end", EntityType.CIRCLE_END);
	}

	final public Link getLastActivityLink() {
		final List<Link> links = getLinks();
		for (int i = links.size() - 1; i >= 0; i--) {
			final Link link = links.get(i);
			if (link.getEntity1().getType() != EntityType.NOTE && link.getEntity2().getType() != EntityType.NOTE) {
				return link;
			}
		}
		return null;
	}

	private void updateLasts(final Entity result) {
		if (result.getType() == EntityType.NOTE) {
			return;
		}
		this.lastEntityConsulted = result;
		if (result.getType() == EntityType.BRANCH) {
			lastEntityBrancheConsulted = result;
		}
	}

	@Override
	public Entity createEntity(String code, String display, EntityType type) {
		final Entity result = super.createEntity(code, display, type);
		updateLasts(result);
		return result;
	}

	public Entity createNote(String code, String display) {
		return super.createEntity(code, display, EntityType.NOTE);
	}

	final protected List<String> getDotStrings() {
		return Arrays.asList("nodesep=.20;", "ranksep=0.4;", "edge [fontsize=11,labelfontsize=11];",
				"node [fontsize=11];");
	}

	public String getDescription() {
		return "(" + entities().size() + " activities)";
	}

	public Entity getLastEntityConsulted() {
		return lastEntityConsulted;
	}

	public Entity getLastEntityBrancheConsulted() {
		return lastEntityBrancheConsulted;
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.ACTIVITY;
	}

}
