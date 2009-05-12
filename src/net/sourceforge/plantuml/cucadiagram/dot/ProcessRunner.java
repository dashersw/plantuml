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

import java.io.IOException;
import java.io.InputStream;

import net.sourceforge.plantuml.Log;

public class ProcessRunner {

	private final String cmd;
	private String error;
	private String input;

	public ProcessRunner(String cmd) {
		this.cmd = cmd;
	}

	public void run() throws IOException, InterruptedException {
		final Process process = Runtime.getRuntime().exec(cmd);
		final ThreadStream errorStream = new ThreadStream(process.getErrorStream());
		final ThreadStream inputStream = new ThreadStream(process.getInputStream());
		errorStream.start();
		inputStream.start();
		process.waitFor();
		errorStream.join(10000L);
		inputStream.join(10000L);
		this.input = inputStream.sb.toString();
		this.error = errorStream.sb.toString();
	}

	static class ThreadStream extends Thread {

		private final InputStream is;
		private final StringBuilder sb = new StringBuilder();

		ThreadStream(InputStream is) {
			this.is = is;
		}

		@Override
		public void run() {
			Log.debug("STARTING " + this);
			int read = 0;
			try {
				while ((read = is.read()) != -1) {
					sb.append((char) read);
				}
			} catch (IOException e) {
				e.printStackTrace();
				sb.append('\n');
				sb.append(e.toString());
			}
			Log.debug("FINISHING " + this);
		}
	}

	public final String getError() {
		return error;
	}

	public final String getInput() {
		return input;
	}

}
