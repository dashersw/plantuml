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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Transcoder2 {

	private final Compression compression;
	private final URLEncoder urlEncoder;
	private final StringCompressor stringCompressor;

	public Transcoder2() {
		this(new AsciiEncoder(), new CompressionHuffman());
	}

	public Transcoder2(URLEncoder urlEncoder, Compression compression) {
		this(urlEncoder, new ArobaseStringCompressor(), compression);
	}

	public Transcoder2(URLEncoder urlEncoder, StringCompressor stringCompressor, Compression compression) {
		this.compression = compression;
		this.urlEncoder = urlEncoder;
		this.stringCompressor = stringCompressor;
	}

	public String encode(String text) throws IOException {

		final String stringAnnoted = stringCompressor.compress(text);

		final int d[] = new int[stringAnnoted.length()];
		for (int i = 0; i < d.length; i++) {
			d[i] = text.charAt(i);
		}

		final List<byte[]> list = new ArraySplitter().split(d);

		final byte b0[] = compression.compress(list.get(0));
		final byte b1[] = compression.compress(list.get(1));

		final byte brut[] = new Multiplexer().mix(Arrays.asList(b0, b1));

		return urlEncoder.encode(brut);
	}

	public String decode(String code) throws IOException {
		final byte compressedData[] = urlEncoder.decode(code);

		final List<byte[]> tabs = new Multiplexer().unmix(compressedData);
		tabs.set(0, compression.decompress(tabs.get(0)));
		tabs.set(1, compression.decompress(tabs.get(1)));
		final int chars[] = new ArraySplitter().merge(tabs);
		final StringBuilder sb = new StringBuilder();

		for (int c : chars) {
			sb.append((char) c);
		}

		return stringCompressor.decompress(sb.toString());
	}

}
