package at.srfg.iasset.repository.utils;

import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.aas4j.v3.dataformat.core.util.AasUtils;
import org.eclipse.aas4j.v3.model.Identifiable;
import org.eclipse.aas4j.v3.model.Key;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.ReferenceTypes;
import org.eclipse.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.aas4j.v3.model.impl.DefaultReference;

import at.srfg.iasset.repository.config.AASModelHelper;

public class ReferenceUtils {
	public static Key firstKey(Reference ref) {
		if (ref == null || ref.getKeys() == null || ref.getKeys().isEmpty()) {
			return null;
		}
		return ref.getKeys().get(0);
	}
	public static KeyTypes firstKeyType(Reference ref) {
		if (ref == null || ref.getKeys() == null || ref.getKeys().isEmpty()) {
			return null;
		}
		return ref.getKeys().get(0).getType();
	}
	public static String firstKeyValue(Reference ref) {
		if (ref == null || ref.getKeys() == null || ref.getKeys().isEmpty()) {
			return null;
		}
		return ref.getKeys().get(0).getValue();
	}
	public static KeyTypes lastKeyType(Reference ref) {
		if (ref == null || ref.getKeys() == null || ref.getKeys().isEmpty()) {
			return null;
		}
		int lastElement = ref.getKeys().size() - 1;
		return ref.getKeys().get(lastElement).getType();
	}
	public static String lastKeyValue(Reference ref) {
		if (ref == null || ref.getKeys() == null || ref.getKeys().isEmpty()) {
			return null;
		}
		int lastElement = ref.getKeys().size() - 1;
		return ref.getKeys().get(lastElement).getValue();
	}
	
	public static Reference asGlobalReference(KeyTypes type, String identifier) {
		return new DefaultReference.Builder()
				.key(new DefaultKey.Builder().type(type).value(identifier).build())
				.type(ReferenceTypes.GLOBAL_REFERENCE)
				.build();
			
	}
	public static Reference fromIdentifiable(Identifiable identifiable) {
		return AasUtils.toReference(identifiable);
	}
	/**
	 * Search for the 
	 * @param references
	 * @param value
	 * @param type
	 * @return
	 */
	public static Optional<Reference> extractReferenceFromList(List<Reference> references, String value, KeyTypes type) {
		return references.stream().filter(new Predicate<Reference>() {

			@Override
			public boolean test(Reference t) {
				// check for the last key
				return type.equals(lastKeyType(t)) && value.equals(lastKeyValue(t));
			}
		}
		).findAny();
		
	}
	
    /**
     * Gets a Java interface representing the type provided by key.
     *
     * @param key The KeyElements type
     * @return a Java interface representing the provided KeyElements type or
     * null if no matching Class/interface could be found. It also returns
     * abstract types like {@link KeyTypes#SUBMODEL_ELEMENT} or {@link KeyTypes#DATA_ELEMENT}
     */
    public static Class<?> keyTypeToClass(KeyTypes key) {
        return Stream.concat(AASModelHelper.INTERFACES.stream(), AASModelHelper.INTERFACES_WITHOUT_DEFAULT_IMPLEMENTATION.stream())
                .filter(x -> x.getSimpleName().equals(key.toString()))
                .findAny()
                .orElse(null);
    }
    /**
     * Gets the KeyElements type matching the provided Referable
     *
     * @param referable The referable to convert to KeyElements type
     * @return the most specific KeyElements type representing the Referable,
     * i.e. abstract types like SUBMODEL_ELEMENT or DATA_ELEMENT are never
     * returned; null if there is no corresponding KeyElements type
     */
    public static KeyTypes referableToKeyType(Referable referable) {
        Class<?> aasInterface = AASModelHelper.getAasInterface(referable.getClass());
        if (aasInterface != null) {
            return KeyTypes.fromValue(aasInterface.getSimpleName());
        }
        return null;
    }
	public static String idShortPath(Reference broker) {
		ListIterator<Key> iterator = broker.getKeys().listIterator(1);
		StringBuffer path = new StringBuffer(); 
		while (iterator.hasNext()) {
			path.append(iterator.next().getValue());
			if ( iterator.hasNext()) {
				path.append(".");
			}
		}
		return path.toString();
	}

}
