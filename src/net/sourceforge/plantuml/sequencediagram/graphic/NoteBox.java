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

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;

class NoteBox extends GraphicalElement {

	private final NotePosition position;

	private final LivingParticipantBox p1;
	private final LivingParticipantBox p2;

	private final Component comp;

	private double delta = 0;

	public NoteBox(double startingY, Component comp, LivingParticipantBox p1, LivingParticipantBox p2,
			NotePosition position) {
		super(startingY);
		if (p1 == null) {
			throw new IllegalArgumentException();
		}
		if (p2 != null ^ position == NotePosition.OVER_SEVERAL) {
			throw new IllegalArgumentException();
		}
		this.p1 = p1;
		this.p2 = p2;
		this.position = position;
		this.comp = comp;
	}

	final public double getPreferredWidth(Graphics2D g2d) {
		return comp.getPreferredWidth(g2d);
	}

	final public double getPreferredHeight(Graphics2D g2d) {
		return comp.getPreferredHeight(g2d);
	}

	@Override
	protected void drawInternal(Graphics2D g2d, double maxX, Context2D context) {
		final double xStart = getStartingX(g2d);
		g2d.translate(xStart, getStartingY());
		final Dimension2D dimensionToUse = new Dimension2DDouble(comp.getPreferredWidth(g2d), comp
				.getPreferredHeight(g2d));
		comp.draw(g2d, dimensionToUse, context);
	}

	public double getStartingX(Graphics2D g2d) {
		final Segment segment = getSegment(g2d);
		final int xStart;
		if (position == NotePosition.LEFT) {
			xStart = (int) (segment.getPos1() - getPreferredWidth(g2d));
		} else if (position == NotePosition.RIGHT) {
			xStart = (int) (segment.getPos2());
		} else if (position == NotePosition.OVER) {
			xStart = (int) (p1.getParticipantBox().getCenterX(g2d) - getPreferredWidth(g2d) / 2);
		} else if (position == NotePosition.OVER_SEVERAL) {
			final double centre = (p1.getParticipantBox().getCenterX(g2d) + p2.getParticipantBox().getCenterX(g2d)) / 2.0;
			xStart = (int) (centre - getPreferredWidth(g2d) / 2.0);
		} else {
			throw new IllegalStateException();
		}
		return xStart + delta;
	}

	private Segment getSegment(Graphics2D g2d) {
		final Segment segment = p1.getLiveThicknessAt(g2d, getStartingY());
		final Segment segment2 = p1.getLiveThicknessAt(g2d, getStartingY() + comp.getPreferredHeight(g2d));
		return segment.merge(segment2);
	}

	public void pushToRight(double x) {
		this.delta += x;
	}

}
