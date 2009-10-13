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
package net.sourceforge.plantuml.sequencediagram;

public class Grouping implements Event {

	private final String title;
	private final GroupingType type;
	private Grouping father;
	private final String comment;

	public Grouping(String title, String comment, GroupingType type) {
		this.title = title;
		this.comment = comment;
		this.type = type;
	}

	public Grouping getFather() {
		return father;
	}

	public void setFather(Grouping g) {
		if (father != null) {
			throw new IllegalStateException();
		}
		this.father = g;
	}

	public String getTitle() {
		return title;
	}

	public GroupingType getType() {
		return type;
	}

	public int getLevel() {
		if (type != GroupingType.START) {
			return getFather().getLevel();
		}
		if (getFather() == null) {
			return 0;
		}
		return getFather().getLevel() + 1;
	}

	public String getComment() {
		return comment;
	}

}
