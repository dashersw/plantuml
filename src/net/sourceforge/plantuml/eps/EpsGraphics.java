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
 * Revision $Revision: 4207 $
 *
 */
package net.sourceforge.plantuml.eps;

import java.awt.Color;
import java.util.Date;

public class EpsGraphics {

	// http://www.linuxfocus.org/Francais/May1998/article43.html
	// http://www.tailrecursive.org/postscript/text.html
	private final StringBuilder body = new StringBuilder();
	private final StringBuilder header = new StringBuilder();

	private Color color = Color.BLACK;
	private Color fillcolor = Color.BLACK;

	private String strokeWidth = "1";
	private String strokeDasharray = null;

	public EpsGraphics() {
		header.append("%!PS-Adobe-3.0 EPSF-3.0\n");
		header.append("%%Creator: PlantUML\n");
		header.append("%%Title: noTitle\n");
		header.append("%%CreationDate: " + new Date() + "\n");

		body.append("gsave\n");
	}

	private boolean closeDone = false;

	private int maxX = 10;
	private int maxY = 10;

	private void ensureVisible(double x, double y) {
		if (x > maxX) {
			maxX = (int) (x + 1);
		}
		if (y > maxY) {
			maxY = (int) (y + 1);
		}
	}

	public void close() {
		checkCloseDone();

		header.append("%%BoundingBox: 0 0 " + maxX + " " + maxY + "\n");
		//header.append("%%DocumentData: Clean7Bit\n");
		//header.append("%%DocumentProcessColors: Black\n");
		header.append("%%ColorUsage: Color\n");
		header.append("%%Origin: 0 0\n");
		header.append("%%EndComments\n\n");

		body.append("grestore\n");

		// if(isClipSet())
		// writer.write("grestore\n");

		body.append("showpage\n");
		body.append("\n");
		body.append("%%EOF");
		closeDone = true;
	}

	private void checkCloseDone() {
		if (closeDone) {
			throw new IllegalStateException();
		}
	}

	public String getEPSCode() {
		if (closeDone == false) {
			close();
		}
		return header.toString() + body.toString();
	}

	public final void setStrokeColor(Color c) {
		checkCloseDone();
		this.color = c;
	}

	public void setFillColor(Color c) {
		checkCloseDone();
		this.fillcolor = c;
	}

	public final void setStrokeWidth(String strokeWidth, String strokeDasharray) {
		checkCloseDone();
		this.strokeWidth = strokeWidth;
		this.strokeDasharray = strokeDasharray;
	}

	public void epsLine(double x1, double y1, double x2, double y2) {
		checkCloseDone();
		append(strokeWidth + " setlinewidth");
		appendColor(color);
		append("newpath");
		append("" + x1 + " " + y1 + " " + " moveto");
		append("" + x2 + " " + y2 + " " + " lineto");
		append("closepath stroke");
		ensureVisible(Math.max(x1, x2), Math.max(y1, y2));
	}

	public void epsPolygon(double... points) {
		checkCloseDone();
		if (fillcolor != null) {
			appendColor(fillcolor);
			append("newpath");
			for (int i = 0; i < points.length; i += 2) {
				ensureVisible(points[i], points[i + 1]);
				if (i == 0) {
					append("" + points[i] + " " + points[i + 1] + " " + " moveto");
				} else {
					append("" + points[i] + " " + points[i + 1] + " " + " lineto");
				}
			}
			append("" + points[0] + " " + points[1] + " " + " lineto");
			append("closepath eofill");
		}

		if (color != null) {
			append(strokeWidth + " setlinewidth");
			appendColor(color);
			append("newpath");
			for (int i = 0; i < points.length; i += 2) {
				ensureVisible(points[i], points[i + 1]);
				if (i == 0) {
					append("" + points[i] + " " + points[i + 1] + " " + " moveto");
				} else {
					append("" + points[i] + " " + points[i + 1] + " " + " lineto");
				}
			}
			append("" + points[0] + " " + points[1] + " " + " lineto");
			append("closepath stroke");
		}

	}

	public void epsRectangle(double x, double y, double width, double height, double rx, double ry) {
		checkCloseDone();
		ensureVisible(x + width, y + height);
		if (fillcolor != null) {
			appendColor(fillcolor);
			append("newpath");
			append("" + x + " " + y + " " + " moveto");
			append("" + (x + width) + " " + y + " " + " lineto");
			append("" + (x + width) + " " + (y + height) + " " + " lineto");
			append("" + x + " " + (y + height) + " " + " lineto");
			append("" + x + " " + y + " " + " lineto");
			append("closepath eofill");
		}

		if (color != null) {
			append(strokeWidth + " setlinewidth");
			appendColor(color);
			append("newpath");
			append("" + x + " " + y + " " + " moveto");
			append("" + (x + width) + " " + y + " " + " lineto");
			append("" + (x + width) + " " + (y + height) + " " + " lineto");
			append("" + x + " " + (y + height) + " " + " lineto");
			append("" + x + " " + y + " " + " lineto");
			append("closepath stroke");
		}
	}

	public void epsEllipse(double x, double y, double xRadius, double yRadius) {
		checkCloseDone();
		ensureVisible(x + xRadius, y + yRadius);
		if (xRadius != yRadius) {
			throw new UnsupportedOperationException();
		}
		if (fillcolor != null) {
			appendColor(fillcolor);
			append("newpath");
			append("" + x + " " + y + " " + xRadius + " 0 360 arc");
			append("closepath eofill");
		}

		if (color != null) {
			append(strokeWidth + " setlinewidth");
			appendColor(color);
			append("newpath");
			append("" + x + " " + y + " " + xRadius + " 0 360 arc");
			append("closepath stroke");
		}
	}

	private void appendColor(Color c) {
		final double r = c.getRed() / 255.0;
		final double g = c.getGreen() / 255.0;
		final double b = c.getBlue() / 255.0;
		append("" + r + " " + g + " " + b + " setrgbcolor");
	}

	private void append(String s) {
		body.append(s + "\n");
	}

}
