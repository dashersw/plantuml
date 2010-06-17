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
package net.sourceforge.plantuml.ugraphic.eps;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import net.sourceforge.plantuml.eps.EpsGraphics;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.skin.UDrawable;
import net.sourceforge.plantuml.ugraphic.AbstractUGraphic;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UText;

public class UGraphicEps extends AbstractUGraphic<EpsGraphics> implements ClipContainer {

	final static Graphics2D imDummy = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB).createGraphics();
	private UClip clip;

	private final StringBounder stringBounder;

	public UGraphicEps() {
		this(new EpsGraphics());
	}

	private UGraphicEps(EpsGraphics eps) {
		super(eps);
		stringBounder = StringBounderUtils.asStringBounder(imDummy);
		registerDriver(URectangle.class, new DriverRectangleEps(this));
		registerDriver(UText.class, new DriverTextEps(getStringBounder(), this));
		registerDriver(ULine.class, new DriverLineEps(this));
		registerDriver(UPolygon.class, new DriverPolygonEps());
		registerDriver(UEllipse.class, new DriverEllipseEps());
		// registerDriver(UImage.class, new DriverImageSvg());
		// registerDriver(UPath.class, new DriverPathSvg(this));
	}

	public void close() {
		getEpsGraphics().close();
	}

	public String getEPSCode() {
		return getEpsGraphics().getEPSCode();
	}

	public EpsGraphics getEpsGraphics() {
		return this.getGraphicObject();
	}

	public StringBounder getStringBounder() {
		return stringBounder;
	}

	public void setClip(UClip clip) {
		this.clip = clip;
	}

	public UClip getClip() {
		return clip;
	}

	public void centerChar(double x, double y, char c, Font font) {
		throw new UnsupportedOperationException();
	}

	static public String getEpsString(UDrawable udrawable) throws IOException {
		final UGraphicEps ug = new UGraphicEps();
		udrawable.drawU(ug);
		return ug.getEPSCode();
	}

}
