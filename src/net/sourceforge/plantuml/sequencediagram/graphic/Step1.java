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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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

class Step1 {

	private final StepData stepData;

	private double freeX = 0;
	private double freeY = 0;

	private final double groupingMargin = 10;

	private int maxGrouping = 0;

	private ConstraintSet constraintSet;

	private String title;
	private Component compTitle;

	public Step1(Skin skin) {
		this.stepData = new StepData(skin);
	}

	public Dimension2D init(Graphics2D g2d) {
		if (freeY == 0) {
			prepareData(g2d);
		} else {
			throw new IllegalStateException();
		}
		return new Dimension2DDouble(freeX + 1 + getGroupingMargin() * 2, getTotalHeight(g2d));
	}

	private double getTotalHeight(Graphics2D g2d) {
		final double signature = 0;
		return freeY + stepData.getTailHeight(g2d) + signature;

	}

	public double getYposition(Graphics2D g2d, Newpage newpage) {
		if (newpage == null) {
			throw new IllegalArgumentException();
		}
		final GraphicalNewpage graphicalNewpage = (GraphicalNewpage) stepData.getEvents().get(newpage);
		return graphicalNewpage.getStartingY();
	}

	// Data Preparation
	private void prepareData(Graphics2D g2d) {
		for (Participant p : stepData.getParticipants().keySet()) {
			prepareParticipant(g2d, p);
		}

		this.freeY = stepData.getHeadHeight(g2d);

		for (LivingParticipantBox p : stepData.getParticipants().values()) {
			p.getParticipantBox().setAlignY(this.freeY);
		}

		if (this.title == null) {
			compTitle = null;
		} else {
			compTitle = stepData.getSkin().createComponent(ComponentType.TITLE, Arrays.asList(this.title));
			this.freeY += compTitle.getPreferredHeight(g2d);
		}

		final Collection<ParticipantBox> col = new ArrayList<ParticipantBox>();
		for (LivingParticipantBox livingParticipantBox : stepData.getParticipants().values()) {
			col.add(livingParticipantBox.getParticipantBox());
		}

		constraintSet = new ConstraintSet(col, freeX);

		for (Event ev : stepData.getEvents().keySet()) {
			if (ev instanceof Message) {
				prepareMessage(g2d, (Message) ev);
			} else if (ev instanceof Note) {
				prepareNote(g2d, (Note) ev);
			} else if (ev instanceof LifeEvent) {
				final LifeEvent lifeEvent = (LifeEvent) ev;
				if (lifeEvent.getType() != LifeEventType.DESTROY) {
					throw new IllegalStateException();
				}
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

	}

	private void prepareMissingSpace(Graphics2D g2d) {
		freeX = constraintSet.getMaxX();
		double missingSpace1 = 0;
		double missingSpace2 = 0;

		for (GraphicalElement ev : stepData.getEvents().values()) {
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
		final GraphicalNewpage graphicalNewpage = new GraphicalNewpage(freeY, stepData.getSkin().createComponent(
				ComponentType.NEWPAGE, null));
		freeY += graphicalNewpage.getPreferredHeight(g2d);
		stepData.getEvents().put(newpage, graphicalNewpage);
	}

	private void prepareGrouping(Graphics2D g2d, Grouping m) {
		final GraphicalElement element;
		if (m.getType() == GroupingType.START) {
			this.maxGrouping++;
			final Component header = stepData.getSkin().createComponent(ComponentType.GROUPING_HEADER,
					Arrays.asList(m.getTitle(), m.getComment()));
			element = new GroupingHeader(freeY, header, (m.getLevel() + 1) * groupingMargin);
			freeY += element.getPreferredHeight(g2d);

		} else if (m.getType() == GroupingType.END) {
			final Grouping father = m.getFather();
			final GroupingHeader header = (GroupingHeader) stepData.getEvents().get(father);
			final Component body = stepData.getSkin().createComponent(ComponentType.GROUPING_BODY, null);
			final Component tail = stepData.getSkin().createComponent(ComponentType.GROUPING_TAIL, null);
			element = new GroupingTail(freeY, header.getStartingY() + header.getPreferredHeight(g2d) / 2.0, (m
					.getLevel() + 1)
					* groupingMargin, body, tail);
			freeY += tail.getPreferredHeight(g2d);

		} else if (m.getType() == GroupingType.ELSE) {
			final Component comp = stepData.getSkin().createComponent(ComponentType.GROUPING_ELSE,
					Arrays.asList(m.getComment()));
			element = new GroupingElse(freeY, comp, (m.getLevel() + 1) * groupingMargin);
			freeY += element.getPreferredHeight(g2d);

		} else {
			throw new IllegalStateException();
		}
		stepData.getEvents().put(m, element);
	}

	private void prepareNote(Graphics2D g2d, Note n) {
		LivingParticipantBox p1 = stepData.getParticipants().get(n.getParticipant());
		LivingParticipantBox p2;
		if (n.getParticipant2() == null) {
			p2 = null;
		} else {
			p2 = stepData.getParticipants().get(n.getParticipant2());
			if (p1.getParticipantBox().getCenterX(g2d) > p2.getParticipantBox().getCenterX(g2d)) {
				final LivingParticipantBox tmp = p1;
				p1 = p2;
				p2 = tmp;
			}
		}
		final NoteBox noteBox = new NoteBox(freeY, stepData.getSkin().createComponent(ComponentType.NOTE,
				n.getStrings()), p1, p2, n.getPosition());
		stepData.getEvents().put(n, noteBox);
		freeY += noteBox.getPreferredHeight(g2d);
	}

	private void prepareMessage(Graphics2D g2d, Message m) {
		final Step1Message step1Message = new Step1Message(g2d, m, stepData, constraintSet, freeY);
		freeY = step1Message.prepareMessage();
	}

	private void prepareParticipant(Graphics2D g2d, Participant p) {
		final ParticipantBox box;

		if (p.getType() == ParticipantType.PARTICIPANT) {
			final Component head = stepData.getSkin().createComponent(ComponentType.PARTICIPANT_HEAD, p.getDisplay());
			final Component line = stepData.getSkin().createComponent(ComponentType.PARTICIPANT_LINE, p.getDisplay());
			final Component tail = stepData.getSkin().createComponent(ComponentType.PARTICIPANT_TAIL, p.getDisplay());
			box = new ParticipantBox(head, line, tail, this.freeX);
		} else if (p.getType() == ParticipantType.ACTOR) {
			final Component head = stepData.getSkin().createComponent(ComponentType.ACTOR_HEAD, p.getDisplay());
			final Component line = stepData.getSkin().createComponent(ComponentType.ACTOR_LINE, p.getDisplay());
			final Component tail = stepData.getSkin().createComponent(ComponentType.ACTOR_TAIL, p.getDisplay());
			box = new ParticipantBox(head, line, tail, this.freeX);
		} else {
			throw new IllegalArgumentException();
		}

		final Component comp = stepData.getSkin().createComponent(ComponentType.ALIVE_LINE, null);

		stepData.getParticipants()
				.put(p, new LivingParticipantBox(box, new LifeLine(box, comp.getPreferredWidth(g2d))));

		this.freeX = box.getMaxX(g2d);
	}

	public void addParticipant(Participant p) {
		stepData.getParticipants().put(p, null);
	}

	public void addEvent(Event event) {
		stepData.getEvents().put(event, null);
	}

	public double getGroupingMargin() {
		return maxGrouping * groupingMargin;
	}

	public final StepData getStepData() {
		return stepData;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public final Component getCompTitle() {
		return compTitle;
	}

}
