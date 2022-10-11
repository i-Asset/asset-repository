package at.srfg.iasset.repository.utils;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.eclipse.aas4j.v3.dataformat.core.util.AasUtils;
import org.eclipse.aas4j.v3.model.Identifiable;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.ReferenceTypes;
import org.eclipse.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.aas4j.v3.model.impl.DefaultReference;

public class ReferenceUtils {
	
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
	
	public static Optional<Reference> getReference(List<Reference> references, String value, KeyTypes type) {
		return references.stream().filter(new Predicate<Reference>() {

			@Override
			public boolean test(Reference t) {
				// check for the last key
				return value.equals(lastKeyValue(t));
			}
		}
		).findAny();
		
	}
}
