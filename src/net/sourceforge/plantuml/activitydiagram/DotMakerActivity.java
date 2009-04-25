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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.dot.DotMaker;

public class DotMakerActivity extends DotMaker {

	public DotMakerActivity(ActivityDiagram diagram, String... dotStrings) {
		super(diagram, dotStrings);
	}

	@Override
	public void generateFile(File out, File actorFile, Map<Entity, File> images) throws IOException {
		if (getActivityDiagram().partitions().size() == 0) {
			super.generateFile(out, actorFile, images);
			return;
		}

		final PrintWriter pw = initPrintWriter(out);
		printPartitions(pw);
		printTranspartitionsLinks(pw);

		pw.println("}");
		pw.close();
	}

	private void printTranspartitionsLinks(PrintWriter pw) {
		final List<Link> links = new ArrayList<Link>();
		for (Link link : getActivityDiagram().getLinks()) {
			if (isTranspartitionsLinks(link)) {
				links.add(link);
			}
		}
		this.printLinks(pw, links);
	}

	private boolean isTranspartitionsLinks(Link link) {
		final Partition p1 = getActivityDiagram().getPartitionOf(link.getEntity1());
		final Partition p2 = getActivityDiagram().getPartitionOf(link.getEntity2());
		return p1.equals(p2) == false;
	}

	private ActivityDiagram getActivityDiagram() {
		return (ActivityDiagram) getDiagram();
	}

	private void printPartitions(PrintWriter pw) {
		int nb = 0;
		for (Partition p : getActivityDiagram().partitions().values()) {
			pw.println("subgraph cluster" + nb + " {");
			pw.println("label=\"" + p.getDisplay() + "\";");
			pw.println("color=white;");
			this.printEntities(pw, p.getEntities());
			final List<Link> links = new ArrayList<Link>();
			for (Link link : getActivityDiagram().getLinks()) {
				if (p.containsFully(link)) {
					links.add(link);
				}
			}
			this.printLinks(pw, links);
			pw.println("}");
			nb++;
		}

	}

}
