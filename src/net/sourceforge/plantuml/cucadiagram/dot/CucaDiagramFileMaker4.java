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
 * Revision $Revision: 4302 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkStyle;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.png.PngSplitter;
import net.sourceforge.plantuml.posimo.Block;
import net.sourceforge.plantuml.posimo.Cluster;
import net.sourceforge.plantuml.posimo.EntityImageBlock;
import net.sourceforge.plantuml.posimo.GraphvizSolverB;
import net.sourceforge.plantuml.posimo.Label;
import net.sourceforge.plantuml.posimo.LabelImage;
import net.sourceforge.plantuml.posimo.Path;
import net.sourceforge.plantuml.posimo.PathDrawer;
import net.sourceforge.plantuml.posimo.PathDrawerInterface;
import net.sourceforge.plantuml.posimo.PathDrawerSquared;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

public final class CucaDiagramFileMaker4 {

	private final CucaDiagram diagram;
	private final Rose rose = new Rose();
	private final Cluster root = new Cluster(null);
	private final Map<Path, Link> paths = new LinkedHashMap<Path, Link>();

	private final Map<Block, Entity> entities = new HashMap<Block, Entity>();
	private final Map<Entity, Block> entities2 = new HashMap<Entity, Block>();

	private int uid = 1;

	public CucaDiagramFileMaker4(CucaDiagram diagram) throws IOException {
		this.diagram = diagram;
	}

	private Block getBlock(Entity ent, Dimension2D dim) {
		final Block b = new Block(uid++, dim.getWidth(), dim.getHeight());
		return b;
	}

	public List<File> createFile(File suggested, List<String> dotStrings, FileFormat fileFormat) throws IOException,
			InterruptedException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(suggested);
			createFile(os, dotStrings, fileFormat);
		} finally {
			if (os != null) {
				os.close();
			}
		}

		if (fileFormat == FileFormat.PNG) {
			final List<File> result = new PngSplitter(suggested, diagram.getHorizontalPages(), diagram
					.getVerticalPages(), diagram.getMetadata()).getFiles();
			for (File f : result) {
				Log.info("Creating file: " + f);
			}
			return result;
		}
		Log.info("Creating file: " + suggested);
		return Arrays.asList(suggested);
	}

	public void createFile(OutputStream os, List<String> dotStrings, FileFormat fileFormat) throws IOException,
			InterruptedException {
		if (fileFormat == FileFormat.PNG) {
			createPng(os, dotStrings);
			// } else if (fileFormat == FileFormat.SVG) {
			// createSvg(os, dotStrings);
			// } else if (fileFormat == FileFormat.EPS) {
			// createEps(os, dotStrings);
		} else {
			throw new UnsupportedOperationException();
		}

	}

	private void createPng(OutputStream os, List<String> dotStrings) throws IOException, InterruptedException {

		final Color background = diagram.getSkinParam().getBackgroundColor().getColor();
		EmptyImageBuilder builder = new EmptyImageBuilder(10, 10, background);
		BufferedImage im = builder.getBufferedImage();
		Graphics2D g2d = builder.getGraphics2D();
		UGraphicG2d ug = new UGraphicG2d(g2d, im);
		StringBounder stringBounder = ug.getStringBounder();

		for (Entity ent : diagram.entities().values()) {
			final Dimension2D d = new EntityImageBlock(ent, rose, diagram.getSkinParam()).getDimension(stringBounder);
			final Block b = getBlock(ent, d);
			entities.put(b, ent);
			entities2.put(ent, b);
			root.addBloc(b);
		}

		for (Link link : diagram.getLinks()) {
			final Block b1 = entities2.get(link.getEntity1());
			final Block b2 = entities2.get(link.getEntity2());
			final Label label;
			if (link.getLabel() == null) {
				label = null;
			} else {
				final LabelImage labelImage = new LabelImage(link, rose, diagram.getSkinParam(), getLabelMargin());
				final Dimension2D dim = labelImage.getDimension(stringBounder);
				label = new Label(dim.getWidth(), dim.getHeight());
			}
			final Path p = new Path(b1, b2, label);
			paths.put(p, link);
		}

		g2d.dispose();

		final GraphvizSolverB solver = new GraphvizSolverB();
		final Dimension2D dim = solver.solve(root, paths.keySet());

		builder = new EmptyImageBuilder((int) (dim.getWidth() + 1), (int) (dim.getHeight() + 1), background);
		im = builder.getBufferedImage();
		g2d = builder.getGraphics2D();
		ug = new UGraphicG2d(g2d, im);
		stringBounder = ug.getStringBounder();

		for (Map.Entry<Path, Link> ent : paths.entrySet()) {
			final LinkType type = ent.getValue().getType();
			final PathDrawer pathDrawer = getPathDrawer(type);
			final Path p = ent.getKey();
			ug.getParam().setColor(rose.getHtmlColor(diagram.getSkinParam(), ColorParam.classBorder).getColor());
			pathDrawer.drawPath(ug, p.getStart(), p.getEnd(), p);
			if (p.getLabel() != null) {
				ug.getParam().setColor(Color.BLACK);
				drawLabel(ug, p);
			}
		}

		for (Map.Entry<Block, Entity> ent : entities.entrySet()) {
			final Entity entity = ent.getValue();
			final Block b = ent.getKey();
			final Point2D pos = b.getPosition();
			new EntityImageBlock(entity, rose, diagram.getSkinParam()).drawU(ug, pos.getX(), pos.getY());
		}

		PngIO.write(im, os);

	}

	private PathDrawer getPathDrawer(final LinkType type) {
		if (type.getStyle() == LinkStyle.INTERFACE_PROVIDER || type.getStyle() == LinkStyle.INTERFACE_USER) {
			return new PathDrawerInterface(new Rose(), diagram.getSkinParam(), type);
		}
		return new PathDrawerSquared(new Rose(), diagram.getSkinParam(), type);
	}
	
	private double getLabelMargin() {
		return 2;
	}

	private void drawLabel(UGraphic ug, Path p) {
//		ug.getParam().setColor(Color.GREEN);
//		ug.getParam().setBackcolor(null);
		final Label label = p.getLabel();
		final Point2D pos = label.getPosition();
		// final Dimension2D dim = label.getSize();
		// ug.draw(pos.getX(), pos.getY(), new URectangle(dim.getWidth(), dim.getHeight()));
		final LabelImage labelImage = new LabelImage(paths.get(p), rose, diagram.getSkinParam(), getLabelMargin());
		labelImage.drawU(ug, pos.getX(), pos.getY());

	}

	/*
	 * private void drawPathOld(UGraphic ug, PointList points) {
	 * ug.getParam().setColor(Color.BLACK); Point2D last = null; for (Point2D
	 * cur : points.getPoints()) { if (last != null) { ug.draw(last.getX(),
	 * last.getY(), new ULine(cur.getX() - last.getX(), cur.getY() -
	 * last.getY())); } last = cur; } }
	 * 
	 * private void drawPath(UGraphic ug, PointList points) {
	 * ug.getParam().setColor(Color.BLACK); Point2D last = null; final int nb =
	 * 10; for (int i = 0; i <= nb; i++) { final Point2D cur =
	 * points.getPoint(1.0 * i / nb); if (last != null) { ug.draw(last.getX(),
	 * last.getY(), new ULine(cur.getX() - last.getX(), cur.getY() -
	 * last.getY())); } last = cur; } }
	 */
}
