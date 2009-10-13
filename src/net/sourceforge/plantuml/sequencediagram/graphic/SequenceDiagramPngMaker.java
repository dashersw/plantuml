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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.png.PngRotation;
import net.sourceforge.plantuml.png.PngSizer;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.LifeEvent;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Newpage;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.Skin;

public class SequenceDiagramPngMaker {

	private static final Graphics2D g2dummy;

	static {
		final BufferedImage imDummy = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		g2dummy = imDummy.createGraphics();

	}

	private final SequenceDiagram sequenceDiagram;

	private final DrawableSet drawableSet;

	private final Dimension2D fullDimension;
	private final List<Page> pages;

	public SequenceDiagramPngMaker(SequenceDiagram sequenceDiagram, Skin skin) {
		this.sequenceDiagram = sequenceDiagram;
		final DrawableSetInitializer initializer = new DrawableSetInitializer(skin, sequenceDiagram.getSkinParam(),
				sequenceDiagram.isShowFootbox());

		for (Participant p : sequenceDiagram.participants().values()) {
			initializer.addParticipant(p);
		}

		final List<Newpage> newpages = new ArrayList<Newpage>();
		for (Event ev : sequenceDiagram.events()) {
			initializer.addEvent(ev);
			if (ev instanceof Message) {
				// TODO mieux faire
				final Message m = (Message) ev;
				for (LifeEvent lifeEvent : m.getLiveEvents()) {
					if (lifeEvent.getType() == LifeEventType.DESTROY
					/*
					 * || lifeEvent.getType() == LifeEventType.CREATE
					 */) {
						initializer.addEvent(lifeEvent);
					}
				}

			}
			if (ev instanceof Newpage) {
				newpages.add((Newpage) ev);
			}
		}
		drawableSet = initializer.createDrawableSet(g2dummy);
		fullDimension = drawableSet.getDimension();
		final Map<Newpage, Double> positions = new LinkedHashMap<Newpage, Double>();
		for (Newpage n : newpages) {
			positions.put(n, initializer.getYposition(g2dummy, n));
		}
		pages = create(drawableSet, positions, sequenceDiagram.isShowFootbox(), sequenceDiagram.getTitle()).getPages();

	}

	private PageSplitter create(DrawableSet drawableSet, Map<Newpage, Double> positions, boolean showFootbox,
			List<String> title) {

		// double titleHeight = 0;
		// if (drawableSet.getComponentTitle() != null) {
		// titleHeight =
		// drawableSet.getComponentTitle().getPreferredHeight(g2dummy);
		// }
		final double headerHeight = drawableSet.getHeadHeight(g2dummy);
		final double tailHeight = drawableSet.getTailHeight(g2dummy, showFootbox);
		final double signatureHeight = 0;
		final double newpageHeight = drawableSet.getSkin().createComponent(ComponentType.NEWPAGE,
				drawableSet.getSkinParam(), Arrays.asList("")).getPreferredHeight(g2dummy);

		return new PageSplitter(fullDimension.getHeight(), /* titleHeight, */headerHeight, positions, tailHeight,
				signatureHeight, newpageHeight, title);
	}

	private File writeOneFile(final File pngFile, final int width, final Page page, final int indice)
			throws IOException {
		BufferedImage im = createImage(width, page, indice);
		if (sequenceDiagram.isRotation()) {
			im = PngRotation.process(im);
		}
		im = PngSizer.process(im, sequenceDiagram.getMinwidth());

		final File f = computeFilename(pngFile, indice);

		Log.info("Creating file: " + f);
		PngIO.write(im, f, sequenceDiagram.getSource());

		return f;
	}

	public List<File> createPng(final File pngFile) throws IOException {

		final List<File> result = new ArrayList<File>();
		for (int i = 0; i < pages.size(); i++) {
			result.add(writeOneFile(pngFile, (int) fullDimension.getWidth(), pages.get(i), i));
		}
		return result;
	}

	public void createPng(OutputStream os) throws IOException {
		final BufferedImage im = createImage((int) fullDimension.getWidth(), pages.get(0), 0);
		PngIO.write(im, os, sequenceDiagram.getSource());
	}

	private BufferedImage createImage(final int diagramWidth, final Page page, final int indice) {
		double delta = 0;
		if (indice > 0) {
			// if (page.getTitle() == null) {
			delta = page.getNewpage1() - page.getHeaderHeight();
			// } else {
			// delta = page.getNewpage1() - page.getHeaderHeight() -
			// page.getTitleHeight();
			// }
		}
		if (delta < 0) {
			throw new IllegalArgumentException();
		}
		int titleHeight = 0;
		int titleWidth = 0;
		Component compTitle = null;

		if (page.getTitle() != null) {
			compTitle = drawableSet.getSkin().createComponent(ComponentType.TITLE, drawableSet.getSkinParam(),
					page.getTitle());
			titleHeight = (int) compTitle.getPreferredHeight(g2dummy);
			titleWidth = (int) compTitle.getPreferredWidth(g2dummy);
		}

		final int width = Math.max(titleWidth, diagramWidth);
		final int height = (int) (titleHeight + page.getHeight());

		final EmptyImageBuilder builder = new EmptyImageBuilder(width, height, Color.WHITE);

		final BufferedImage im = builder.getBufferedImage();
		final Graphics2D g2dOk = builder.getGraphics2D();

		if (compTitle != null) {
			g2dOk.translate((width - titleWidth) / 2, 0);
			drawTitle(g2dOk, compTitle);
			g2dOk.translate(-(width - titleWidth) / 2, 0);
		}

		if (titleHeight > 0) {
			g2dOk.translate((width - diagramWidth) / 2, titleHeight);
		}

		// final boolean showTitle = indice == 0 || page.getTitle() != null;
		drawableSet.draw(g2dOk, delta, im.getWidth(), page, /* showTitle, */sequenceDiagram.isShowFootbox());
		g2dOk.dispose();
		return im;
	}

	private void drawTitle(Graphics2D g2d, Component compTitle) {
		final double h = compTitle.getPreferredHeight(g2d);
		final double w = compTitle.getPreferredWidth(g2d);
		// final double xpos = (getMaxX() - w) / 2;
		// g2d.translate(xpos, 0);
		compTitle.draw(g2d, new Dimension2DDouble(w, h), new SimpleContext2D(false));
		// g2d.translate(-xpos, 0);
	}

	static public File computeFilename(File pngFile, int i) {
		if (i == 0) {
			return pngFile;
		}
		final File dir = pngFile.getParentFile();
		String name = pngFile.getName();
		name = name.replaceAll("\\.png$", "_" + String.format("%03d", i) + ".png");
		return new File(dir, name);

	}

}
