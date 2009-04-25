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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkType;

public class DotMaker {

	private final CucaDiagram diagram;

	private static final String RED = "\"#A80036\"";
	private static final String YELLOW = "\"#FEFECE\"";
	private static final String YELLOW_NOTE = "\"#FBFB77\"";

	private static boolean isJunit = false;

	public static void goJunit() {
		isJunit = true;
	}

	public DotMaker(CucaDiagram diagram, String... dotStrings) {
		this.diagram = diagram;
		this.dotStrings = dotStrings;
	}

	private final String[] dotStrings;

	public void generateFile(final File out, File actorFile) throws IOException {

		final PrintWriter pw = new PrintWriter(out);

		pw.println("digraph unix {");
		if (isJunit == false) {
			for (String s : dotStrings) {
				pw.println(s);
			}
		}

		this.actorFile = actorFile;
		printEntities(pw);
		printLinks(pw);

		pw.println("}");
		pw.close();
	}

	private File actorFile;

	private void printLinks(PrintWriter pw) {
		for (Link link : diagram.getLinks()) {
			String decoration = "[color=" + RED + ",";
			if (link.getLabel() != null) {
				decoration += "label=\"" + link.getLabel() + "\",";
			}
			if (link.getQualifier1() != null) {
				decoration += "taillabel=\"" + link.getQualifier1() + "\",";
			}
			if (link.getQualifier2() != null) {
				decoration += "headlabel=\"" + link.getQualifier2() + "\",";
			}
			decoration += getSpecificDecoration(link.getType());
			final int len = link.getLenght();
			final String lenString = len >= 3 ? ",minlen=" + (len - 1) : "";
			pw
					.println(link.getEntity1().getUid() + " -> " + link.getEntity2().getUid() + decoration + lenString
							+ "];");
			if (len == 1) {
				pw.println("{rank=same; " + link.getEntity1().getUid() + "; " + link.getEntity2().getUid() + "}");
			}

		}

	}

	private String getSpecificDecoration(LinkType link) {
		if (link == LinkType.COMPOSITION) {
			return "arrowtail=none,arrowhead=diamond";
		} else if (link == LinkType.AGREGATION) {
			return "arrowtail=none,arrowhead=ediamond";
		} else if (link == LinkType.NAVASSOC) {
			return "arrowtail=none,arrowhead=open";
		} else if (link == LinkType.EXTENDS) {
			return "arrowtail=none,arrowhead=empty,arrowsize=2";
		} else if (link == LinkType.NAVASSOC_DASHED) {
			return "arrowtail=none,arrowhead=open,style=dashed";
		} else if (link == LinkType.IMPLEMENTS) {
			return "arrowtail=none,arrowhead=empty,arrowsize=2,style=dashed";
		} else if (link == LinkType.ASSOCIED) {
			return "arrowtail=none,arrowhead=none";
		} else if (link == LinkType.ASSOCIED_DASHED) {
			return "arrowtail=none,arrowhead=none,style=dashed";
		} else if (link == LinkType.COMPOSITION_INV) {
			return "dir=back,arrowtail=diamond,arrowhead=none";
		} else if (link == LinkType.AGREGATION_INV) {
			return "dir=back,arrowtail=ediamond,arrowhead=none";
		} else if (link == LinkType.NAVASSOC_INV) {
			return "dir=back,arrowtail=open,arrowhead=none";
		} else if (link == LinkType.EXTENDS_INV) {
			return "dir=back,arrowtail=empty,arrowhead=none,arrowsize=2";
		} else if (link == LinkType.NAVASSOC_DASHED_INV) {
			return "dir=back,arrowtail=open,arrowhead=none,style=dashed";
		} else if (link == LinkType.IMPLEMENTS_INV) {
			return "dir=back,arrowtail=empty,arrowhead=none,arrowsize=2,style=dashed";
		}
		throw new IllegalArgumentException(link.toString());
	}

