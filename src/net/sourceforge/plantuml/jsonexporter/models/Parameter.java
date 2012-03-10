package net.sourceforge.plantuml.jsonexporter.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parameter extends Base {
	
	private String paramName;
	private String type;
	
	public Parameter(String paramSignature) {
		
		paramName = paramSignature;
		
		Pattern parser = Pattern.compile("(\\w+)(\\s*:\\s*(\\w+))?");
		Matcher m = parser.matcher(paramSignature);
		
		if(m.matches()){
			paramName = m.group(1);
			type = toArrayType((m.group(3)));
		}
		
	}

	public String getParamName() {
		return paramName;
	}

	public String getType() {
		return type;
	}

}
