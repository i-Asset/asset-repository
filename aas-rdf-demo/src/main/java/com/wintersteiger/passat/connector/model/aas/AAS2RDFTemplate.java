package com.wintersteiger.passat.connector.model.aas;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.eclipse.digitaltwin.aas4j.v3.dataformat.xml.XmlDeserializer;
import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEnvironment;
import org.eclipse.esmf.samm.KnownVersion;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import com.fasterxml.jackson.databind.JsonDeserializer;

import at.srfg.iasset.connector.environment.AASEnvironment;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AAS2RDFTemplate implements AASEnvironment {

//	@Resource(lookup = "/samm/meta-model/2.2.0/aspect-meta-model-definitions.ttl")
//	private InputStream samm;
	


	private Environment loadEnvironment() throws Exception {
		FileInputStream fs = new FileInputStream(new File("AAS2RDFDemo.json"));
		Environment env = new org.eclipse.digitaltwin.aas4j.v3.dataformat.json.JsonDeserializer().read(fs, Environment.class); 
//		Environment env = new XmlDeserializer().read(fs);
		
		return env;
	}
	@Override
	public Environment getAASData() {
		try {
			return loadEnvironment();
		} catch (Exception e) {
			e.printStackTrace();
			return new DefaultEnvironment.Builder().build();
		}
	}
	
//	@Override
//	public Model getRDFData() {
//		try {
//			Model model = loadSAMM();
//			model.addAll(loadMaintenanceDefinition());
//			return model;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			return new TreeModel();
//		}
//	}
//	private Model loadMaintenanceDefinition() throws Exception {
//		FileInputStream fs = new FileInputStream(new File("SkiMaintenance.ttl"));
//		Model maintenance = Rio.parse(fs, RDFFormat.TURTLE);
//		return maintenance;
//	}
//	private Model loadSAMM() throws Exception {
//		if ( samm == null) {
//			ClassLoader loader = KnownVersion.SAMM_2_2_0.getClass().getClassLoader();
//			InputStream is = loader.getResourceAsStream("samm/meta-model/2.0.0/aspect-meta-model-definitions.ttl");
//			Model maintenance = Rio.parse(is, RDFFormat.TURTLE);
//			return maintenance;
//			
//		}
//		return new TreeModel();
//	}
}
