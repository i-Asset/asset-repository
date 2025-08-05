package at.srfg.iasset.repository.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.digitaltwin.aas4j.v3.dataformat.core.internal.deserialization.EnumDeserializer;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.core.util.AasUtils;
import org.eclipse.digitaltwin.aas4j.v3.model.Identifiable;
import org.eclipse.digitaltwin.aas4j.v3.model.Key;
import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.Referable;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.eclipse.digitaltwin.aas4j.v3.model.ReferenceTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultReference;

import at.srfg.iasset.repository.config.AASModelHelper;

public class ReferenceUtils {
	public static Key[] keys(Reference ref) {
		if (ref == null || ref.getKeys() == null || ref.getKeys().isEmpty()) {
			return null;
		}
		Key[] keys = new Key[ref.getKeys().size()];
		ref.getKeys().toArray(keys);
		return keys;
		 
	}
	public static Iterator<Key> keyIterator(Reference ref) {
		if (ref == null || ref.getKeys() == null || ref.getKeys().isEmpty()) {
			return null;
		}
		return ref.getKeys().iterator();
	}
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
	public static Reference[] asGlobalReferences(String ... values) {
		ArrayList<Reference> ref = new ArrayList<>();
		if ( values != null ) {
			for (String value : values) {
				ref.add(asGlobalReference(KeyTypes.GLOBAL_REFERENCE, value));
			}
		}
		return ref.toArray(new Reference[0]);
		
	}
	public static Reference asGlobalReference(KeyTypes type, String identifier) {
		return new DefaultReference.Builder()
				.type(ReferenceTypes.EXTERNAL_REFERENCE)
				.keys(new DefaultKey.Builder().type(type).value(identifier).build())
				.build();
			
	}
	public static Reference asGlobalReference(String identifier) {
		return new DefaultReference.Builder()
				.type(ReferenceTypes.EXTERNAL_REFERENCE)
				.keys(new DefaultKey.Builder().type(KeyTypes.GLOBAL_REFERENCE).value(identifier).build())
				.build();
			
	}
    /**
     * Creates a reference for an Identifiable instance using provided
     * implementation types for reference and key
     *
     * @param identifiable the identifiable to create the reference for
     * @param referenceType implementation type of Reference interface
     * @param keyType implementation type of Key interface
     * @return a reference representing the identifiable
     */
	public static <T extends Reference> T toReference(Identifiable identifiable, Class<T> referenceType, Class<? extends Key> keyType) {
        try {
            T reference = referenceType.getConstructor().newInstance();
            reference.setType(ReferenceTypes.MODEL_REFERENCE);
            Key key = keyType.getConstructor().newInstance();
            key.setType(referableToKeyType(identifiable));
            key.setValue(identifiable.getId());
            reference.setKeys(List.of(key));
            return reference;
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new IllegalArgumentException("error parsing reference - could not instantiate reference type", ex);
        }
    }

