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
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
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
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.StickMan;
import net.sourceforge.plantuml.skin.rose.Rose;

public class CucaPngMaker {

	private final CucaDiagram diagram;
	private static final String ACTOR_FILENAME = "actor.png";
	private static final String C_FILENAME = "stereotypec.png";
	private static final String I_FILENAME = "stereotypei.png";
	private static final String A_FILENAME = "stereotypea.png";
	private static final String E_FILENAME = "stereotypee.png";

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
		final Map<EntityType, File> staticImages = new EnumMap<EntityType, File>(EntityType.class);
		final Map<Entity, File> imageFiles = createImages();
		try {
			tmpFile = File.createTempFile("plantuml", ".dot");
			imageFiles.putAll(createImages());
			staticImages.put(EntityType.ACTOR, ensurePngActorPresent(tmpFile.getParentFile()));
			staticImages.put(EntityType.ABSTRACT_CLASS, ensurePngAPresent(tmpFile.getParentFile()));
			staticImages.put(EntityType.CLASS, ensurePngCPresent(tmpFile.getParentFile()));
			staticImages.put(EntityType.INTERFACE, ensurePngIPresent(tmpFile.getParentFile()));
			staticImages.put(EntityType.ENUM, ensurePngEPresent(tmpFile.getParentFile()));
			dotMaker.generateFile(tmpFile, staticImages, imageFiles);
			final IGraphviz graphviz = GraphvizUtils.create(tmpFile);
			graphviz.createPng(pngFile);
		} finally {
			if (Option.getInstance().isKeepFiles() == false) {
				tmpFile.delete();
				for (File f : staticImages.values()) {
					f.delete();
				}
				for (File f : imageFiles.values()) {
					f.delete();
				}
			}
		}
		return new PngSplitter(pngFile, diagram.getHorizontalPages(), diagram.getVerticalPages()).getFiles();
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
		if (entity.getType() == EntityType.NOTE) {
			return createImageForNote(entity);
		}
		if (entity.getType() == EntityType.ABSTRACT_CLASS || entity.getType() == EntityType.CLASS
				|| entity.getType() == EntityType.ENUM || entity.getType() == EntityType.INTERFACE) {
			return createImageForCircleCharacter(entity);
		}
		return null;
	}

	private File createImageForNote(Entity entity) throws IOException {
		final File f = File.createTempFile("plantuml", ".png");
		if (Option.getInstance().isKeepFiles() == false) {
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

	private File createImageForCircleCharacter(Entity entity) throws IOException {
		final File f = File.createTempFile("plantuml", ".png");
		final Stereotype stereotype = entity.getStereotype();

		if (stereotype == null || stereotype.getColor() == null) {
			return null;
		}

		final CircledCharacter circledCharacter = new CircledCharacter(stereotype.getCharacter(), font, stereotype
				.getColor(), red, Color.BLACK);
		generateCircleCharacterFile(f, circledCharacter);
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

	private File ensurePngCPresent(File dir) throws IOException {
		final CircledCharacter circledCharacter = new CircledCharacter('C', font, green, red, Color.BLACK);
		return generateCircleCharacterFile(dir, C_FILENAME, circledCharacter);
	}

	private File ensurePngAPresent(File dir) throws IOException {
		final CircledCharacter circledCharacter = new CircledCharacter('A', font, blue, red, Color.BLACK);
		return generateCircleCharacterFile(dir, A_FILENAME, circledCharacter);
	}

	private File ensurePngIPresent(File dir) throws IOException {
		final CircledCharacter circledCharacter = new CircledCharacter('I', font, violet, red, Color.BLACK);
		return generateCircleCharacterFile(dir, I_FILENAME, circledCharacter);
	}

	private File ensurePngEPresent(File dir) throws IOException {
		final CircledCharacter circledCharacter = new CircledCharacter('E', font, rose, red, Color.BLACK);
		return generateCircleCharacterFile(dir, E_FILENAME, circledCharacter);
	}

	private File generateCircleCharacterFile(File dir, String filename, final CircledCharacter circledCharacter)
			throws IOException {
		final File result = new File(dir, filename);
		generateCircleCharacterFile(result, circledCharacter);
		return result;
	}

	private void generateCircleCharacterFile(File file, final CircledCharacter circledCharacter) throws IOException {
		final EmptyImageBuilder builder = new EmptyImageBuilder(30, 30, yellow);

		BufferedImage im = builder.getBufferedImage();
		final Graphics2D g2d = builder.getGraphics2D();

		circledCharacter.draw(g2d);
		im = im.getSubimage(0, 0, (int) circledCharacter.getPreferredWidth(g2d) + 5, (int) circledCharacter
				.getPreferredHeight(g2d) + 1);

		ImageIO.write(im, "png", file);
	}

	final private Color yellow = new Color(Integer.parseInt("FEFECE", 16));
	final private Color green = new Color(Integer.parseInt("ADD1B2", 16));
	final private Color violet = new Color(Integer.parseInt("B4A7E5", 16));
	final private Color blue = new Color(Integer.parseInt("A9DCDF", 16));
	final private Color rose = new Color(Integer.parseInt("EB937F", 16));

	final private Color red = new Color(Integer.parseInt("A80036", 16));
	final private Font font = new Font("Courier", Font.BOLD, 17);

	protected CucaDiagram getDiagram() {
		return diagram;
	}
}
