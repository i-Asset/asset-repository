package at.srfg.iasset.repository.component.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.Identifiable;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;

import at.srfg.iasset.repository.component.IAssetModel;

public class AssetModel implements IAssetModel {
	private final Identifiable root;
	
	public AssetModel(Identifiable shell) {
		this.root = shell;
	}

	@Override
	public AssetAdministrationShell getShell() {
		
		return null;
	}

	@Override
	public Identifiable getRoot() {
		return root;
	}

	@Override
	public Optional<Referable> getElement(Reference reference) {
		
		return Optional.empty();
	}

	@Override
	public Object getElementValue(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getElementValue(Reference reference) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Referable setElementValue(Reference element, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Referable setElementValue(String path, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Referable setElement(Reference parent, Referable element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Referable setElement(String path, Referable element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteElement(Referable referable) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, Object> execute(Reference reference, Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> execute(String path, Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Referable> getElement(String path) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public <T extends Referable> Optional<T> getElement(String path, Class<T> clazz) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public void setValueConsumer(String pathToProperty, Consumer<String> consumer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValueSupplier(String pathToProperty, Supplier<String> supplier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFunction(String pathToOperation, Function<Map<String, Object>, Object> function) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startEventProcessing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopEventProcessing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean setAssetAdministrationShell(String identifier, AssetAdministrationShell shell) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Submodel setSubmodel(String aasIdentifier, String submodelIdentifier, Submodel submodel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Submodel getSubmodel(String aasIdentifier, String submodelIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SubmodelElement getSubmodelElement(String aasIdentifier, Reference reference) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reference> getSubmodels(String aasIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}

}
