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
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.plantuml.activitydiagram.ActivityDiagramFactory;
import net.sourceforge.plantuml.classdiagram.ClassDiagramFactory;
import net.sourceforge.plantuml.printskin.PrintSkinFactory;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagramFactory;

class JavaFileReader {

	private final File file;
	private final File outputDirectory;

	public JavaFileReader(File file) {
		this(file, file.getAbsoluteFile().getParentFile());
	}

	public JavaFileReader(File file, File outputDirectory) {
		if (file.exists() == false) {
			throw new IllegalArgumentException();
		}
		if (outputDirectory == null) {
			throw new IllegalArgumentException("outputDirectory null");
		}
		if (outputDirectory.exists() == false) {
			throw new IllegalArgumentException();
		}
		this.file = file;
		this.outputDirectory = outputDirectory;
	}

	public List<StartUml> execute() throws IOException, InterruptedException {
		FileSystem.getInstance().setCurrentDir(file.getParentFile());
		final SortedMap<Integer, PSystemParameter> r1 = execute1(new SequenceDiagramFactory(), Collections
				.<Integer> emptyList());
		final SortedMap<Integer, PSystemParameter> r2 = execute1(new ClassDiagramFactory(), Collections
				.<Integer> emptyList());
		final SortedMap<Integer, PSystemParameter> r3 = execute1(new ActivityDiagramFactory(), Collections
				.<Integer> emptyList());
		final SortedMap<Integer, PSystemParameter> r4 = execute1(new PrintSkinFactory(), Collections
				.<Integer> emptyList());

		final SortedSet<Integer> lines = new TreeSet<Integer>(r1.keySet());
		lines.addAll(r2.keySet());

		final List<StartUml> result = new ArrayList<StartUml>();

		int cpt = 0;
		for (Integer i : lines) {
			final PSystemParameter s1 = r1.get(i);
			final PSystemParameter s2 = r2.get(i);
			final PSystemParameter s3 = r3.get(i);
			final PSystemParameter s4 = r4.get(i);

			final PSystemParameter system;
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
				system = new PSystemParameter(merge, s1.getStartuml());
			}

			String newName = s1.getFilename();

			if (newName == null) {
				newName = changeName(file.getName(), cpt++);
			}

			final File png = new File(outputDirectory, newName);
			final FilePng filePng = new FilePng(file, png);
			png.getParentFile().mkdirs();

			system.getSystem().createPng(png);
			result.add(new StartUml(filePng, system.getSystem().getDescription(), i));
		}

		return Collections.unmodifiableList(result);
	}

	private boolean isOk(PSystem ps) {
		if (ps == null || ps instanceof PSystemError) {
			return false;
		}
		return true;
	}

	private SortedMap<Integer, PSystemParameter> execute1(PSystemFactory systemFactory, Collection<Integer> toSkip)
			throws IOException, InterruptedException {
		
		return new DataReader(new FileReader(file), systemFactory, toSkip).getPSystems();
	}

	static String changeName(String name, int cpt) {
		if (cpt == 0) {
			return name.replaceAll("\\.\\w+$", ".png");
		}
		return name.replaceAll("\\.\\w+$", "_" + String.format("%03d", cpt) + ".png");
	}

}
