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
 * Revision $Revision: 4351 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.asciiart.CharArea;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.VerticalPosition;
import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.png.PngTitler;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.LifeEvent;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Newpage;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.eps.UGraphicEps;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;
import net.sourceforge.plantuml.ugraphic.svg.UGraphicSvg;

public class SequenceDiagramFileMaker implements FileMaker {

	private static final StringBounder dummyStringBounder;

	static {
		final BufferedImage imDummy = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		dummyStringBounder = StringBounderUtils.asStringBounder(imDummy.createGraphics());
	}

	private final SequenceDiagram diagram;
	private final DrawableSet drawableSet;
	private final Dimension2D fullDimension;
	private final List<Page> pages;
	private final FileFormat fileFormat;

	public SequenceDiagramFileMaker(SequenceDiagram sequenceDiagram, Skin skin, FileFormat fileFormat) {
		this.diagram = sequenceDiagram;
		this.fileFormat = fileFormat;
		final DrawableSetInitializer initializer = new DrawableSetInitializer(skin, sequenceDiagram.getSkinParam(),
				sequenceDiagram.isShowFootbox());

		for (Participant p : sequenceDiagram.participants().values()) {
			initializer.addParticipant(p);
		}

		final List<Newpage> newpages = new ArrayList<Newpage>();
		for (Event ev : sequenceDiagram.events()) {
			initializer.addEvent(ev);
			if (ev instanceof Message) {
				// TODO mieux faire
				final Message m = (Message) ev;
				for (LifeEvent lifeEvent : m.getLiveEvents()) {
					if (lifeEvent.getType() == LifeEventType.DESTROY
					/*
					 * || lifeEvent.getType() == LifeEventType.CREATE
					 */) {
						initializer.addEvent(lifeEvent);
					}
				}

			}
			if (ev instanceof Newpage) {
				newpages.add((Newpage) ev);
			}
		}
		drawableSet = initializer.createDrawableSet(dummyStringBounder);
		fullDimension = drawableSet.getDimension();
		final Map<Newpage, Double> positions = new LinkedHashMap<Newpage, Double>();
		for (Newpage n : newpages) {
			positions.put(n, initializer.getYposition(dummyStringBounder, n));
		}
		pages = create(drawableSet, positions, sequenceDiagram.isShowFootbox(), sequenceDiagram.getTitle()).getPages();

	}

	private PageSplitter create(DrawableSet drawableSet, Map<Newpage, Double> positions, boolean showFootbox,
			List<String> title) {

		final double headerHeight = drawableSet.getHeadHeight(dummyStringBounder);
		final double tailHeight = drawableSet.getTailHeight(dummyStringBounder, showFootbox);
		final double signatureHeight = 0;
		final double newpageHeight = drawableSet.getSkin().createComponent(ComponentType.NEWPAGE,
				drawableSet.getSkinParam(), Arrays.asList("")).getPreferredHeight(dummyStringBounder);

		return new PageSplitter(fullDimension.getHeight(), headerHeight, positions, tailHeight, signatureHeight,
				newpageHeight, title);
	}

	public List<File> createMany(final File suggestedFile) throws IOException {
		final List<File> result = new ArrayList<File>();
		if (fileFormat == FileFormat.TXT) {
			final CharArea im = createCharArea((int) fullDimension.getWidth(), pages.get(0), 0);
			im.print(System.err);
			// } else if (OptionFlags.getInstance().getFileFormat() ==
			// FileFormat.SVG) {
			// for (int i = 0; i < pages.size(); i++) {
			// final GraphicsXml im = createImageXml((int)
			// fullDimension.getWidth(), pages.get(i), i);
			// final File f = computeFilename(suggestedFile, i);
			// OutputStream os = null;
			// try {
			// os = new FileOutputStream(f);
			// im.createXml(os);
			// } finally {
			// if (os != null) {
			// os.close();
			// }
			// }
			// Log.info("Creating file: " + f);
			// result.add(f);
			// }
		} else {
			for (int i = 0; i < pages.size(); i++) {
				final UGraphic createImage = createImage((int) fullDimension.getWidth(), pages.get(i), i);
				final File f = computeFilename(suggestedFile, i, fileFormat);
				Log.info("Creating file: " + f);
				if (createImage instanceof UGraphicG2d) {
					final BufferedImage im = ((UGraphicG2d) createImage).getBufferedImage();
					Log.info("Image size " + im.getWidth() + " x " + im.getHeight());
					PngIO.write(im, f, diagram.getMetadata());
				} else if (createImage instanceof UGraphicSvg) {
					final UGraphicSvg svg = (UGraphicSvg) createImage;
					final FileOutputStream fos = new FileOutputStream(f);
					try {
						svg.createXml(fos);
					} finally {
						fos.close();
					}
				} else if (createImage instanceof UGraphicEps) {
					final UGraphicEps eps = (UGraphicEps) createImage;
					final FileWriter fw = new FileWriter(f);
					try {
						fw.write(eps.getEPSCode());
					} finally {
						fw.close();
					}
				}
				Log.info("File size : " + f.length());
				result.add(f);
			}
		}
		return result;
	}

