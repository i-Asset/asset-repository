package at.srfg.iasset.repository.model.helper.value.mapper;

import org.eclipse.digitaltwin.aas4j.v3.model.Blob;

import com.fasterxml.jackson.databind.JsonNode;

import at.srfg.iasset.repository.model.helper.value.BlobValue;

public class BlobValueMapper implements ValueMapper<Blob, BlobValue> {

	@Override
	public BlobValue mapToValue(Blob modelElement) {
		return new BlobValue(modelElement.getContentType(), modelElement.getValue());
	}

	@Override
	public Blob mapValueToElement(Blob modelElement, JsonNode valueNode) {
		valueNode.get("value");
		return modelElement;
	}

}
