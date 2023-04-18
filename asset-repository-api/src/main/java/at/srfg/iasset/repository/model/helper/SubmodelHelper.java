package at.srfg.iasset.repository.model.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.eclipse.aas4j.v3.model.Entity;
import org.eclipse.aas4j.v3.model.Key;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;
import org.eclipse.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.aas4j.v3.model.SubmodelElementList;

import com.fasterxml.jackson.databind.JsonNode;

import at.srfg.iasset.repository.model.helper.value.SubmodelElementValue;
import at.srfg.iasset.repository.utils.ReferenceUtils;

/**
 * Support Class for manipulating, traversing {@link Submodel} and contained {@link SubmodelElement}.
 * 
 * TODO: check for implementing as a service with static methods
 * @author dglachs
 *
 */
public class SubmodelHelper {
	
	private final Submodel submodel;
//	private Optional<ModelEventProvider> eventProvider;
	
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
	public Optional<SubmodelElement> removeSubmodelElementAt(String path) {
		Iterator<String> tokenIterator = new Path(path).iterator();
		Referable parent = submodel;
		SubmodelElement toDelete = null;
		
		while (tokenIterator.hasNext()) {
			String token = tokenIterator.next();
			Optional<SubmodelElement> optElement = getChild(parent, token, SubmodelElement.class);
			
			if ( ! optElement.isPresent() ) {
				// not deleted!
				return Optional.empty();
			}
			else {
				toDelete = optElement.get();
			}
			if (tokenIterator.hasNext()) {
				parent = optElement.get();
				
			}
		}
		if ( removeChild(parent, toDelete)) {
			// check for data elements and event elements		
			return Optional.of(toDelete);
		}
		return Optional.empty();
	}
	/**
	 * Retrieve the {@link SubmodelElement} element at at given path
	 * @param path
	 * @return
	 */
	public Optional<SubmodelElement> getSubmodelElementAt(String path) {
		Path thePath = new Path(path);
		//
		Iterator<String> tokenIterator = thePath.iterator();
		Referable parent = submodel;
		SubmodelElement element = null;
		while (tokenIterator.hasNext()) {
			
			
			String token = tokenIterator.next();
			
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
	public Reference getReference(String path) {
		Reference modelRef = ReferenceUtils.toReference(submodel);
		Path thePath = new Path(path);
		Iterator<String> tokenIterator = thePath.iterator();
		Referable parent = submodel;
		SubmodelElement element = null;
		while (tokenIterator.hasNext()) {
			
			
			String token = tokenIterator.next();
			
			Optional<SubmodelElement> optElement = getChild(parent, token, SubmodelElement.class);
			
			
			
			if ( ! optElement.isPresent() ) {
				throw new IllegalArgumentException(String.format("Provided path %s is not valid", path));
			}
			else {
				element = optElement.get();
				modelRef = ReferenceUtils.toReference(modelRef, element);
			}
			if (tokenIterator.hasNext()) {
				parent = optElement.get();
				
			}
			
		}
		return modelRef;
	}
	public Optional<SubmodelElement> getSubmodelElementAt(List<Key> keys) {
		Iterator<Key> keyIterator = keys.iterator();
		Referable element = submodel;
		while ( keyIterator.hasNext() ) {
			Key key = keyIterator.next();
			Class<?> keyClass = ReferenceUtils.keyTypeToClass(key.getType());
			Optional<SubmodelElement> child = getChild(submodel, key.getValue(), SubmodelElement.class);
			if ( child.isPresent() && keyClass.isInstance(child.get())) {
				element = child.get(); 
			}
			else {
				return Optional.empty();
			}
		}
		if (SubmodelElement.class.isInstance(element) ) {
			return Optional.of(SubmodelElement.class.cast(element));
		}
		return Optional.empty();
	}
	public <T extends SubmodelElement> Optional<T> getSubmodelElementAt(String path, Class<T> clazz) {
		Optional<SubmodelElement> elem = getSubmodelElementAt(path);
		if (elem.isPresent() && clazz.isInstance(elem.get())) {
			return Optional.of(clazz.cast(elem.get()));
		}
		return Optional.empty();
	}
	/**
	 * Obtain the value-only object for a {@link SubmodelElement}. 
	 * @param path The path pointing to the element
	 * @return
	 */
	public Object getValueAt(String path) {
		
		Optional<SubmodelElement> elem = getSubmodelElementAt(path);
		if ( elem.isPresent()) {
			return getValueOnly(elem.get());
			
		}
		return new HashMap<String, Object>();
	}
	/**
	 * Update the value of a {@link SubmodelElement}
	 * @param path The path pointing to the element
	 * @param value The value
	 * @return
	 */
	public Optional<SubmodelElement> setValueAt(String path, JsonNode value) {
		Optional<SubmodelElement> elem = getSubmodelElementAt(path);
		if ( elem.isPresent()) {
			setValueOnly(elem.get(), value);
			
		}
		return elem;
	}
	private void setValueOnly(SubmodelElement submodelElement, JsonNode value) {
		ValueHelper.applyValue(submodelElement, value);
		
	}
	/**
	 * Access a child element based on it's idShort
	 * @param <T>
	 * @param parent
	 * @param idShort
	 * @param type
	 * @return
	 */
	private <T extends Referable> Optional<T> getChild(Referable parent, String idShort, Class<T> type) {
		if ( SubmodelElementList.class.isInstance(parent)) {
			List<SubmodelElement> children = getChildren(parent);
			int index = Integer.valueOf(idShort);
			if ( children.size()>index) {
				SubmodelElement elem = children.get(index);
				if (type.isInstance(elem)) {
					return Optional.of(type.cast(elem));
				}
			}
			
		}
		else {
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

	public Optional<SubmodelElement> setSubmodelElementAt(String idShortPath, SubmodelElement body) {
		Path path = new Path(idShortPath);
		//
		body.setIdShort(path.getLast());
		Iterator<String> tokenIterator = path.iterator();
		Referable parent = submodel;
		SubmodelElement element = null;
		while (tokenIterator.hasNext()) {
			
			
			String token = tokenIterator.next();
			if ( path.isLast(token)) {
				addChild(parent, body);
				return Optional.of(body);
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
	public SubmodelElementValue getValueOnly(SubmodelElement referable) {
		return ValueHelper.toValue(referable);
	}
	public static <T extends SubmodelElement>  T resolveElement(Referable container, Reference semanticId, Class<T> clazz) {
		if (Submodel.class.isInstance(container)) {
			
		}
		return null;
	}
	public Optional<Referable> resolveKeyPath(Iterator<Key> keyIterator ) {
		return resolveKeyPath(submodel, keyIterator);
		
			
		
	}
	public Object resolveValue(Iterator<Key> keyIterator) {
		return resolveValue(submodel, keyIterator);
	}
	private Optional<Referable> resolveKeyPath(Referable container, Iterator<Key> iterator  ) {
		if ( iterator.hasNext()) {
			Key elementKey = iterator.next();
			
			Optional<Referable>  element = getChild(container, elementKey.getValue(), Referable.class);
			if ( element.isPresent() && iterator.hasNext()) {
				return resolveKeyPath(element.get(), iterator);
			}
			return element;
			
		}
		return Optional.empty();
	}
	private Object resolveValue(Referable container, Iterator<Key> iterator) {
		Key elementKey = iterator.next();
		
		Optional<SubmodelElement>  element = getChild(container, elementKey.getValue(), SubmodelElement.class);
		if ( element.isPresent() && iterator.hasNext()) {
			return resolveKeyPath(element.get(), iterator);
		}
		if ( element.isPresent()) {
			return getValueOnly(element.get());
		}
		return null;
	}
}
