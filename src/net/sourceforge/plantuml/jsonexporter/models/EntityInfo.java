package net.sourceforge.plantuml.jsonexporter.models;

import net.sourceforge.plantuml.cucadiagram.IEntity;

public class EntityInfo extends Base {
	
	private String className;
	private String[] namespace;
	
	//public
	public static EntityInfo fromPlantUmlEntity(IEntity e){
		EntityInfo info = new EntityInfo();
		info.className = findClassName(e.getCode());
		info.namespace = findNamespace(e);
		
		return info;
	}

	public String getClassName() {
		return className;
	}

	public String[] getNamespace() {
		return namespace;
	}
	
}
