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
package net.sourceforge.plantuml.classdiagram.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityPackage;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;

public class SqlImporter {

	private final ClassDiagram classDiagram;
	private final File source;

	public SqlImporter(ClassDiagram classDiagram, File source) {
		this.classDiagram = classDiagram;
		this.source = source;
	}

	public void process() throws IOException {
		final String content = getContentString();
		processTableCreate(content);
		processAddConstraint(content);

	}

	private void processTableCreate(final String content) {
		final Pattern p = Pattern.compile("(?s)\\bcreate\\s+table\\s+(\\w+)\\b.*?\\(((?:\\([^()]*\\)|[^()]+)+)\\)");
		final Matcher m = p.matcher(content);

		while (m.find()) {
			final Entity ent = classDiagram.getOrCreateClass(new String(m.group(1)));
			ent.setStereotype(new Stereotype("<< (T,#BBBBBB) >>"));
			addFields(ent, m.group(2));
		}
	}

	private void addFields(Entity ent, String data) {
		final Pattern p = Pattern.compile("(?s)\\s*((?:\\([^()]*\\)|[^(),]+)+)\\s*,?");
		final Matcher m = p.matcher(data);
		// System.err.println("data="+data);
		while (m.find()) {
			final String s = m.group(1).replaceAll("\\s+", " ");
			ent.addField(s);
			// System.err.println("s="+s);
		}

	}

	private void processAddConstraint(final String content) {
		final Pattern p = Pattern
				.compile("(?s)\\balter\\s+table\\s+(\\w+)[^;]+\\badd\\s+constraint\\s+(\\w+)[^;]+references\\s+(\\w+)");
		final Matcher m = p.matcher(content);

		final boolean putConstraint = classDiagram.entities().size() < 100;

		while (m.find()) {
			final String table1 = m.group(1);
			final String constraint = putConstraint ? m.group(2) : "";
			final String table2 = m.group(3);

			final Entity ent1 = classDiagram.getOrCreateClass(table1);
			final Entity ent2 = classDiagram.getOrCreateClass(table2);
			if (classDiagram.isDirectlyLinked(ent1, ent2) == false) {
				final Link link = new Link(ent1, ent2, LinkType.NAVASSOC, constraint, 2, null, null);
				classDiagram.addLink(link);
			}
		}

		if (false && classDiagram.getLinks().size() > 100) {
			final Set<Set<Entity>> all = new HashSet<Set<Entity>>();
			for (Entity ent : classDiagram.entities().values()) {
				System.err.println("ent=" + ent);
				final Set<Entity> linked = classDiagram.getAllLinkedTo(ent);
				System.err.println("linked.size()=" + linked.size());
				all.add(linked);
			}
			System.err.println("linked=" + all.size());

			int x = 0;
			for (Set<Entity> set : all) {
				final EntityPackage entityPackage = classDiagram.getOrCreatePackage("x" + x);
				x++;
				for (Entity ent : set) {
					ent.setEntityPackage(entityPackage);
				}
			}
			System.err.println("OK");
		}

		int cpt = 0;

		if (false && classDiagram.getLinks().size() > 100) {
			final Collection<Link> newLinks = new ArrayList<Link>();
			for (Entity ent : new ArrayList<Entity>(classDiagram.entities().values())) {
				final Set<Link> links = classDiagram.linksArrivingTo(ent);
				if (links.size() > 5) {
					final StringBuilder note = new StringBuilder();
					for (Link l : links) {
						l.setInvis(true);
						note.append(l.getEntity1().getCode());
						note.append("\\n");
					}
					final Entity n = classDiagram.createEntity("SQL" + cpt, note.toString(), EntityType.NOTE);
					cpt++;
					newLinks.add(new Link(n, ent, LinkType.NAVASSOC_DASHED, null, 2, null, null));
				}
			}
			System.err.println("Adding notes " + newLinks.size());
			for (Link l : newLinks) {
				l.setWeight(100);
				classDiagram.addLink(l);
			}
		}
	}

	private String getContentString() throws IOException {
		final StringBuilder sb = new StringBuilder();
		final BufferedReader br = new BufferedReader(new FileReader(source));
		String s;
		while ((s = br.readLine()) != null) {
			sb.append(s);
			sb.append('\n');
		}
		return sb.toString();
	}

}
