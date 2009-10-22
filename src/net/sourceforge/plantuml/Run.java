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

import javax.swing.UIManager;

import net.sourceforge.plantuml.png.MetadataTag;
import net.sourceforge.plantuml.swing.MainWindow;

public class Run {

	public static void main(String[] argsArray) throws IOException, InterruptedException {
		//final List<String> args = Option.getInstance().manageOption(argsArray);
		final Option option = new Option(argsArray);
		if (option.getResult().size() == 0 && OptionFlags.getInstance().isMetadata() == false) {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (Exception e) {
			}
			new MainWindow();
		} else {
			manageFiles(option);
		}
	}

	private static void manageFile(File f, Option option) throws IOException, InterruptedException {
		if (OptionFlags.getInstance().isMetadata()) {
			System.out.println("------------------------");
			System.out.println(f);
			// new Metadata().readAndDisplayMetadata(f);
			System.out.println();
			System.out.println(new MetadataTag(f, "plantuml").getData());
			System.out.println("------------------------");
		} else {
			new SourceFileReader(option.getDefaultDefines(), f, option.getOutputDir(),
					option.getConfig()).getGeneratedImages();
		}
	}

	private static void manageFiles(Option option) throws IOException, InterruptedException {

		File lockFile = null;
		try {
			if (OptionFlags.getInstance().isWord()) {
				final File dir = new File(option.getResult().get(0));
				final File javaIsRunningFile = new File(dir, "javaisrunning.tmp");
				javaIsRunningFile.delete();
				lockFile = new File(dir, "javaumllock.tmp");
			}
			processArgs(option);
		} finally {
			if (lockFile != null) {
				lockFile.delete();
			}
		}

	}

	private static void processArgs(Option option) throws IOException, InterruptedException {
		for (String s : option.getResult()) {
			final FileGroup group = new FileGroup(s, option.getExcludes(), option);
			for (File f : group.getFiles()) {
				manageFile(f, option);
			}
		}
	}

}
