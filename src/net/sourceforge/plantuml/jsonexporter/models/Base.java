package net.sourceforge.plantuml.jsonexporter.models;

import net.sourceforge.plantuml.jsonexporter.Exporter;

public class Base {
	
	
	public String toArrayType(String type){
		
		String output = type;
		if(type != null
			&& type.contains(Exporter.KEY_ARRAY)){
			
			String newType = type.replaceAll(Exporter.KEY_ARRAY, "")
					.replaceAll("\\[", "")
					.replaceAll("\\]", "");
			
			output = Exporter.ARRAY_PREFIX + newType + Exporter.ARRAY_POSTFIX;
		}
		
		return output;
	}
}
