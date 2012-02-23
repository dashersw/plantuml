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
 * Revision $Revision: 7393 $
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

import net.sourceforge.plantuml.jsonexporter.Options;
import net.sourceforge.plantuml.preproc.Defines;

public class Option {

	private final List<String> excludes = new ArrayList<String>();
	private final List<String> config = new ArrayList<String>();
	private final Map<String, String> defines = new LinkedHashMap<String, String>();
	private String charset;
	private boolean computeurl = false;
	private boolean decodeurl = false;
	private boolean pipe = false;
	private boolean syntax = false;
	private boolean checkOnly = false;
	private boolean pattern = false;
	private boolean duration = false;
	private int nbThreads = 0;
	private int ftpPort = -1;

	private File outputDir = null;
	private File outputFile = null;

	private final List<String> result = new ArrayList<String>();

	public Option() {
	}

	private FileFormat fileFormat = FileFormat.PNG;

	public FileFormat getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(FileFormat fileFormat) {
		this.fileFormat = fileFormat;
	}

	public Option(String... arg) throws InterruptedException, IOException {
		if (arg.length == 0) {
			OptionFlags.getInstance().setGui(true);
		}
		for (int i = 0; i < arg.length; i++) {
			String s = arg[i];
			if (s.equalsIgnoreCase("-tsvg") || s.equalsIgnoreCase("-svg")) {
				setFileFormat(FileFormat.SVG);
			} else if (s.equalsIgnoreCase("-thtml") || s.equalsIgnoreCase("-html")) {
				setFileFormat(FileFormat.HTML);
			} else if (s.equalsIgnoreCase("-txmi") || s.equalsIgnoreCase("-xmi")) {
				setFileFormat(FileFormat.XMI_STANDARD);
			} else if (s.equalsIgnoreCase("-txmi:argo") || s.equalsIgnoreCase("-xmi:argo")) {
				setFileFormat(FileFormat.XMI_ARGO);
			} else if (s.equalsIgnoreCase("-txmi:star") || s.equalsIgnoreCase("-xmi:star")) {
				setFileFormat(FileFormat.XMI_STAR);
			} else if (s.equalsIgnoreCase("-teps") || s.equalsIgnoreCase("-eps")) {
				setFileFormat(FileFormat.EPS);
			} else if (s.equalsIgnoreCase("-teps:text") || s.equalsIgnoreCase("-eps:text")) {
				setFileFormat(FileFormat.EPS_TEXT);
			} else if (s.equalsIgnoreCase("-tdot") || s.equalsIgnoreCase("-dot")) {
				setFileFormat(FileFormat.DOT);
				OptionFlags.getInstance().setKeepTmpFiles(true);
			} else if (s.equalsIgnoreCase("-ttxt") || s.equalsIgnoreCase("-txt")) {
				setFileFormat(FileFormat.ATXT);
			} else if (s.equalsIgnoreCase("-tutxt") || s.equalsIgnoreCase("-utxt")) {
				setFileFormat(FileFormat.UTXT);
			} else if (s.equalsIgnoreCase("-pdf") || s.equalsIgnoreCase("-tpdf")) {
				setFileFormat(FileFormat.PDF);
			} else if (s.equalsIgnoreCase("-output") || s.equalsIgnoreCase("-o")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				outputDir = new File(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg[i]));
			} else if (s.equalsIgnoreCase("-ofile")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				outputFile = new File(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg[i]));
			} else if (s.equalsIgnoreCase("-ojson")){
                i++;
                if (i == arg.length) {
                    continue;
                }
                // Setting json output directory in exporter module
                // TODO: in order not to change the existing code
                // config passing is left ugly
                Options.FILES_OUPUT_DIRECTORY = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg[i]);
            }
			else if (s.equalsIgnoreCase("-graphvizdot") || s.equalsIgnoreCase("-graphviz_dot")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				OptionFlags.getInstance().setDotExecutable(
						StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg[i]));
			} else if (s.equalsIgnoreCase("-charset")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				charset = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg[i]);
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
			} else if (s.equalsIgnoreCase("-nbthread") || s.equalsIgnoreCase("-nbthreads")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				final String nb = arg[i];
				if ("auto".equalsIgnoreCase(nb)) {
					this.nbThreads = defaultNbThreads();
				} else if (nb.matches("\\d+")) {
					this.nbThreads = Integer.parseInt(nb);
				}
			} else if (s.equalsIgnoreCase("-checkonly")) {
				this.checkOnly = true;
				OptionFlags.getInstance().setFailOnError(true);
			} else if (s.equalsIgnoreCase("-config")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				initConfig(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg[i]));
			} else if (s.equalsIgnoreCase("-computeurl") || s.equalsIgnoreCase("-encodeurl")) {
				this.computeurl = true;
			} else if (s.startsWith("-x")) {
				s = s.substring(2);
				excludes.add(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(s));
			} else if (s.equalsIgnoreCase("-debugdot")) {
				OptionFlags.getInstance().setDebugDot(true);
			} else if (s.equalsIgnoreCase("-verbose") || s.equalsIgnoreCase("-v")) {
				OptionFlags.getInstance().setVerbose(true);
			} else if (s.equalsIgnoreCase("-pipe") || s.equalsIgnoreCase("-p")) {
				pipe = true;
			} else if (s.equalsIgnoreCase("-pattern")) {
				pattern = true;
			} else if (s.equalsIgnoreCase("-syntax")) {
				syntax = true;
				OptionFlags.getInstance().setQuiet(true);
			} else if (s.equalsIgnoreCase("-duration")) {
				duration = true;
			} else if (s.equalsIgnoreCase("-keepfiles") || s.equalsIgnoreCase("-keepfile")) {
				OptionFlags.getInstance().setKeepTmpFiles(true);
			} else if (s.equalsIgnoreCase("-metadata")) {
				OptionFlags.getInstance().setMetadata(true);
			} else if (s.equalsIgnoreCase("-logdata")) {
				i++;
				if (i == arg.length) {
					continue;
				}
				OptionFlags.getInstance().setLogData(
						new File(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg[i])));
			} else if (s.equalsIgnoreCase("-word")) {
				OptionFlags.getInstance().setWord(true);
				OptionFlags.getInstance().setQuiet(true);
			} else if (s.equalsIgnoreCase("-forcegd")) {
				OptionFlags.getInstance().setForceGd(true);
			} else if (s.equalsIgnoreCase("-forcecairo")) {
				OptionFlags.getInstance().setForceCairo(true);
			} else if (s.equalsIgnoreCase("-quiet")) {
				OptionFlags.getInstance().setQuiet(true);
			} else if (s.equalsIgnoreCase("-decodeurl")) {
				this.decodeurl = true;
			} else if (s.equalsIgnoreCase("-version")) {
				OptionPrint.printVersion();
			} else if (s.equalsIgnoreCase("-checkversion")) {
				OptionPrint.checkVersion();
			} else if (s.startsWith("-D")) {
				manageDefine(s.substring(2));
			} else if (s.startsWith("-S")) {
				manageSkinParam(s.substring(2));
			} else if (s.equalsIgnoreCase("-testdot")) {
				OptionPrint.printTestDot();
			} else if (s.equalsIgnoreCase("-about") || s.equalsIgnoreCase("-author") || s.equalsIgnoreCase("-authors")) {
				OptionPrint.printAbout();
			} else if (s.equalsIgnoreCase("-help") || s.equalsIgnoreCase("-h") || s.equalsIgnoreCase("-?")) {
				OptionPrint.printHelp();
			} else if (s.equalsIgnoreCase("-language")) {
				OptionPrint.printLanguage();
			} else if (s.equalsIgnoreCase("-gui")) {
				OptionFlags.getInstance().setGui(true);
			} else if (s.equalsIgnoreCase("-nosuggestengine")) {
				OptionFlags.getInstance().setUseSuggestEngine(false);
			} else if (s.equalsIgnoreCase("-failonerror")) {
				OptionFlags.getInstance().setFailOnError(true);
			} else if (s.equalsIgnoreCase("-printfonts")) {
				OptionFlags.getInstance().setPrintFonts(true);
			} else if (s.toLowerCase().startsWith("-ftp")) {
				final int x = s.indexOf(':');
				if (x == -1) {
					this.ftpPort = 4242;
				} else {
					this.ftpPort = Integer.parseInt(s.substring(x + 1));
				}
			} else if (s.startsWith("-c")) {
				s = s.substring(2);
				config.add(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(s));
			} else {
				result.add(s);
			}
		}
	}

	public int getFtpPort() {
		return ftpPort;
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

	private void manageSkinParam(String s) {
		final Pattern p = Pattern.compile("^(\\w+)(?:=(.*))?$");
		final Matcher m = p.matcher(s);
		if (m.find()) {
			skinParam(m.group(1), m.group(2));
		}
	}

	private void skinParam(String var, String value) {
		if (var != null && value != null) {
			config.add("skinparamlocked " + var + " " + value);
		}

	}

	public final File getOutputDir() {
		return outputDir;
	}

	public final static String getPattern() {
		return "(?i)^.*\\.(txt|tex|java|htm|html|c|h|cpp|apt)$";
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

	public final String getCharset() {
		return charset;
	}

	public void setCharset(String s) {
		this.charset = s;

	}

	public final boolean isComputeurl() {
		return computeurl;
	}

	public final boolean isDecodeurl() {
		return decodeurl;
	}

	public final boolean isPipe() {
		return pipe;
	}

	public final boolean isSyntax() {
		return syntax;
	}

	public final boolean isPattern() {
		return pattern;
	}

	public FileFormatOption getFileFormatOption() {
		return new FileFormatOption(getFileFormat());
	}

	public final boolean isDuration() {
		return duration;
	}

	public final int getNbThreads() {
		return nbThreads;
	}

	public final void setNbThreads(int nb) {
		this.nbThreads = nb;
	}

	public static int defaultNbThreads() {
		return Runtime.getRuntime().availableProcessors();
	}

	public final boolean isCheckOnly() {
		return checkOnly;
	}

	public final void setCheckOnly(boolean checkOnly) {
		this.checkOnly = checkOnly;
	}

	public final File getOutputFile() {
		return outputFile;
	}

}
