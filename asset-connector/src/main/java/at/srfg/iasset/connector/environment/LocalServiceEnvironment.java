package at.srfg.iasset.connector.environment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.aas4j.v3.dataformat.core.util.AasUtils;
import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.ConceptDescription;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;

import at.srfg.iasset.connector.component.impl.AASFull;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.model.custom.InstanceEnvironment;
import at.srfg.iasset.repository.model.helper.SubmodelHelper;
import at.srfg.iasset.repository.utils.ReferenceUtils;
import ch.qos.logback.core.db.dialect.MySQLDialect;

public class LocalServiceEnvironment implements ServiceEnvironment {
	InstanceEnvironment environment;
	
	public LocalServiceEnvironment() {
		environment = new InstanceEnvironment();
		// 
		environment.addAssetAdministrationShell(AASFull.AAS_1.getId(), AASFull.AAS_1);
		environment.addAssetAdministrationShell(AASFull.AAS_2.getId(), AASFull.AAS_2);
		environment.addAssetAdministrationShell(AASFull.AAS_3.getId(), AASFull.AAS_3);
		environment.addAssetAdministrationShell(AASFull.AAS_4.getId(), AASFull.AAS_4);
		environment.addSubmodel(AASFull.SUBMODEL_1.getId(), AASFull.SUBMODEL_1);
		environment.addSubmodel(AASFull.SUBMODEL_2.getId(), AASFull.SUBMODEL_2);
		environment.addSubmodel(AASFull.SUBMODEL_3.getId(), AASFull.SUBMODEL_3);
		environment.addSubmodel(AASFull.SUBMODEL_4.getId(), AASFull.SUBMODEL_4);
		environment.addSubmodel(AASFull.SUBMODEL_5.getId(), AASFull.SUBMODEL_5);
		environment.addSubmodel(AASFull.SUBMODEL_6.getId(), AASFull.SUBMODEL_6);
		environment.addSubmodel(AASFull.SUBMODEL_7.getId(), AASFull.SUBMODEL_7);
		environment.addConceptDescription(AASFull.CONCEPT_DESCRIPTION_1);
		environment.addConceptDescription(AASFull.CONCEPT_DESCRIPTION_2);
		environment.addConceptDescription(AASFull.CONCEPT_DESCRIPTION_3);
		environment.addConceptDescription(AASFull.CONCEPT_DESCRIPTION_4);
	}

	@Override
	public Optional<Submodel> getSubmodel(String identifier) {
		return Optional.empty();
	}

	@Override
	public Optional<Submodel> getSubmodel(String aasIdentifier, String submodelIdentifier) {
		return environment.getSubmodel(aasIdentifier, submodelIdentifier);
	}

	@Override
	public Optional<AssetAdministrationShell> getAssetAdministrationShell(String identifier) {
		return environment.getAssetAdministrationShell(identifier);
	}

	@Override
	public AssetAdministrationShell setAssetAdministrationShell(String aasIdentifier, AssetAdministrationShell theShell) {
		environment.addAssetAdministrationShell(aasIdentifier, theShell);
		return theShell;
	}

	@Override
	public Optional<ConceptDescription> getConceptDescription(String identifier) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public boolean deleteAssetAdministrationShellById(String identifier) {
		return environment.deleteAssetAdministrationShell(identifier);
	}

	@Override
	public boolean deleteSubmodelReference(String aasIdentifier, Reference ref) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T extends Referable> Optional<T> resolve(Reference reference, Class<T> type) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<AssetAdministrationShell> getAllAssetAdministrationShells() {
		return environment.getAssetAdministrationShells();
	}

	@Override
	public boolean deleteSubmodelElement(String aasIdentifier, String submodelIdentifier, String path) {
		Optional<Submodel> submodel = environment.getSubmodel(aasIdentifier, submodelIdentifier);
		if ( submodel.isPresent()) {
			SubmodelHelper helper = new SubmodelHelper(submodel.get());
			return helper.removeSubmodelElementAt(path);
		}
		return false;
	}

	@Override
	public SubmodelElement setSubmodelElement(String aasIdentifier, String submodelIdentifier, String idShortPath,
			SubmodelElement body) {
		Optional<Submodel> submodel = environment.getSubmodel(aasIdentifier, submodelIdentifier);
		if ( submodel.isPresent()) {
			SubmodelHelper helper = new SubmodelHelper(submodel.get());
			Submodel changed = helper.setSubmodelElementAt(idShortPath, body);
			if ( changed != null) {
				return body;
			}
		}
		return null;
	}

	@Override
	public Optional<SubmodelElement> getSubmodelElement(String aasIdentifier, String submodelIdentifier, String path) {
		Optional<Submodel> submodel = environment.getSubmodel(aasIdentifier, submodelIdentifier);
		if ( submodel.isPresent()) {
			return new SubmodelHelper(submodel.get()).getSubmodelElementAt(path);
		}
		return Optional.empty();
	}

	@Override
	public Submodel setSubmodel(String aasIdentifier, String submodelIdentifier, Submodel submodel) {
		Optional<AssetAdministrationShell> shell = environment.getAssetAdministrationShell(aasIdentifier);
		if ( shell.isPresent()) {
			AssetAdministrationShell theShell = shell.get();
			Optional<Reference> ref = ReferenceUtils.getReference(theShell.getSubmodels(), submodelIdentifier, KeyTypes.SUBMODEL);
			if (ref.isEmpty()) {
				Reference newRef = AasUtils.toReference(submodel);
				theShell.getSubmodels().add(newRef);
			}
			environment.addSubmodel(submodelIdentifier, submodel);
		}
		return null;
	}

	@Override
	public Optional<Referable> getSubmodelElement(String aasIdentifier, Reference element) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Object getElementValue(String aasIdentifier, String submodelIdentifier, String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setElementValue(String aasIdentifier, String submodelIdentifier, String path, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ConceptDescription setConceptDescription(String cdIdentifier, ConceptDescription conceptDescription) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reference> getSubmodelReferences(String aasIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reference> setSubmodelReferences(String aasIdentifier, List<Reference> submodels) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reference> deleteSubmodelReference(String id, String submodelIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SubmodelElement setSubmodelElement(String id, String submodelIdentifier, SubmodelElement element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> invokeOperation(String id, String base64Decode, String path,
			Map<String, Object> parameterMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
