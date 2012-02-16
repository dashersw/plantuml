package net.sourceforge.plantuml.jsonexporter.models;

import net.sourceforge.plantuml.jsonexporter.Exporter;
import net.sourceforge.plantuml.skin.VisibilityModifier;

public class Base {
	
	
	/**
	 * Exchanges any array[<ClassName>] types to <ClassName>[]
	 *
	 * @param type the type
	 * @return the string
	 */
	public static String toArrayType(String type){
		
		String output = type;
		if(type != null
			&& type.contains(Exporter.KEY_ARRAY)){
			
			String newType = type.replaceAll(Exporter.KEY_ARRAY, "")
					.replaceAll("\\[", "")
					.replaceAll("\\]", "");
			
			output = Exporter.OUTPUT_ARRAY_PREFIX + newType + Exporter.OUTPUT_ARRAY_POSTFIX;
		}
		
		return output;
	}
	
	/**
	 * Exchanges the VisibilityModifier to public,
	 * private, protected or package.
	 *
	 * @param visibilityModifier the visibility modifier
	 * @return the string
	 */
	public static String toVisibility(VisibilityModifier visibilityModifier){
		
		if(visibilityModifier == null) { return null; }
		
		for(String mod: Exporter.OUTPUT_VISIBILITIES){
			if(visibilityModifier
				.name().toLowerCase().contains(mod)){
				return mod;
			}
		}
		
		return null;
	}
}
