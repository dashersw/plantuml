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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityPackage;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Link;

public abstract class AbstractDiagram implements PSystem, CucaDiagram {

	private int horizontalPages = 1;
	private int verticalPages = 1;

	private final Map<String, Entity> entities = new TreeMap<String, Entity>();

	private final List<Link> links = new ArrayList<Link>();

	private final Map<String, EntityPackage> packages = new LinkedHashMap<String, EntityPackage>();
	private EntityPackage currentPackage = null;

	protected void change() {
		cache.clear();
	}

	protected Entity getOrCreateEntity(String code, EntityType defaultType) {
		Entity result = entities.get(code);
		if (result == null) {
			change();
			result = new Entity(code, code, defaultType, currentPackage);
			entities.put(code, result);
		}
		return result;
	}

	final public Collection<EntityPackage> getPackages() {
		return Collections.unmodifiableCollection(packages.values());
	}

	public EntityPackage getOrCreatePackage(String code) {
		EntityPackage p = packages.get(code);
		if (p == null) {
			change();
			p = new EntityPackage(code);
			packages.put(code, p);
		}
		currentPackage = p;
		return p;
	}

	public Entity createEntity(String code, String display, EntityType type) {
		change();
		if (entities.containsKey(code)) {
			throw new IllegalArgumentException("Already known: " + code);
		}
		if (display == null) {
			display = code;
		}
		final Entity entity = new Entity(code, display, type, currentPackage);
		entities.put(code, entity);
		return entity;
	}

	private final Map<List<Entity>, Boolean> cache = new HashMap<List<Entity>, Boolean>();

	public boolean isDirectlyLinked(Entity ent1, Entity ent2) {
		final List<Entity> key = Arrays.asList(ent1, ent2);
		Boolean result = cache.get(key);
		if (result == null) {
			result = Boolean.valueOf(isDirectlyLinkedSlow(ent1, ent2));
			cache.put(key, result);
		}
		return result.booleanValue();
	}

	private boolean isDirectlyLinkedSlow(Entity ent1, Entity ent2) {
		for (Link link : links) {
			if (link.getEntity1() == ent1 && link.getEntity2() == ent2) {
				return true;
			}
			if (link.getEntity1() == ent2 && link.getEntity2() == ent1) {
				return true;
			}
		}
		return false;
	}

	private boolean isDirectyLinked(Entity ent1, Collection<Entity> others) {
		for (Entity ent2 : others) {
			if (isDirectlyLinked(ent1, ent2)) {
				return true;
			}
		}
		return false;
	}

	public final Set<Link> linksArrivingTo(Entity ent) {
		final Set<Link> result = new HashSet<Link>();
		for (Link link : links) {
			if (link.getEntity2() == ent) {
				result.add(link);
			}
		}
		return Collections.unmodifiableSet(result);
	}

	public final Set<Entity> getAllLinkedTo(Entity ent1) {
		final Set<Entity> result = new HashSet<Entity>();
		result.add(ent1);
		int size = 0;
		do {
			size = result.size();
			for (Entity ent : entities.values()) {
				if (isDirectyLinked(ent, result)) {
					result.add(ent);
				}
			}
		} while (size != result.size());
		return Collections.unmodifiableSet(result);
	}

	final public Map<String, Entity> entities() {
		return Collections.unmodifiableMap(entities);
	}

	final public void addLink(Link link) {
		change();
		links.add(link);
	}

	final public List<Link> getLinks() {
		return Collections.unmodifiableList(links);
	}

	final public int getHorizontalPages() {
		return horizontalPages;
	}

	final public void setHorizontalPages(int horizontalPages) {
		this.horizontalPages = horizontalPages;
	}

	final public int getVerticalPages() {
		return verticalPages;
	}

	final public void setVerticalPages(int verticalPages) {
		this.verticalPages = verticalPages;
	}

}
