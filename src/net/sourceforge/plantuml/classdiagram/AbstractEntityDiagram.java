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
 * Revision $Revision: 7600 $
 *
 */
package net.sourceforge.plantuml.classdiagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.IEntity;

public abstract class AbstractEntityDiagram extends CucaDiagram {

	abstract public IEntity getOrCreateClass(String code);

	final protected List<String> getDotStrings() {
		// return Arrays.asList("nodesep=.5;", "ranksep=0.8;", "edge
		// [fontsize=11,labelfontsize=11];",
		// "node [fontsize=11,height=.35,width=.55];");

		final List<String> def = Arrays.asList("nodesep=.35;", "ranksep=0.8;", "edge [fontsize=11,labelfontsize=11];",
				"node [fontsize=11,height=.35,width=.55];");
		if (getPragma().isDefine("graphattributes")==false) {
			return def;
		}
		final String attribute = getPragma().getValue("graphattributes");
		final List<String> result = new ArrayList<String>(def);
		result.add(attribute);
		return Collections.unmodifiableList(result);
	}

	final public String getDescription() {
		return "(" + entities().size() + " entities)";
	}
	
	
	protected final String getFullyQualifiedCode(String code) {
		if (code.startsWith("\\") || code.startsWith("~") || code.startsWith(".")) {
			return code.substring(1);
		}
		if (code.contains(".")) {
			return code;
		}
		final Group g = this.getCurrentGroup();
		if (g == null) {
			return code;
		}
		final String namespace = g.getNamespace();
		if (namespace == null) {
			return code;
		}
		return namespace + "." + code;
	}

	protected final String getShortName(String code) {
		final String namespace = getNamespace(code);
		if (namespace == null) {
			return code;
		}
		return code.substring(namespace.length() + 1);
	}

	protected final String getNamespace(String code) {
		assert code.startsWith("\\") == false;
		assert code.startsWith("~") == false;
		do {
			final int x = code.lastIndexOf('.');
			if (x == -1) {
				return null;
			}
			code = code.substring(0, x);
		} while (entityExist(code));
		return code;
	}
	


}
