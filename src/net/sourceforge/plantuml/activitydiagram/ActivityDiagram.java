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
package net.sourceforge.plantuml.activitydiagram;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.classdiagram.AbstractDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.dot.CucaPngMaker;

public class ActivityDiagram extends AbstractDiagram {

	private Entity lastEntityConsulted;
	private Entity lastEntityBrancheConsulted;

	private Map<String, Partition> partitions = new LinkedHashMap<String, Partition>();

	public Entity getOrCreate(String code, EntityType type) {
		final Entity result = super.getOrCreateEntity(code, type);
		if (result.getType() != type) {
			return null;
		}
		updateLasts(result);
		return result;
	}

	public Partition createPartition(String code, String display) {
		final Partition p = new Partition(code, display);
		partitions.put(code, p);
		return p;
	}

	public Partition getPartition(String code) {
		return partitions.get(code);
	}

	public Partition getPartitionOf(Entity entity) {
		for (Partition p : partitions.values()) {
			if (p.contains(entity)) {
				return p;
			}
		}
		return null;
	}

	private void updateLasts(final Entity result) {
		this.lastEntityConsulted = result;
		if (result.getType() == EntityType.BRANCH) {
			lastEntityBrancheConsulted = result;
		}
	}

	@Override
	public Entity createEntity(String code, String display, EntityType type, String stereotype) {
		final Entity result = super.createEntity(code, display, type, stereotype);
		updateLasts(result);
		return result;
	}

	public List<File> createPng(File pngFile) throws IOException, InterruptedException {

		final CucaPngMaker maker = new CucaPngMaker(this);
		return maker.createPng(pngFile, "nodesep=.20;", "ranksep=0.4;", "edge [fontsize=11,labelfontsize=11];",
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

	public Map<String, Partition> partitions() {
		return Collections.unmodifiableMap(partitions);
	}

}
