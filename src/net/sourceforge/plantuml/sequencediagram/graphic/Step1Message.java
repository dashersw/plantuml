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
import java.util.List;

import net.sourceforge.plantuml.sequencediagram.LifeEvent;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.MessageNumber;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;

class Step1Message {

	private final Graphics2D g2d;

	private final DrawableSet drawingSet;

	private final Message message;

	private double freeY;

	private final MessageArrow messageArrow;

	private final ComponentType type;

	private final Component note;

	Step1Message(Graphics2D g2d, Message message, DrawableSet drawingSet, double freeY) {
		this.g2d = g2d;
		this.message = message;
		this.freeY = freeY;
		this.drawingSet = drawingSet;

		final double x1 = getParticipantBox1().getCenterX(g2d);
		final double x2 = getParticipantBox2().getCenterX(g2d);

		this.type = isSelfMessage() ? getSelfArrowType(message) : getArrowType(message, x1, x2);

		if (isSelfMessage()) {
			this.messageArrow = null;
		} else {
			this.messageArrow = new MessageArrow(freeY, drawingSet.getSkin(), drawingSet.getSkin().createComponent(
					type, drawingSet.getSkinParam(), getLabelOfMessage(message)), getLivingParticipantBox1(),
					getLivingParticipantBox2());
		}

		if (message.getNote() == null) {
			note = null;
		} else {
			note = drawingSet.getSkin().createComponent(ComponentType.NOTE, drawingSet.getSkinParam(),
					message.getNote());
		}

	}

	double prepareMessage(ConstraintSet constraintSet) {
		final Arrow graphic = createArrow();
		final double arrowYStartLevel = graphic.getArrowYStartLevel(g2d);
		final double arrowYEndLevel = graphic.getArrowYEndLevel(g2d);

		for (LifeEvent lifeEvent : message.getLiveEvents()) {
			beforeMessage(lifeEvent, arrowYStartLevel);
		}

		final double length;
		if (isSelfMessage()) {
			length = graphic.getArrowOnlyWidth(g2d)
					+ getLivingParticipantBox1().getLiveThicknessAt(g2d, arrowYStartLevel).getLength();
		} else {
			length = graphic.getArrowOnlyWidth(g2d)
					+ getLivingParticipantBox(NotePosition.LEFT).getLifeLine().getRightShift(arrowYStartLevel)
					+ getLivingParticipantBox(NotePosition.RIGHT).getLifeLine().getLeftShift(arrowYStartLevel);
		}

		freeY += graphic.getPreferredHeight(g2d);
		drawingSet.addEvent(message, graphic);

		if (isSelfMessage()) {
			constraintSet.getConstraintAfter(getParticipantBox1()).ensureValue(length);
		} else {
			constraintSet.getConstraint(getParticipantBox1(), getParticipantBox2()).ensureValue(length);
		}

		for (LifeEvent lifeEvent : message.getLiveEvents()) {
			afterMessage(g2d, lifeEvent, arrowYEndLevel);
		}

		return freeY;
	}

	private boolean isSelfMessage() {
		return getParticipantBox1().equals(getParticipantBox2());
	}

	private ParticipantBox getParticipantBox1() {
		return getLivingParticipantBox1().getParticipantBox();
	}

	private ParticipantBox getParticipantBox2() {
		return getLivingParticipantBox2().getParticipantBox();
	}

	private LivingParticipantBox getLivingParticipantBox1() {
		return drawingSet.getLivingParticipantBox(message.getParticipant1());
	}

	private LivingParticipantBox getLivingParticipantBox2() {
		return drawingSet.getLivingParticipantBox(message.getParticipant2());
	}

	private LivingParticipantBox getLivingParticipantBox(NotePosition position) {
		if (isSelfMessage()) {
			throw new IllegalStateException();
		}
		return messageArrow.getParticipantAt(g2d, position);
	}

	private List<? extends CharSequence> getLabelOfMessage(Message message) {
		if (message.getMessageNumber() == null) {
			return message.getLabel();
		}
		final List<CharSequence> result = new ArrayList<CharSequence>();
		result.add(new MessageNumber(message.getMessageNumber()));
		result.addAll(message.getLabel());
		return result;
	}

	private void beforeMessage(LifeEvent n, final double pos) {
		final Participant p = n.getParticipant();
		final LifeLine line = drawingSet.getLivingParticipantBox(p).getLifeLine();

		if (n.getType() != LifeEventType.ACTIVATE) {
			return;
		}
		assert n.getType() == LifeEventType.ACTIVATE;
		line.addSegmentVariation(LifeSegmentVariation.LARGER, pos);
	}

	private void afterMessage(Graphics2D g2d, LifeEvent n, final double pos) {
		final Participant p = n.getParticipant();
		final LifeLine line = drawingSet.getLivingParticipantBox(p).getLifeLine();

		if (n.getType() == LifeEventType.ACTIVATE) {
			return;
		}

		if (n.getType() == LifeEventType.DESTROY) {
			final Component comp = drawingSet.getSkin().createComponent(ComponentType.DESTROY,
					drawingSet.getSkinParam(), null);
			final double delta = comp.getPreferredHeight(g2d) / 2;
			final LifeDestroy destroy = new LifeDestroy(pos - delta, drawingSet.getLivingParticipantBox(p)
					.getParticipantBox(), comp);
			drawingSet.addEvent(n, destroy);
		} else if (n.getType() != LifeEventType.DEACTIVATE) {
			throw new IllegalStateException();
		}

		line.addSegmentVariation(LifeSegmentVariation.SMALLER, pos);
	}

	private Arrow createArrow() {
		if (message.getNote() != null && isSelfMessage()) {
			return new ArrowAndNoteBox(g2d, new MessageSelfArrow(freeY, drawingSet.getSkin(), drawingSet.getSkin()
					.createComponent(type, drawingSet.getSkinParam(), getLabelOfMessage(message)),
					getLivingParticipantBox1()), note, message.getNotePosition());
		} else if (message.getNote() != null) {
			return new ArrowAndNoteBox(g2d, messageArrow, note, message.getNotePosition());
		} else if (isSelfMessage()) {
			return new MessageSelfArrow(freeY, drawingSet.getSkin(), drawingSet.getSkin().createComponent(type,
					drawingSet.getSkinParam(), getLabelOfMessage(message)), getLivingParticipantBox1());
		} else {
			return messageArrow;
		}
	}

	private ComponentType getSelfArrowType(Message m) {
		return m.isDotted() ? ComponentType.DOTTED_SELF_ARROW : ComponentType.SELF_ARROW;
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
