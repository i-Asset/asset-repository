package at.srfg.iasset.repository.model.helper.payload;

import java.util.Iterator;

import org.eclipse.aas4j.v3.model.Key;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Reference;

import com.fasterxml.jackson.annotation.JsonValue;

import at.srfg.iasset.repository.utils.ReferenceUtils;

public class ReferenceValue extends PayloadValue {
	@JsonValue
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ReferenceValue(Reference reference) {
		if (reference != null && reference.getKeys() != null) {
			if (KeyTypes.GLOBAL_REFERENCE.equals(ReferenceUtils.firstKeyType(reference))) {
				this.value = ReferenceUtils.firstKeyValue(reference);
			}
			else {
				StringBuffer valueBuffer = new StringBuffer();
				Iterator<Key> keyIterator = ReferenceUtils.keyIterator(reference);
				while (keyIterator.hasNext()) {
					Key key = keyIterator.next();
					valueBuffer.append(String.format("(%s)%s", key.getType(), key.getValue()));
					if (keyIterator.hasNext()) {
						valueBuffer.append(",");
					}
				}
				this.value = valueBuffer.toString();
			}
		}

	}

}
