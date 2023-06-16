package at.srfg.iasset.repository.model.helper.payload;

public interface PayloadValueMapper<O, V extends PayloadValue> {
	V mapToValue(O modelElement);
	O mapFromValue(V valueElement);
	
}
