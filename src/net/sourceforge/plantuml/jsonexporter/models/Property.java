package net.sourceforge.plantuml.jsonexporter.models;

import net.sourceforge.plantuml.cucadiagram.Member;

public class Property extends Base {
	
	private String propName;
	private String visibility;
	private Type type;
	
	public static Property fromPlantUmlMember(Member member){
		
		Property property = new Property();
		String name = member.getDisplayWithoutVisibilityChar();
		String[] parts = name.split(":");
		
		property.propName = parts[0].trim();
		
		if(parts.length > 1){	
			property.type = new Type(parts[1].trim());
		}
		
		// assign visibility
		if(member.getVisibilityModifier() != null){
			property.visibility = toVisibility(member.getVisibilityModifier());
		}
		
		return property;
	}

	public String getPropName() {
		return propName;
	}

	public String getVisibility() {
		return visibility;
	}

	public Type getType() {
		return type;
	}
	
}
