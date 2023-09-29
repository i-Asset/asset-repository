package at.srfg.iasset.repository.utils;

import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;

public enum GloballyIdentifiables {
	GlobalReference(KeyTypes.GLOBAL_REFERENCE),
	AssetAdministrationShell(KeyTypes.ASSET_ADMINISTRATION_SHELL),
	ConceptDescription(KeyTypes.CONCEPT_DESCRIPTION),
	Identifiable(KeyTypes.ASSET_ADMINISTRATION_SHELL, KeyTypes.SUBMODEL, KeyTypes.CONCEPT_DESCRIPTION),
	Submodel(KeyTypes.SUBMODEL);
	;
	private KeyTypes[] types;
	private GloballyIdentifiables(KeyTypes ... types) {
		this.types = types;
	}
	
	

}
