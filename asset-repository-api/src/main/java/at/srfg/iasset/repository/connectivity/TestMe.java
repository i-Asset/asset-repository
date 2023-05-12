package at.srfg.iasset.repository.connectivity;

import java.util.Optional;

import org.eclipse.aas4j.v3.dataformat.core.util.AasUtils;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.ReferenceTypes;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;
import org.eclipse.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.aas4j.v3.model.impl.DefaultReference;

import at.srfg.iasset.repository.model.helper.visitor.SemanticLookupVisitor;

public class TestMe {

	public static void main(String[] args) {
		
		ConnectionProvider c1 = ConnectionProvider.getConnection("http://localhost:8081/");
////		c1.getShellInterface().getSubmodelElement(null, null)
//		ConnectionProvider c = ConnectionProvider.getConnection("https://192.168.48.92/");
//		SemanticLookupService lookup = c.getSemanticLookupInterface();
//		
//		ConceptBase base= lookup.getConcept("0173-1#01-ADG629#002");
//		
//		ConnectionProvider local = ConnectionProvider.getConnection("http://localhost:5050/test");
//		
//		
//		IAssetAdministrationShellRepositoryInterface conn = c.getRepositoryInterface();
//		IAssetAdministrationShellInterface l = local.getShellInterface();
//		l.getAssetAdministrationShell();
//		
//		Object o = l.getValue("https://acplt.org/Test_Submodel", "ExampleSubmodelCollectionOrdered");
		
		
//		
		Reference ref = new DefaultReference.Builder()
				.type(ReferenceTypes.MODEL_REFERENCE)
				.key(new DefaultKey.Builder().type(KeyTypes.SUBMODEL).value("http://iasset.salzburgresearch.at/application#eventConfig").build())
				.key(new DefaultKey.Builder().type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION).value("messageBroker").build())
				.build();
		Reference hostsRef = new DefaultReference.Builder()
				.type(ReferenceTypes.GLOBAL_REFERENCE)
				.key(new DefaultKey.Builder().type(KeyTypes.CONCEPT_DESCRIPTION).value("http://iasset.salzburgresearch.at/data/messageBroker/hosts").build())
				.build();
		Reference typeRef = new DefaultReference.Builder()
				.type(ReferenceTypes.GLOBAL_REFERENCE)
				.key(new DefaultKey.Builder().type(KeyTypes.CONCEPT_DESCRIPTION).value("http://iasset.salzburgresearch.at/data/messageBroker/brokerType").build())
				.build();
		// obtain requested submodel (taken from config??) 
		Submodel config = c1.getSubmodelInterface().getSubmodel("http://iasset.salzburgresearch.at/application#eventConfig");
		if (config != null) {
			Optional<SubmodelElement> elem1 = new SemanticLookupVisitor(config).findElement(ref); 
			if ( elem1.isPresent() && SubmodelElementCollection.class.isInstance(elem1.get())) {
				SubmodelElementCollection coll = (SubmodelElementCollection)elem1.get();
				Optional<SubmodelElement> hostsElement = new SemanticLookupVisitor(coll).findElement(hostsRef);
				
				Optional<SubmodelElement> typeElement = new SemanticLookupVisitor(coll).findElement(typeRef);
				
			}
		}
		
//		
//				
//		Object broker = c.getRepositoryInterface().getValue("http://iasset.salzburgresearch.at/application", "http://iasset.salzburgresearch.at/application#eventConfig", "messageBroker");
//		Object broker2 = c.getSubmodelInterface().getValue("http://iasset.salzburgresearch.at/application#eventConfig", "messageBroker");
//		AssetAdministrationShell theShell = conn.getAssetAdministrationShell("https://acplt.org/Test_AssetAdministrationShell");
//		Submodel sub = conn.getSubmodel("https://acplt.org/Test_AssetAdministrationShell", "https://acplt.org/Test_Submodel");
		
		
	}

}
