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
package net.sourceforge.plantuml.applet;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import net.sourceforge.plantuml.SystemFactoryTry;
import net.sourceforge.plantuml.PSystem;
import net.sourceforge.plantuml.PSystemError;
import net.sourceforge.plantuml.StartUml;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagramFactory;
import net.sourceforge.plantuml.sequencediagram.graphic.SequenceDiagramPngMaker2;

public class UmlApplet extends Applet {

	private SequenceDiagramPngMaker2 pngMaker2;
	private GraphicStrings pngError;
	private BufferedImage image;

	@Override
	public void init() {
		super.init();

		final String source = getParameter("source");
		if (source != null) {
			try {
				computeSource(source);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void callJavascript(String source) {
		try {
			computeSource(source);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		repaint();
	}

	protected final List<String> getStrings(Reader reader) throws IOException {
		final List<String> result = new ArrayList<String>();
		BufferedReader includer = null;
		try {
			includer = new BufferedReader(reader);
			String s = null;
			while ((s = includer.readLine()) != null) {
				result.add(s);
			}
		} finally {
			if (includer != null) {
				includer.close();
			}
		}
		return result;
	}

	private void computeSource(String source) throws IOException, InterruptedException {
		source = source.replaceAll("@startuml.*", "");
		source = source.replaceAll("@enduml", "");
		source = "@startuml\n" + source + "\n@enduml";

		final SystemFactoryTry dataReader = new SystemFactoryTry(getStrings(new StringReader(source)), new SequenceDiagramFactory());
		final SortedMap<Integer, StartUml> r = dataReader.getAllStartUml();
		if (r.size() > 0) {
			final PSystem system = r.values().iterator().next().getSystem();
			pngMaker2 = null;
			pngError = null;
			image = null;
			if (system instanceof SequenceDiagram) {
				final SequenceDiagram seq = (SequenceDiagram) system;
				pngMaker2 = new SequenceDiagramPngMaker2(seq);
			} else if (system instanceof PSystemError) {
				pngError = ((PSystemError) system).getPngError();
			}
		}
	}

	@Override
	public void start() {
		super.start();
	}

	@Override
	public void paint(Graphics g) {

		if (image != null) {
			g.drawImage(image, 0, 0, null);
		} else if (pngError != null) {
			pngError.draw((Graphics2D) g);
		} else if (pngMaker2 != null) {
			try {
				pngMaker2.draw((Graphics2D) g);
				final Dimension2D dim = pngMaker2.getFullDimension();
				image = new BufferedImage((int) dim.getWidth(), (int) dim.getHeight(), BufferedImage.TYPE_INT_RGB);
				final Graphics2D g2d = image.createGraphics();
				g2d.setColor(Color.WHITE);
				g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
				pngMaker2.draw(g2d);
				g2d.dispose();
				g.drawImage(image, 0, 0, null);
			} catch (IOException e) {
				g.drawString("error " + e, 5, 15);
			}
		}

	}
}
