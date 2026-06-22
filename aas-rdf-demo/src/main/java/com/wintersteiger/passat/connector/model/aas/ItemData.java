package com.wintersteiger.passat.connector.model.aas;
import java.io.File;
import java.io.FileInputStream;

import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEnvironment;

import at.srfg.iasset.connector.environment.AASEnvironment;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ItemData implements AASEnvironment {


	private Environment loadEnvironment() throws Exception {
		FileInputStream fs = new FileInputStream(new File("ItemData.json"));
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
}
