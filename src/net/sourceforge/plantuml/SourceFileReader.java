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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.preproc.Defines;

public class SourceFileReader extends AbstractSourceReader {

	private final File file;
	private final File outputDirectory;
	private final List<String> config;

	public SourceFileReader(File file) throws IOException {
		this(file, file.getAbsoluteFile().getParentFile());
	}

	public SourceFileReader(final File file, File outputDirectory) throws IOException {
		this(new Defines(), file, outputDirectory, Collections.<String> emptyList());
	}

	public SourceFileReader(Defines defines, final File file, File outputDirectory, List<String> config)
			throws IOException {
		super(defines);
		this.config = config;
		if (file.exists() == false) {
			throw new IllegalArgumentException();
		}
		FileSystem.getInstance().setCurrentDir(file.getParentFile());
		if (outputDirectory == null) {
			outputDirectory = file.getAbsoluteFile().getParentFile();
		} else if (outputDirectory.isAbsolute() == false) {
			outputDirectory = FileSystem.getInstance().getFile(outputDirectory.getName());
		}
		if (outputDirectory.exists() == false) {
			outputDirectory.mkdirs();
		}
		this.file = file;
		this.outputDirectory = outputDirectory;
	}

	public List<GeneratedImage> getGeneratedImages() throws IOException, InterruptedException {
		Log.info("Reading file: " + file);

		int cpt = 0;
		final List<GeneratedImage> result = new ArrayList<GeneratedImage>();

		for (StartUml startUml : getAllStartUml(config)) {
			String newName = startUml.getFilename();

			if (newName == null) {
				newName = changeName(file.getName(), cpt++);
			}

			final File png = new File(outputDirectory, newName);
			png.getParentFile().mkdirs();

			for (File f : startUml.getSystem().createPng(png)) {
				final String desc = "[" + file.getName() + "] " + startUml.getSystem().getDescription();
				final GeneratedImage generatedImage = new GeneratedImage(f, desc);
				result.add(generatedImage);
			}

		}

		Log.info("Number of image(s): " + result.size());

		return Collections.unmodifiableList(result);
	}

	static String changeName(String name, int cpt) {
		if (cpt == 0) {
			return name.replaceAll("\\.\\w+$", ".png");
		}
		return name.replaceAll("\\.\\w+$", "_" + String.format("%03d", cpt) + ".png");
	}

	@Override
	protected Reader getReader() throws FileNotFoundException {
		return new FileReader(file);
	}

}
