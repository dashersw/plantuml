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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class DirWatcher {

	final private File dir;

	final private Map<File, Long> modifieds = new HashMap<File, Long>();

	public DirWatcher(File dir) {
		this.dir = dir;
	}

	public SortedMap<FilePng, String> buildCreatedFiles() throws IOException, InterruptedException {
		final SortedMap<FilePng, String> result = new TreeMap<FilePng, String>();
		for (File f : dir.listFiles()) {
			if (f.isFile() == false) {
				continue;
			}
			if (f.getName().toLowerCase().endsWith(".java") == false
					&& f.getName().toLowerCase().endsWith(".tex") == false
					&& f.getName().toLowerCase().endsWith(".txt") == false
					&& f.getName().toLowerCase().endsWith(".htm") == false
					&& f.getName().toLowerCase().endsWith(".html") == false) {
				continue;
			}
			final long modified = f.lastModified();
			final Long previousModified = modifieds.get(f);

			if (previousModified == null || previousModified != modified) {
				for (Map.Entry<FilePng, String> entry : getPngFileCreated(f).entrySet()) {
					result.put(entry.getKey(), entry.getValue());
					modifieds.put(f, modified);
				}
			}
		}
		return Collections.unmodifiableSortedMap(result);
	}

	/*
	static private void list(File dir, boolean recurse, Collection<File> result) {
		for (File f : dir.listFiles()) {
			if (f.isDirectory() && recurse) {
				list(f, recurse, result);
			}
			if (f.isFile() == false) {
				continue;
			}
			if (f.getName().toLowerCase().endsWith(".java") == false
					&& f.getName().toLowerCase().endsWith(".tex") == false
					&& f.getName().toLowerCase().endsWith(".txt") == false
					&& f.getName().toLowerCase().endsWith(".htm") == false
					&& f.getName().toLowerCase().endsWith(".html") == false) {
				continue;
			}
			result.add(f);
		}

	}
	*/

	private SortedMap<FilePng, String> getPngFileCreated(File f) throws IOException, InterruptedException {
		final SortedMap<FilePng, String> result = new TreeMap<FilePng, String>();
		for (StartUml s : new JavaFileReader(f).execute()) {
			result.put(s.getPng(), s.getDescription());
		}
		return Collections.unmodifiableSortedMap(result);
	}

}
