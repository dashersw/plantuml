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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityPackage;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;

final public class DotMaker implements GraphvizMaker {

	private final CucaDiagram diagram;

	private static final String RED = "\"#A80036\"";
	private static final String YELLOW = "\"#FEFECE\"";
	// private static final String YELLOW_NOTE = "\"#FBFB77\"";

	private static boolean isJunit = false;

	private final String[] dotStrings;
	private final Map<EntityType, File> staticImages;
	private final Map<Entity, File> images;

	private boolean underline = false;

	public static void goJunit() {
		isJunit = true;
	}

	public DotMaker(Map<EntityType, File> staticImages, Map<Entity, File> images, CucaDiagram diagram,
			String... dotStrings) {
		this.diagram = diagram;
		this.dotStrings = dotStrings;
		this.staticImages = staticImages;
		this.images = images;

	}

	public String createDotString() throws IOException {

		final StringBuilder sb = new StringBuilder();

		initPrintWriter(sb);

		printPackages(sb);
		printEntities(sb, getUnpackagedEntities());
		printLinks(sb, diagram.getLinks());

		sb.append("}");

		return sb.toString();
	}

	private void initPrintWriter(StringBuilder sb) {

		sb.append("digraph unix {");
		if (isJunit == false) {
			for (String s : dotStrings) {
				sb.append(s);
			}
		}
		sb.append("ratio=auto;");
		// pw.println("concentrate=true;");
		// pw.println("size=\"40,40;\"");
		sb.append("searchsize=500;");
		// pw.println("rankdir=LR;");
	}

	private Collection<Entity> getUnpackagedEntities() {
		final List<Entity> result = new ArrayList<Entity>();
		for (Entity ent : diagram.entities().values()) {
			if (ent.getEntityPackage() == null) {
				result.add(ent);
			}
		}
		return result;
	}

	private void printPackages(StringBuilder sb) {
		for (EntityPackage p : diagram.getPackages()) {
			sb.append("subgraph " + p.getUid() + " {");
			sb.append("label=\"" + p.getCode() + "\";");
			sb.append("color=\"black\";");
			if (p.getBackColor() != null) {
				sb.append("fillcolor=\"" + p.getBackColor().getAsHtml() + "\";");
				sb.append("style=filled;");
			}
			this.printEntities(sb, p.getEntities());
			for (Link link : diagram.getLinks()) {
				eventuallySameRank(sb, p, link);
			}
			sb.append("}");
		}

	}

	private void printLinks(StringBuilder sb, List<Link> links) {
		for (Link link : links) {
			String decoration = "[color=" + RED + ",";

			if (link.getWeight() > 1) {
				decoration += "weight=" + link.getWeight() + ",";
			}

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
			if (link.isInvis()) {
				decoration += ",style=invis";
			}

			final int len = link.getLenght();
			final String lenString = len >= 3 ? ",minlen=" + (len - 1) : "";
			sb.append(link.getEntity1().getUid() + " -> " + link.getEntity2().getUid() + decoration + lenString + "];");
			eventuallySameRank(sb, null, link);
		}
	}

