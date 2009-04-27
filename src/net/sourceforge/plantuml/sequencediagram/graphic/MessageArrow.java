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
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.Skin;

class MessageArrow extends SimpleArrow {

	private final LivingParticipantBox p1;
	private final LivingParticipantBox p2;

	public MessageArrow(double startingY, Skin skin, Component arrow, LivingParticipantBox p1, LivingParticipantBox p2) {
		super(startingY, skin, arrow);

		if (p1 == p2) {
			throw new IllegalArgumentException();
		}
		if (p1 == null || p2 == null) {
			throw new IllegalArgumentException();
		}
		this.p1 = p1;
		this.p2 = p2;
	}

	public double getActualWidth(Graphics2D g2d) {
		final double r = getRightEndInternal(g2d) - getLeftStartInternal(g2d);
		assert r > 0;
		return r;
	}

	private double getLeftStartInternal(Graphics2D g2d) {
		return getParticipantAt(g2d, NotePosition.LEFT).getLiveThicknessAt(g2d, getArrowYStartLevel(g2d)).getPos2();
	}

	private double getRightEndInternal(Graphics2D g2d) {
		return getParticipantAt(g2d, NotePosition.RIGHT).getLiveThicknessAt(g2d, getArrowYStartLevel(g2d)).getPos1();
	}

	public double getPreferredHeight(Graphics2D g2d) {
		return getArrow().getPreferredHeight(g2d);
	}

	public double getStartingX(Graphics2D g2d) {
		return getLeftStartInternal(g2d);
	}

	public int getDirection(Graphics2D g2d) {
		final double x1 = p1.getParticipantBox().getCenterX(g2d);
		final double x2 = p2.getParticipantBox().getCenterX(g2d);
		if (x1 < x2) {
			return 1;
		}
		return -1;
	}

	public LivingParticipantBox getParticipantAt(Graphics2D g2d, NotePosition position) {
		final int direction = getDirection(g2d);
		if (direction == 1 && position == NotePosition.RIGHT) {
			return p2;
		}
		if (direction == 1 && position == NotePosition.LEFT) {
			return p1;
		}
		if (direction == -1 && position == NotePosition.RIGHT) {
			return p1;
		}
		if (direction == -1 && position == NotePosition.LEFT) {
			return p2;
		}
		throw new IllegalArgumentException();
	}

	@Override
	public double getPreferredWidth(Graphics2D g2d) {
		return getArrow().getPreferredWidth(g2d);
	}

	@Override
	protected void drawInternal(Graphics2D g2d, double maxX, Context2D context) {
		g2d.translate(getStartingX(g2d), getStartingY());
		getArrow().draw(g2d, getActualDimension(g2d), context);
	}

	private Dimension2D getActualDimension(Graphics2D g2d) {
		return new Dimension2DDouble(getActualWidth(g2d), getArrow().getPreferredHeight(g2d));
	}

	@Override
	public double getArrowYStartLevel(Graphics2D g2d) {
		if (getArrow() instanceof ArrowComponent) {
			final ArrowComponent arrowComponent = (ArrowComponent) getArrow();
			final Dimension2D dim = new Dimension2DDouble(arrowComponent.getPreferredWidth(g2d), arrowComponent
					.getPreferredHeight(g2d));
			return getStartingY() + arrowComponent.getStartPoint(g2d, dim).getY();
		}
		return getStartingY();
	}

	@Override
	public double getArrowYEndLevel(Graphics2D g2d) {
		if (getArrow() instanceof ArrowComponent) {
			final ArrowComponent arrowComponent = (ArrowComponent) getArrow();
			final Dimension2D dim = new Dimension2DDouble(arrowComponent.getPreferredWidth(g2d), arrowComponent
					.getPreferredHeight(g2d));
			return getStartingY() + arrowComponent.getEndPoint(g2d, dim).getY();
		}
		return getStartingY() + getArrow().getPreferredHeight(g2d);
	}
}
