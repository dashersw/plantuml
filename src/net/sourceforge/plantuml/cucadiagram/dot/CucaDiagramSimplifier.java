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
 * Revision $Revision: 4524 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.GroupType;

public final class CucaDiagramSimplifier {

	private final CucaDiagram diagram;
	private final boolean isEps;

	public CucaDiagramSimplifier(CucaDiagram diagram, List<String> dotStrings, boolean isEps) throws IOException,
			InterruptedException {
		this.diagram = diagram;
		this.isEps = isEps;
		boolean changed;
		do {
			changed = false;
			final Collection<Group> groups = new ArrayList<Group>(diagram.getGroups());
			for (Group g : groups) {
				if (diagram.isAutarkic(g)) {
					final EntityType type = g.getType() == GroupType.CONCURRENT_STATE ? EntityType.STATE_CONCURRENT
							: EntityType.STATE;
					final Entity proxy = new Entity("#" + g.getCode(), g.getDisplay(), type, g.getParent());
					for (String field : g.getEntityCluster().fields()) {
						proxy.addField(field);
					}
					computeImageGroup(g, proxy, dotStrings);
					diagram.overideGroup(g, proxy);
					changed = true;
				}
			}
		} while (changed);
	}

	private void computeImageGroup(final Group group, final Entity entity, List<String> dotStrings) throws IOException,
			FileNotFoundException, InterruptedException {
		final GroupPngMaker maker = new GroupPngMaker(diagram, group, isEps);
		final File f = CucaDiagramFileMaker.createTempFile("inner");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			maker.createPng(fos, dotStrings);
			final String svg = maker.createSvg(dotStrings);
			entity.setImageFile(new DrawFile(f, svg));
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

}
