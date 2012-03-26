package net.sourceforge.plantuml.jsonexporter.models;

import java.util.Arrays;

import net.sourceforge.plantuml.jsonexporter.Options;

public class Type {

	protected String name;
	protected boolean array;
	protected boolean cmp;

	/**
	 * Instantiates a new type.
	 * 
	 * @param signature
	 */
	public Type(String signature) {
		// TODO: move array rep string/regex to options?
		this.name = signature.replaceAll("\\[\\]", "");
		this.array = signature.indexOf("[]") > 0;

		// search if type is primitive
		// or not
		this.cmp = !(Arrays
			.asList(Options.OUTPUT_PRIMITIVES)
			.contains(this.name.toLowerCase()));

	}
}
