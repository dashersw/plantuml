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
 * Revision $Revision: 4236 $
 * 
 */
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Dimension2D;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.cucadiagram.dot.Graphviz;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizUtils;

public class GraphvizSolver {

	public Dimension2D solve(Cluster root, Collection<Path> paths) throws IOException, InterruptedException {
		final String dotString = new DotxMaker(root, paths).createDotString();
		System.err.println("dotString=" + dotString);

		//exportPng(dotString, new File("png", "test1.png"));

		final Graphviz graphviz = GraphvizUtils.create(dotString, "plain-ext");
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		graphviz.createPng(baos);
		baos.close();
		final byte[] result = baos.toByteArray();
		final String s = new String(result, "UTF-8");
		System.err.println("result=" + s);

		final Pattern pGraph = Pattern.compile("(?m)^graph 1 ([0-9.]+) ([0-9.]+)");
		final Matcher mGraph = pGraph.matcher(s);
		if (mGraph.find() == false) {
			throw new IllegalStateException();
		}
		final double width = 72.0 * Double.parseDouble(mGraph.group(1));
		final double height = 72.0 * Double.parseDouble(mGraph.group(2));
		final Mirror mirror = new Mirror(height);

		final Pattern pNode = Pattern.compile("(?m)^node b(\\d+) ([-0-9.]+) ([-0-9.]+) ");
		final Matcher mNode = pNode.matcher(s);
		while (mNode.find()) {
			final Block b = root.getBlock(Integer.parseInt(mNode.group(1)));
			b.setCenterX(Double.parseDouble(mNode.group(2)) * 72.0);
			b.setCenterY(mirror.getMirrored(Double.parseDouble(mNode.group(3)) * 72.0));
		}

		final Pattern pedge = Pattern
				.compile("(?m)^edge b(\\d+) b(\\d+) (\\d+) ([-0-9. ]+) (?:\"[^\"]*\"|\\w+) ([-0-9.]+) ([-0-9.]+)");
		final Matcher mEdge = pedge.matcher(s);
		while (mEdge.find()) {
			final Path p = getPath(paths, Integer.parseInt(mEdge.group(1)), Integer.parseInt(mEdge.group(2)));
			final int nb = Integer.parseInt(mEdge.group(3));
			StringTokenizer st = new StringTokenizer(mEdge.group(4), " ");
			for (int i = 0; i < nb; i++) {
				final double x = Double.parseDouble(st.nextToken());
				final double y = Double.parseDouble(st.nextToken());
				p.addPoint(x * 72.0, mirror.getMirrored(y * 72.0));
			}
			final double labelX = Double.parseDouble(mEdge.group(5));
			final double labelY = Double.parseDouble(mEdge.group(6));
			p.setLabelPositionCenter(labelX * 72.0, mirror.getMirrored(labelY * 72.0));
		}

		return new Dimension2DDouble(width, height);
	}

	private void exportPng(final String dotString, File f) throws IOException, InterruptedException {
		final Graphviz graphviz = GraphvizUtils.create(dotString, "png");
		final OutputStream os = new FileOutputStream(f);
		graphviz.createPng(os);
		os.close();
	}

	private Path getPath(Collection<Path> paths, int start, int end) {
		for (Path p : paths) {
			if (p.getStart().getUid() == start && p.getEnd().getUid() == end) {
				return p;
			}
		}
		throw new IllegalArgumentException();

	}
}
