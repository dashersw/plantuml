package net.sourceforge.plantuml.jsonexporter.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parameter extends Base {
	
	private String paramName;
	private Type type;
	
	public Parameter(String paramSignature) {
		
		paramName = paramSignature;
		
		Pattern parser = Pattern.compile("\\s*(\\w+)\\s*(:\\s*([\\w|\\.]+(\\[\\])?))?");
		Matcher m = parser.matcher(paramSignature);
		
		if(m.matches()){
			paramName = m.group(1);
			String typeSignature = (m.group(3));
			if(typeSignature != null){
				type = new Type(typeSignature);
			}
		}
	}

	public String getParamName() {
		return paramName;
	}

	public Type getType() {
		return type;
	}

}
