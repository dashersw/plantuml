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
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Skin;

public class SequenceDiagramPngMaker {

	private final SequenceDiagram sequenceDiagram;

	private final Step1 step1;
	private Step2 step2;

	private final List<Newpage> newpages = new ArrayList<Newpage>();

	private static final Graphics2D g2dummy;

	static {
		final BufferedImage imDummy = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		g2dummy = imDummy.createGraphics();

	}

	public SequenceDiagramPngMaker(SequenceDiagram sequenceDiagram, Skin skin) {
		this.sequenceDiagram = sequenceDiagram;
		this.step1 = new Step1(skin);
		prepareData();
	}

	public List<File> createPng(final File pngFile) throws IOException {
		final Dimension2D fullDimension = step1.init(g2dummy);
		final List<Page> pages = computePages(fullDimension);

		final List<File> result = new ArrayList<File>();
		for (int i = 0; i < pages.size(); i++) {
			result.add(writeOneFile(pngFile, (int) fullDimension.getWidth(), pages.get(i), i));
		}
		return result;
	}
	
	private File writeOneFile(final File pngFile, final int width, final Page page, final int indice) throws IOException {
		final BufferedImage im = createImage(width, page, indice);
		final File f = computeFilename(pngFile, indice);

		Log.info("Creating file: " + f);
		PngIO.write(im, f, sequenceDiagram.getSource());

		if (sequenceDiagram.isRotation()) {
			new PngRotation(f, sequenceDiagram.getSource());
		}
		new PngSizer(f, sequenceDiagram.getMinwidth(), sequenceDiagram.getSource());
		return f;
	}


	public void createPng(OutputStream os) throws IOException {
		final Dimension2D fullDimension = step1.init(g2dummy);
		final List<Page> pages = computePages(fullDimension);
		final BufferedImage im = createImage((int) fullDimension.getWidth(), pages.get(0), 0);
		PngIO.write(im, os, sequenceDiagram.getSource());
	}

	private List<Page> computePages(final Dimension2D fullDimension) {
		step2 = new Step2(step1.getStepData(), fullDimension, step1.getGroupingMargin());

		final Map<Newpage, Double> positions = new LinkedHashMap<Newpage, Double>();
		for (Newpage n : newpages) {
			positions.put(n, step1.getYposition(g2dummy, n));
		}
		double titleHeight = 0;
		if (step1.getCompTitle() != null) {
			titleHeight = step1.getCompTitle().getPreferredHeight(g2dummy);
		}
		final double headerHeight = step1.getStepData().getHeadHeight(g2dummy);
		final double tailHeight = step1.getStepData().getTailHeight(g2dummy);
		final double signatureHeight = 0;
		final double newpageHeight = step1.getStepData().getSkin().createComponent(ComponentType.NEWPAGE,
				Arrays.asList("")).getPreferredHeight(g2dummy);
		final List<Page> pages = new PageSplitter(fullDimension.getHeight(), titleHeight, headerHeight, positions,
				tailHeight, signatureHeight, newpageHeight).getPages();
		return pages;
	}

	private BufferedImage createImage(final int width, final Page page, final int indice) {
		double delta = 0;
		if (indice > 0) {
			delta = page.getNewpage1() - page.getHeaderHeight();
		}
		if (delta < 0) {
			throw new IllegalArgumentException();
		}
		final Component compTitle = indice == 0 ? step1.getCompTitle() : null;

		final EmptyImageBuilder builder = new EmptyImageBuilder(width, (int) page.getHeight(), Color.WHITE);

		final BufferedImage im = builder.getBufferedImage();
		final Graphics2D g2dOk = builder.getGraphics2D();

		step2.draw(g2dOk, compTitle, delta, im.getWidth(), im.getHeight(), page);
		g2dOk.dispose();
		return im;
	}

	private void prepareData() {

		step1.setTitle(sequenceDiagram.getTitle());
		
		for (Participant p : sequenceDiagram.participants().values()) {
			step1.addParticipant(p);
		}

		for (Event ev : sequenceDiagram.events()) {
			step1.addEvent(ev);
			if (ev instanceof Message) {
				// TODO mieux faire
				final Message m = (Message) ev;
				for (LifeEvent lifeEvent : m.getLiveEvents()) {
					if (lifeEvent.getType() == LifeEventType.DESTROY) {
						step1.addEvent(lifeEvent);
					}
				}

			}
			if (ev instanceof Newpage) {
				newpages.add((Newpage) ev);
			}
		}
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
