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
package net.sourceforge.plantuml.cucadiagram;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import net.sourceforge.plantuml.graphic.HtmlColor;


public class EntityPackage {

	private static int CPT = 0;

	private final Collection<Entity> entities = new HashSet<Entity>();
	private final String code;
	private HtmlColor backColor;

	private final int cpt = CPT++;

	public EntityPackage(String code) {
		if (code == null || code.length() == 0) {
			throw new IllegalArgumentException();
		}
		this.code = code;
	}
	
	public void addEntity(Entity entity) {
		if (entities.contains(entity)) {
			throw new IllegalArgumentException();
		}
		entities.add(entity);
	}
	
	public boolean containsFully(Link link) {
		return contains(link.getEntity1()) && contains(link.getEntity2());
	}

	public boolean contains(Entity entity) {
		return entities.contains(entity);
	}



	public Collection<Entity> getEntities() {
		return Collections.unmodifiableCollection(entities);
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

}
