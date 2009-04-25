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
package net.sourceforge.plantuml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PSystemError implements PSystem {

	private final List<String> errors = new ArrayList<String>();

	public PSystemError(String error) {
		this.errors.add(error);
	}

	private PSystemError(Collection<String> errors) {
		this.errors.addAll(errors);
	}

	static public PSystemError merge(PSystemError... ps) {
		final Set<String> set = new TreeSet<String>();
		for (PSystemError system : ps) {
			if (system != null) {
				set.addAll(system.errors);
			}
		}
		if (set.size() == 0) {
			throw new IllegalArgumentException();
		}
		return new PSystemError(set);
	}

	public List<File> createPng(File pngFile) throws IOException, InterruptedException {
		final List<String> strings = new ArrayList<String>();
		strings.add("]SYNTAX ERROR?");
		strings.addAll(errors);
		new PngError(strings).writeError(pngFile);
		return Arrays.asList(pngFile);
	}

	public String getDescription() {
		return "(Error: " + errors.get(0) + ")";
	}

	List<String> getErrors() {
		return errors;
	}

}
