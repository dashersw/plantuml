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
package net.sourceforge.plantuml.code;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Transcoder {

	private final Compression compression;
	private final URLEncoder urlEncoder;
	private final StringCompressor stringCompressor;

	public Transcoder() {
		this(new AsciiEncoder(), new CompressionHuffman());
	}

	public Transcoder(URLEncoder urlEncoder, Compression compression) {
		this(urlEncoder, new ArobaseStringCompressor(), compression);
	}

	public Transcoder(URLEncoder urlEncoder, StringCompressor stringCompressor, Compression compression) {
		this.compression = compression;
		this.urlEncoder = urlEncoder;
		this.stringCompressor = stringCompressor;
	}

	public String encode(String text) throws IOException {

		final String stringAnnoted = stringCompressor.compress(text);

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		final OutputStreamWriter osw = new OutputStreamWriter(baos, "UTF-8");
		osw.write(stringAnnoted);
		osw.close();
		baos.close();
		final byte[] data = baos.toByteArray();
		final byte[] compressedData = compression.compress(data);

		return urlEncoder.encode(compressedData);
	}

	public String decode(String code) throws IOException {
		final byte compressedData[] = urlEncoder.decode(code);
		final byte data[] = compression.decompress(compressedData);

		final ByteArrayInputStream bais = new ByteArrayInputStream(data);

		final InputStreamReader isr = new InputStreamReader(bais, "UTF-8");

		final StringBuilder sb = new StringBuilder();
		int read;
		while ((read = isr.read()) != -1) {
			sb.append((char) read);
		}

		return stringCompressor.decompress(sb.toString());
	}

}
