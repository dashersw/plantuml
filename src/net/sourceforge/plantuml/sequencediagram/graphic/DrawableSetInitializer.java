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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Grouping;
import net.sourceforge.plantuml.sequencediagram.GroupingType;
import net.sourceforge.plantuml.sequencediagram.LifeEvent;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Newpage;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.ParticipantType;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Skin;

class DrawableSetInitializer {

	private final DrawableSet drawableSet;
	private final boolean showTail;

	private double freeX = 0;
	private double freeY = 0;

	private final double groupingMargin = 10;

	private int maxGrouping = 0;

	private ConstraintSet constraintSet;

	// private Component compTitle;

	public DrawableSetInitializer(Skin skin, SkinParam skinParam, boolean showTail) {
		this.drawableSet = new DrawableSet(skin, skinParam);
		this.showTail = showTail;

	}

	public DrawableSet createDrawableSet(Graphics2D g2d/* , List<String> title */) {
		if (freeY != 0) {
			throw new IllegalStateException();
		}

		for (Participant p : drawableSet.getAllParticipants()) {
			prepareParticipant(g2d, p);
		}

		this.freeY = drawableSet.getHeadHeight(g2d);

		for (LivingParticipantBox p : drawableSet.getAllLivingParticipantBox()) {
			p.getParticipantBox().setTopStartingY(this.freeY);
		}

		// if (title != null && title.size() > 0) {
		// compTitle =
		// drawableSet.getSkin().createComponent(ComponentType.TITLE, title);
		// this.freeY += compTitle.getPreferredHeight(g2d);
		// }

		final Collection<ParticipantBox> col = new ArrayList<ParticipantBox>();
		for (LivingParticipantBox livingParticipantBox : drawableSet.getAllLivingParticipantBox()) {
			col.add(livingParticipantBox.getParticipantBox());
		}

		constraintSet = new ConstraintSet(col, freeX);

		for (Event ev : drawableSet.getAllEvents()) {
			if (ev instanceof Message) {
				prepareMessage(g2d, (Message) ev);
			} else if (ev instanceof Note) {
				prepareNote(g2d, (Note) ev);
			} else if (ev instanceof LifeEvent) {
				prepareLiveEvent(g2d, (LifeEvent) ev);
			} else if (ev instanceof Grouping) {
				prepareGrouping(g2d, (Grouping) ev);
			} else if (ev instanceof Newpage) {
				prepareNewpage(g2d, (Newpage) ev);
			} else {
				throw new IllegalStateException();
			}
		}

		constraintSet.takeConstraintIntoAccount(g2d);

		prepareMissingSpace(g2d);
		final double diagramWidth = freeX + 1 + getGroupingMargin() * 2;
		// double titleWidth = 0;
		// if (compTitle != null) {
		// titleWidth = compTitle.getPreferredWidth(g2d);
		// }
		// final double width = Math.max(titleWidth, diagramWidth);
		drawableSet.setDimension(new Dimension2DDouble(diagramWidth, getTotalHeight(g2d))/*
																							 * ,
																							 * (width -
																							 * diagramWidth) /
																							 * 2
																							 */);
		drawableSet.setGroupingMargin(getGroupingMargin());
		// drawableSet.setComponentTitle(getComponentTitle());
		return drawableSet;
	}

	private double getTotalHeight(Graphics2D g2d) {
		final double signature = 0;
		return freeY + drawableSet.getTailHeight(g2d, showTail) + signature;
	}

	public double getYposition(Graphics2D g2d, Newpage newpage) {
		if (newpage == null) {
			throw new IllegalArgumentException();
		}
		final GraphicalNewpage graphicalNewpage = (GraphicalNewpage) drawableSet.getEvent(newpage);
		return graphicalNewpage.getStartingY();
	}

	private void prepareMissingSpace(Graphics2D g2d) {
		freeX = constraintSet.getMaxX();
		double missingSpace1 = 0;
		double missingSpace2 = 0;

		for (GraphicalElement ev : drawableSet.getAllGraphicalElements()) {
			final double startX = ev.getStartingX(g2d);
			final double delta1 = -startX;
			if (delta1 > missingSpace1) {
				missingSpace1 = delta1;
			}
			final double endX = startX + ev.getPreferredWidth(g2d);
			final double delta2 = endX - freeX;
			if (delta2 > missingSpace2) {
				missingSpace2 = delta2;
			}
		}
		if (missingSpace1 > 0) {
			constraintSet.pushToLeft(missingSpace1);
		}
		freeX = constraintSet.getMaxX() + missingSpace2;
	}

	private void prepareNewpage(Graphics2D g2d, Newpage newpage) {
		final GraphicalNewpage graphicalNewpage = new GraphicalNewpage(freeY, drawableSet.getSkin().createComponent(
				ComponentType.NEWPAGE, drawableSet.getSkinParam(), null));
		freeY += graphicalNewpage.getPreferredHeight(g2d);
		drawableSet.addEvent(newpage, graphicalNewpage);
	}

