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
package net.sourceforge.plantuml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirWatcher {

	final private File dir;

	final private Map<File, Long> modifieds = new HashMap<File, Long>();

	public DirWatcher(File dir) {
		this.dir = dir;
	}

	public List<GeneratedImage> buildCreatedFiles() throws IOException,
			InterruptedException {
		final List<GeneratedImage> result = new ArrayList<GeneratedImage>();
		for (File f : dir.listFiles()) {
			if (f.isFile() == false) {
				continue;
			}
			if (fileToSkip(f.getName())) {
				continue;
			}
			final long modified = f.lastModified();
			final Long previousModified = modifieds.get(f);

			if (previousModified == null || previousModified != modified) {
				for (GeneratedImage g : new JavaFileReader(f).getGeneratedImages()) {
					result.add(g);
					modifieds.put(f, modified);
				}
			}
		}
		return Collections.unmodifiableList(result);
	}

	private boolean fileToSkip(String name) {
		name = name.toLowerCase();
		return name.endsWith(".java") == false
				&& name.endsWith(".tex") == false
				&& name.endsWith(".txt") == false
				&& name.endsWith(".htm") == false
				&& name.endsWith(".html") == false;
	}
}
