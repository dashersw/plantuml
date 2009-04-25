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
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.SimpleContext2D;

class Segment {

	final private double pos1;
	final private double pos2;

	Segment(double pos1, double pos2) {
		this.pos1 = pos1;
		this.pos2 = pos2;
		if (pos2 < pos1) {
			throw new IllegalArgumentException("pos1=" + pos1 + " pos2=" + pos2);
		}
	}

	@Override
	public boolean equals(Object obj) {
		final Segment this2 = (Segment) obj;
		return pos1 == this2.pos1 && pos2 == this2.pos2;
	}

	@Override
	public int hashCode() {
		return new Double(pos1).hashCode() + new Double(pos2).hashCode();
	}

	public boolean contains(double y) {
		return y >= pos1 && y <= pos2;
	}

	@Override
	public String toString() {
		return "" + pos1 + " - " + pos2;
	}

	public void draw(Graphics2D g2d, Component comp, int level) {
		final AffineTransform t = g2d.getTransform();
		g2d.translate((level - 1) * comp.getPreferredWidth(g2d) / 2, pos1);
		final Dimension2D dim = new Dimension2DDouble(comp.getPreferredWidth(g2d), pos2 - pos1);
		comp.draw(g2d, dim, new SimpleContext2D(false));
		g2d.setTransform(t);
	}

	public double getLength() {
		return pos2 - pos1;
	}

	public double getPos1() {
		return pos1;
	}

	public double getPos2() {
		return pos2;
	}

	public Segment merge(Segment this2) {
		return new Segment(Math.min(this.pos1, this2.pos1), Math.max(this.pos2, this2.pos2));
	}

}
