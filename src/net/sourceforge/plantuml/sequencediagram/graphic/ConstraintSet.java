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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ConstraintSet {

	private final ParticipantBoxSimple firstBorder;

	private final ParticipantBoxSimple lastborder;

	final private List<Pushable> participantList = new ArrayList<Pushable>();
	final private Map<List<Pushable>, Constraint> constraints = new HashMap<List<Pushable>, Constraint>();

	public ConstraintSet(Collection<? extends Pushable> all, double freeX) {

		this.participantList.add(firstBorder = new ParticipantBoxSimple(0));
		this.participantList.addAll(all);
		this.participantList.add(lastborder = new ParticipantBoxSimple(freeX));
	}

	public double getMaxX() {
		return lastborder.getCenterX(null);
	}

	public Constraint getConstraint(Pushable p1, Pushable p2) {
		if (p1 == null || p2 == null || p1 == p2) {
			throw new IllegalArgumentException();
		}
		final int i1 = participantList.indexOf(p1);
		final int i2 = participantList.indexOf(p2);
		if (i1 == -1 || i2 == -1) {
			throw new IllegalArgumentException();
		}
		if (i1 > i2) {
			return getConstraint(p2, p1);
		}
		final List<Pushable> key = Arrays.asList(p1, p2);
		Constraint result = constraints.get(key);
		if (result == null) {
			result = new Constraint(p1, p2);
			constraints.put(key, result);
		}
		return result;
	}

	public Constraint getConstraintAfter(Pushable p1) {
		if (p1 == null) {
			throw new IllegalArgumentException();
		}
		return getConstraint(p1, getOtherParticipant(p1, 1));
	}

	public Constraint getConstraintBefore(Pushable p1) {
		if (p1 == null) {
			throw new IllegalArgumentException();
		}
		return getConstraint(p1, getOtherParticipant(p1, -1));
	}

	private Pushable getOtherParticipant(Pushable p, int delta) {
		final int i = participantList.indexOf(p);
		if (i == -1) {
			throw new IllegalArgumentException();
		}
		return participantList.get(i + delta);
	}

	public void takeConstraintIntoAccount(Graphics2D g2d) {
		for (int dist = 1; dist < participantList.size(); dist++) {
			pushEverybody(g2d, dist);
		}
	}

	private void pushEverybody(Graphics2D g2d, int dist) {
		for (int i = 0; i < participantList.size() - dist; i++) {
			final Pushable p1 = participantList.get(i);
			final Pushable p2 = participantList.get(i + dist);
			final Constraint c = getConstraint(p1, p2);
			ensureSpaceAfter(g2d, p1, p2, c.getValue());
		}
	}

	private void pushToLeftParticipantBox(double deltaX, Pushable firstToChange) {
		if (deltaX <= 0) {
			throw new IllegalArgumentException();
		}
		if (firstToChange == null) {
			throw new IllegalArgumentException();
		}
		// freeX += deltaX;
		boolean founded = false;
		for (Pushable box : participantList) {
			if (box.equals(firstToChange)) {
				founded = true;
			}
			if (founded) {
				box.pushToLeft(deltaX);
			}
		}
	}

	public void pushToLeft(double delta) {
		pushToLeftParticipantBox(delta, firstBorder);
	}

	private void ensureSpaceAfter(Graphics2D g2d, Pushable p1, Pushable p2, double space) {
		if (p1.equals(p2)) {
			throw new IllegalArgumentException();
		}
		if (p1.getCenterX(g2d) > p2.getCenterX(g2d)) {
			ensureSpaceAfter(g2d, p2, p1, space);
			return;
		}
		assert p1.getCenterX(g2d) < p2.getCenterX(g2d);
		final double existingSpace = p2.getCenterX(g2d) - p1.getCenterX(g2d);
		if (existingSpace < space) {
			pushToLeftParticipantBox(space - existingSpace, p2);
		}

	}

	public final Pushable getFirstBorder() {
		return firstBorder;
	}

	public final Pushable getLastborder() {
		return lastborder;
	}

}
