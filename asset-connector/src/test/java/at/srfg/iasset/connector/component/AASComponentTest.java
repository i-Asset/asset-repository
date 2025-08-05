package at.srfg.iasset.connector.component;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.Referable;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.connector.component.impl.AASFull;
import at.srfg.iasset.repository.api.IAssetAdministrationShellRepositoryInterface;
import at.srfg.iasset.repository.connectivity.rest.ClientFactory;
import at.srfg.iasset.repository.connectivity.rest.ConsumerFactory;
import at.srfg.iasset.repository.exception.ShellNotFoundException;

public class AASComponentTest {
	
	private static AASComponent aas;
	private static IAssetAdministrationShellRepositoryInterface client;
	
	@BeforeClass
	public static void setup() {
		aas = AASComponent.create();
		aas.add(AASFull.AAS_BELT_TEMPLATE);
		aas.startEndpoint();
		
		
		client = ConsumerFactory.createConsumer(
				aas.getEndpoint().getServiceAddress().toString(),
				ClientFactory.getInstance().getClient(),
				IAssetAdministrationShellRepositoryInterface.class);
			
	}
	@Test
	public void testDataLoaded() {
		List<AssetAdministrationShell> shells = client.getAssetAdministrationShells();
		AssetAdministrationShell shell = shells.get(0);
		try {
			aas.add(shell.getId(), AASFull.SUBMODEL_BELT_EVENT_TEMPLATE);
			aas.add(shell.getId(), AASFull.SUBMODEL_BELT_OPERATIONS_TEMPLATE);
			aas.add(shell.getId(), AASFull.SUBMODEL_BELT_PROPERTIES_TEMPLATE);
			
		} catch (ShellNotFoundException snf ) {
			
		}
		assertTrue(shells.size() == 1);
		List<Reference> submodels = client.getSubmodels(shell.getId());
		assertTrue(submodels.size() == 3 );
		Submodel submodel = client.getSubmodel(shell.getId(), AASFull.SUBMODEL_BELT_OPERATIONS_TEMPLATE.getId());
		assertTrue(submodel != null && submodel.getId().equals(AASFull.SUBMODEL_BELT_OPERATIONS_TEMPLATE.getId()));
		Referable data = client.getSubmodelElement(shell.getId(), AASFull.SUBMODEL_BELT_PROPERTIES_TEMPLATE.getId(), "beltData");
		assertNotNull(data);
		
		
//		aas.get
	}
	
	@Test
	public void testObjectMapper() throws Exception {
		ObjectMapper mapper = ClientFactory.getObjectMapper();
		String aas = mapper.writeValueAsString(AASFull.SUBMODEL_BELT_PROPERTIES_INSTANCE);
		System.out.println(aas);
		aas = mapper.writeValueAsString(AASFull.SUBMODEL_BELT_PROPERTIES_TEMPLATE);
		System.out.println(aas);
		
	}
	@AfterClass
	public static void shutDown() {
		aas.stopEndpoint();
		AASComponent.close();
	}

}