	public void createOne(OutputStream os, int index) throws IOException {
		final UGraphic createImage = createImage((int) fullDimension.getWidth(), pages.get(index), index);
		if (createImage instanceof UGraphicG2d) {
			final BufferedImage im = ((UGraphicG2d) createImage).getBufferedImage();
			PngIO.write(im, os, diagram.getMetadata());
		} else if (createImage instanceof UGraphicSvg) {
			final UGraphicSvg svg = (UGraphicSvg) createImage;
			svg.createXml(os);
		} else if (createImage instanceof UGraphicEps) {
			final UGraphicEps eps = (UGraphicEps) createImage;
			os.write(eps.getEPSCode().getBytes());
		}
	}

	private CharArea createCharArea(final int diagramWidth, Page page, int i) {
		final CharArea charArea = new CharArea();
		drawableSet.drawTxt(charArea, diagramWidth, page, diagram.isShowFootbox());
		return charArea;
	}

	private double getImageWidth(SequenceDiagramArea area, boolean rotate) {
		final int minsize = diagram.getMinwidth();
		final double w = getImageWidthWithoutMinsize(area, rotate);
		if (minsize == Integer.MAX_VALUE) {
			return w;
		}
		if (w >= minsize) {
			return w;
		}
		return minsize;
	}

	private double getImageWidthWithoutMinsize(SequenceDiagramArea area, boolean rotate) {
		final double w;
		if (rotate) {
			w = area.getHeight();
		} else {
			w = area.getWidth();
		}
		return w;
	}

	private double getImageHeight(SequenceDiagramArea area, final Page page, boolean rotate) {
		if (rotate) {
			return area.getWidth();
		}
		return area.getHeight();
	}

	private UGraphic createImage(final int diagramWidth, final Page page, final int indice) {
		double delta = 0;
		if (indice > 0) {
			delta = page.getNewpage1() - page.getHeaderHeight();
		}
		if (delta < 0) {
			throw new IllegalArgumentException();
		}

		final SequenceDiagramArea area = new SequenceDiagramArea(diagramWidth, page.getHeight());

		Component compTitle = null;

		if (page.getTitle() != null) {
			compTitle = drawableSet.getSkin().createComponent(ComponentType.TITLE, drawableSet.getSkinParam(),
					page.getTitle());
			area.setTitleArea(compTitle.getPreferredWidth(dummyStringBounder), compTitle
					.getPreferredHeight(dummyStringBounder));
		}
		addFooter2(area);
		addHeader2(area);

		final Color backColor = diagram.getSkinParam().getBackgroundColor().getColor();

		// System.err.println("toto=" + fileFormat);

		final UGraphic ug;
		final double imageWidth = getImageWidth(area, diagram.isRotation());
		if (fileFormat == FileFormat.PNG) {
			double imageHeight = getImageHeight(area, page, diagram.isRotation());
			if (imageHeight == 0) {
				imageHeight = 1;
			}
			final EmptyImageBuilder builder = new EmptyImageBuilder((int) imageWidth, (int) imageHeight, backColor);

			final Graphics2D graphics2D = builder.getGraphics2D();
			if (diagram.isRotation()) {
				final AffineTransform at = new AffineTransform(0, 1, 1, 0, 0, 0);
				at.concatenate(new AffineTransform(-1, 0, 0, 1, imageHeight, 0));
				at.concatenate(AffineTransform.getTranslateInstance(0.01, 0));
				graphics2D.setTransform(at);
			}
			ug = new UGraphicG2d(graphics2D, builder.getBufferedImage());
		} else if (fileFormat == FileFormat.SVG) {
			if (backColor.equals(Color.WHITE)) {
				ug = new UGraphicSvg();
			} else {
				ug = new UGraphicSvg(HtmlColor.getAsHtml(backColor));
			}
		} else if (fileFormat == FileFormat.EPS) {
			ug = new UGraphicEps();
		} else {
			throw new UnsupportedOperationException();
		}

		final int diff = (int) Math.round((imageWidth - getImageWidthWithoutMinsize(area, diagram.isRotation())) / 2);
		if (diagram.isRotation()) {
			ug.translate(0, diff);
		} else {
			ug.translate(diff, 0);
		}

		if (compTitle != null) {
			ug.translate(area.getTitleX(), area.getTitleY());
			final StringBounder stringBounder = ug.getStringBounder();
			final double h = compTitle.getPreferredHeight(stringBounder);
			final double w = compTitle.getPreferredWidth(stringBounder);
			compTitle.drawU(ug, new Dimension2DDouble(w, h), new SimpleContext2D(false));
			ug.translate(-area.getTitleX(), -area.getTitleY());
		}

		addHeader3(area, ug);
		addFooter3(area, ug);

		ug.translate(area.getSequenceAreaX(), area.getSequenceAreaY());
		drawableSet.drawU(ug, delta, diagramWidth, page, diagram.isShowFootbox());

		return ug;
	}

