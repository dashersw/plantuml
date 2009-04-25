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
package net.sourceforge.plantuml.graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import net.sourceforge.plantuml.sequencediagram.graphic.Dimension2DDouble;

class HtmlLine implements Line {

	private final JLabel jLabel;

	// private final Font font;

	public HtmlLine(String text, Font font, Color paint) {
		jLabel = new JLabel("<html>" + text + "</html>", SwingConstants.LEFT);
		jLabel.setFont(font);
		jLabel.setForeground(paint);
		// this.font = font;

	}

	public Dimension2D calculateDimensions(Graphics2D g2d) {
		jLabel.setAlignmentX(0);
		jLabel.setAlignmentY(0);
		// System.err.println("dim=" + dim);
		// System.err.println("border=" + jLabel.getBorder());
		// System.err.println("inset=" + jLabel.getInsets());
		// System.err.println("verticalAligne=" +
		// jLabel.getVerticalAlignment());
		// System.err.println("visibleRect=" + jLabel.getVisibleRect());
		// System.err.println("X=" + jLabel.getAlignmentX());
		// System.err.println("Y=" + jLabel.getAlignmentY());
		// System.err.println("Pref=" + jLabel.getPreferredSize());
		// System.err.println("Min=" + jLabel.getMinimumSize());
		final Dimension pref = jLabel.getPreferredSize();
		// final FontMetrics fm = g2d.getFontMetrics(font);
		// System.err.println("getMaxAdvance=" + fm.getMaxAdvance());
		// System.err.println("getMaxAscent=" + fm.getMaxAscent());
		// System.err.println("getMaxDecent=" + fm.getMaxDecent());
		// System.err.println("getMaxDescent=" + fm.getMaxDescent());
		return new Dimension2DDouble(pref.getWidth(), pref.getHeight());
	}

	public void draw(Graphics2D g2d, double x, double y) {
		final Dimension2D dim = calculateDimensions(g2d);
		final AffineTransform at = g2d.getTransform();
		g2d.translate(x, y);
		jLabel.setBounds(0, 0, (int) dim.getWidth(), (int) dim.getHeight());
		g2d.setTransform(at);
		jLabel.print(g2d);
	}
}
