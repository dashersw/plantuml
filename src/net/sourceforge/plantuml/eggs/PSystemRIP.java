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
 * Revision $Revision: 4041 $
 *
 */
package net.sourceforge.plantuml.eggs;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.graphic.GraphicStrings;

public class PSystemRIP extends AbstractPSystem {

	private final List<String> strings = new ArrayList<String>();
	private final BufferedImage image;

	public PSystemRIP() throws IOException {
		strings.add(" To my Grandfather,");
		strings.add(" A mon grand-pere,");
		strings.add(" ");
		strings.add("          <b>Jean CANOUET");
		strings.add(" ");
		strings.add(" 31-OCT-1921 <i>(Neuilly-Sur-Seine, France)");
		strings.add(" 15-SEP-2009 <i>(Nanterre, France)");
		strings.add(" ");
		strings.add("         <b>Requiescat In Pace");
		strings.add(" ");

		final InputStream is = getClass().getClassLoader().getResourceAsStream("net/sourceforge/plantuml/eggs/jean1.jpg");
		image = ImageIO.read(is);
		is.close();
	}

	public List<File> createFiles(File suggestedFile, FileFormat fileFormat) throws IOException, InterruptedException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(suggestedFile);
			getGraphicStrings().writeImage(os, fileFormat);
		} finally {
			if (os != null) {
				os.close();
			}
		}
		return Arrays.asList(suggestedFile);
	}

	public void createFile(OutputStream os, int index, FileFormat fileFormat) throws IOException {
		getGraphicStrings().writeImage(os, fileFormat);
	}

	private GraphicStrings getGraphicStrings() throws IOException {
		final Font font = new Font("SansSerif", Font.PLAIN, 12);
		return new GraphicStrings(strings, font, Color.BLACK, Color.WHITE, image);
	}

	public String getDescription() {
		return "(RIP)";
	}

}