	private void printEntities(PrintWriter pw) {
		for (Entity entity : diagram.entities().values()) {
			final EntityType type = entity.getType();
			final String label = getLabel(entity);
			if (type == EntityType.CLASS) {
				pw.println(entity.getUid() + " [margin=0,fillcolor=" + YELLOW + ",color=" + RED
						+ ",style=filled,shape=box," + label + "];");
			} else if (type == EntityType.USECASE) {
				pw.println(entity.getUid() + " [fillcolor=" + YELLOW + ",color=" + RED + ",style=filled," + label
						+ "];");
			} else if (type == EntityType.ACTOR) {
				pw.println(entity.getUid() + " [shape=plaintext," + label + "];");
			} else if (type == EntityType.INTERFACE) {
				pw.println(entity.getUid() + " [fillcolor=" + YELLOW + ",color=" + RED + ",style=filled,shape=circle,"
						+ label + "];");
			} else if (type == EntityType.COMPONENT) {
				pw.println(entity.getUid() + " [fillcolor=" + YELLOW + ",color=" + RED
						+ ",style=filled,shape=component," + label + "];");
			} else if (type == EntityType.NOTE) {
				pw.println(entity.getUid() + " [fillcolor=" + YELLOW_NOTE + ",color=" + RED
						+ ",style=filled,shape=note," + label + "];");
			} else if (type == EntityType.ACTIVITY) {
				pw.println(entity.getUid() + " [fillcolor=" + YELLOW + ",color=" + RED
						+ ",style=\"rounded,filled\",shape=octagon," + label + "];");
			} else if (type == EntityType.BRANCH) {
				pw.println(entity.getUid() + " [fillcolor=" + YELLOW + ",color=" + RED
						+ ",style=\"filled\",shape=diamond,height=.25,width=.25,label=\"\"];");
			} else if (type == EntityType.SYNCHRO_BAR) {
				pw.println(entity.getUid() + " [fillcolor=black,color=black,style=\"filled\","
						+ "shape=rect,height=.08,width=1.30,label=\"\"];");
			} else if (type == EntityType.CIRCLE_START) {
				pw.println(entity.getUid() + " [fillcolor=black,color=black,style=\"filled\","
						+ "shape=circle,width=.20,label=\"\"];");
			} else if (type == EntityType.CIRCLE_END) {
				pw.println(entity.getUid() + " [fillcolor=black,color=black,style=\"filled\","
						+ "shape=doublecircle,width=.13,label=\"\"];");
			} else {
				throw new IllegalStateException();
			}

		}

	}

	private String getLabel(Entity entity) {
		if (entity.getType() == EntityType.CLASS) {
			return "label=" + getLabelForClass(entity);
		}
		if (entity.getType() == EntityType.ACTOR) {
			return "label=" + getLabelForActor(entity);
		}
		final String stereotype = entity.getStereotype();

		if (stereotype != null) {
			return "label=<" + manageStereotype(stereotype) + manageHtml(entity.getDisplay()) + ">";
		}
		return "label=\"" + entity.getDisplay() + "\"";
	}

	private String manageStereotype(String stereotype) {
		return "<BR ALIGN=\"LEFT\" /><FONT FACE=\"Italic\">" + manageHtml(stereotype) + "</FONT><BR/>";
	}

	private String getLabelForActor(Entity entity) {
		final String actorAbsolutePath = actorFile.getAbsolutePath().replace('/', '\\');
		final String stereotype = entity.getStereotype();

		final StringBuilder sb = new StringBuilder("<<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\">");
		if (stereotype != null) {
			sb.append("<TR><TD>" + manageStereotype(stereotype) + "</TD></TR>");
		}
		sb.append("<TR><TD><IMG SRC=\"" + actorAbsolutePath + "\"/></TD></TR>");
		sb.append("<TR><TD>" + entity.getDisplay() + "</TD></TR>");
		sb.append("</TABLE>>");
		return sb.toString();

	}

	private String getLabelForClass(Entity entity) {
		final String stereotype = entity.getStereotype();

		final StringBuilder sb = new StringBuilder("<<FONT POINT-SIZE=\"12\"><TABLE BGCOLOR=" + YELLOW
				+ " BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"4\">");
		sb.append("<TR><TD>");
		if (stereotype != null) {
			sb.append(manageStereotype(stereotype));
		}
		sb.append(manageHtml(entity.getDisplay()));
		sb.append("</TD></TR>");
		sb.append("<TR><TD WIDTH=\"55\">");
		for (String s : entity.fields()) {
			sb.append("<BR ALIGN=\"LEFT\"/>");
			sb.append("<FONT POINT-SIZE=\"10\">" + manageHtml(s) + "</FONT>");
		}
		sb.append("</TD></TR>");
		sb.append("<TR><TD>");
		for (String s : entity.methods()) {
			sb.append("<BR ALIGN=\"LEFT\"/>");
			sb.append("<FONT POINT-SIZE=\"10\">" + manageHtml(s) + "</FONT>");
			// sb.append("<FONT FACE=\"bold\" POINT-SIZE=\"12\">" +
			// manageHtml(s) + "</FONT>");
			// sb.append("<FONT FACE=\"italic\" POINT-SIZE=\"12\">" +
			// manageHtml(s) + "</FONT>");
			// sb.append("<FONT FACE=\"line\" POINT-SIZE=\"12\">" +
			// manageHtml(s) + "</FONT>");
		}
		sb.append("</TD></TR>");
		sb.append("</TABLE></FONT>>");

		return sb.toString();
	}

	private String manageHtml(String s) {
		s = s.replace("<", "&lt;");
		s = s.replace(">", "&gt;");
		return s;
	}

}
