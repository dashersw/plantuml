package net.sourceforge.plantuml.jsonexporter.models;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.cucadiagram.Member;

public class Method extends Base {
	private String methodName;
	private ArrayList<Parameter> parameters = new ArrayList<Parameter>();
	private String returns;
	
	public static Method fromPlantUmlMember(Member member){
		
		Method method = new Method();
		
		String fullName = member.getDisplayWithoutVisibilityChar();
		
		// split signature and return type
		String[] parts = fullName.split(":");
		
		// find method name and params list
		Pattern p = Pattern.compile("(\\w+)\\s*\\((.*)\\)");
		Matcher matcher = p.matcher(parts[0].trim());
		
		if(matcher.matches()){
			
			method.methodName = matcher.group(1).trim();
			
			// if method has params
			if(matcher.groupCount() >= 2){
				
				// split param names
				String[] paramParts = matcher.group(2).split(",");
				
				for(String param: paramParts){
					String paramName = param.trim();
					if(paramName!= null 
							&& !paramName.equals("")){
						method.parameters.add(
							new Parameter(method.toArrayType(paramName))
						);
					}
				}
			}
		}
		
		// if schema includes a return type
		if(parts.length > 1){
			method.returns = method.toArrayType(parts[1].trim());
		}
		
		return method;
	}
}
