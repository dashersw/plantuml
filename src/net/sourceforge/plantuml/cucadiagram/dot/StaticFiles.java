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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.graphic.CircledCharacter;
import net.sourceforge.plantuml.skin.CircleInterface;
import net.sourceforge.plantuml.skin.StickMan;
import net.sourceforge.plantuml.skin.rose.Rose;

public class StaticFiles {

	private final String circleInterfaceName = "cinterface.png";
	private final String actorName = "actor.png";
	private final String cName = "stereotypec.png";
	private final String iName = "stereotypei.png";
	private final String aName = "stereotypea.png";
	private final String eName = "stereotypee.png";

	private final Color stereotypeCBackground;
	private final Color stereotypeIBackground;
	private final Color stereotypeABackground;
	private final Color stereotypeEBackground;

	private final Color interfaceBorder;
	private final Color classborder;
	private final Color actorBorder;
	private final Color classBackground;
	private final Color actorBackground;
	private final Color interfaceBackground;
	private final Color background;

	final private Font font = new Font("Courier", Font.BOLD, 17);

	private final Map<EntityType, File> staticImages = new EnumMap<EntityType, File>(EntityType.class);

	private static final Collection<File> toDelete = new ArrayList<File>();

	private void deleteOnExit() {
		if (toDelete.isEmpty()) {
			toDelete.addAll(staticImages.values());
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					for (File f : toDelete) {
						delete(f);
					}
				}
			});
		}
	}

	public StaticFiles(SkinParam param) throws IOException {
		final Rose rose = new Rose();
		actorBorder = rose.getHtmlColor(param, ColorParam.actorBorder).getColor();
		classborder = rose.getHtmlColor(param, ColorParam.classBorder).getColor();
		interfaceBorder = rose.getHtmlColor(param, ColorParam.interfaceBorder).getColor();
		interfaceBackground = rose.getHtmlColor(param, ColorParam.interfaceBackground).getColor();
		actorBackground = rose.getHtmlColor(param, ColorParam.actorBackground).getColor();
		classBackground = rose.getHtmlColor(param, ColorParam.classBackground).getColor();
		stereotypeCBackground = rose.getHtmlColor(param, ColorParam.stereotypeCBackground).getColor();
		stereotypeABackground = rose.getHtmlColor(param, ColorParam.stereotypeABackground).getColor();
		stereotypeIBackground = rose.getHtmlColor(param, ColorParam.stereotypeIBackground).getColor();
		stereotypeEBackground = rose.getHtmlColor(param, ColorParam.stereotypeEBackground).getColor();

		background = param.getBackgroundColor().getColor();

		final File dir = getTmpDir();
		staticImages.put(EntityType.CIRCLE_INTERFACE, ensurePngCircleInterfacePresent(dir));
		staticImages.put(EntityType.ACTOR, ensurePngActorPresent(dir));
		staticImages.put(EntityType.ABSTRACT_CLASS, ensurePngAPresent(dir));
		staticImages.put(EntityType.CLASS, ensurePngCPresent(dir));
		staticImages.put(EntityType.INTERFACE, ensurePngIPresent(dir));
		staticImages.put(EntityType.ENUM, ensurePngEPresent(dir));

		deleteOnExit();
	}

	public File getTmpDir() {
		final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		if (tmpDir.exists() == false || tmpDir.isDirectory() == false) {
			throw new IllegalStateException();
		}
		return tmpDir;
	}

	public static void delete(File f) {
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
		final StickMan smallMan = new StickMan(actorBackground, actorBorder);

		final EmptyImageBuilder builder = new EmptyImageBuilder((int) smallMan.getPreferredWidth(null), (int) smallMan
				.getPreferredHeight(null), background);

		final BufferedImage im = builder.getBufferedImage();
		final Graphics2D g2d = builder.getGraphics2D();

		smallMan.draw(g2d);

		final File result = new File(dir, actorName);
		Log.info("Creating temporary file: " + result);
		ImageIO.write(im, "png", result);
		return result;

	}

	private File ensurePngCircleInterfacePresent(File dir) throws IOException {

		final CircleInterface circleInterface = new CircleInterface(interfaceBackground, interfaceBorder);

		final EmptyImageBuilder builder = new EmptyImageBuilder((int) circleInterface.getPreferredWidth(null),
				(int) circleInterface.getPreferredHeight(null), background);

		final BufferedImage im = builder.getBufferedImage();
		final Graphics2D g2d = builder.getGraphics2D();

		circleInterface.draw(g2d);

		final File result = new File(dir, circleInterfaceName);
		Log.info("Creating temporary file: " + result);
		ImageIO.write(im, "png", result);
		return result;

	}

	private File ensurePngCPresent(File dir) throws IOException {
		final CircledCharacter circledCharacter = new CircledCharacter('C', font, stereotypeCBackground, classborder,
				Color.BLACK);
		return generateCircleCharacterFile(dir, cName, circledCharacter, classBackground);
	}

	private File ensurePngAPresent(File dir) throws IOException {
		final CircledCharacter circledCharacter = new CircledCharacter('A', font, stereotypeABackground, classborder,
				Color.BLACK);
		return generateCircleCharacterFile(dir, aName, circledCharacter, classBackground);
	}

	private File ensurePngIPresent(File dir) throws IOException {
		final CircledCharacter circledCharacter = new CircledCharacter('I', font, stereotypeIBackground, classborder,
				Color.BLACK);
		return generateCircleCharacterFile(dir, iName, circledCharacter, classBackground);
	}

	private File ensurePngEPresent(File dir) throws IOException {
		final CircledCharacter circledCharacter = new CircledCharacter('E', font, stereotypeEBackground, classborder,
				Color.BLACK);
		return generateCircleCharacterFile(dir, eName, circledCharacter, classBackground);
	}

	private File generateCircleCharacterFile(File dir, String filename, final CircledCharacter circledCharacter,
			final Color yellow) throws IOException {
		final File result = new File(dir, filename);
		Log.info("Creating temporary file: " + result);
		generateCircleCharacterFile(result, circledCharacter, yellow);
		return result;
	}

	public void generateCircleCharacterFile(File file, final CircledCharacter circledCharacter, Color yellow)
			throws IOException {
		final EmptyImageBuilder builder = new EmptyImageBuilder(30, 30, yellow);

		BufferedImage im = builder.getBufferedImage();
		final Graphics2D g2d = builder.getGraphics2D();

		circledCharacter.draw(g2d, 0, 0);
		im = im.getSubimage(0, 0, (int) circledCharacter.getPreferredWidth(g2d) + 5, (int) circledCharacter
				.getPreferredHeight(g2d) + 1);

		ImageIO.write(im, "png", file);
	}

	public final Map<EntityType, File> getStaticImages() {
		return Collections.unmodifiableMap(staticImages);
	}

}
