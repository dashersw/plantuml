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
package net.sourceforge.plantuml.cucadiagram;

public enum LinkType {

	ASSOCIED, ASSOCIED_DASHED,
	EXTENDS, EXTENDS_INV,
	IMPLEMENTS, IMPLEMENTS_INV,
	COMPOSITION, COMPOSITION_INV,
	AGREGATION, AGREGATION_INV,
	NAVASSOC, NAVASSOC_INV,
	NAVASSOC_DASHED, NAVASSOC_DASHED_INV;
	
	public boolean isDashed() {
		return this == ASSOCIED_DASHED || this == IMPLEMENTS || this == IMPLEMENTS_INV || this == NAVASSOC_DASHED
				|| this == NAVASSOC_DASHED_INV;
	}
	
	public LinkType getDashed() {
		if (this == NAVASSOC) {
			return NAVASSOC_DASHED;
		}
		if (this == ASSOCIED) {
			return ASSOCIED_DASHED;
		}
		if (this == EXTENDS) {
			return IMPLEMENTS;
		}
		throw new UnsupportedOperationException();
	}
	
	public LinkType getInv() {
		if (this == EXTENDS) {
			return EXTENDS_INV;
		}
		if (this == IMPLEMENTS) {
			return IMPLEMENTS_INV;
		}
		if (this == COMPOSITION) {
			return COMPOSITION_INV;
		}
		if (this == AGREGATION) {
			return AGREGATION_INV;
		}
		if (this == NAVASSOC) {
			return NAVASSOC_INV;
		}
		if (this == NAVASSOC_DASHED) {
			return NAVASSOC_DASHED_INV;
		}
		throw new UnsupportedOperationException();
	}

}
