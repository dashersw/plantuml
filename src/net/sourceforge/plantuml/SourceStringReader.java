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

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Arrays;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.plantuml.activitydiagram.ActivityDiagramFactory;
import net.sourceforge.plantuml.classdiagram.ClassDiagramFactory;
import net.sourceforge.plantuml.printskin.PrintSkinFactory;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagramFactory;

public class SourceStringReader {

	private final String source;

	public SourceStringReader(String source) {
		this.source = source;
	}

	public String generateImage(OutputStream os) throws IOException {
		return generateImage(os, 0);
	}

	public String generateImage(OutputStream os, int numImage) throws IOException {
		final SortedMap<Integer, StartUml> r1 = tryThisFactory(new SequenceDiagramFactory());
		final SortedMap<Integer, StartUml> r2 = tryThisFactory(new ClassDiagramFactory());
		final SortedMap<Integer, StartUml> r3 = tryThisFactory(new ActivityDiagramFactory());
		final SortedMap<Integer, StartUml> r4 = tryThisFactory(new PrintSkinFactory());

		final SortedSet<Integer> lines = new TreeSet<Integer>(r1.keySet());
		lines.addAll(r2.keySet());

		int nb = 0;
		for (Integer i : lines) {
			final StartUml s1 = r1.get(i);
			final StartUml s2 = r2.get(i);
			final StartUml s3 = r3.get(i);
			final StartUml s4 = r4.get(i);

			final StartUml system;
			if (isOk(s1.getSystem())) {
				system = s1;
			} else if (isOk(s2.getSystem())) {
				system = s2;
			} else if (isOk(s3.getSystem())) {
				system = s3;
			} else if (isOk(s4.getSystem())) {
				system = s4;
			} else {
				final PSystemError merge = PSystemError.merge((PSystemError) s1.getSystem(), (PSystemError) s2
						.getSystem(), (PSystemError) s3.getSystem());
				system = new StartUml(merge, s1.getStartuml());
			}

			if (nb == numImage) {
				system.getSystem().createPng(os);
				return system.getSystem().getDescription();
			}
			nb++;
		}

		final PngError error = new PngError(Arrays.asList("No @startuml found"));
		error.writeError(os);

		return null;

	}

	private boolean isOk(PSystem ps) {
		if (ps == null || ps instanceof PSystemError) {
			return false;
		}
		return true;
	}

	private SortedMap<Integer, StartUml> tryThisFactory(PSystemFactory systemFactory) throws IOException {

		return new DataReader(new StringReader(source), systemFactory).getAllStartUml();
	}

}
