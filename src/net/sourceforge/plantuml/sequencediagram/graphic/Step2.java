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

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.SimpleContext2D;

class Step2 {

	private final StepData stepData;

	private final double maxX;
	private final double maxY;
	private final double groupingMargin;

	public Step2(StepData stepData, Dimension2D fullDimension, double groupingMargin) {
		this.stepData = stepData;
		this.maxX = fullDimension.getWidth();
		this.maxY = fullDimension.getHeight();
		this.groupingMargin = groupingMargin;
	}

	public void draw(Graphics2D g2d, Component compTitle, final double delta, int width, int height, Page p) {
		
		double deltaY = 0;
		if (compTitle != null) {
			this.drawTitle(g2d, compTitle);
			deltaY = compTitle.getPreferredHeight(g2d);
		}

		clipAndTranslate(delta, width, p, g2d);
		this.drawPlayground(g2d, height, new SimpleContext2D(true));

		g2d.setClip(null);
		g2d.setTransform(new AffineTransform());
		
		this.drawLine(g2d, height, deltaY);
		this.drawHeadTail(g2d, height, deltaY);

		clipAndTranslate(delta, width, p, g2d);
		this.drawPlayground(g2d, height, new SimpleContext2D(false));
	}

	private void clipAndTranslate(final double delta, int width, Page p, final Graphics2D g2d) {
		g2d.setClip(0, (int) p.getBodyRelativePosition(), width, (int) (p.getBodyHeight() + 1));
		if (delta > 0) {
			g2d.translate(0, -delta);
		}
	}

	private void drawLine(Graphics2D g2d, double height, double deltaY) {
		g2d.translate(groupingMargin, 0);
		final Dimension2D dim = new Dimension2DDouble(maxX, height);
		for (LivingParticipantBox box : stepData.getParticipants().values()) {
			box.getParticipantBox().drawLine(g2d, dim, deltaY);
		}
		g2d.translate(-groupingMargin, 0);

	}

	private void drawHeadTail(Graphics2D g2d, double height, double deltaY) {
		g2d.translate(groupingMargin, 0);
		final Dimension2D dim = new Dimension2DDouble(maxX, height);

		for (LivingParticipantBox box : stepData.getParticipants().values()) {
			box.getParticipantBox().drawHeadTail(g2d, dim, deltaY);
		}
		g2d.translate(-groupingMargin, 0);
	}

	private void drawTitle(Graphics2D g2d, Component compTitle) {
		final double h = compTitle.getPreferredHeight(g2d);
		final double w = compTitle.getPreferredWidth(g2d);
		final double xpos = (maxX - w) / 2;
		g2d.translate(xpos, 0);
		compTitle.draw(g2d, new Dimension2DDouble(w, h), new SimpleContext2D(false));
		g2d.translate(-xpos, 0);
	}

	private void drawPlayground(Graphics2D g2d, double height, Context2D context) {
		g2d.translate(groupingMargin, 0);

		for (Participant p : stepData.getParticipants().keySet()) {
			drawLifeLine(g2d, p);
		}
		for (GraphicalElement element : stepData.getEvents().values()) {
			element.draw(g2d, maxX, context);
		}
		g2d.setClip(null);
		g2d.translate(-groupingMargin, 0);
	}

	// Drawing
	private void drawLifeLine(Graphics2D g2d, Participant p) {
		final LifeLine line = stepData.getParticipants().get(p).getLifeLine();

		line.finish(maxY);
		final Component comp = stepData.getSkin().createComponent(ComponentType.ALIVE_LINE, null);
		line.draw(g2d, comp);

	}

}
