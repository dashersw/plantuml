package net.sourceforge.plantuml.jsonexporter;

import java.io.BufferedWriter;
import java.io.File;
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

	/**
	 * Writes the given group to the output directory.
	 *
	 * @param data
	 * @param group
	 * @return true, if write successful
	 */
	public boolean write(DotData data, Group group) {

		try {
			// check if output directory exists
			// if not, create
			
			File jsonOutputDir = new File(Options.FILES_OUPUT_DIRECTORY);
			createOutputDirectoryIfDoesntExist(jsonOutputDir);
			
			String jsonFileName = group.getDisplay() + ".json";
			
			if(group.entities() != null){	
				// Outputs metadata about class diagram in JSON
				HashMap<String, Entity> output = new HashMap<String, Entity>();
				for (Entry<String, IEntity> e : group.entities().entrySet()) {
					output.put(e.getKey(),
							Entity.fromPlantUmlEntity(e.getValue(), data));
				}

				BufferedWriter writer = new BufferedWriter(
						new FileWriter(
							jsonOutputDir.getAbsolutePath() 
							+ "/" 
							+ jsonFileName));
				
				writer.write(new Gson().toJson(output));
				writer.close();
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * Creates the output directory if doesn't exist.
	 */
	public void createOutputDirectoryIfDoesntExist(File dir){
		File outputDir = dir;
		if(!outputDir.exists()){
			outputDir.mkdir();
		}
	}
}
