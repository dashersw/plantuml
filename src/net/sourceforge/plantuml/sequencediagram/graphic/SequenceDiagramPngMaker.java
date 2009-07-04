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
import net.sourceforge.plantuml.skin.ComponentType;
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
		final DrawableSetInitializer initializer = new DrawableSetInitializer(skin, sequenceDiagram.isShowFootbox());

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
					if (lifeEvent.getType() == LifeEventType.DESTROY /* || lifeEvent.getType() == LifeEventType.CREATE*/) {
						initializer.addEvent(lifeEvent);
					}
				}

			}
			if (ev instanceof Newpage) {
				newpages.add((Newpage) ev);
			}
		}
		drawableSet = initializer.createDrawableSet(g2dummy, sequenceDiagram.getTitle());
		fullDimension = drawableSet.getDimension();
		final Map<Newpage, Double> positions = new LinkedHashMap<Newpage, Double>();
		for (Newpage n : newpages) {
			positions.put(n, initializer.getYposition(g2dummy, n));
		}
		pages = create(drawableSet, positions, sequenceDiagram.isShowFootbox()).getPages();

	}

	private PageSplitter create(DrawableSet drawableSet, Map<Newpage, Double> positions, boolean showFootbox) {

		double titleHeight = 0;
		if (drawableSet.getComponentTitle() != null) {
			titleHeight = drawableSet.getComponentTitle().getPreferredHeight(g2dummy);
		}
		final double headerHeight = drawableSet.getHeadHeight(g2dummy);
		final double tailHeight = drawableSet.getTailHeight(g2dummy, showFootbox);
		final double signatureHeight = 0;
		final double newpageHeight = drawableSet.getSkin().createComponent(ComponentType.NEWPAGE, Arrays.asList(""))
				.getPreferredHeight(g2dummy);

		return new PageSplitter(fullDimension.getHeight(), titleHeight, headerHeight, positions, tailHeight,
				signatureHeight, newpageHeight);
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

	private BufferedImage createImage(final int width, final Page page, final int indice) {
		double delta = 0;
		if (indice > 0) {
			delta = page.getNewpage1() - page.getHeaderHeight();
		}
		if (delta < 0) {
			throw new IllegalArgumentException();
		}

		final EmptyImageBuilder builder = new EmptyImageBuilder(width, (int) page.getHeight(), Color.WHITE);

		final BufferedImage im = builder.getBufferedImage();
		final Graphics2D g2dOk = builder.getGraphics2D();

		final boolean showTitle = indice == 0;
		drawableSet.draw(g2dOk, delta, im.getWidth(), im.getHeight(), page, showTitle, sequenceDiagram.isShowFootbox());
		g2dOk.dispose();
		return im;
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
