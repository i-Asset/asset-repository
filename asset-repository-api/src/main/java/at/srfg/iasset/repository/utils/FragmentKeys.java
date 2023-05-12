package at.srfg.iasset.repository.utils;

import org.eclipse.aas4j.v3.model.KeyTypes;

public class FragmentKeys {
	public static boolean isFragmentKey(KeyTypes type) {
		switch(type) {
		case SUBMODEL_ELEMENT:
		case SUBMODEL_ELEMENT_COLLECTION:
		case SUBMODEL_ELEMENT_LIST:
		case RELATIONSHIP_ELEMENT:
		case REFERENCE_ELEMENT:
		case RANGE:
		case PROPERTY:
		case OPERATION:
		case MULTI_LANGUAGE_PROPERTY:
		case FILE:
		case EVENT_ELEMENT:
		case BASIC_EVENT_ELEMENT:
		case ENTITY:
		case DATA_ELEMENT:
		case CAPABILITY:
		case BLOB:
		case ANNOTATED_RELATIONSHIP_ELEMENT:
		case FRAGMENT_REFERENCE:
			return true;
		}
		return false;
	};
}
