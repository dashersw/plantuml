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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Stereotype;

public class NeatoMaker {

	private final CucaDiagram diagram;

	private static final String RED = "\"#A80036\"";
	private static final String YELLOW = "\"#FEFECE\"";
	// private static final String YELLOW_NOTE = "\"#FBFB77\"";

	private static boolean isJunit = false;

	public static void goJunit() {
		isJunit = true;
	}

	public NeatoMaker(CucaDiagram diagram, String... dotStrings) {
		this.diagram = diagram;
		this.dotStrings = dotStrings;
	}

	private final String[] dotStrings;
	private Map<EntityType, File> staticImages;
	private Map<Entity, File> images;

	public void generateFile(final File out, Map<EntityType, File> staticImages, Map<Entity, File> images)
			throws IOException {

		final PrintWriter pw = initPrintWriter(out);

		this.staticImages = staticImages;
		this.images = images;

		printEntities(pw, getUnpackagedEntities());
		printLinks(pw, diagram.getLinks());

		pw.println("}");
		pw.close();
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

	protected void debugFile(File f) throws IOException {
		final BufferedReader br = new BufferedReader(new FileReader(f));
		String s;
		while ((s = br.readLine()) != null) {
			System.out.println(s);
		}
		br.close();
	}

	protected PrintWriter initPrintWriter(final File out) throws FileNotFoundException {
		final PrintWriter pw = new PrintWriter(out);

		pw.println("graph unix {");
		//pw.println("size=\"50,50\";");
		//pw.println("sep=0.2;");
		pw.println("overlap=scalexy;");
		//pw.println("overlap=ortho;");
		return pw;
	}

	protected void printSpecialHeader(PrintWriter pw) {
	}

	protected void printLinks(PrintWriter pw, List<Link> links) {

		for (Link link : links) {
			pw.println(link.getEntity1().getUid() + " -- " + link.getEntity2().getUid() + ";");
		}
	}

	protected void printEntities(PrintWriter pw, Collection<Entity> entities) {
		for (Entity entity : entities) {
			final String label = getLabel(entity);
			pw.println(entity.getUid() + ";");
//			pw.println(entity.getUid() + " [margin=0,fillcolor=" + YELLOW + ",color=" + RED
//					+ ",style=filled,shape=box," + label + "];");
		}
	}

	protected void printEntitiesOld(PrintWriter pw, Collection<Entity> entities) {
		for (Entity entity : entities) {
			final EntityType type = entity.getType();
			final String label = getLabel(entity);
			if (type == EntityType.ABSTRACT_CLASS || type == EntityType.CLASS || type == EntityType.INTERFACE
					|| type == EntityType.ENUM) {
				pw.println(entity.getUid() + " [margin=0,fillcolor=" + YELLOW + ",color=" + RED
						+ ",style=filled,shape=box," + label + "];");
			} else if (type == EntityType.USECASE) {
				pw.println(entity.getUid() + " [fillcolor=" + YELLOW + ",color=" + RED + ",style=filled," + label
						+ "];");
			} else if (type == EntityType.ACTOR) {
				pw.println(entity.getUid() + " [margin=0,shape=plaintext," + label + "];");
			} else if (type == EntityType.COMPONENT) {
				pw.println(entity.getUid() + " [fillcolor=" + YELLOW + ",color=" + RED
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
				pw.println(entity.getUid() + " [margin=0,pad=0,label=\"\",shape=none,image=\"" + absolutePath + "\"];");
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
		if (entity.getType() == EntityType.ABSTRACT_CLASS || entity.getType() == EntityType.CLASS
				|| entity.getType() == EntityType.INTERFACE || entity.getType() == EntityType.ENUM) {
			return "label=" + getLabelForClassOrInterfaceOrEnum(entity);
		} else if (entity.getType() == EntityType.ACTOR) {
			return "label=" + getLabelForActor(entity);
		} else if (entity.getType() == EntityType.NOTE) {
			return "label=" + getLabelForNote(entity);
		}
		// final Stereotype stereotype = entity.getStereotype();
		// if (stereotype != null) {
		// return "label=<" + stereotype.getHtmlCodeForDot() +
		// StringUtils.manageHtml(entity.getDisplay()) + ">";
		// }
		return "label=\"" + entity.getDisplay() + "\"";
	}

	private String getLabelForNote(Entity entity) {
		final File file = images.get(entity);
		if (file == null) {
			throw new IllegalStateException();
		}
		if (file.exists() == false) {
			throw new IllegalStateException();
		}
		final String absolutePath = StringUtils.getPlateformDependentAbsolutePath(file);

		final StringBuilder sb = new StringBuilder("<<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\">");
		sb
				.append("<TR BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\"><TD BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\"><IMG SRC=\""
						+ absolutePath + "\"/></TD></TR>");
		sb.append("</TABLE>>");
		return sb.toString();

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
		sb.append("<TR><TD WIDTH=\"55\">");
		for (String s : entity.fields()) {
			sb.append("<FONT POINT-SIZE=\"10\">" + StringUtils.manageHtml(s) + "</FONT>");
			sb.append("<BR ALIGN=\"LEFT\"/>");
		}
		sb.append("</TD></TR>");
		sb.append("<TR><TD>");
		for (String s : entity.methods()) {
			sb.append("<FONT POINT-SIZE=\"10\">" + StringUtils.manageHtml(s) + "</FONT>");
			sb.append("<BR ALIGN=\"LEFT\"/>");
		}
		sb.append("</TD></TR>");
		sb.append("</TABLE></FONT>>");

		return sb.toString();
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
		return "<BR ALIGN=\"LEFT\" /><FONT FACE=\"Italic\">" + StringUtils.manageHtml(stereotype.getLabel())
				+ "</FONT><BR/>";
	}

	protected CucaDiagram getDiagram() {
		return diagram;
	}

}
