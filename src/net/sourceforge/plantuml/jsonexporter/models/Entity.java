package net.sourceforge.plantuml.jsonexporter.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;


public class Entity extends Base {
	
	private String className;
	private String type;
	private String superClass;
	private String stereotype;
	
	private ArrayList<String> requires = new ArrayList<String>();
	private ArrayList<Property> properties = new ArrayList<Property>();
	private ArrayList<Method> constructors = new ArrayList<Method>();
	private ArrayList<Method> methods = new ArrayList<Method>();
	
	public static Entity fromPlantUmlEntity(IEntity e, DotData data){
		
		Entity entity = new Entity();
		entity.className = e.getCode();
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
			entity.superClass = iEntity.getCode();
		}
		
		// export inheritance and requiring entities
		Iterator<IEntity> relationsIt =  data.getAllLinkedDirectedTo(e).iterator();
		while(relationsIt.hasNext()){
			IEntity rEntity = relationsIt.next();
			entity.requires.add(rEntity.getCode());
		}
		
		return entity;
	}

	public String getClassName() {
		return className;
	}

	public String getType() {
		return type;
	}

	public String getSuperClass() {
		return superClass;
	}

	public String getStereotype() {
		return stereotype;
	}

	public ArrayList<String> getRequires() {
		return requires;
	}

	public ArrayList<Property> getProperties() {
		return properties;
	}

	public ArrayList<Method> getConstructors() {
		return constructors;
	}

	public ArrayList<Method> getMethods() {
		return methods;
	}
	
}
