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
 * Revision $Revision: 5079 $
 *
 */
package net.sourceforge.plantuml.html;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.cucadiagram.Stereotype;

public final class CucaDiagramHtmlMaker {

	private final CucaDiagram diagram;
	private final File dir;

	public CucaDiagramHtmlMaker(CucaDiagram diagram, File dir) {
		this.diagram = diagram;
		this.dir = dir;
	}

	public List<File> create() throws IOException {
		final boolean ok = dir.mkdirs();
		if (dir.exists() == false) {
			throw new IOException("Cannot create " + dir);
		}
		exportIndexAll();
		for (Map.Entry<String, Entity> ent : diagram.entities().entrySet()) {
			export(ent.getKey(), ent.getValue());

		}
		return Arrays.asList(dir);
	}

	private void exportIndexAll() throws IOException {
		final File f = new File(dir, "index.html");
		final PrintWriter pw = new PrintWriter(f);
		pw.println("<html>");
		pw.println("<ul>");
		for (Map.Entry<String, Entity> ent : new TreeMap<String, Entity>(diagram.entities()).entrySet()) {
			pw.println("<li>");
			pw.println(htmlLink(ent.getValue().getCode()));
			pw.println("</li>");
		}
		pw.println("</ul>");
		htmlClose(pw);

	}

	private void export(String code, Entity entity) throws IOException {
		final File f = new File(dir, code + ".html");
		final PrintWriter pw = new PrintWriter(f);
		pw.println("<html>");
		pw.println("<h2>" + entity.getType().toHtml() + "</h2>");
		for (CharSequence s : entity.getDisplay2()) {
			pw.println(StringUtils.unicode2(s.toString()));
			pw.println("<br>");
		}
		pw.println("<hr>");
		final Stereotype stereotype = entity.getStereotype();
		if (stereotype != null) {
			pw.println("<h3>Stereotype</h3>");
			for (String s : stereotype.getLabels()) {
				pw.println(s);
				pw.println("<br>");
			}
			pw.println("<hr>");
		}

		if (entity.getFieldsToDisplay().size() == 0) {
			pw.println("<h2>No fields</h2>");
		} else {
			pw.println("<h2>Fields:</h2>");
			pw.println("<ul>");
			for (Member m : entity.getFieldsToDisplay()) {
				pw.println("<li>");
				pw.println(StringUtils.unicode2(m.getDisplayWithVisibilityChar()));
				pw.println("</li>");
			}
			pw.println("</ul>");
		}

		pw.println("<hr>");
		if (entity.getMethodsToDisplay().size() == 0) {
			pw.println("<h2>No methods</h2>");
		} else {
			pw.println("<h2>Methods:</h2>");
			pw.println("<ul>");
			for (Member m : entity.getMethodsToDisplay()) {
				pw.println("<li>");
				pw.println(StringUtils.unicode2(m.getDisplayWithVisibilityChar()));
				pw.println("</li>");
			}
			pw.println("</ul>");
		}

		pw.println("<hr>");
		final Collection<Link> links = getLinks(entity);
		if (links.size() == 0) {
			pw.println("<h2>No links</h2>");
		} else {
			pw.println("<h2>Links:</h2>");
			pw.println("<ul>");
			for (Link l : links) {
				pw.println("<li>");
				printLink(pw, l, entity);
				pw.println("</li>");
			}
			pw.println("</ul>");
		}

		htmlClose(pw);
	}

	private void htmlClose(final PrintWriter pw) {
		pw.println("<hr>");
		pw.println("<a href=index.html>Back to index</a>");
		pw.println("</html>");
		pw.close();
	}

	private void printLink(PrintWriter pw, Link link, Entity entity) {
		// final IEntity other = link.getOther(entity);
		String ent1 = link.getEntity1().getCode();
		String ent2 = link.getEntity2().getCode();
		if (link.getEntity1() == entity) {
			ent1 = "<i>" + StringUtils.unicode2(ent1) + "</i>";
			ent2 = htmlLink(ent2);
		} else {
			ent1 = htmlLink(ent1);
			ent2 = "<i>" + StringUtils.unicode2(ent2) + "</i>";
		}
		pw.println(link.getType().getHtml(ent1, ent2));
		// pw.println(htmlLink(other.getCode()));
		if (link.getLabel() != null) {
			pw.println("&nbsp;:&nbsp;");
			pw.println(StringUtils.unicode2(link.getLabel()));
		}
	}

	private String htmlLink(String code) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<a href=\"");
		sb.append(StringUtils.unicode2(code) + ".html");
		sb.append("\">");
		sb.append(StringUtils.unicode2(code));
		sb.append("</a>");
		return sb.toString();

	}

	private Collection<Link> getLinks(IEntity ent) {
		final List<Link> result = new ArrayList<Link>();
		for (Link link : diagram.getLinks()) {
			if (link.contains(ent)) {
				result.add(link);
			}
		}
		return Collections.unmodifiableList(result);
	}
}
