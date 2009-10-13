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

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.LifeEvent;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Newpage;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.skin.ComponentType;

public class SequenceDiagramPngMaker2 {
	private final DrawableSetInitializer initializer;
	private DrawableSet drawableSet;

	private Dimension2D fullDimension;

	// private Component compTitle;
	private final List<String> title;

	private List<Page> pages;

	public SequenceDiagramPngMaker2(SequenceDiagram sequenceDiagram) {
		this.initializer = new DrawableSetInitializer(sequenceDiagram.getSkin(), sequenceDiagram.getSkinParam(), true);
		this.title = sequenceDiagram.getTitle();

		for (Participant p : sequenceDiagram.participants().values()) {
			initializer.addParticipant(p);
		}

		for (Event ev : sequenceDiagram.events()) {
			initializer.addEvent(ev);
			if (ev instanceof Message) {
				// TODO mieux faire
				final Message m = (Message) ev;
				for (LifeEvent lifeEvent : m.getLiveEvents()) {
					if (lifeEvent.getType() == LifeEventType.DESTROY) {
						initializer.addEvent(lifeEvent);
					}
				}

			}
		}
	}

	public void draw(Graphics2D g2d) throws IOException {
		if (fullDimension == null) {
			drawableSet = initializer.createDrawableSet(g2d/* , title */);
			fullDimension = drawableSet.getDimension();

			final Map<Newpage, Double> positions = new LinkedHashMap<Newpage, Double>();

			// double titleHeight = 0;
			// if (drawableSet.getComponentTitle() != null) {
			// titleHeight =
			// drawableSet.getComponentTitle().getPreferredHeight(g2d);
			// }
			final double headerHeight = drawableSet.getHeadHeight(g2d);
			final double tailHeight = drawableSet.getTailHeight(g2d, true);
			final double signatureHeight = 0;
			final double newpageHeight = drawableSet.getSkin().createComponent(ComponentType.NEWPAGE,
					drawableSet.getSkinParam(), Arrays.asList("")).getPreferredHeight(g2d);
			pages = new PageSplitter(fullDimension.getHeight(), /* titleHeight, */headerHeight, positions, tailHeight,
					signatureHeight, newpageHeight, null).getPages();

			// compTitle = drawableSet.getComponentTitle();
		}

		drawableSet.draw(g2d, 0, (int) fullDimension.getWidth(), /*
																	 * (int)
																	 * fullDimension.getHeight(),
																	 */pages.get(0), /* true, */
		true);
	}

	public final Dimension2D getFullDimension() {
		return fullDimension;
	}

}
