package com.wintersteiger.passat.connector.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.function.Function;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.esmf.aspectmodel.aas.AasFileFormat;
import org.eclipse.esmf.aspectmodel.aas.AasGenerationConfig;
import org.eclipse.esmf.aspectmodel.aas.AasGenerationConfigBuilder;
import org.eclipse.esmf.aspectmodel.loader.AspectModelLoader;
import org.eclipse.esmf.metamodel.AspectModel;

public class Generator {

	AspectModel aspectModel;
	
	public static void main(String [] args) {
		try {
			Generator g = new Generator();
			g.doTheStuff("DppMetadata.ttl", "Nameplate.ttl", "ProductModel.ttl", "ItemData.ttl");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	void doTheStuff(String ... fileName) throws Exception {
		for (String f : fileName) {
			generate(new File(f));
		}
	}
	void generate(File input) {
		if ( input.exists() && input.canRead()) {
			System.out.println("Processing file: " + input.getAbsolutePath());
			AspectModel model = new AspectModelLoader().load(input);
			
			
			AasGenerationConfig config = AasGenerationConfigBuilder.builder()
					.format(AasFileFormat.JSON) 
					.aspectData(null)
					.propertyMappers(null)
					.build();
	
			File outputFile = new File(FilenameUtils.getBaseName(input.getAbsolutePath()) + ".json");
			new MyOwnAspectModelAasGenerator(model.aspect(), config).generate(new Function<String, OutputStream>(){
	
				@Override
				public OutputStream apply(String t) {
					try {
						FileOutputStream out = new FileOutputStream(outputFile);
						return out;
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return System.out;
				}});
		} else {
			System.err.println("Cannot read file: " + input.getAbsolutePath());
		}
	}
}
