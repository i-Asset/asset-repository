package at.srfg.iasset.connector.environment;

import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;

public interface AASEnvironment {
	
	public Environment getAASData();
	/**
	 * allow for the injection of arbitrary meta model data
	 * @return
	 */
	default public Model getRDFData() {
		return new TreeModel();
	}

}
