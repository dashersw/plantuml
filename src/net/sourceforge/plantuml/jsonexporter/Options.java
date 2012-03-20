package net.sourceforge.plantuml.jsonexporter;

public class Options {
	
	final public static String KEY_ARRAY = "array";
	
	final public static String[] OUTPUT_VISIBILITIES = 
			new String[]{"public", "private", "protected", "package"};
	// TODO: allow overriding of this setting via file input
	final public static String[] OUTPUT_PRIMITIVES = 
			new String[]{"number", "boolean", "object", "string"};
	
	public static String FILES_OUPUT_DIRECTORY = "json/";

}
