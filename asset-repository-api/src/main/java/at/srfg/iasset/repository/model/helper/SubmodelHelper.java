package at.srfg.iasset.repository.model.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.eclipse.aas4j.v3.model.Entity;
import org.eclipse.aas4j.v3.model.Key;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Property;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;
import org.eclipse.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.aas4j.v3.model.SubmodelElementList;

import at.srfg.iasset.repository.utils.ReferenceUtils;


public class SubmodelHelper {
	public static final String PATH_DELIMITER = "\\.";
	private final Submodel submodel;
	
	public SubmodelHelper(Submodel submodel) {
		this.submodel = submodel;
	}
	
	public Submodel getSubmodel() {
		return submodel;
	}
	/**
	 * remove the {@link SubmodelElement} identified by a dot-separated path
	 * @param path 
	 * @return
	 */
	public boolean removeSubmodelElementAt(String path) {
		String[] pathTokens = path.split(PATH_DELIMITER);
		
		List<String> tokenList = Arrays.asList(pathTokens);
		Iterator<String> tokenIterator = tokenList.iterator();
		Referable parent = submodel;
		SubmodelElement toDelete = null;
		
		while (tokenIterator.hasNext()) {
			String token = tokenIterator.next();
			Optional<SubmodelElement> optElement = getChild(parent, token, SubmodelElement.class);
			
			if ( ! optElement.isPresent() ) {
				return false;
			}
			else {
				toDelete = optElement.get();
			}
			if (tokenIterator.hasNext()) {
				parent = optElement.get();
				
			}
		}
		return removeChild(parent, toDelete);
	}
	/**
	 * Retrieve the {@link SubmodelElement} element at at given path
	 * @param path
	 * @return
	 */
	public Optional<SubmodelElement> getSubmodelElementAt(String path) {
		String[] pathTokens = path.trim().split(PATH_DELIMITER);
		
		List<String> tokenList = Arrays.asList(pathTokens);
		Iterator<String> tokenIterator = tokenList.iterator();
		Referable parent = submodel;
		SubmodelElement element = null;
		while (tokenIterator.hasNext()) {
			
			
			String token = tokenIterator.next();
			// SubmodelElementList ...
			if ( token.contains("[")) {
				
			}
			
			Optional<SubmodelElement> optElement = getChild(parent, token, SubmodelElement.class);
			
			
			
			if ( ! optElement.isPresent() ) {
				return Optional.empty();
			}
			else {
				element = optElement.get();
			}
			if (tokenIterator.hasNext()) {
				parent = optElement.get();
				
			}
			
		}
		return Optional.ofNullable(element);
	}
	public Object getValueAt(String path) {
		Optional<SubmodelElement> elem = getSubmodelElementAt(path);
		if ( elem.isPresent()) {
			return getValueOnly(elem.get());
			
		}
		return null;
	}
	private <T extends Referable> Optional<T> getChild(Referable parent, String idShort, Class<T> type) {
		Optional<SubmodelElement> element = getChildren(parent).stream()
			.filter(new Predicate<SubmodelElement>() {
				
				@Override
				public boolean test(SubmodelElement t) {
					return idShort.equalsIgnoreCase(t.getIdShort());
				}})
			.findFirst();
		if ( element.isPresent()) {
			SubmodelElement e = element.get();
			if ( type.isInstance(e)) {
				return Optional.of(type.cast(e));
			}
			
		}
		return Optional.empty();
	}
	/**
	 * Helper me 
	 * @param parent
	 * @return
	 */
	private List<SubmodelElement> getChildren(Referable parent) {
		
		if (Submodel.class.isInstance(parent)) {
			return Submodel.class.cast(parent).getSubmodelElements();
		} else if ( SubmodelElementCollection.class.isInstance(parent)) {
			return SubmodelElementCollection.class.cast(parent).getValues();
		} else if ( SubmodelElementList.class.isInstance(parent)) {
			return SubmodelElementList.class.cast(parent).getValues();
		} else if ( Entity.class.isInstance(parent)) {
			return Entity.class.cast(parent).getStatements();
		} else {
			return new ArrayList<SubmodelElement>();
		}
		
	}
	private boolean removeChild(Referable parent, String idShort) {
		Optional<Referable> element = getChild(parent, idShort, Referable.class);
		if ( element.isPresent() ) {
			return getChildren(parent).remove(element.get());
		}
		return false;
	}
	private void addChild(Referable parent, SubmodelElement child) {
		// remove the child
		removeChild(parent, child.getIdShort());
		if (Submodel.class.isInstance(parent)) {
			Submodel.class.cast(parent).getSubmodelElements().add(child);
		} else if ( SubmodelElementCollection.class.isInstance(parent)) {
			SubmodelElementCollection.class.cast(parent).getValues().add(child);
		} else if ( SubmodelElementList.class.isInstance(parent)) {
			SubmodelElementList.class.cast(parent).getValues().add(child);
		} else if ( Entity.class.isInstance(parent)) {
			Entity.class.cast(parent).getStatements().add(child);
		} 
		
	}
	private boolean removeChild(Referable parent, SubmodelElement child) {
		return getChildren(parent).remove(child);
	}

	public Submodel setSubmodelElementAt(String idShortPath, SubmodelElement body) {
		if ( idShortPath == null ) {
			// Submodel
			removeChild(submodel, body);
			addChild(submodel, body);
		}
		else {
			// somewhere in the path
			Optional<SubmodelElement> elem = getSubmodelElementAt(idShortPath);
			if ( elem.isPresent()) {
				removeChild(elem.get(),body);
				addChild(elem.get(),body);
				
			}
			
		}
		
		return submodel;
	}

	public Optional<Referable> resolveReference(Reference element) {
		// the first entry points to the submodel
		String lastKeyValue = ReferenceUtils.lastKeyValue(element);
		String firstKeyValue = ReferenceUtils.firstKeyValue(element);
		Referable referenced = submodel;
		for (Key key : element.getKeys()) {
			if ( KeyTypes.SUBMODEL.equals(key.getType()) ) {
				if ( submodel.getId().equalsIgnoreCase(lastKeyValue)) {
					return Optional.of(submodel);
				}
				if ( submodel.getId().equalsIgnoreCase(firstKeyValue)) {
					continue;
				}
				throw new IllegalStateException("Reference is invalid!");
			}
			Optional<Referable> referable = getChild(referenced, key.getValue(), Referable.class);
			if ( referable.isPresent() ) {
				referenced = referable.get();
			}
		}
		if (referenced.getIdShort().equalsIgnoreCase(ReferenceUtils.lastKeyValue(element))) {
			return Optional.of(referenced);
		}
		return Optional.empty();
	}
	public Map<String, Object> getValueOnly(Referable referable) {
		Map<String, Object> resultMap = new HashMap<String,Object>();
		for (SubmodelElement sme : getChildren(referable)) {
			if ( Property.class.isInstance(sme)) {
				resultMap.put(sme.getIdShort(), Property.class.cast(sme).getValue());
			}
			else if ( SubmodelElementCollection.class.isInstance(sme)) {
				resultMap.put(sme.getIdShort(), getValueOnly(sme));
			}
			else if ( SubmodelElementList.class.isInstance(sme)) {
				resultMap.put(sme.getIdShort(), getValueOnly(sme));
			}
			else if ( BasicEventElement.class.isInstance(sme)) {
				
			}
		}
		
		return resultMap;
	}
	
}
