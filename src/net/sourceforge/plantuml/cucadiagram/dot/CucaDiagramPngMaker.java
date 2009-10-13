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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.Option;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.CircledCharacter;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.png.PngRotation;
import net.sourceforge.plantuml.png.PngSizer;
import net.sourceforge.plantuml.png.PngSplitter;
import net.sourceforge.plantuml.png.PngTitler;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.rose.Rose;

public final class CucaDiagramPngMaker {

	private final CucaDiagram diagram;
	private final StaticFiles staticFiles = StaticFiles.getInstance();

	final private Color red = new Color(Integer.parseInt("A80036", 16));
	final private Font font = new Font("Courier", Font.BOLD, 17);

	static private final Graphics2D dummyGraphics2D;

	static {
		final EmptyImageBuilder builder = new EmptyImageBuilder(10, 10, Color.WHITE);
		dummyGraphics2D = builder.getGraphics2D();
	}

	public CucaDiagramPngMaker(CucaDiagram diagram) {
		this.diagram = diagram;
	}

	public List<File> createPng(File pngFile, String... dotStrings) throws IOException, InterruptedException {

		OutputStream os = null;
		try {
			os = new FileOutputStream(pngFile);
			createPng(os, dotStrings);
		} finally {
			if (os != null) {
				os.close();
			}
		}

		return new PngSplitter(pngFile, diagram.getHorizontalPages(), diagram.getVerticalPages(), diagram.getSource())
				.getFiles();
	}

	public void createPng(OutputStream os, String... dotStrings) throws IOException, InterruptedException {
		final Map<Entity, File> imageFiles = new HashMap<Entity, File>();
		try {
			populateImages(imageFiles);
			final GraphvizMaker dotMaker = createDotMaker(staticFiles.getStaticImages(), imageFiles, dotStrings);
			final String dotString = dotMaker.createDotString();
			final boolean isUnderline = dotMaker.isUnderline();
			final Graphviz graphviz = GraphvizUtils.create(dotString);

			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			graphviz.createPng(baos);
			baos.close();

			final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			BufferedImage im = ImageIO.read(bais);
			bais.close();
			if (isUnderline) {
				new UnderlineTrick(im, new Color(Integer.parseInt("FEFECF", 16)), Color.BLACK).process();
			}

			im = PngTitler.process(im, diagram.getTitle());
			if (diagram.isRotation()) {
				im = PngRotation.process(im);
			}
			im = PngSizer.process(im, diagram.getMinwidth());

			PngIO.write(im, os, diagram.getSource());
		} finally {
			cleanTemporaryFiles(imageFiles);
		}
	}

	private void cleanTemporaryFiles(final Map<Entity, File> imageFiles) {
		if (Option.getInstance().isKeepFiles() == false) {
			for (File f : imageFiles.values()) {
				staticFiles.delete(f);
			}
		}
	}

	private GraphvizMaker createDotMaker(Map<EntityType, File> staticImages, Map<Entity, File> images,
			String... dotStrings) {
		// return new NeatoMaker(diagram, dotStrings);
		return new DotMaker(staticImages, images, diagram, dotStrings);
	}

	private void populateImages(Map<Entity, File> result) throws IOException {
		for (Entity entity : diagram.entities().values()) {
			final File f = createImage(entity);
			if (f != null) {
				result.put(entity, f);
			}
		}
	}

	File createImage(Entity entity) throws IOException {
		if (entity.getType() == EntityType.NOTE) {
			return createImageForNote(entity);
		}
		if (entity.getType() == EntityType.ACTIVITY) {
			return createImageForActivity(entity);
		}
		if (entity.getType() == EntityType.ABSTRACT_CLASS || entity.getType() == EntityType.CLASS
				|| entity.getType() == EntityType.ENUM || entity.getType() == EntityType.INTERFACE) {
			return createImageForCircleCharacter(entity);
		}
		return null;
	}

	private File createImageForNote(Entity entity) throws IOException {
		final File f = createTempFile("plantumlB");

		final Rose skin = new Rose();

		final Component comp = skin.createComponent(ComponentType.NOTE, diagram.getSkinParam(), StringUtils
				.getWithNewlines(entity.getDisplay()));
		final int width = (int) comp.getPreferredWidth(dummyGraphics2D);
		final int height = (int) comp.getPreferredHeight(dummyGraphics2D);

		final EmptyImageBuilder builder = new EmptyImageBuilder(width, height, Color.WHITE);
		final BufferedImage im = builder.getBufferedImage();
		final Graphics2D g2d = builder.getGraphics2D();

		comp.draw(g2d, new Dimension(width, height), new SimpleContext2D(false));
		PngIO.write(im, f);

		g2d.dispose();
		return f;
	}

	private File createImageForActivity(Entity entity) throws IOException {
		return null;
	}

	private File createImageForCircleCharacter(Entity entity) throws IOException {
		final Stereotype stereotype = entity.getStereotype();

		if (stereotype == null || stereotype.getColor() == null) {
			return null;
		}

		final File f = createTempFile("plantumlA");
		final CircledCharacter circledCharacter = new CircledCharacter(stereotype.getCharacter(), font, stereotype
				.getColor(), red, Color.BLACK);
		staticFiles.generateCircleCharacterFile(f, circledCharacter);
		return f;

	}

	private File createTempFile(String prefix) throws IOException {
		final File f = File.createTempFile(prefix, ".png");
		Log.info("Creating temporary file: " + f);
		if (Option.getInstance().isKeepFiles() == false) {
			f.deleteOnExit();
		}
		return f;
	}

}
