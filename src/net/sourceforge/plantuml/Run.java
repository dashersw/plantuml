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

import net.sourceforge.plantuml.png.MetadataTag;
import net.sourceforge.plantuml.swing.MainWindow;

public class Run {

	public static void main(String[] argsArray) throws IOException, InterruptedException {
		final List<String> args = Option.getInstance().manageOption(argsArray);
		if (args.size() == 0 && Option.getInstance().isMetadata() == false) {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (Exception e) {
			}
			new MainWindow();
		} else {
			manageFiles(args);
		}
	}

	private static void manageFile(File f) throws IOException, InterruptedException {
		if (Option.getInstance().isMetadata()) {
			System.out.println("------------------------");
			System.out.println(f);
			// new Metadata().readAndDisplayMetadata(f);
			System.out.println();
			System.out.println(new MetadataTag(f, "plantuml").getData());
			System.out.println("------------------------");
		} else {
			new SourceFileReader(Option.getInstance().getDefaultDefines(), f, Option.getInstance().getOutputDir()).getGeneratedImages();
		}
	}

	private static void manageFiles(List<String> args) throws IOException, InterruptedException {

		File lockFile = null;
		try {
			if (Option.getInstance().isWord()) {
				final File dir = new File(args.get(0));
				final File javaIsRunningFile = new File(dir, "javaisrunning.tmp");
				javaIsRunningFile.delete();
				lockFile = new File(dir, "javaumllock.tmp");
			}
			processArgs(args);
		} finally {
			if (lockFile != null) {
				lockFile.delete();
			}
		}

	}

	private static void processArgs(List<String> args) throws IOException, InterruptedException {
		for (String s : args) {
			final FileGroup group = new FileGroup(s, Option.getInstance().getExcludes());
			for (File f : group.getFiles()) {
				manageFile(f);
			}
		}
	}

}
