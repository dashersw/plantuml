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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.preproc.Defines;

public class Option {

	private final List<String> excludes = new ArrayList<String>();
	private final List<String> config = new ArrayList<String>();
	private final Map<String, String> defines = new LinkedHashMap<String, String>();

	private File outputDir = null;
	private final List<String> result = new ArrayList<String>();

	public Option() {
	}

	public Option(String... arg) throws InterruptedException, IOException {
		for (int i = 0; i < arg.length; i++) {
			String s = arg[i];
			if (s.equalsIgnoreCase("-output") || s.equalsIgnoreCase("-o")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				outputDir = new File(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg[i]));
			} else if (s.startsWith("-o") && s.length() > 3) {
				s = s.substring(2);
				outputDir = new File(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(s));
			} else if (s.equalsIgnoreCase("-recurse") || s.equalsIgnoreCase("-r")) {
				// recurse = true;
			} else if (s.equalsIgnoreCase("-exclude") || s.equalsIgnoreCase("-x")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				excludes.add(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg[i]));
			} else if (s.equalsIgnoreCase("-config")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				initConfig(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg[i]));
			} else if (s.startsWith("-x")) {
				s = s.substring(2);
				excludes.add(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(s));
			} else if (s.equalsIgnoreCase("-verbose") || s.equalsIgnoreCase("-v")) {
				OptionFlags.getInstance().setVerbose(true);
			} else if (s.equalsIgnoreCase("-keepfiles") || s.equalsIgnoreCase("-keepfile")) {
				OptionFlags.getInstance().setKeepTmpFiles(true);
			} else if (s.equalsIgnoreCase("-metadata")) {
				OptionFlags.getInstance().setMetadata(true);
			} else if (s.equalsIgnoreCase("-word")) {
				OptionFlags.getInstance().setWord(true);
			} else if (s.equalsIgnoreCase("-version")) {
				OptionPrint.printVersion();
			} else if (s.startsWith("-D")) {
				manageDefine(s.substring(2));
			} else if (s.equalsIgnoreCase("-testdot")) {
				OptionPrint.printTestDot();
			} else if (s.equalsIgnoreCase("-help") || s.equalsIgnoreCase("-h")) {
				OptionPrint.printHelp();
			} else {
				result.add(s);
			}
		}
	}

	public void initConfig(String filename) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));
			String s = null;
			while ((s = br.readLine()) != null) {
				config.add(s);
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	private void manageDefine(String s) {
		final Pattern p = Pattern.compile("^(\\w+)(?:=(.*))?$");
		final Matcher m = p.matcher(s);
		if (m.find()) {
			define(m.group(1), m.group(2));
		}
	}

	public final File getOutputDir() {
		return outputDir;
	}

	public final String getPattern() {
		return "(?i)^.*\\.(txt|tex|java|htm|html|c|h|cpp)$";
	}

	public void setOutputDir(File f) {
		outputDir = f;
	}

	public final List<String> getExcludes() {
		return Collections.unmodifiableList(excludes);
	}

	public Defines getDefaultDefines() {
		final Defines result = new Defines();
		for (Map.Entry<String, String> ent : defines.entrySet()) {
			result.define(ent.getKey(), ent.getValue());

		}
		return result;
	}

	public void define(String name, String value) {
		defines.put(name, value);
	}

	public List<String> getConfig() {
		return Collections.unmodifiableList(config);
	}

	public final List<String> getResult() {
		return Collections.unmodifiableList(result);
	}

}
