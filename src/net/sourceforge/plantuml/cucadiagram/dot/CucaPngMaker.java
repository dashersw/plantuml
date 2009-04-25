/* ========================================================================
 * Plantuml : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques (for Atos Origin).
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of Plantuml.
 *
 * Plantuml is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Plantuml distributed in the hope that it will be useful, but
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
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.skin.StickMan;

public class CucaPngMaker {

	private final CucaDiagram diagram;
	private static final String ACTOR_FILENAME = "actor.png";

	public CucaPngMaker(CucaDiagram diagram) {
		this.diagram = diagram;
	}

	public List<File> createPng(File pngFile, String... dotStrings) throws IOException, InterruptedException {
		final DotMaker dotMaker = new DotMaker(diagram, dotStrings);
		File tmpFile = null;
		File actorFile = null;
		try {
			tmpFile = File.createTempFile("plantuml", ".dot");
			actorFile = ensurePngActorPresent(tmpFile.getParentFile());
			dotMaker.generateFile(tmpFile, actorFile);
			final Graphviz graphviz = new Graphviz(tmpFile);
			graphviz.createPng(pngFile);
		} finally {
			tmpFile.delete();
			actorFile.delete();
		}
		return Arrays.asList(pngFile);
	}

	private File ensurePngActorPresent(File dir) throws IOException {

		final Color yellow = new Color(Integer.parseInt("FEFECE", 16));
		final Color red = new Color(Integer.parseInt("A80036", 16));
		final StickMan smallMan = new StickMan(yellow, red);

		final BufferedImage im = new BufferedImage((int) smallMan.getPreferredWidth(null), (int) smallMan
				.getPreferredHeight(null), BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2d = im.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, 640, 320);

		smallMan.draw(g2d);

		final File result = new File(dir, ACTOR_FILENAME);
		ImageIO.write(im, "png", result);
		return result;

	}
}
