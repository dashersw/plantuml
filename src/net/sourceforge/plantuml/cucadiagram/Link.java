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

public class Link {

	final private Entity cl1;
	final private Entity cl2;
	final private LinkType type;
	final private String label;
	final private int lenght;
	final private String qualifier1;
	final private String qualifier2;

	public Link(Entity cl1, Entity cl2, LinkType type, String label, int length, String qualifier1, String qualifier2) {
		if (length < 1) {
			throw new IllegalArgumentException();
		}
		this.cl1 = cl1;
		this.cl2 = cl2;
		this.type = type;
		this.label = label;
		this.lenght = length;
		this.qualifier1 = qualifier1;
		this.qualifier2 = qualifier2;
	}

	public Entity getEntity1() {
		return cl1;
	}

	public Entity getEntity2() {
		return cl2;
	}

	public LinkType getType() {
		return type;
	}

	public String getLabel() {
		return label;
	}

	public int getLenght() {
		return lenght;
	}

	public String getQualifier1() {
		return qualifier1;
	}

	public String getQualifier2() {
		return qualifier2;
	}

}
