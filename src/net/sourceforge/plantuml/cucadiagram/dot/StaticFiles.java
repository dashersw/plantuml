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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.graphic.CircledCharacter;
import net.sourceforge.plantuml.skin.StickMan;

public class StaticFiles {

	private static final StaticFiles singleton = new StaticFiles();

	private final String actorName = "actor.png";
	private final String cName = "stereotypec.png";
	private final String iName = "stereotypei.png";
	private final String aName = "stereotypea.png";
	private final String eName = "stereotypee.png";

	final private Color yellow = new Color(Integer.parseInt("FEFECE", 16));
	final private Color green = new Color(Integer.parseInt("ADD1B2", 16));
	final private Color violet = new Color(Integer.parseInt("B4A7E5", 16));
	final private Color blue = new Color(Integer.parseInt("A9DCDF", 16));
	final private Color rose = new Color(Integer.parseInt("EB937F", 16));

	final private Color red = new Color(Integer.parseInt("A80036", 16));
	final private Font font = new Font("Courier", Font.BOLD, 17);

	private final Map<EntityType, File> staticImages = new EnumMap<EntityType, File>(EntityType.class);

	public static StaticFiles getInstance() {
		return singleton;
	}

	private StaticFiles() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				clean();
			}
		});
	}

	private void clean() {
		for (File f : staticImages.values()) {
			delete(f);
		}
	}

	public Map<EntityType, File> getStaticImages() throws IOException {
		if (staticImages.isEmpty()) {
			final File dir = getTmpDir();
			staticImages.put(EntityType.ACTOR, ensurePngActorPresent(dir));
			staticImages.put(EntityType.ABSTRACT_CLASS, ensurePngAPresent(dir));
			staticImages.put(EntityType.CLASS, ensurePngCPresent(dir));
			staticImages.put(EntityType.INTERFACE, ensurePngIPresent(dir));
			staticImages.put(EntityType.ENUM, ensurePngEPresent(dir));
		}
		return Collections.unmodifiableMap(staticImages);
	}

	public File getTmpDir() {
		final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		if (tmpDir.exists() == false || tmpDir.isDirectory() == false) {
			throw new IllegalStateException();
		}
		return tmpDir;
	}

	public void delete(File f) {
		if (f == null) {
			return;
		}
		Thread.yield();
		Log.info("Deleting temporary file " + f);
		final boolean ok = f.delete();
		if (ok == false) {
			Log.error("Cannot delete: " + f);
		}
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

		final File result = new File(dir, actorName);
		Log.info("Creating temporary file: " + result);
		ImageIO.write(im, "png", result);
		return result;

	}

	private File ensurePngCPresent(File dir) throws IOException {
		final CircledCharacter circledCharacter = new CircledCharacter('C', font, green, red, Color.BLACK);
		return generateCircleCharacterFile(dir, cName, circledCharacter);
	}

	private File ensurePngAPresent(File dir) throws IOException {
		final CircledCharacter circledCharacter = new CircledCharacter('A', font, blue, red, Color.BLACK);
		return generateCircleCharacterFile(dir, aName, circledCharacter);
	}

	private File ensurePngIPresent(File dir) throws IOException {
		final CircledCharacter circledCharacter = new CircledCharacter('I', font, violet, red, Color.BLACK);
		return generateCircleCharacterFile(dir, iName, circledCharacter);
	}

	private File ensurePngEPresent(File dir) throws IOException {
		final CircledCharacter circledCharacter = new CircledCharacter('E', font, rose, red, Color.BLACK);
		return generateCircleCharacterFile(dir, eName, circledCharacter);
	}

	private File generateCircleCharacterFile(File dir, String filename, final CircledCharacter circledCharacter)
			throws IOException {
		final File result = new File(dir, filename);
		Log.info("Creating temporary file: " + result);
		generateCircleCharacterFile(result, circledCharacter);
		return result;
	}

	public void generateCircleCharacterFile(File file, final CircledCharacter circledCharacter) throws IOException {
		final EmptyImageBuilder builder = new EmptyImageBuilder(30, 30, yellow);

		BufferedImage im = builder.getBufferedImage();
		final Graphics2D g2d = builder.getGraphics2D();

		circledCharacter.draw(g2d);
		im = im.getSubimage(0, 0, (int) circledCharacter.getPreferredWidth(g2d) + 5, (int) circledCharacter
				.getPreferredHeight(g2d) + 1);

		ImageIO.write(im, "png", file);
	}

}
