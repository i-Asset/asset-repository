package at.srfg.iasset.connector.environment;

import static org.junit.jupiter.api.Assertions.fail;

import java.net.URI;

import org.junit.jupiter.api.Test;

class LocalServiceEnvironmentTester {

	
	@Test
	void testObjectValidation() {
		LocalServiceEnvironment local = new LocalServiceEnvironment(URI.create("http://localhost:8081/"));
		// search for the submodel holding the SIP
		// this model is searched with the semanticId of the SIP!
		// 
//		local.getSubmodelInstance()
	}

}