    /**
     * Creates a reference for an Identifiable instance
     *
     * @param identifiable the identifiable to create the reference for
     * @return a reference representing the identifiable
     */
    public static Reference toReference(Identifiable identifiable) {
        return toReference(identifiable, 
        		AASModelHelper.getDefaultImplementation(Reference.class), 
        		AASModelHelper.getDefaultImplementation(Key.class));
    }
    /**
     * Creates a reference for an element given a potential parent using
     * provided implementation types for reference and key
     *
     * @param parent Reference to the parent. Can only be null when element is
     * instance of Identifiable, otherwise result will always be null
     * @param element the element to create a reference for
     * @param referenceType implementation type of Reference interface
     * @param keyType implementation type of Key interface
     *
     * @return A reference representing the element or null if either element is
     * null or parent is null and element not an instance of Identifiable. In
     * case element is an instance of Identifiable, the returned reference will
     * only contain one key pointing directly to the element.
     * 
     * @implNote Taken from {@link AasUtils} created by Fraunhofer, copied in order to avoid loading of "original" ReflectionHelper!
     * 
     */
    public static <T extends Reference> T toReference(Reference parent, Referable element, Class<T> referenceType, Class<? extends Key> keyType) {
        if (element == null) {
            return null;
        } else if (Identifiable.class.isAssignableFrom(element.getClass())) {
            return toReference((Identifiable) element, referenceType, keyType);
        } else {
            T result = clone(parent, referenceType, keyType);
            if (result != null) {
                try {
                    Key newKey = keyType.getConstructor().newInstance();
                    newKey.setType(referableToKeyType(element));
                    newKey.setValue(element.getIdShort());
                    result.getKeys().add(newKey);
                } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    throw new IllegalArgumentException("error parsing reference - could not instantiate reference type", ex);
                }
            }
            return result;
        }
    }
    /**
     * Creates a deep-copy clone of a reference
     *
     * @param reference the reference to clone
     * @return the cloned reference
     * 
     * @implNote Taken from {@link AasUtils} created by Fraunhofer, copied in order to avoid loading of "original" ReflectionHelper
     */
    public static Reference clone(Reference reference) {
        return clone(reference, AASModelHelper.getDefaultImplementation(Reference.class), AASModelHelper.getDefaultImplementation(Key.class));
    }

    /**
     * Creates a deep-copy clone of a reference using provided implementation
     * types for reference and key
     *
     * @param reference the reference to clone
     * @param referenceType implementation type of Reference interface
     * @param keyType implementation type of Key interface
     *
     * @return the cloned reference
     * 
     * @implNote Taken from {@link AasUtils} created by Fraunhofer, copied in order to avoid loading of "original" ReflectionHelper
     * 
     */
    public static <T extends Reference> T clone(Reference reference, Class<T> referenceType, Class<? extends Key> keyType) {
        if (reference == null || reference.getKeys() == null || reference.getKeys().isEmpty()) {
            return null;
        }
        try {
            T result = referenceType.getConstructor().newInstance();
            List<Key> newKeys = new ArrayList<>();
            for (Key key : reference.getKeys()) {
                Key newKey = keyType.getConstructor().newInstance();
                newKey.setType(key.getType());
                newKey.setValue(key.getValue());
                newKeys.add(newKey);
            }
            result.setKeys(newKeys);
//            result.setType(reference.getType());
            return result;
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new IllegalArgumentException("error parsing reference - could not instantiate reference type", ex);
        }
    }

    /**
     * Creates a reference for an element given a potential parent
     * 
     *
     * @param parent Reference to the parent. Can only be null when element is
     * instance of Identifiable, otherwise result will always be null
     * @param element the element to create a reference for
     * @return A reference representing the element or null if either element is
     * null or parent is null and element not an instance of Identifiable. In
     * case element is an instance of Identifiable, the returned reference will
     * only contain one key pointing directly to the element.
     * 
     * @implNote Taken from {@link AasUtils} created by Fraunhofer, copied in order to avoid loading of "original" ReflectionHelper
     */
    public static Reference toReference(Reference parent, Referable element) {
        return toReference(parent,
                element,
                AASModelHelper.getDefaultImplementation(Reference.class),
                AASModelHelper.getDefaultImplementation(Key.class));
    }

	/**
	 * Search for the 
	 * @param references
	 * @param value
	 * @param type
	 * @return
	 */
	public static Optional<? extends Reference> extractReferenceFromList(List<? extends Reference> references, String value, KeyTypes type) {
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
     * 
     * @implNote Taken from {@link AasUtils} created by Fraunhofer, copied in order to avoid loading of "original" ReflectionHelper
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
     * 
     * @implNote Taken from {@link AasUtils} created by Fraunhofer, copied in order to avoid loading of "original" ReflectionHelper
     * 
     */
    public static KeyTypes referableToKeyType(Referable referable) {
        Class<?> aasInterface = AASModelHelper.getAasInterface(referable.getClass());
        if (aasInterface != null) {
            return KeyTypes.valueOf(EnumDeserializer.deserializeEnumName(aasInterface.getSimpleName()));
//            return KeyTypes.fromValue(aasInterface.getSimpleName());
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
