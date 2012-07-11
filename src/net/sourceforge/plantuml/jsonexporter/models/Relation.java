package net.sourceforge.plantuml.jsonexporter.models;

import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;

public class Relation extends Base {
	
	private String className;
	private String[] namespace;
	private String fullName;
	private String label;
	private String type;
	private String name;
	private String mappedBy;
	private String cascade;

	//public
	public static Relation fromPlantUmlEntity(Link link, int direction){
		Relation info = new Relation();

		IEntity e = link.getEntity1();
		String left = link.getQualifier2();
		String right = link.getQualifier1();
		if (direction == 2) {
			e = link.getEntity2();
			left = link.getQualifier1();
			right = link.getQualifier2();
		}

		left = left == null ? "One" : left.equals("1") ? "One" : left.equals("*") ? "Many" : left;
		right = right == null ? "One" : right.equals("1") ? "One" : right.equals("*") ? "Many" : right;

		info.name = left + "To" + right;
		if (info.name.equals("ManyToOne")) {
        	info.mappedBy = "inversedBy";
        } else {
			info.mappedBy = "mappedBy";
        }

		if (info.name.equals("OneToMany")) {
			info.cascade = "all";
		}

		info.className = findClassName(e.getCode());
		info.namespace = findNamespace(e);
		info.fullName = toFullName(info.className, info.namespace);

		info.label = link.getLabel();
		if (link.getType().getDecor1() != LinkDecor.NONE) {
			info.type = link.getType().getDecor1().name();
		} else if (link.getType().getDecor2() != LinkDecor.NONE) {
			info.type = link.getType().getDecor2().name();
		}

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

	public String getLabel() {
		return label;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getMappedBy() {
		return mappedBy;
	}

	public String getCascade() {
		return cascade;
	}
}
