package net.sourceforge.plantuml.jsonexporter.models;

public class Parameter extends Base {
	
	private String paramName;
	private String type;
	
	public Parameter(String param) {
		paramName = param;
	}
}
