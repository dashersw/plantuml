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
package net.sourceforge.plantuml.cucadiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Entity {

	private static int CPT = 0;

	private final String code;
	private final String display;

	private final int cpt = CPT++;
	private final EntityType type;

	private final String stereotype;

	private final List<String> fields = new ArrayList<String>();
	private final List<String> methods = new ArrayList<String>();

	public Entity(String code, String display, EntityType type) {
		this(code, display, type, null);
	}

	public Entity(String code, String display, EntityType type, String stereotype) {
		if (code == null || code.length() == 0) {
			throw new IllegalArgumentException();
		}
		if (display == null || display.length() == 0) {
			throw new IllegalArgumentException();
		}
		this.type = type;
		this.code = code;
		this.display = display;
		this.stereotype = stereotype;
	}

	public void addFieldOrMethod(String s) {
		if (isMethod(s)) {
			methods.add(s);
		} else {
			fields.add(s);
		}
	}

	private boolean isMethod(String s) {
		return s.contains("(") || s.contains(")");
	}

	public List<String> methods() {
		return Collections.unmodifiableList(methods);
	}

	public List<String> fields() {
		return Collections.unmodifiableList(fields);
	}

	public EntityType getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public String getDisplay() {
		return display;
	}

	public String getUid() {
		return "cl" + cpt;
	}

	public String getStereotype() {
		return stereotype;
	}

}
