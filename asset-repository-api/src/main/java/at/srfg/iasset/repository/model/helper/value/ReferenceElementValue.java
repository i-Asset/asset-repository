package at.srfg.iasset.repository.model.helper.value;

import java.util.Arrays;
import java.util.List;

import org.eclipse.digitaltwin.aas4j.v3.model.Key;

import com.fasterxml.jackson.annotation.JsonValue;

public class ReferenceElementValue extends DataElementValue {
	@JsonValue
	private List<Key> value;

	public ReferenceElementValue(List<Key> langString) {
		value = langString;		
	}
	public ReferenceElementValue(Key ...keys ) {
		value = Arrays.asList(keys);
	}
}
