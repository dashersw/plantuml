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
import java.util.List;

import javax.swing.UIManager;

import net.sourceforge.plantuml.swing.MainWindow;

public class Run {

	public static void main(String[] argsArray) throws IOException, InterruptedException {
		final List<String> args = Option.getInstance().manageOption(argsArray);
		if (args.size() == 0) {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (Exception e) {
			}
			new MainWindow();
		} else {
			manageFiles(args);
		}
	}

	private static void manageDir(File dir) throws IOException, InterruptedException {
		File lockFile = null;
		try {
			lockFile = new File(dir, "javaumllock.tmp");
			final DirWatcher dirWatcher = new DirWatcher(dir);
			dirWatcher.buildCreatedFiles();
		} finally {
			if (lockFile != null) {
				lockFile.delete();
			}
		}
	}

	private static void manageFile(File f) throws IOException, InterruptedException {
		new JavaFileReader(f).execute();
	}

	private static void manageFiles(List<String> args) throws IOException, InterruptedException {
		for (String s : args) {
			final File f = new File(s);
			if (f.isDirectory()) {
				manageDir(f);
			} else if (f.isFile()) {
				manageFile(f);
			}

		}
	}

}
