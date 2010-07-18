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
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.posimo.Block;
import net.sourceforge.plantuml.posimo.Cluster;
import net.sourceforge.plantuml.posimo.EntityImageBlock;
import net.sourceforge.plantuml.posimo.Frame;
import net.sourceforge.plantuml.posimo.GraphvizSolverB;
import net.sourceforge.plantuml.posimo.Label;
import net.sourceforge.plantuml.posimo.LabelImage;
import net.sourceforge.plantuml.posimo.Path;
import net.sourceforge.plantuml.posimo.PathDrawerInterface;
import net.sourceforge.plantuml.posimo.PositionableUtils;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;

public final class PlayField {

	private final Rose rose = new Rose();
	private final ISkinParam skinParam;

	private final Cluster root = new Cluster(null);
	private final Map<Path, Link> paths = new LinkedHashMap<Path, Link>();

	private final Map<IEntity, Block> blocks = new HashMap<IEntity, Block>();
	private final Map<IEntity, Cluster> clusters = new HashMap<IEntity, Cluster>();
	private final Map<IEntity, EntityImageBlock> images = new HashMap<IEntity, EntityImageBlock>();

	final private double marginLabel = 6;

	private int uid = 1;

	public PlayField(ISkinParam skinParam) {
		this.skinParam = skinParam;
	}

	public void initInternal(Collection<IEntity> entities, Collection<Link> links, StringBounder stringBounder) {
		if (blocks.size() != 0 || paths.size() != 0 || images.size() != 0 || clusters.size() != 0) {
			throw new IllegalStateException();
		}
		if (entities.size() != new HashSet<IEntity>(entities).size()) {
			throw new IllegalArgumentException("Duplicate entities");
		}
		if (links.size() != new HashSet<Link>(links).size()) {
			throw new IllegalArgumentException("Duplicate entities");
		}

		for (IEntity ent : entities) {
			if (ent.getType() == EntityType.GROUP && ent.getParent().isAutonom() == false) {
				clusters.put(ent, new Cluster(root));
				System.err.println("Creating cluster for " + ent + " " + clusters.get(ent));
			}
		}

		for (IEntity ent : entities) {
			System.err.println("ENT=" + ent);
			if (ent.getType() == EntityType.GROUP && ent.getParent().isAutonom() == false) {
				assert clusters.containsKey(ent);
				continue;
			}
			assert clusters.containsKey(ent) == false;
			Cluster parentCluster = root;
			if (ent.getType() != EntityType.GROUP && ent.getParent() != null) {
				parentCluster = clusters.get(ent.getParent().getEntityCluster());
				if (parentCluster == null) {
					parentCluster = root;
				}
			}
			final EntityImageBlock entityImageBlock = new EntityImageBlock(ent, rose, skinParam, links);
			final Dimension2D d = entityImageBlock.getDimension(stringBounder);
			final Block b = new Block(uid++, d.getWidth(), d.getHeight());
			blocks.put(ent, b);
			images.put(ent, entityImageBlock);
			parentCluster.addBloc(b);
		}

		for (Cluster cl : clusters.values()) {
			if (cl.getContents().size() == 0) {
				throw new IllegalStateException();
			}
		}

		for (Link link : links) {
			System.err.println("LINK=" + link);
			if (entities.contains(link.getEntity1()) && entities.contains(link.getEntity2())) {
				final Block b1 = getToto(link.getEntity1());
				final Block b2 = getToto(link.getEntity2());
				final Label label;
				if (link.getLabel() == null) {
					label = null;
				} else {
					final LabelImage labelImage = new LabelImage(link, rose, skinParam);
					final Dimension2D dim = Dimension2DDouble
							.delta(labelImage.getDimension(stringBounder), marginLabel);
					label = new Label(dim.getWidth(), dim.getHeight());
				}
				final Path p = new Path(b1, b2, label);
				paths.put(p, link);
			}
		}
	}

