package net.sourceforge.plantuml.jsonexporter.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;


public class Entity extends Base {
	
	private String className;
	private String[] namespace;
	private String type;
	private EntityInfo inherits;
	private String stereotype;
	
	private ArrayList<EntityInfo> requires = new ArrayList<EntityInfo>();
	private ArrayList<Property> properties = new ArrayList<Property>();
	private ArrayList<Method> constructors = new ArrayList<Method>();
	private ArrayList<Method> methods = new ArrayList<Method>();
	
	public static Entity fromPlantUmlEntity(IEntity e, DotData data){
		
		Entity entity = new Entity();
		
		entity.className = findClassName(e.getCode());
		entity.namespace = findNamespace(e);
		entity.type = e.getType().name().toLowerCase();
		
		if(e.getStereotype() != null){
			entity.stereotype = e.getStereotype().getLabel();
		}
		
		List<Member> members = e.getFieldsToDisplay();
		if(members != null){
			for(Member member: members){
				entity.properties.add(Property.fromPlantUmlMember(member));
			}
		}
		
		members = e.getMethodsToDisplay();
		if(members != null){
			// search for constructors and other methods
			for(Member member: members){
				
				Method method = Method.fromPlantUmlMember(member);
				if(method.isConstructor(e.getCode())){
					entity.constructors.add(method);
				}else {
					entity.methods.add(method);
				}
			}
		}
		
		// check if entity relationship is inheritance
		IEntity iEntity = data.getInheritedEntity(e);
		if(iEntity != null){
			entity.inherits = EntityInfo.fromPlantUmlEntity(iEntity);
		}
		
		// export inheritance and requiring entities
		Set<IEntity> requiresSet = data.getAllRequiredEntities(e);
		Iterator<IEntity> requiresIt = requiresSet.iterator();
		while(requiresIt.hasNext()){
			IEntity rEntity = requiresIt.next();
			entity.requires.add(EntityInfo.fromPlantUmlEntity(rEntity));
		}
		
		return entity;
	}

	public String getClassName() {
		return className;
	}

	public String getType() {
		return type;
	}

	public EntityInfo getSuperClass() {
		return inherits;
	}

	public String getStereotype() {
		return stereotype;
	}

	public ArrayList<EntityInfo> getRequires() {
		return requires;
	}

	public ArrayList<Property> getProperties() {
		return properties;
	}

	public String[] getNamespace() {
		return namespace;
	}

	public ArrayList<Method> getConstructors() {
		return constructors;
	}

	public ArrayList<Method> getMethods() {
		return methods;
	}

}
