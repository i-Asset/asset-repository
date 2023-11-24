package at.srfg.iasset.repository.model.helper.value.mapper;

import org.eclipse.digitaltwin.aas4j.v3.model.File;

import at.srfg.iasset.repository.model.helper.value.FileValue;

public class FileValueMapper implements ValueMapper<File, FileValue> {

	@Override
	public FileValue mapToValue(File modelElement) {
		return new FileValue(modelElement.getContentType(), modelElement.getValue());
	}



}
