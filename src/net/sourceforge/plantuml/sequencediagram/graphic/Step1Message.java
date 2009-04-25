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

import java.awt.Graphics2D;

import net.sourceforge.plantuml.sequencediagram.LifeEvent;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;

class Step1Message {

	private final Graphics2D g2d;

	private final StepData stepData;

	private final ConstraintSet constraintSet;

	private final Message m;
	private double freeY;

	private final LivingParticipantBox lp1;
	private final LivingParticipantBox lp2;
	private final ParticipantBox p1;
	private final ParticipantBox p2;

	private final LivingParticipantBox leftBox;
	private final LivingParticipantBox rightBox;
	private final MessageArrow messageArrow;

	private final ComponentType type;

	private final Component note;

	Step1Message(Graphics2D g2d, Message m, StepData stepData, ConstraintSet constraintSet, double freeY) {
		this.g2d = g2d;
		this.m = m;
		this.freeY = freeY;
		this.stepData = stepData;
		this.constraintSet = constraintSet;

		this.lp1 = stepData.getParticipants().get(m.getParticipant1());
		this.lp2 = stepData.getParticipants().get(m.getParticipant2());
		this.p1 = lp1.getParticipantBox();
		this.p2 = lp2.getParticipantBox();
		final double x1 = p1.getCenterX(g2d);
		final double x2 = p2.getCenterX(g2d);

		this.type = p1.equals(p2) ? getSelfArrowType(m) : getArrowType(m, x1, x2);

		if (p1.equals(p2)) {
			this.messageArrow = null;
			this.leftBox = null;
			this.rightBox = null;
		} else {
			this.messageArrow = new MessageArrow(freeY, stepData.getSkin(), stepData.getSkin().createComponent(type,
					m.getLabel()), lp1, lp2);
			this.leftBox = messageArrow.getParticipantAt(g2d, NotePosition.LEFT);
			this.rightBox = messageArrow.getParticipantAt(g2d, NotePosition.RIGHT);
		}

		if (m.getNote() == null) {
			note = null;
		} else {
			note = stepData.getSkin().createComponent(ComponentType.NOTE, m.getNote());
		}

	}

	private void manageActivation(LifeEvent n, final double pos) {
		final Participant p = n.getParticipant();
		final LifeLine line = stepData.getParticipants().get(p).getLifeLine();

		if (n.getType() != LifeEventType.ACTIVATE) {
			return;
		}
		line.addEvent(n.getType(), pos);
	}

	private void manageDeactivation(Graphics2D g2d, LifeEvent n, final double pos) {
		final Participant p = n.getParticipant();
		final LifeLine line = stepData.getParticipants().get(p).getLifeLine();

		if (n.getType() == LifeEventType.ACTIVATE) {
			return;
		}

		if (n.getType() == LifeEventType.DESTROY) {
			final Component comp = stepData.getSkin().createComponent(ComponentType.DESTROY, null);
			final double delta = comp.getPreferredHeight(g2d) / 2;
			final LifeDestroy destroy = new LifeDestroy(pos - delta, stepData.getParticipants().get(p)
					.getParticipantBox(), comp);
			stepData.getEvents().put(n, destroy);
		}

		line.addEvent(n.getType(), pos);
	}

	public double prepareMessage() {
		final Arrow graphic = createArrow();
		final double arrowYStartLevel = graphic.getArrowYStartLevel(g2d);
		final double arrowYEndLevel = graphic.getArrowYEndLevel(g2d);

		for (LifeEvent lifeEvent : m.getLiveEvents()) {
			manageActivation(lifeEvent, arrowYStartLevel);
		}

		final double length;
		if (p1.equals(p2)) {
			length = graphic.getArrowOnlyWidth(g2d) + lp1.getLiveThicknessAt(g2d, arrowYStartLevel).getLength();
		} else {
			length = graphic.getArrowOnlyWidth(g2d) + leftBox.getLifeLine().getRightShift(arrowYStartLevel)
					+ rightBox.getLifeLine().getLeftShift(arrowYStartLevel);
		}

		freeY += graphic.getPreferredHeight(g2d);
		stepData.getEvents().put(m, graphic);

		if (p1.equals(p2)) {
			constraintSet.getConstraintAfter(p1).ensureValue(length);
		} else {
			constraintSet.getConstraint(p1, p2).ensureValue(length);
		}

		for (LifeEvent lifeEvent : m.getLiveEvents()) {
			manageDeactivation(g2d, lifeEvent, arrowYEndLevel);
		}

		return freeY;
	}

	private Arrow createArrow() {
		if (m.getNote() != null && p1.equals(p2)) {
			return new ArrowAndNoteBox(g2d, new MessageSelfArrow(freeY, stepData.getSkin(), stepData.getSkin()
					.createComponent(type, m.getLabel()), lp1), note, m.getNotePosition());
		} else if (m.getNote() != null) {
			return new ArrowAndNoteBox(g2d, messageArrow, note, m.getNotePosition());
		} else if (p1.equals(p2)) {
			return new MessageSelfArrow(freeY, stepData.getSkin(), stepData.getSkin().createComponent(type,
					m.getLabel()), lp1);
		} else {
			return messageArrow;
		}
	}

	private ComponentType getSelfArrowType(Message m) {
		final ComponentType type = m.isDotted() ? ComponentType.DOTTED_SELF_ARROW : ComponentType.SELF_ARROW;
		return type;
	}

	private ComponentType getArrowType(Message m, final double x1, final double x2) {
		final ComponentType type;

		if (m.isDotted()) {
			type = x2 > x1 ? ComponentType.DOTTED_ARROW : ComponentType.RETURN_DOTTED_ARROW;
		} else {
			type = x2 > x1 ? ComponentType.ARROW : ComponentType.RETURN_ARROW;
		}
		return type;
	}

}
