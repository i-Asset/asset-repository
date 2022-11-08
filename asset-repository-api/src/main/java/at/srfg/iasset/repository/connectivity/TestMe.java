package at.srfg.iasset.repository.connectivity;

import java.util.Optional;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.Submodel;

import at.srfg.iasset.repository.api.IAssetAdministrationShellInterface;
import at.srfg.iasset.repository.api.IAssetAdministrationShellRepositoryInterface;
import at.srfg.iasset.repository.api.SemanticLookupService;
import at.srfg.iasset.semantic.model.ConceptBase;

public class TestMe {

	public static void main(String[] args) {
		ConnectionProvider c = ConnectionProvider.getConnection("http://localhost:8081/");
		SemanticLookupService lookup = c.getSemanticLookupInterface();
		
		ConceptBase base= lookup.getConcept("0173-1#01-ADG629#002");
		
		ConnectionProvider local = ConnectionProvider.getConnection("http://localhost:5050/test");
		
		
		IAssetAdministrationShellRepositoryInterface conn = c.getRepositoryInterface();
		IAssetAdministrationShellInterface l = local.getShellInterface();
		l.getAssetAdministrationShell();
		
		Object o = l.getValue("https://acplt.org/Test_Submodel", "ExampleSubmodelCollectionOrdered");
		
		
		Object broker = c.getRepositoryInterface().getValue("http://iasset.salzburgresearch.at/application", "http://iasset.salzburgresearch.at/application#eventConfig", "messageBroker");
		Object broker2 = c.getSubmodelInterface().getValue("http://iasset.salzburgresearch.at/application#eventConfig", "messageBroker");
		AssetAdministrationShell theShell = conn.getAssetAdministrationShell("https://acplt.org/Test_AssetAdministrationShell");
		Submodel sub = conn.getSubmodel("https://acplt.org/Test_AssetAdministrationShell", "https://acplt.org/Test_Submodel");
		
		
	}

}
