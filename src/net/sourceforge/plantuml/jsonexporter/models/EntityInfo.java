package net.sourceforge.plantuml.jsonexporter.models;

import net.sourceforge.plantuml.cucadiagram.IEntity;

public class EntityInfo extends Base {
	
	private String className;
	private String[] namespace;
	private String fullName;
	
	//public
	public static EntityInfo fromPlantUmlEntity(IEntity e){
		EntityInfo info = new EntityInfo();
		info.className = findClassName(e.getCode());
		info.namespace = findNamespace(e);
		info.fullName = toFullName(info.className, info.namespace);

		return info;
	}

	public String getClassName() {
		return className;
	}

	public String[] getNamespace() {
		return namespace;
	}
	
	public String getFullName() {
		return fullName;
	}
	
}
