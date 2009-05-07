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
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.Option;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.StickMan;
import net.sourceforge.plantuml.skin.rose.Rose;

public class CucaPngMaker {

	private final CucaDiagram diagram;
	private static final String ACTOR_FILENAME = "actor.png";

	static private final Graphics2D dummyGraphics2D;

	static {
		final EmptyImageBuilder builder = new EmptyImageBuilder(10, 10, Color.WHITE);
		dummyGraphics2D = builder.getGraphics2D();
	}

	public CucaPngMaker(CucaDiagram diagram) {
		this.diagram = diagram;
	}

	public List<File> createPng(File pngFile, String... dotStrings) throws IOException, InterruptedException {
		final DotMaker dotMaker = createDotMaker(dotStrings);
		File tmpFile = null;
		File actorFile = null;
		final Map<Entity, File> imageFiles = createImages();
		try {
			tmpFile = File.createTempFile("plantuml", ".dot");
			actorFile = ensurePngActorPresent(tmpFile.getParentFile());
			imageFiles.putAll(createImages());
			dotMaker.generateFile(tmpFile, actorFile, imageFiles);
			final Graphviz graphviz = new Graphviz(tmpFile);
			graphviz.createPng(pngFile);
		} finally {
			if (Option.getInstance().isKeepTmpFiles() == false) {
				tmpFile.delete();
				actorFile.delete();
				for (File f : imageFiles.values()) {
					f.delete();
				}
			}
		}
		return Arrays.asList(pngFile);
	}

	protected DotMaker createDotMaker(String... dotStrings) {
		return new DotMaker(diagram, dotStrings);
	}

	private Map<Entity, File> createImages() throws IOException {
		final Map<Entity, File> result = new HashMap<Entity, File>();
		for (Entity entity : diagram.entities().values()) {
			final File f = createImage(entity);
			if (f != null) {
				result.put(entity, f);
			}
		}
		return result;
	}

	File createImage(Entity entity) throws IOException {
		if (entity.getType() != EntityType.NOTE) {
			return null;
		}
		final File f = File.createTempFile("plantuml", ".png");
		if (Option.getInstance().isKeepTmpFiles() == false) {
			f.deleteOnExit();
		}

		final Rose skin = new Rose();

		final Component comp = skin.createComponent(ComponentType.NOTE, StringUtils
				.getWithNewlines(entity.getDisplay()));
		final int width = (int) comp.getPreferredWidth(dummyGraphics2D);
		final int height = (int) comp.getPreferredHeight(dummyGraphics2D);

		final EmptyImageBuilder builder = new EmptyImageBuilder(width, height, Color.WHITE);
		final BufferedImage im = builder.getBufferedImage();
		final Graphics2D g2d = builder.getGraphics2D();

		comp.draw(g2d, new Dimension(width, height), new SimpleContext2D(false));
		ImageIO.write(im, "png", f);

		g2d.dispose();
		return f;
	}

	private File ensurePngActorPresent(File dir) throws IOException {

		final Color yellow = new Color(Integer.parseInt("FEFECE", 16));
		final Color red = new Color(Integer.parseInt("A80036", 16));
		final StickMan smallMan = new StickMan(yellow, red);

		final EmptyImageBuilder builder = new EmptyImageBuilder((int) smallMan.getPreferredWidth(null), (int) smallMan
				.getPreferredHeight(null), Color.WHITE);

		final BufferedImage im = builder.getBufferedImage();
		final Graphics2D g2d = builder.getGraphics2D();

		smallMan.draw(g2d);

		final File result = new File(dir, ACTOR_FILENAME);
		ImageIO.write(im, "png", result);
		return result;

	}

	protected CucaDiagram getDiagram() {
		return diagram;
	}
}
