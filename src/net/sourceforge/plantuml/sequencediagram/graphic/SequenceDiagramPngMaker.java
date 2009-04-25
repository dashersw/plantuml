/* ========================================================================
 * Plantuml : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques (for Atos Origin).
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of Plantuml.
 *
 * Plantuml is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Plantuml distributed in the hope that it will be useful, but
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.EmptyImageBuilder;
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
	private final File pngFile;

	private final Step1 step1;
	private Step2 step2;

	private final List<Newpage> newpages = new ArrayList<Newpage>();

	public SequenceDiagramPngMaker(SequenceDiagram sequenceDiagram, Skin skin, final File pngFile) {
		this.sequenceDiagram = sequenceDiagram;
		this.step1 = new Step1(skin);
		this.pngFile = pngFile;
		prepareData();
	}

	public List<File> createPng() throws IOException {
		final List<File> result = new ArrayList<File>();
		final BufferedImage imDummy = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2d = imDummy.createGraphics();

		final Dimension2D fullDimension = step1.init(g2d);

		step2 = new Step2(step1.getStepData(), fullDimension, step1.getGroupingMargin());

		final Map<Newpage, Double> positions = new LinkedHashMap<Newpage, Double>();
		for (Newpage n : newpages) {
			positions.put(n, step1.getYposition(g2d, n));
		}
		double titleHeight = 0;
		if (step1.getCompTitle() != null) {
			titleHeight = step1.getCompTitle().getPreferredHeight(g2d);
		}
		final double headerHeight = step1.getStepData().getHeadHeight(g2d);
		final double tailHeight = step1.getStepData().getTailHeight(g2d);
		final double signatureHeight = 0;
		final double newpageHeight = step1.getStepData().getSkin().createComponent(ComponentType.NEWPAGE,
				Arrays.asList("")).getPreferredHeight(g2d);
		final List<Page> pages = new PageSplitter(fullDimension.getHeight(), titleHeight, headerHeight, positions,
				tailHeight, signatureHeight, newpageHeight).getPages();

		for (int i = 0; i < pages.size(); i++) {
			result.add(writeOneFile(g2d, fullDimension, pages, i));
		}
		g2d.dispose();

		return result;
	}

	private File writeOneFile(Graphics2D g2d, final Dimension2D fullDimension, final List<Page> pages, final int indice)
			throws IOException {

		double delta = 0;
		if (indice > 0) {
			delta = pages.get(indice).getNewpage1() - pages.get(indice).getHeaderHeight();
		}
		if (delta < 0) {
			throw new IllegalArgumentException();
		}
		final Component compTitle = indice == 0 ? step1.getCompTitle() : null;
		
		final EmptyImageBuilder builder = new EmptyImageBuilder((int) fullDimension.getWidth(), (int) pages.get(indice).getHeight(), Color.WHITE);
		
		final BufferedImage im = builder.getBufferedImage();
		final Graphics2D g2dOk = builder.getGraphics2D();
		
		step2.draw(g2dOk, compTitle, delta, im, pages.get(indice));

		final File f = computeFilename(pngFile, indice);

		ImageIO.write(im, "png", f);
		g2dOk.dispose();
		return f;

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

	File computeFilename(File pngFile, int i) {
		if (i == 0) {
			return pngFile;
		}
		final File dir = pngFile.getParentFile();
		String name = pngFile.getName();
		name = name.replaceAll("\\.png$", "_" + String.format("%03d", i) + ".png");
		return new File(dir, name);

	}

}