	static public File computeFilename(File pngFile, int i, FileFormat fileFormat) {
		if (i == 0) {
			return pngFile;
		}
		final File dir = pngFile.getParentFile();
		String name = pngFile.getName();
		name = name.replaceAll("\\" + fileFormat.getFileSuffix() + "$", "_" + String.format("%03d", i)
				+ fileFormat.getFileSuffix());
		return new File(dir, name);

	}

	private void addFooter2(SequenceDiagramArea area) {
		final Color titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.FOOTER).getColor();
		final String fontFamily = diagram.getSkinParam().getFontFamily(FontParam.FOOTER);
		final int fontSize = diagram.getSkinParam().getFontSize(FontParam.FOOTER);
		final PngTitler pngTitler = new PngTitler(titleColor, diagram.getFooter(), fontSize, fontFamily, diagram
				.getFooterAlignement(), VerticalPosition.BOTTOM);
		final Dimension2D dim = pngTitler.getTextDimension(dummyStringBounder);
		if (dim != null) {
			area.setFooterArea(dim.getWidth(), dim.getHeight(), 3);
		}
	}

	private void addHeader2(SequenceDiagramArea area) {
		final Color titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.HEADER).getColor();
		final String fontFamily = diagram.getSkinParam().getFontFamily(FontParam.HEADER);
		final int fontSize = diagram.getSkinParam().getFontSize(FontParam.HEADER);
		final PngTitler pngTitler = new PngTitler(titleColor, diagram.getHeader(), fontSize, fontFamily, diagram
				.getHeaderAlignement(), VerticalPosition.TOP);
		final Dimension2D dim = pngTitler.getTextDimension(dummyStringBounder);
		if (dim != null) {
			area.setHeaderArea(dim.getWidth(), dim.getHeight(), 3);
		}
	}

	private void addFooter3(SequenceDiagramArea area, UGraphic ug) {
		final Color titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.FOOTER).getColor();
		final String fontFamily = diagram.getSkinParam().getFontFamily(FontParam.FOOTER);
		final int fontSize = diagram.getSkinParam().getFontSize(FontParam.FOOTER);
		final PngTitler pngTitler = new PngTitler(titleColor, diagram.getFooter(), fontSize, fontFamily, diagram
				.getFooterAlignement(), VerticalPosition.BOTTOM);
		final TextBlock text = pngTitler.getTextBlock();
		if (text == null) {
			return;
		}
		text.drawU(ug, area.getFooterX(diagram.getFooterAlignement()), area.getFooterY());
	}

	private void addHeader3(SequenceDiagramArea area, UGraphic ug) {
		final Color titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.HEADER).getColor();
		final String fontFamily = diagram.getSkinParam().getFontFamily(FontParam.HEADER);
		final int fontSize = diagram.getSkinParam().getFontSize(FontParam.HEADER);
		final PngTitler pngTitler = new PngTitler(titleColor, diagram.getHeader(), fontSize, fontFamily, diagram
				.getHeaderAlignement(), VerticalPosition.TOP);
		final TextBlock text = pngTitler.getTextBlock();
		if (text == null) {
			return;
		}
		text.drawU(ug, area.getHeaderX(diagram.getHeaderAlignement()), area.getHeaderY());
	}

}