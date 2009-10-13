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
package net.sourceforge.plantuml.graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;

class TextBlockSpotted extends TextBlockSimple {

	private final CircledCharacter circledCharacter;

	public TextBlockSpotted(CircledCharacter circledCharacter, List<? extends CharSequence> texts, Font font,
			Color paint, HorizontalAlignement horizontalAlignement) {
		super(texts, font, paint, horizontalAlignement);
		this.circledCharacter = circledCharacter;
	}

	@Override
	public Dimension2D calculateDimension(Graphics2D g2d) {
		final double widthCircledCharacter = getCircledCharacterWithAndMargin(g2d);
		final double heightCircledCharacter = circledCharacter.getPreferredHeight(g2d);

		final Dimension2D dim = super.calculateDimension(g2d);
		return new Dimension2DDouble(dim.getWidth() + widthCircledCharacter, Math.max(heightCircledCharacter, dim
				.getHeight()));
	}

	private double getCircledCharacterWithAndMargin(Graphics2D g2d) {
		return circledCharacter.getPreferredWidth(g2d) + 6.0;
	}

	@Override
	public void draw(Graphics2D g2d, double x, double y) {
		final AffineTransform at = g2d.getTransform();

		final double deltaY = calculateDimension(g2d).getHeight() - circledCharacter.getPreferredHeight(g2d);

		//g2d.translate(x, y + deltaY / 2.0);
		circledCharacter.draw(g2d, (int) x, (int) (y + deltaY / 2.0));
		//circledCharacter.draw(g2d);

		g2d.setTransform(at);
		final double widthCircledCharacter = getCircledCharacterWithAndMargin(g2d);
		g2d.translate(widthCircledCharacter, 0);

		super.draw(g2d, x, y);

		g2d.setTransform(at);
	}

}
