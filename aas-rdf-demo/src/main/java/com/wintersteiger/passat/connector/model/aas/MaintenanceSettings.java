package com.wintersteiger.passat.connector.model.aas;

import at.srfg.iasset.connector.component.config.Configurable;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
/**
 * Settings
 * @author dglachs
 *
 */
@Dependent
public class MaintenanceSettings {

	@Inject
	@Configurable("ski_maintenance.aas.id")
	private String aasId;


	@Inject
	@Configurable("ski_maintenance.aas.submodel.id")
	private String submodelId;

	
	@Inject
	@Configurable("ski_maintenance.aas.submodel.requestOperation.idShort")
	private String requestOperation;

	@Inject
	@Configurable("ski_maintenance.aas.submodel.resultOperation.idShort")
	private String resultOperation;

	public String getAasId() {
		return aasId;
	}

	public void setAasId(String aasId) {
		this.aasId = aasId;
	}

	public String getSubmodelId() {
		return submodelId;
	}

	public void setSubmodelId(String submodelId) {
		this.submodelId = submodelId;
	}

	public String getRequestOperation() {
		return requestOperation;
	}

	public void setRequestOperation(String requestOperation) {
		this.requestOperation = requestOperation;
	}

	public String getResultOperation() {
		return resultOperation;
	}

	public void setResultOperation(String eventName) {
		this.resultOperation = eventName;
	}

	
}
