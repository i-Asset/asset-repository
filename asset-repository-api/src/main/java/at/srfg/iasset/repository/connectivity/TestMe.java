package at.srfg.iasset.repository.connectivity;

import java.util.function.Consumer;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.LangString;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;

import at.srfg.iasset.repository.api.IAssetAdministrationShellInterface;
import at.srfg.iasset.repository.api.IAssetAdministrationShellRepositoryInterface;

public class TestMe {

	public static void main(String[] args) {
		ConnectionProvider c = ConnectionProvider.getConnection("http://localhost:8081/");
		ConnectionProvider local = ConnectionProvider.getConnection("http://localhost:5050/test");
		
		
		IAssetAdministrationShellRepositoryInterface conn = c.getRepositoryInterface();
		IAssetAdministrationShellInterface l = local.getShellInterface();
		l.getAssetAdministrationShell();
		
		Object o = l.getValue("https://acplt.org/Test_Submodel", "ExampleSubmodelCollectionOrdered");
		
		Object broker = c.getRepositoryInterface().getValue("http://iasset.salzburgresearch.at/application", "http://iasset.salzburgresearch.at/application#eventConfig", "messageBroker");
		
		AssetAdministrationShell theShell = conn.getAssetAdministrationShell("https://acplt.org/Test_AssetAdministrationShell");
		Submodel sub = conn.getSubmodel("https://acplt.org/Test_AssetAdministrationShell", "https://acplt.org/Test_Submodel");
		
		
	}

}
