package net.sourceforge.plantuml.jsonexporter.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;


public class Entity extends Base {
	
	private String className;
	private String type;
	private String superClass;
	private ArrayList<String> requires = new ArrayList<String>();
	
	private ArrayList<Property> properties = new ArrayList<Property>();
	private ArrayList<Method> methods = new ArrayList<Method>();
	
	public static Entity fromPlantUmlEntity(IEntity e, DotData data){
		Entity entity = new Entity();
		entity.className = e.getCode();
		entity.type = e.getType().name().toLowerCase();
		
		List<Member> members = e.getFieldsToDisplay();
		if(members != null){
			for(Member member: members){
				entity.properties.add(Property.fromPlantUmlMember(member));
			}
		}
		
		members = e.getMethodsToDisplay();
		if(members != null){
			for(Member member: members){
				entity.methods.add(Method.fromPlantUmlMember(member));
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
}
