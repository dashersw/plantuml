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

import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;

class ArrowAndNoteBox extends Arrow {

	private final SimpleArrow arrow;
	private final NoteBox noteBox;

	public ArrowAndNoteBox(Graphics2D g2d, SimpleArrow arrow, Component noteComp, NotePosition notePosition) {
		super(arrow.getStartingY(), arrow.getSkin());
		this.arrow = arrow;

		final double arrowHeight = arrow.getPreferredHeight(g2d);

		if (arrow instanceof MessageSelfArrow) {
			final MessageSelfArrow m = (MessageSelfArrow) arrow;
			noteBox = new NoteBox(arrow.getStartingY(), noteComp, m.getParticipant1(), null, notePosition);
			if (notePosition == NotePosition.RIGHT) {
				noteBox.pushToRight(arrow.getPreferredWidth(g2d));
			}
		} else {
			final MessageArrow m = (MessageArrow) arrow;
			noteBox = new NoteBox(arrow.getStartingY(), noteComp, m.getParticipantAt(g2d, notePosition), null,
					notePosition);
		}

		final double noteHeight = noteBox.getPreferredHeight(g2d);
		final double myHeight = getPreferredHeight(g2d);

		final double diffHeightArrow = myHeight - arrowHeight;
		final double diffHeightNote = myHeight - noteHeight;
		if (diffHeightArrow > 0) {
			arrow.pushToDown(diffHeightArrow / 2);
		}
		if (diffHeightNote > 0) {
			noteBox.pushToDown(diffHeightNote / 2);
		}
	}
	
	@Override
	final public double getArrowOnlyWidth(Graphics2D g2d) {
		return arrow.getPreferredWidth(g2d);
	}


	@Override
	protected void drawInternal(Graphics2D g2d, double maxX, Context2D context) {
		arrow.draw(g2d, maxX, context);
		noteBox.draw(g2d, maxX, context);

	}

	@Override
	public double getPreferredHeight(Graphics2D g2d) {
		return Math.max(arrow.getPreferredHeight(g2d), noteBox.getPreferredHeight(g2d));
	}

	@Override
	public double getPreferredWidth(Graphics2D g2d) {
		double w = arrow.getPreferredWidth(g2d);
		if (arrow instanceof MessageArrow) {
			final MessageArrow messageArrow = (MessageArrow) arrow;
			w = Math.max(w, messageArrow.getActualWidth(g2d));
		}
		return w + noteBox.getPreferredWidth(g2d);
	}

	@Override
	public double getStartingX(Graphics2D g2d) {
		return Math.min(arrow.getStartingX(g2d), noteBox.getStartingX(g2d));
	}

	@Override
	public int getDirection(Graphics2D g2d) {
		return arrow.getDirection(g2d);
	}

	@Override
	public double getArrowYStartLevel(Graphics2D g2d) {
		return arrow.getArrowYStartLevel(g2d);
	}

	@Override
	public double getArrowYEndLevel(Graphics2D g2d) {
		return arrow.getArrowYEndLevel(g2d);
	}

}
