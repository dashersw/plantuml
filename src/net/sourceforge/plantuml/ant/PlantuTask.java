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
package net.sourceforge.plantuml.ant;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import net.sourceforge.plantuml.DirWatcher;
import net.sourceforge.plantuml.GeneratedImage;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

// <?xml version="1.0"?>
//
// <project name="OwnTaskExample" default="main" basedir=".">
// <taskdef name="plot" classname="plot.PlotTask" classpath="build"/>
//
// <target name="main">
// <mytask message="Hello World! MyVeryOwnTask works!"/>
// </target>
// </project>

// Carriage Return in UTF-8 XML: &#13;
// Line Feed in UTF-8 XML: &#10;
public class PlantuTask extends Task {

	private String dir = ".";

	// The method executing the task
	@Override
	public void execute() throws BuildException {

		this.log("Starting PlantUML");
		final File f = new File(dir);
		if (f.exists() == false) {
			final String s = "The file " + f.getAbsolutePath()
					+ " does not exists.";
			this.log(s);
			throw new BuildException(s);
		}
		final DirWatcher dirWatcher = new DirWatcher(f);
		try {
			final Collection<GeneratedImage> result = dirWatcher
					.buildCreatedFiles();
			for (GeneratedImage g : result) {
				this.log(g + " " + g.getDescription());
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BuildException(e.toString());
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new BuildException(e.toString());
		}

	}

	public void setDir(String s) {
		this.dir = s;
	}

}
