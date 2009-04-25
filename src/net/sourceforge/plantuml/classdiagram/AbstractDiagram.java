/* ========================================================================
 * Plantuml : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques (for Atos Origin).
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of Plantuml.
 *
 * Plantuml is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Plantuml distributed in the hope that it will be useful, but
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
package net.sourceforge.plantuml.classdiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.plantuml.PSystem;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Link;

public abstract class AbstractDiagram implements PSystem, CucaDiagram {

	private final Map<String, Entity> entities = new TreeMap<String, Entity>();

	private final List<Link> links = new ArrayList<Link>();

	protected Entity getOrCreateEntity(String code, EntityType defaultType) {
		Entity result = entities.get(code);
		if (result == null) {
			result = new Entity(code, code, defaultType, null);
			entities.put(code, result);
		}
		return result;
	}

	public Entity createEntity(String code, String display, EntityType type, String stereotype) {
		if (entities.containsKey(code)) {
			throw new IllegalArgumentException();
		}
		if (display == null) {
			display = code;
		}
		final Entity entity = new Entity(code, display, type, stereotype);
		entities.put(code, entity);
		return entity;
	}

	final public Map<String, Entity> entities() {
		return Collections.unmodifiableMap(entities);
	}

	final public void addLink(Link link) {
		links.add(link);
	}

	final public List<Link> getLinks() {
		return Collections.unmodifiableList(links);
	}

}
