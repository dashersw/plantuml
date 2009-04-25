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
package net.sourceforge.plantuml.swing;

import net.sourceforge.plantuml.FilePng;

public class SimpleLine implements Comparable<SimpleLine>{

	private final FilePng fireResult;
	private String desc;

	public SimpleLine(FilePng f, String desc) {
		this.fireResult = f;
		this.desc = desc;
	}

	public String toString() {
		final StringBuilder sb = new StringBuilder(fireResult.getPngFile().getName());
		sb.append(" ");
		sb.append(desc);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return fireResult.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		final SimpleLine this2 = (SimpleLine) obj;
		return this2.fireResult.equals(this.fireResult);
	}

	public boolean exists() {
		return fireResult.getPngFile().exists();
	}

	public int compareTo(SimpleLine this2) {
		return this.fireResult.compareTo(this2.fireResult);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public FilePng getFireResult() {
		return fireResult;
	}

}
