package at.srfg.iasset.repository.model.helper.value.mapper;

import org.eclipse.aas4j.v3.model.Blob;

import at.srfg.iasset.repository.model.helper.value.BlobValue;

public class BlobValueMapper implements ValueMapper<Blob, BlobValue> {

	@Override
	public BlobValue getValueOnly(Blob modelElement) {
		return new BlobValue(modelElement.getContentType(), modelElement.getValue());
	}

}
