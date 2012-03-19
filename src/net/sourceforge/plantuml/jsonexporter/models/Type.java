package net.sourceforge.plantuml.jsonexporter.models;


public class Type {
	
	protected String name;
	protected boolean array;
	protected boolean cmp;
	
	public Type(String signature){
		this.name = signature.replaceAll("\\[\\]", "");
		this.array = signature.indexOf("[]") > 0;
		
		// TODO: find if cmp or not
	}
}
