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
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.plantuml.activitydiagram.ActivityDiagramFactory;
import net.sourceforge.plantuml.classdiagram.ClassDiagramFactory;
import net.sourceforge.plantuml.printskin.PrintSkinFactory;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagramFactory;

public class SourceStringReader {

	private final String source;
	private final File outputFile;

	public SourceStringReader(String source, File outputFile) {
		if (outputFile == null) {
			throw new IllegalArgumentException("outputDirectory null");
		}
		this.source = source;
		this.outputFile = outputFile;
	}

	public List<GeneratedImage> getGeneratedImages() throws IOException, InterruptedException {
		final SortedMap<Integer, StartUml> r1 = tryThisFactory(new SequenceDiagramFactory());
		final SortedMap<Integer, StartUml> r2 = tryThisFactory(new ClassDiagramFactory());
		final SortedMap<Integer, StartUml> r3 = tryThisFactory(new ActivityDiagramFactory());
		final SortedMap<Integer, StartUml> r4 = tryThisFactory(new PrintSkinFactory());

		final SortedSet<Integer> lines = new TreeSet<Integer>(r1.keySet());
		lines.addAll(r2.keySet());

		final List<GeneratedImage> result = new ArrayList<GeneratedImage>();

		int cpt = 0;
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

			String newName = s1.getFilename();

			if (newName == null) {
				newName = changeName(outputFile.getName(), cpt++);
			}

			final File png = new File(outputFile.getParentFile(), newName);
			png.getParentFile().mkdirs();

			system.getSystem().createPng(png);
			final String desc = "[" + source.length() + " characters] " + system.getSystem().getDescription();
			final GeneratedImage generatedImage = new GeneratedImage(png, desc);
			result.add(generatedImage);
		}

		return Collections.unmodifiableList(result);
	}

	private boolean isOk(PSystem ps) {
		if (ps == null || ps instanceof PSystemError) {
			return false;
		}
		return true;
	}

	private SortedMap<Integer, StartUml> tryThisFactory(PSystemFactory systemFactory) throws IOException,
			InterruptedException {

		return new DataReader(new StringReader(source), systemFactory).getAllStartUml();
	}

	static String changeName(String name, int cpt) {
		if (cpt == 0) {
			return name.replaceAll("\\.\\w+$", ".png");
		}
		return name.replaceAll("\\.\\w+$", "_" + String.format("%03d", cpt) + ".png");
	}

}
