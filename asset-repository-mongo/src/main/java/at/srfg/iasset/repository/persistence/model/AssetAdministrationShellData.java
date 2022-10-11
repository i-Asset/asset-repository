package at.srfg.iasset.repository.persistence.model;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;

public class AssetAdministrationShellData extends Document<AssetAdministrationShell> {
	public AssetAdministrationShellData() {
		
	}
	public AssetAdministrationShellData(AssetAdministrationShell data) {
		setData(data);
	}
}
