package at.srfg.iasset.repository.model.helper.value.mapper;

import org.eclipse.aas4j.v3.model.File;

import com.fasterxml.jackson.databind.JsonNode;

import at.srfg.iasset.repository.model.helper.value.FileValue;

public class FileValueMapper implements ValueMapper<File, FileValue> {

	@Override
	public FileValue getValueOnly(File modelElement) {
		return new FileValue(modelElement.getContentType(), modelElement.getValue());
	}



}
