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
 */
package net.sourceforge.plantuml.version;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizUtils;
import net.sourceforge.plantuml.graphic.GraphicPosition;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.ugraphic.UFont;

public class PSystemVersion extends AbstractPSystem {

	private final List<String> strings = new ArrayList<String>();
	private final BufferedImage image;

	PSystemVersion(boolean withImage, List<String> args) throws IOException {
		strings.addAll(args);
		if (withImage) {
			final InputStream is = getClass().getResourceAsStream("logo.png");
			image = ImageIO.read(is);
			is.close();
		} else {
			image = null;
		}
	}

	public void exportDiagram(OutputStream os, StringBuilder cmap, int index, FileFormatOption fileFormat)
			throws IOException {
		getGraphicStrings().writeImage(os, fileFormat);
	}

	public static PSystemVersion createShowVersion() throws IOException {
		final List<String> strings = new ArrayList<String>();
		strings.add("<b>PlantUML version " + Version.version() + "</b> (" + new Date(Version.compileTime()) + ")");
		strings.add(" ");

		strings.addAll(GraphvizUtils.getTestDotStrings(true));
		strings.add(" ");
		final Properties p = System.getProperties();
		strings.add(p.getProperty("java.runtime.name"));
		strings.add(p.getProperty("java.vm.name"));
		strings.add(p.getProperty("java.runtime.version"));
		strings.add(p.getProperty("os.name"));
		strings.add("Processors: " + Runtime.getRuntime().availableProcessors());
		return new PSystemVersion(true, strings);
	}

	public static PSystemVersion createShowAuthors() throws IOException {
		// Duplicate in OptionPrint
		final List<String> strings = new ArrayList<String>();
		strings.add("<b>PlantUML version " + Version.version() + "</b> (" + new Date(Version.compileTime()) + ")");
		strings.add(" ");
		strings.add("<u>Original idea</u>: Arnaud Roques");
		strings.add("<u>Word Macro</u>: Alain Bertucat & Matthieu Sabatier");
		strings.add("<u>Eclipse Plugin</u>: Claude Durif & Anne Pecoil");
		strings.add("<u>Servlet & XWiki</u>: Maxime Sinclair");
		strings.add("<u>Site design</u>: Raphael Cotisson");
		strings.add("<u>Logo</u>: Benjamin Croizet");

		strings.add(" ");
		strings.add("http://plantuml.sourceforge.net");
		strings.add(" ");
		return new PSystemVersion(true, strings);
	}

	public static PSystemVersion createCheckVersions(String host, String port) throws IOException {
		final List<String> strings = new ArrayList<String>();
		strings.add("<b>PlantUML version " + Version.version() + "</b> (" + new Date(Version.compileTime()) + ")");

		final int lastversion = extractDownloadableVersion(host, port);

		int lim = 7;
		if (lastversion == -1) {
			strings.add("<b><color:red>Error</b>");
			strings.add("<color:red>Cannot connect to http://plantuml.sourceforge.net/</b>");
			strings.add("Maybe you should set your proxy ?");
			strings.add("@startuml");
			strings.add("checkversion(proxy=myproxy.com,port=8080)");
			strings.add("@enduml");
			lim = 9;
		} else if (lastversion == 0) {
			strings.add("<b><color:red>Error</b>");
			strings.add("Cannot retrieve last version from http://plantuml.sourceforge.net/</b>");
		} else {
			strings.add("<b>Last available version for download</b> : " + lastversion);
			strings.add(" ");
			if (Version.version() >= lastversion) {
				strings.add("<b><color:green>Your version is up to date.</b>");
			} else {
				strings.add("<b><color:red>A newer version is available for download.</b>");
			}
		}

		while (strings.size() < lim) {
			strings.add(" ");
		}

		return new PSystemVersion(true, strings);
	}

	public static int extractDownloadableVersion(String host, String port) {
		if (host != null && port != null) {
			System.setProperty("http.proxyHost", host);
			System.setProperty("http.proxyPort", port);
		}

		try {
			final URL url = new URL("http://plantuml.sourceforge.net/download.html");
			final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setUseCaches(false);
			urlConnection.connect();
			if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				final BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				final int lastversion = extractVersion(in);
				in.close();
				urlConnection.disconnect();
				return lastversion;
			}
		} catch (IOException e) {
			Log.error(e.toString());
		}
		return -1;
	}

	private static int extractVersion(BufferedReader in) throws IOException {
		String s;
		final Pattern p = Pattern.compile(".*\\.(\\d{4})\\..*");
		while ((s = in.readLine()) != null) {
			final Matcher m = p.matcher(s);
			if (m.matches()) {
				final String v = m.group(1);
				return Integer.parseInt(v);
			}
		}
		return 0;
	}

	public static PSystemVersion createTestDot() throws IOException {
		final List<String> strings = new ArrayList<String>();
		strings.addAll(GraphvizUtils.getTestDotStrings(true));
		return new PSystemVersion(false, strings);
	}

	private GraphicStrings getGraphicStrings() throws IOException {
		final UFont font = new UFont("SansSerif", Font.PLAIN, 12);
		return new GraphicStrings(strings, font, HtmlColor.BLACK, HtmlColor.WHITE, image,
				GraphicPosition.BACKGROUND_CORNER, false);
		// return new GraphicStrings(strings, font, Color.BLACK, Color.WHITE,
		// false);
	}

	public String getDescription() {
		return "(Version)";
	}

}
