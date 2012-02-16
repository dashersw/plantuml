package net.sourceforge.plantuml.jsonexporter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map.Entry;

import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.jsonexporter.models.Entity;

import com.google.gson.Gson;

public class Exporter {
	
	private static Exporter instance;

	private Exporter() {
	}
	
	public static Exporter getInstance(){
		if(instance == null){
			instance = new Exporter();
		}
		return instance;
	}

	public boolean write(DotData data, Group group) {

		try {
			String jsonFileName = group.getDisplay().replaceAll(" ", "-") + ".json";
			
			if(group.entities() != null){	
				// Outputs metadata about class diagram in JSON
				HashMap<String, Entity> output = new HashMap<String, Entity>();
				for (Entry<String, IEntity> e : group.entities().entrySet()) {
					output.put(e.getKey(),
							Entity.fromPlantUmlEntity(e.getValue(), data));
				}

				BufferedWriter writer = new BufferedWriter(
						new FileWriter("json/" + jsonFileName));
				writer.write(new Gson().toJson(output));
				writer.close();
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	final public static String KEY_ARRAY = "array";
	final public static String[] OUTPUT_VISIBILITIES = 
			new String[]{"public", "private", "protected", "package"};
	final public static String OUTPUT_ARRAY_PREFIX = "";
	final public static String OUTPUT_ARRAY_POSTFIX = "[]";

}