	private Block getToto(IEntity ent) {
		final Block result = blocks.get(ent);
		if (result != null) {
			return result;
		}
		if (clusters.containsKey(ent) == false) {
			throw new IllegalArgumentException();
		}
		return blocks.get(getOneOf(ent.getParent()));
	}

	private IEntity getOneOf(Group gr) {
		assert gr != null;
		return gr.entities().values().iterator().next();
	}

	public void drawInternal(UGraphic ug) {

		for (Map.Entry<IEntity, Cluster> ent : clusters.entrySet()) {
			final Frame frame = new Frame(StringUtils.getWithNewlines(ent.getKey().getDisplay()), Color.BLACK,
					skinParam.getFont(FontParam.CLASS), rose.getHtmlColor(skinParam, ColorParam.classBorder).getColor());
			final Rectangle2D rect = PositionableUtils.convert(ent.getValue());
			final double oldX = ug.getTranslateX();
			final double oldY = ug.getTranslateY();
			ug.translate(rect.getX(), rect.getY());
			frame.drawU(ug, new Dimension2DDouble(rect.getWidth(), rect.getHeight()), null);
			ug.setTranslate(oldX, oldY);
		}

		final double margin = 4;

		for (Map.Entry<Path, Link> ent : paths.entrySet()) {
			final LinkType type = ent.getValue().getType();
			final PathDrawerInterface pathDrawer = new PathDrawerInterface(new Rose(), skinParam, type);
			final Path p = ent.getKey();
			ug.getParam().setColor(rose.getHtmlColor(skinParam, ColorParam.classBorder).getColor());
			pathDrawer.drawPathBefore(ug, PositionableUtils.addMargin(p.getStart(), margin, margin), PositionableUtils
					.addMargin(p.getEnd(), margin, margin), p);
			if (p.getLabel() != null) {
				ug.getParam().setColor(Color.BLACK);
				drawLabel(ug, p);
			}
		}

		for (Map.Entry<IEntity, Block> ent : blocks.entrySet()) {
			final IEntity entity = ent.getKey();
			final Block b = ent.getValue();
			final Point2D pos = b.getPosition();
			images.get(entity).drawU(ug, pos.getX(), pos.getY(), margin, margin);
		}

		for (Map.Entry<Path, Link> ent : paths.entrySet()) {
			final LinkType type = ent.getValue().getType();
			final PathDrawerInterface pathDrawer = new PathDrawerInterface(new Rose(), skinParam, type);
			final Path p = ent.getKey();
			ug.getParam().setColor(rose.getHtmlColor(skinParam, ColorParam.classBorder).getColor());
			pathDrawer.drawPathAfter(ug, PositionableUtils.addMargin(p.getStart(), margin, margin), PositionableUtils
					.addMargin(p.getEnd(), margin, margin), p);
		}
	}

	public Dimension2D solve() throws IOException, InterruptedException {
		final GraphvizSolverB solver = new GraphvizSolverB();
		System.err.println("sub=" + root.getSubClusters());
		final Dimension2D dim = Dimension2DDouble.delta(solver.solve(root, paths.keySet()), 20);
		return dim;
	}

	private void drawLabel(UGraphic ug, Path p) {
		final Label label = p.getLabel();
		final Point2D pos = label.getPosition();
		if (OptionFlags.getInstance().isDebugDot()) {
			ug.getParam().setColor(Color.GREEN);
			ug.getParam().setBackcolor(null);
			final Dimension2D dim = label.getSize();
			ug.draw(pos.getX(), pos.getY(), new URectangle(dim.getWidth(), dim.getHeight()));
			final LabelImage labelImage = new LabelImage(paths.get(p), rose, skinParam);
			final Dimension2D dimImage = labelImage.getDimension(ug.getStringBounder());
			ug.draw(pos.getX(), pos.getY(), new URectangle(dimImage.getWidth(), dimImage.getHeight()));
		}
		final LabelImage labelImage = new LabelImage(paths.get(p), rose, skinParam);
		labelImage.drawU(ug, pos.getX(), pos.getY());

	}
}
