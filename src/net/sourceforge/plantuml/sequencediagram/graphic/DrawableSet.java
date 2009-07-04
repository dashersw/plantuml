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
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.Skin;

class DrawableSet {

	private final Map<Participant, LivingParticipantBox> participants = new LinkedHashMap<Participant, LivingParticipantBox>();
	private final Map<Event, GraphicalElement> events = new LinkedHashMap<Event, GraphicalElement>();
	private final Skin skin;
	private Dimension2D dimension;
	private double titleWidthOffset;

	private Component componentTitle;
	private double groupingMargin;

	DrawableSet(Skin skin) {
		this.skin = skin;
	}

	public final Skin getSkin() {
		return skin;
	}

	public Set<Event> getAllEvents() {
		return Collections.unmodifiableSet(events.keySet());
	}

	public Set<Participant> getAllParticipants() {
		return Collections.unmodifiableSet(participants.keySet());
	}

	public Collection<LivingParticipantBox> getAllLivingParticipantBox() {
		return Collections.unmodifiableCollection(participants.values());
	}

	public Collection<GraphicalElement> getAllGraphicalElements() {
		return Collections.unmodifiableCollection(events.values());
	}

	public LivingParticipantBox getLivingParticipantBox(Participant p) {
		return participants.get(p);
	}

	public GraphicalElement getEvent(Event ev) {
		return events.get(ev);
	}

	public double getHeadHeight(Graphics2D g2d) {
		double r = 0;
		for (LivingParticipantBox livingParticipantBox : participants.values()) {
			final double y = livingParticipantBox.getParticipantBox().getHeadHeight(g2d);
			r = Math.max(r, y);
		}
		return r;
	}

	public double getTailHeight(Graphics2D g2d, boolean showTail) {
		if (showTail == false) {
			return 1;
		}
		double r = 0;
		for (LivingParticipantBox livingParticipantBox : participants.values()) {
			final double y = livingParticipantBox.getParticipantBox().getTailHeight(g2d);
			r = Math.max(r, y);
		}
		return r;
	}

	public void addParticipant(Participant p, LivingParticipantBox box) {
		participants.put(p, box);
	}

	public void addEvent(Event event, GraphicalElement object) {
		events.put(event, object);

	}

	void setDimension(Dimension2D dim, double titleWidthOffset) {
		if (dimension != null) {
			throw new IllegalStateException();
		}
		this.titleWidthOffset = titleWidthOffset;
		this.dimension = dim;
	}

	public Dimension2D getDimension() {
		return dimension;
	}

	public final Component getComponentTitle() {
		return componentTitle;
	}

	public final void setComponentTitle(Component componentTitle) {
		this.componentTitle = componentTitle;
	}

	// ---------

	void draw(Graphics2D g2d, final double delta, int width, int height, Page p, boolean showTitle, boolean showTail) {

		double titleHeight = 0;
		if (showTitle && componentTitle != null) {
			this.drawTitle(g2d, componentTitle);
			titleHeight = componentTitle.getPreferredHeight(g2d);
		}

		g2d.translate(titleWidthOffset, 0);
		final AffineTransform at = g2d.getTransform();

		clipAndTranslate(delta, width, p, g2d);
		this.drawPlayground(g2d, height, new SimpleContext2D(true));

		g2d.setClip(null);
		g2d.setTransform(at);

		g2d.translate(0, titleHeight);
		this.drawLine(g2d, height - titleHeight, showTail);
		this.drawHeadTail(g2d, showTail ? height : 0);
		g2d.translate(0, -titleHeight);

		clipAndTranslate(delta, width, p, g2d);
		this.drawPlayground(g2d, height, new SimpleContext2D(false));
	}

	private void clipAndTranslate(final double delta, int width, Page p, final Graphics2D g2d) {
		g2d.setClip(0, (int) p.getBodyRelativePosition(), width, (int) (p.getBodyHeight() + 1));
		if (delta > 0) {
			g2d.translate(0, -delta);
		}
	}

	public final void setGroupingMargin(double groupingMargin) {
		this.groupingMargin = groupingMargin;
	}

	private void drawLine(Graphics2D g2d, double height, boolean showTail) {
		g2d.translate(groupingMargin, 0);
		for (LivingParticipantBox box : getAllLivingParticipantBox()) {
			box.drawLine(g2d, height, showTail);
		}
		g2d.translate(-groupingMargin, 0);

	}

	private void drawHeadTail(Graphics2D g2d, double height) {
		g2d.translate(groupingMargin, 0);
		for (LivingParticipantBox box : getAllLivingParticipantBox()) {
			box.getParticipantBox().drawHeadTail(g2d, height);
		}
		g2d.translate(-groupingMargin, 0);
	}

	private void drawTitle(Graphics2D g2d, Component compTitle) {
		final double h = compTitle.getPreferredHeight(g2d);
		final double w = compTitle.getPreferredWidth(g2d);
		final double xpos = (getMaxX() - w) / 2;
		g2d.translate(xpos, 0);
		compTitle.draw(g2d, new Dimension2DDouble(w, h), new SimpleContext2D(false));
		g2d.translate(-xpos, 0);
	}

	private double getMaxX() {
		return dimension.getWidth();
	}

	private double getMaxY() {
		return dimension.getHeight();
	}

	private void drawPlayground(Graphics2D g2d, double height, Context2D context) {
		g2d.translate(groupingMargin, 0);

		for (Participant p : getAllParticipants()) {
			drawLifeLine(g2d, p);
		}
		for (GraphicalElement element : getAllGraphicalElements()) {
			element.draw(g2d, getMaxX(), context);
		}
		g2d.setClip(null);
		g2d.translate(-groupingMargin, 0);
	}

	// Drawing
	private void drawLifeLine(Graphics2D g2d, Participant p) {
		final LifeLine line = getLivingParticipantBox(p).getLifeLine();

		line.finish(getMaxY());
		final Component comp = getSkin().createComponent(ComponentType.ALIVE_LINE, null);
		line.draw(g2d, comp);

	}

}
