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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Transcoder3 {

	private final Compression compression;
	private final URLEncoder urlEncoder;
	private final StringCompressor stringCompressor;

	public Transcoder3() {
		this(new AsciiEncoder(), new CompressionHuffman());
	}

	public Transcoder3(URLEncoder urlEncoder, Compression compression) {
		this(urlEncoder, new ArobaseStringCompressor(), compression);
	}

	public Transcoder3(URLEncoder urlEncoder, StringCompressor stringCompressor, Compression compression) {
		this.compression = compression;
		this.urlEncoder = urlEncoder;
		this.stringCompressor = stringCompressor;
	}

	public String encode(String text) throws IOException {

		final String stringAnnoted = stringCompressor.compress(text);

		final StringBuilder d3 = new StringBuilder();

		final WordListProducer wordListProducer = new WordListProducer();
		final List<Integer> words = new ArrayList<Integer>();
		final List<Integer> positions = new ArrayList<Integer>();

		for (int i = 0; i < stringAnnoted.length(); i++) {

			final int idxWord = wordListProducer.getWordStarting(text.substring(i));
			if (idxWord == -1) {
				final char c = text.charAt(i);
				wordListProducer.addChar(c);
				d3.append(c);
			} else {
				d3.append(i);
				//d3.append('\u0001')
				positions.add(i);
				words.add(idxWord);
				i += wordListProducer.getWords().get(idxWord).length() - 1;
			}
		}
		System.err.println("from "+stringAnnoted.length()+" to "+d3.length());
		System.err.println("positions="+positions);
		//System.err.println("w=" + wordListProducer.getWords());
		System.err.println("words=" + words);

		final byte b0[] = compression.compress(d3.toString().getBytes("UTF-8"));
		//final byte b1[] = compression.compress(list.get(1));

		final byte brut[] = new Multiplexer().mix(Arrays.asList(b0));

		return urlEncoder.encode(b0);
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
