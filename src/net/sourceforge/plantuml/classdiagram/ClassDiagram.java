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
package net.sourceforge.plantuml.classdiagram;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.dot.CucaDiagramPngMaker;

public class ClassDiagram extends CucaDiagram {

	public Entity getOrCreateClass(String code) {
		return getOrCreateEntity(code, EntityType.CLASS);
	}

	public Entity getOrCreateClass(String name, EntityType type) {
		if (type != EntityType.ABSTRACT_CLASS && type != EntityType.CLASS && type != EntityType.INTERFACE
				&& type != EntityType.ENUM) {
			throw new IllegalArgumentException();
		}
		return getOrCreateEntity(name, type);
	}

	public List<File> createPng(File pngFile) throws IOException, InterruptedException {
		final CucaDiagramPngMaker maker = new CucaDiagramPngMaker(this);
		return maker.createPng(pngFile, "nodesep=.5;", "ranksep=0.8;", "edge [fontsize=11,labelfontsize=11];",
				"node [fontsize=11,height=.35,width=.55];");
	}

	public void createPng(OutputStream os) throws IOException {
		final CucaDiagramPngMaker maker = new CucaDiagramPngMaker(this);
		try {
			maker.createPng(os, "nodesep=.5;", "ranksep=0.8;", "edge [fontsize=11,labelfontsize=11];",
					"node [fontsize=11,height=.35,width=.55];");
		} catch (InterruptedException e) {
			Log.error(e.toString());
			throw new IOException(e.toString());
		}
	}

	public String getDescription() {
		return "(" + entities().size() + " entities)";
	}

	public boolean insertBetween(Entity entity1, Entity entity2, Entity node) {
		final Link link = foundLink(entity1, entity2);
		if (link == null) {
			return false;
		}
		final Link l1 = new Link(entity1, node, link.getType(), link.getLabel(), link.getLenght(),
				link.getQualifier1(), null);
		final Link l2 = new Link(node, entity2, link.getType(), link.getLabel(), link.getLenght(), null, link
				.getQualifier2());
		addLink(l1);
		addLink(l2);
		removeLink(link);
		return true;

	}

	private Link foundLink(Entity entity1, Entity entity2) {
		Link result = null;
		final List<Link> links = getLinks();
		for (int i = links.size() - 1; i >= 0; i--) {
			final Link l = links.get(i);
			if (l.isBetween(entity1, entity2)) {
				if (result != null) {
					return null;
				}
				result = l;
			}
		}
		return result;
	}

}
