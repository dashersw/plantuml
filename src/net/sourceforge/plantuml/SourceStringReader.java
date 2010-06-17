/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 4041 $
 *
 */
package net.sourceforge.plantuml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.preproc.Defines;

public class SourceStringReader extends AbstractSourceReader {

	private final String source;
	private final List<String> config;

	public SourceStringReader(String source) {
		this(new Defines(), source, Collections.<String> emptyList());
	}

	public SourceStringReader(Defines defines, String source, List<String> config) {
		super(defines);
		this.source = source;
		this.config = config;
	}

	public String generateImage(OutputStream os) throws IOException {
		return generateImage(os, 0);
	}

	public String generateImage(OutputStream os, FileFormat fileFormat) throws IOException {
		return generateImage(os, 0, fileFormat);
	}

	public String generateImage(OutputStream os, int numImage) throws IOException {
		return generateImage(os, numImage, FileFormat.PNG);
	}

	public String generateImage(OutputStream os, int numImage, FileFormat fileFormat) throws IOException {
		try {
			for (StartUml startUml : getAllStartUml(config)) {
				startUml.getSystem().createFile(os, numImage, fileFormat);
				return startUml.getSystem().getDescription();
			}

			final GraphicStrings error = new GraphicStrings(Arrays.asList("No @startuml found"));
			error.writeImage(os, fileFormat);

			return null;
		} catch (InterruptedException e) {
			return null;
		}
	}

	@Override
	protected Reader getReader() {
		return new StringReader(source);
	}

}
