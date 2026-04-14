package com.wintersteiger.passat.connector.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.function.Function;

import org.eclipse.esmf.aspectmodel.aas.AasFileFormat;
import org.eclipse.esmf.aspectmodel.aas.AasGenerationConfig;
import org.eclipse.esmf.aspectmodel.aas.AasGenerationConfigBuilder;
import org.eclipse.esmf.aspectmodel.aas.AspectModelAasGenerator;
import org.eclipse.esmf.aspectmodel.aas.DefaultPropertyMapper;
import org.eclipse.esmf.aspectmodel.loader.AspectModelLoader;
import org.eclipse.esmf.metamodel.AspectModel;

public class Generator {

	AspectModel aspectModel;
	
	public static void main(String [] args) {
		try {
			Generator g = new Generator();
			g.doTheStuff();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	void doTheStuff() throws Exception {
		DefaultPropertyMapper propertyDefault = new DefaultPropertyMapper();
		AspectModel model = new AspectModelLoader().load(new File("AAS2RDFDemo.ttl"));
		
		
		AasGenerationConfig config = AasGenerationConfigBuilder.builder()
				.format(AasFileFormat.JSON) 
				.aspectData(null)
				.propertyMappers(null)
				.build();
		new MyOwnAspectModelAasGenerator(model.aspect(), config).generate(new Function<String, OutputStream>(){

			@Override
			public OutputStream apply(String t) {
				try {
					FileOutputStream out = new FileOutputStream(new File("AAS2RDFDemo.json"));
					return out;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return System.out;
			}});
	}

}
