package at.srfg.iasset.repository.connectivity;

import java.util.function.Consumer;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.LangString;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;

import at.srfg.iasset.repository.api.IAssetAdministrationShellRepositoryInterface;

public class TestMe {

	public static void main(String[] args) {
		ConnectionProvider c = ConnectionProvider.getConnection("http://localhost:8080");
		
		IAssetAdministrationShellRepositoryInterface conn = c.getRepositoryInterface();
		
		
		AssetAdministrationShell theShell = conn.getAssetAdministrationShell("https://acplt.org/Test_AssetAdministrationShell");
		Submodel sub = conn.getSubmodel("https://acplt.org/Test_AssetAdministrationShell", "https://acplt.org/Test_Submodel");
		
		if ( theShell != null ) {
			for (Reference ref : theShell.getSubmodels()) {
				System.out.println( ref.getKeys().get(0).getValue());
				Referable submodel = conn.getSubmodel(theShell.getId(), ref.getKeys().get(0).getValue());
				if ( Submodel.class.isInstance(submodel)) {
					Submodel.class.cast(submodel).getDescriptions().forEach(new Consumer<LangString>() {

						@Override
						public void accept(LangString t) {
							System.out.println(t.getLanguage() + " " + t.getText());
							
						}
					});
				}
			}
		}
		
		if ( sub != null ) {
			for (SubmodelElement elem : sub.getSubmodelElements()) {
				System.out.println(elem.getIdShort());
				System.out.println(conn.getValue(theShell.getId(), sub.getId(), elem.getIdShort()));
			}
			
		}
		
	}

}
