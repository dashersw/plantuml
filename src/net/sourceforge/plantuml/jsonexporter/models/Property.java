package net.sourceforge.plantuml.jsonexporter.models;

import net.sourceforge.plantuml.cucadiagram.Member;

public class Property extends Base {
	
	private String propName;
	private String type;
	private String cmp;
	
	public static Property fromPlantUmlMember(Member member){
		
		Property property = new Property();
		String name = member.getDisplayWithoutVisibilityChar();
		String[] parts = name.split(":");
		
		property.propName = parts[0].trim();
		
		if(parts.length > 1){	
			property.type = property.toArrayType(parts[1].trim());
		}
		
		return property;
	}
	
}
