package net.sourceforge.plantuml.jsonexporter.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.jsonexporter.Options;
import net.sourceforge.plantuml.jsonexporter.Utils;
import net.sourceforge.plantuml.skin.VisibilityModifier;

public class Base {
	
	/**
	 * Exchanges the VisibilityModifier to public,
	 * private, protected or package.
	 *
	 * @param visibilityModifier the visibility modifier
	 * @return the string
	 */
	public static String toVisibility(VisibilityModifier visibilityModifier){
		
		if(visibilityModifier == null) { return null; }
		
		for(String mod: Options.OUTPUT_VISIBILITIES){
			if(visibilityModifier
				.name().toLowerCase().contains(mod)){
				return mod;
			}
		}
		
		return null;
	}
	
	public static String toFullName(String className, String[] namespace){
		if(namespace != null){
			List<String> parts = new ArrayList<String>(Arrays.asList(namespace));
			parts.add(className);
			return Utils.join(parts, ".");
		}
		return className;
	}
	
	public static String findClassName(String fullName){
		String[] parts = fullName.split("\\.");
		return parts[parts.length - 1];
	}
	
	public static String[] findNamespace(IEntity e){
		
		Group group = e.getParent();
		ArrayList<String> list = new ArrayList<String>();
		
		if(group != null){
			do {
				list.add(group.getCode() + ".");
			} while((group = group.getParent()) != null);
		}
		
		// the list is populated reversely
		// but representation should be from top to bottom
		Collections.reverse(list);
		
		StringBuilder builder = new StringBuilder();
		for(String name: list){
			builder.append(name);
		}
		
		return builder.toString().split("\\.");
	}
}
