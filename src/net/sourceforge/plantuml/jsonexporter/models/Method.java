package net.sourceforge.plantuml.jsonexporter.models;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.cucadiagram.Member;

public class Method extends Base {
	
	private String methodName;
	private String visibility;
	private ArrayList<Parameter> parameters = new ArrayList<Parameter>();
	private String returns;
	
	public static Method fromPlantUmlMember(Member member){
		
		Method method = new Method();
		
		String fullName = member.getDisplayWithoutVisibilityChar().trim();
		
		// parses method name and return type
		Pattern outputTypeSearchPattern = Pattern.compile("(\\w+)\\s*\\([^\\)]*\\)\\s*(:\\s*(\\w+))?");
		Matcher m = outputTypeSearchPattern.matcher(fullName);
		
		while(m.find()){
			method.methodName = m.group(1);
			method.returns = m.group(3);
		}
		
		
		// parses method parameters
		Pattern paramsPattern = Pattern.compile("\\(([^\\)]*)\\)");
		m = paramsPattern.matcher(fullName);
		
		if(m.find()){
			String paramSignature = m.group(1);
			String[] params = paramSignature.split(",");
			
			for(String param: params){
				if(!param.equals("")){
					method.parameters.add(new Parameter(param));
				}
			}
		}
		
		// assign visibility
		if(member.getVisibilityModifier() != null){
			method.visibility = toVisibility(member.getVisibilityModifier());;
		}
		
		return method;
	}
	
	public boolean isConstructor(String entityCode){
		
		if(getMethodName() == null || entityCode == null){
			return false;
		}
		
		int index = entityCode.indexOf(getMethodName());
		return index == 0
				|| (index > 0 && entityCode.charAt(index-1) == '.');
	}

	public String getMethodName() {
		return methodName;
	}

	public String getVisibility() {
		return visibility;
	}

	public ArrayList<Parameter> getParameters() {
		return parameters;
	}

	public String getReturns() {
		return returns;
	}
	
}