	private void prepareGrouping(Graphics2D g2d, Grouping m) {
		final GraphicalElement element;
		if (m.getType() == GroupingType.START) {
			this.maxGrouping++;
			final Component header = drawableSet.getSkin().createComponent(ComponentType.GROUPING_HEADER,
					drawableSet.getSkinParam(), Arrays.asList(m.getTitle(), m.getComment()));
			element = new GroupingHeader(freeY, header, (m.getLevel() + 1) * groupingMargin);
			freeY += element.getPreferredHeight(g2d);

		} else if (m.getType() == GroupingType.END) {
			final Grouping father = m.getFather();
			final GroupingHeader header = (GroupingHeader) drawableSet.getEvent(father);
			final Component body = drawableSet.getSkin().createComponent(ComponentType.GROUPING_BODY,
					drawableSet.getSkinParam(), null);
			final Component tail = drawableSet.getSkin().createComponent(ComponentType.GROUPING_TAIL,
					drawableSet.getSkinParam(), null);
			element = new GroupingTail(freeY, header.getStartingY() + header.getPreferredHeight(g2d) / 2.0, (m
					.getLevel() + 1)
					* groupingMargin, body, tail);
			freeY += tail.getPreferredHeight(g2d);

		} else if (m.getType() == GroupingType.ELSE) {
			final Component comp = drawableSet.getSkin().createComponent(ComponentType.GROUPING_ELSE,
					drawableSet.getSkinParam(), Arrays.asList(m.getComment()));
			element = new GroupingElse(freeY, comp, (m.getLevel() + 1) * groupingMargin);
			freeY += element.getPreferredHeight(g2d);

		} else {
			throw new IllegalStateException();
		}
		drawableSet.addEvent(m, element);
	}

	private void prepareNote(Graphics2D g2d, Note n) {
		LivingParticipantBox p1 = drawableSet.getLivingParticipantBox(n.getParticipant());
		LivingParticipantBox p2;
		if (n.getParticipant2() == null) {
			p2 = null;
		} else {
			p2 = drawableSet.getLivingParticipantBox(n.getParticipant2());
			if (p1.getParticipantBox().getCenterX(g2d) > p2.getParticipantBox().getCenterX(g2d)) {
				final LivingParticipantBox tmp = p1;
				p1 = p2;
				p2 = tmp;
			}
		}
		final NoteBox noteBox = new NoteBox(freeY, drawableSet.getSkin().createComponent(ComponentType.NOTE,
				drawableSet.getSkinParam(), n.getStrings()), p1, p2, n.getPosition());
		drawableSet.addEvent(n, noteBox);
		freeY += noteBox.getPreferredHeight(g2d);
	}

	private void prepareLiveEvent(Graphics2D g2d, LifeEvent lifeEvent) {
		if (lifeEvent.getType() != LifeEventType.DESTROY && lifeEvent.getType() != LifeEventType.CREATE) {
			throw new IllegalStateException();
		}
		if (lifeEvent.getType() == LifeEventType.CREATE) {
			final Participant p = lifeEvent.getParticipant();
			drawableSet.getLivingParticipantBox(p).create(freeY);
			drawableSet.getLivingParticipantBox(p).getParticipantBox().setShowHead(false);
			final Component head = drawableSet.getSkin().createComponent(ComponentType.PARTICIPANT_HEAD,
					drawableSet.getSkinParam(), p.getDisplay());
			final LifeDestroy destroy = new LifeDestroy(freeY, drawableSet.getLivingParticipantBox(p)
					.getParticipantBox(), head);
			drawableSet.addEvent(lifeEvent, destroy);
			freeY += destroy.getPreferredHeight(g2d);
		}
	}

	private void prepareMessage(Graphics2D g2d, Message m) {
		final Step1Message step1Message = new Step1Message(g2d, m, drawableSet, freeY);
		freeY = step1Message.prepareMessage(constraintSet);
	}

	private void prepareParticipant(Graphics2D g2d, Participant p) {
		final ParticipantBox box;

		if (p.getType() == ParticipantType.PARTICIPANT) {
			final Component head = drawableSet.getSkin().createComponent(ComponentType.PARTICIPANT_HEAD,
					drawableSet.getSkinParam(), p.getDisplay());
			final Component line = drawableSet.getSkin().createComponent(ComponentType.PARTICIPANT_LINE,
					drawableSet.getSkinParam(), p.getDisplay());
			final Component tail = drawableSet.getSkin().createComponent(ComponentType.PARTICIPANT_TAIL,
					drawableSet.getSkinParam(), p.getDisplay());
			box = new ParticipantBox(head, line, tail, this.freeX);
		} else if (p.getType() == ParticipantType.ACTOR) {
			final Component head = drawableSet.getSkin().createComponent(ComponentType.ACTOR_HEAD,
					drawableSet.getSkinParam(), p.getDisplay());
			final Component line = drawableSet.getSkin().createComponent(ComponentType.ACTOR_LINE,
					drawableSet.getSkinParam(), p.getDisplay());
			final Component tail = drawableSet.getSkin().createComponent(ComponentType.ACTOR_TAIL,
					drawableSet.getSkinParam(), p.getDisplay());
			box = new ParticipantBox(head, line, tail, this.freeX);
		} else {
			throw new IllegalArgumentException();
		}

		final Component comp = drawableSet.getSkin().createComponent(ComponentType.ALIVE_LINE,
				drawableSet.getSkinParam(), null);

		drawableSet.addParticipant(p, new LivingParticipantBox(box, new LifeLine(box, comp.getPreferredWidth(g2d))));

		this.freeX = box.getMaxX(g2d);
	}

	public void addParticipant(Participant p) {
		drawableSet.addParticipant(p, null);
	}

	public void addEvent(Event event) {
		drawableSet.addEvent(event, null);
	}

	private double getGroupingMargin() {
		return maxGrouping * groupingMargin;
	}

	// private Component getComponentTitle() {
	// return compTitle;
	// }

}
