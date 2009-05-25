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
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.Skin;

class MessageSelfArrow extends SimpleArrow {

	private final LivingParticipantBox p1;

	public MessageSelfArrow(double startingY, Skin skin, Component arrow, LivingParticipantBox p1) {
		super(startingY, skin, arrow);
		this.p1 = p1;
	}

	@Override
	public double getPreferredHeight(Graphics2D g2d) {
		return getArrow().getPreferredHeight(g2d);
	}

	@Override
	public double getPreferredWidth(Graphics2D g2d) {
		return getArrow().getPreferredWidth(g2d);
	}

	@Override
	protected void drawInternal(Graphics2D g2d, double maxX, Context2D context) {
		g2d.translate(getStartingX(g2d), getStartingY());
		getArrow().draw(g2d, new Dimension2DDouble(getPreferredWidth(g2d), getPreferredHeight(g2d)), context);

	}

	@Override
	public double getStartingX(Graphics2D g2d) {
		return p1.getLiveThicknessAt(g2d, getArrowYStartLevel(g2d)).getPos2();
	}

	@Override
	public int getDirection(Graphics2D g2d) {
		return 1;
	}

	public LivingParticipantBox getParticipant1() {
		return p1;
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