	private void eventuallySameRank(StringBuilder sb, EntityPackage entityPackage, Link link) {
		if (workAroundDotBug()) {
			return;
		}
		final int len = link.getLenght();
		if (len == 1 && link.getEntity1().getEntityPackage() == entityPackage
				&& link.getEntity2().getEntityPackage() == entityPackage) {
			sb.append("{rank=same; " + link.getEntity1().getUid() + "; " + link.getEntity2().getUid() + "}");
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

	private void printEntities(StringBuilder sb, Collection<Entity> entities) {
		for (Entity entity : entities) {
			final EntityType type = entity.getType();
			final String label = getLabel(entity);
			if (type == EntityType.ABSTRACT_CLASS || type == EntityType.CLASS || type == EntityType.INTERFACE
					|| type == EntityType.ENUM) {
				sb.append(entity.getUid() + " [margin=0,fillcolor=" + YELLOW + ",color=" + RED
						+ ",style=filled,shape=box," + label + "];");
			} else if (type == EntityType.USECASE) {
				sb
						.append(entity.getUid() + " [fillcolor=" + YELLOW + ",color=" + RED + ",style=filled," + label
								+ "];");
			} else if (type == EntityType.ACTOR) {
				sb.append(entity.getUid() + " [margin=0,shape=plaintext," + label + "];");
			} else if (type == EntityType.COMPONENT) {
				sb.append(entity.getUid() + " [fillcolor=" + YELLOW + ",color=" + RED
						+ ",style=filled,shape=component," + label + "];");
			} else if (type == EntityType.NOTE) {
				final File file = images.get(entity);
				if (file == null) {
					throw new IllegalStateException();
				}
				if (file.exists() == false) {
					throw new IllegalStateException();
				}
				final String absolutePath = StringUtils.getPlateformDependentAbsolutePath(file);
				sb.append(entity.getUid() + " [margin=0,pad=0," + label + ",shape=none,image=\"" + absolutePath
						+ "\"];");
			} else if (type == EntityType.ACTIVITY) {
				sb.append(entity.getUid() + " [fillcolor=" + YELLOW + ",color=" + RED
						+ ",style=\"rounded,filled\",shape=octagon," + label + "];");
			} else if (type == EntityType.BRANCH) {
				sb.append(entity.getUid() + " [fillcolor=" + YELLOW + ",color=" + RED
						+ ",style=\"filled\",shape=diamond,height=.25,width=.25,label=\"\"];");
			} else if (type == EntityType.SYNCHRO_BAR) {
				sb.append(entity.getUid() + " [fillcolor=black,color=black,style=\"filled\","
						+ "shape=rect,height=.08,width=1.30,label=\"\"];");
			} else if (type == EntityType.CIRCLE_START) {
				sb.append(entity.getUid() + " [fillcolor=black,color=black,style=\"filled\","
						+ "shape=circle,width=.20,label=\"\"];");
			} else if (type == EntityType.CIRCLE_END) {
				sb.append(entity.getUid() + " [fillcolor=black,color=black,style=\"filled\","
						+ "shape=doublecircle,width=.13,label=\"\"];");
			} else if (type == EntityType.POINT_FOR_ASSOCIATION) {
				sb.append(entity.getUid() + " [shape=point,color=" + RED + "];");
			} else {
				throw new IllegalStateException();
			}

		}

	}

	private String getLabel(Entity entity) {
		if (entity.getType() == EntityType.ABSTRACT_CLASS || entity.getType() == EntityType.CLASS
				|| entity.getType() == EntityType.INTERFACE || entity.getType() == EntityType.ENUM) {
			return "label=" + getLabelForClassOrInterfaceOrEnum(entity);
		} else if (entity.getType() == EntityType.ACTOR) {
			return "label=" + getLabelForActor(entity);
		} else if (entity.getType() == EntityType.NOTE) {
			return "label=" + getLabelForNote(entity);
		} else if (entity.getType() == EntityType.ACTIVITY) {
			return "label=" + getLabelForActivity(entity);
		}
		return "label=\"" + entity.getDisplay() + "\"";
	}

	private String getLabelForNote(Entity entity) {
		return "\"\"";
	}

	private String getLabelForActivity(Entity entity) {
		return "<" + manageHtmlIB(entity.getDisplay(), 14) + ">";
	}

	private String getLabelForActor(Entity entity) {
		final String actorAbsolutePath = StringUtils.getPlateformDependentAbsolutePath(staticImages
				.get(EntityType.ACTOR));
		final Stereotype stereotype = entity.getStereotype();

		final StringBuilder sb = new StringBuilder("<<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\">");
		if (isThereLabel(stereotype)) {
			sb.append("<TR><TD>" + getHtmlCodeForDot(stereotype) + "</TD></TR>");
		}
		sb.append("<TR><TD><IMG SRC=\"" + actorAbsolutePath + "\"/></TD></TR>");
		sb.append("<TR><TD>" + entity.getDisplay() + "</TD></TR>");
		sb.append("</TABLE>>");
		return sb.toString();

	}

	private String getLabelForClassOrInterfaceOrEnum(Entity entity) {
		File cFile = images.get(entity);
		if (cFile == null) {
			cFile = staticImages.get(entity.getType());
		}
		if (cFile == null) {
			throw new IllegalStateException();
		}
		// if (cFile.exists() == false) {
		// throw new IllegalStateException("" + cFile);
		// }
		final String circleAbsolutePath = StringUtils.getPlateformDependentAbsolutePath(cFile);

		final StringBuilder sb = new StringBuilder("<<FONT POINT-SIZE=\"12\"><TABLE BGCOLOR=" + YELLOW
				+ " BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"4\">");
		sb.append("<TR><TD>");

		sb.append(getHtmlHeaderTableForClassOrInterfaceOrEnum(entity, circleAbsolutePath));

		sb.append("</TD></TR>");
		sb.append("<TR ALIGN=\"LEFT\"><TD WIDTH=\"55\" ALIGN=\"LEFT\">");

		final int defaultFontSize = 10;
		for (String s : entity.fields()) {
			sb.append(manageHtmlIB(s, defaultFontSize));
			sb.append("<BR ALIGN=\"LEFT\"/>");
		}
		sb.append("</TD></TR>");
		sb.append("<TR ALIGN=\"LEFT\"><TD ALIGN=\"LEFT\">");
		for (String s : entity.methods()) {
			sb.append(manageHtmlIB(s, defaultFontSize));
			sb.append("<BR ALIGN=\"LEFT\"/>");
		}
		sb.append("</TD></TR>");
		sb.append("</TABLE></FONT>>");

		return sb.toString();
	}

	private String manageHtmlIB(String s, int defaultFontSize) {
		final DotExpression dotExpression = new DotExpression(s, defaultFontSize);
		final String result = dotExpression.getDotHtml();
		if (dotExpression.isUnderline()) {
			underline = true;
		}
		return result;
	}

	private String getHtmlHeaderTableForClassOrInterfaceOrEnum(Entity entity, final String circleAbsolutePath) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\">");
		sb.append("<TR><TD ALIGN=\"RIGHT\"><IMG SRC=\"" + circleAbsolutePath + "\"/></TD><TD ALIGN=\"LEFT\">");

		final Stereotype stereotype = entity.getStereotype();
		if (isThereLabel(stereotype)) {
			sb.append(getHtmlCodeForDot(stereotype));
		}
		final boolean italic = entity.getType() == EntityType.ABSTRACT_CLASS
				|| entity.getType() == EntityType.INTERFACE;
		if (italic) {
			sb.append("<FONT FACE=\"italic\">");
		} else {
			sb.append("<FONT FACE=\"normal\">");
		}
		sb.append(StringUtils.manageHtml(entity.getDisplay()));
		sb.append("</FONT>");
		sb.append("</TD></TR></TABLE>");
		return sb.toString();
	}

	private boolean isThereLabel(final Stereotype stereotype) {
		return stereotype != null && stereotype.getLabel() != null;
	}

	private String getHtmlCodeForDot(Stereotype stereotype) {
		return "<BR ALIGN=\"LEFT\" /><FONT FACE=\"italic\">" + StringUtils.manageHtml(stereotype.getLabel())
				+ "</FONT><BR/>";
	}

	public final boolean isUnderline() {
		return underline;
	}

	private boolean workAroundDotBug() {
		for (Link link : diagram.getLinks()) {
			if (link.getLenght() != 1) {
				return false;
			}
		}
		for (Entity ent : diagram.entities().values()) {
			if (diagram.getAllLinkedTo(ent).size() == 0) {
				return true;
			}
		}
		return false;
	}

}
