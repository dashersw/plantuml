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
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.plantuml.activitydiagram.ActivityDiagramFactory;
import net.sourceforge.plantuml.classdiagram.ClassDiagramFactory;
import net.sourceforge.plantuml.componentdiagram.ComponentDiagramFactory;
import net.sourceforge.plantuml.eggs.PSystemEggFactory;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.preproc.Preprocessor;
import net.sourceforge.plantuml.printskin.PrintSkinFactory;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagramFactory;
import net.sourceforge.plantuml.sudoku.PSystemSudokuFactory;
import net.sourceforge.plantuml.usecasediagram.UsecaseDiagramFactory;
import net.sourceforge.plantuml.version.PSystemVersionFactory;

public abstract class AbstractSourceReader {

	private final Defines defines;

	public AbstractSourceReader(Defines defines) {
		this.defines = defines;
	}

	private SortedMap<Integer, StartUml> tryThisFactory(List<String> strings, PSystemFactory systemFactory)
			throws IOException, InterruptedException {
		return new DataReader(strings, systemFactory).getAllStartUml();

	}

	protected List<StartUml> getAllStartUml() throws IOException, InterruptedException {
		final List<String> strings = getStrings(getReader());
		final SortedMap<Integer, StartUml> r1 = tryThisFactory(strings, new SequenceDiagramFactory());
		final SortedMap<Integer, StartUml> r2 = tryThisFactory(strings, new ClassDiagramFactory());
		final SortedMap<Integer, StartUml> r3 = tryThisFactory(strings, new ActivityDiagramFactory());
		final SortedMap<Integer, StartUml> r3b = tryThisFactory(strings, new UsecaseDiagramFactory());
		final SortedMap<Integer, StartUml> r3c = tryThisFactory(strings, new ComponentDiagramFactory());
		final SortedMap<Integer, StartUml> r4 = tryThisFactory(strings, new PrintSkinFactory());
		final SortedMap<Integer, StartUml> r5 = tryThisFactory(strings, new PSystemVersionFactory());
		final SortedMap<Integer, StartUml> r6 = tryThisFactory(strings, new PSystemSudokuFactory());
		final SortedMap<Integer, StartUml> r7 = tryThisFactory(strings, new PSystemEggFactory());

		final SortedSet<Integer> lines = new TreeSet<Integer>(r1.keySet());
		lines.addAll(r2.keySet());
		lines.addAll(r3.keySet());
		lines.addAll(r3b.keySet());
		lines.addAll(r3c.keySet());
		lines.addAll(r4.keySet());
		lines.addAll(r5.keySet());
		lines.addAll(r6.keySet());
		lines.addAll(r7.keySet());

		final List<StartUml> result = new ArrayList<StartUml>();

		for (Integer i : lines) {
			final StartUml s1 = r1.get(i);
			final StartUml s2 = r2.get(i);
			final StartUml s3 = r3.get(i);
			final StartUml s3b = r3b.get(i);
			final StartUml s3c = r3c.get(i);
			final StartUml s4 = r4.get(i);
			final StartUml s5 = r5.get(i);
			final StartUml s6 = r6.get(i);
			final StartUml s7 = r7.get(i);

			final StartUml system;
			if (isOk(s1.getSystem())) {
				system = s1;
			} else if (isOk(s2.getSystem())) {
				system = s2;
			} else if (isOk(s3.getSystem())) {
				system = s3;
			} else if (isOk(s3b.getSystem())) {
				system = s3b;
			} else if (isOk(s3c.getSystem())) {
				system = s3c;
			} else if (isOk(s4.getSystem())) {
				system = s4;
			} else if (isOk(s5.getSystem())) {
				system = s5;
			} else if (isOk(s6.getSystem())) {
				system = s6;
			} else if (isOk(s7.getSystem())) {
				system = s7;
			} else {
				final PSystemError merge = PSystemError.merge((PSystemError) s1.getSystem(), (PSystemError) s2
						.getSystem(), (PSystemError) s3.getSystem(), (PSystemError) s3b.getSystem(), (PSystemError) s3c.getSystem());
				system = new StartUml(merge, s1.getStartuml());
			}

			result.add(system);
		}

		return result;
	}

	private boolean isOk(PSystem ps) {
		if (ps == null || ps instanceof PSystemError) {
			return false;
		}
		return true;
	}

	protected final List<String> getStrings(Reader reader) throws IOException {
		final List<String> result = new ArrayList<String>();
		Preprocessor includer = null;
		try {
			includer = new Preprocessor(reader, defines);
			String s = null;
			while ((s = includer.readLine()) != null) {
				result.add(s);
			}
		} finally {
			if (includer != null) {
				includer.close();
			}
		}
		return Collections.unmodifiableList(result);
	}

	protected abstract Reader getReader() throws IOException;
}
