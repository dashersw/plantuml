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
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;

import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.preproc.Defines;

public class SourceStringReader extends AbstractSourceReader {

	private final String source;

	public SourceStringReader(String source) {
		this(new Defines(), source);
	}

	public SourceStringReader(Defines defines, String source) {
		super(defines);
		this.source = source;
	}

	public String generateImage(OutputStream os) throws IOException {
		return generateImage(os, 0);
	}

	public String generateImage(OutputStream os, int numImage) throws IOException {
		try {
			int nb = 0;
			for (StartUml startUml : getAllStartUml(Collections.<String> emptyList())) {

				if (nb == numImage) {
					startUml.getSystem().createPng(os);
					return startUml.getSystem().getDescription();
				}
				nb++;
			}

			final GraphicStrings error = new GraphicStrings(Arrays.asList("No @startuml found"));
			error.writeImage(os);

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
