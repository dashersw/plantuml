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
 * Revision $Revision: 4192 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.plantuml.UniqueSequence;
import net.sourceforge.plantuml.graphic.HtmlColor;

public class Group {

	private final Map<String, Entity> entities = new LinkedHashMap<String, Entity>();
	private final String code;
	private final String display;
	private final String namespace;
	
	private HtmlColor backColor;
	private final Group parent;
	private boolean dashed;
	private boolean rounded;
	private boolean bold;

	private final GroupType type;

	private Entity entityCluster;

	private final int cpt = UniqueSequence.getValue();

	public Group(String code, String display, String namespace, GroupType type, Group parent) {
		if (type == null) {
			throw new IllegalArgumentException();
		}
		if (code == null || code.length() == 0) {
			throw new IllegalArgumentException();
		}
		this.namespace = namespace;
		this.type = type;
		this.parent = parent;
		this.code = code;
		this.display = display;
	}

	@Override
	public String toString() {
		return "{" + code + "}";
	}

	public void addEntity(Entity entity) {
		if (entities.containsValue(entity)) {
			throw new IllegalArgumentException();
		}
		if (entities.containsKey(entity.getCode())) {
			throw new IllegalArgumentException(entity.getCode());
		}
		if (entity.getType() == EntityType.GROUP) {
			throw new IllegalArgumentException();
		}
		entities.put(entity.getCode(), entity);
	}

	public boolean containsFully(Link link) {
		return contains((Entity) link.getEntity1()) && contains((Entity) link.getEntity2());
	}

	public boolean contains(Entity entity) {
		return entities.containsValue(entity);
	}

	public Map<String, Entity> entities() {
		return Collections.unmodifiableMap(entities);
	}

	public String getCode() {
		return code;
	}

	public String getUid() {
		return "cluster" + cpt;
	}

	public final HtmlColor getBackColor() {
		return backColor;
	}

	public final void setBackColor(HtmlColor backColor) {
		this.backColor = backColor;
	}

	public final Group getParent() {
		return parent;
	}

	public final boolean isDashed() {
		return dashed;
	}

	public final void setDashed(boolean dashed) {
		this.dashed = dashed;
	}

	public final boolean isRounded() {
		return rounded;
	}

	public final void setRounded(boolean rounded) {
		this.rounded = rounded;
	}

	public GroupType getType() {
		return type;
	}

	public final Entity getEntityCluster() {
		if (entityCluster == null) {
			throw new IllegalStateException();
		}
		return entityCluster;
	}

	public final void setEntityCluster(Entity entityCluster) {
		if (entityCluster == null) {
			throw new IllegalArgumentException();
		}
		this.entityCluster = entityCluster;
	}

//	public boolean isEmpty() {
//		return entities.isEmpty();
//	}

	public String getDisplay() {
		return display;
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public void moveEntitiesTo(Group dest) {
		for (Entity ent : entities.values()) {
			ent.moveTo(dest);
		}
		entities.clear();

	}

	public String getNamespace() {
		return namespace;
	}

}
