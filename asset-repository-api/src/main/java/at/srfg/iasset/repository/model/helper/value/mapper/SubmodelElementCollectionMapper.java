package at.srfg.iasset.repository.model.helper.value.mapper;

import org.eclipse.aas4j.v3.model.SubmodelElement;
import org.eclipse.aas4j.v3.model.SubmodelElementCollection;

import at.srfg.iasset.repository.model.helper.ValueHelper;
import at.srfg.iasset.repository.model.helper.value.SubmodelElementCollectionValue;

public class SubmodelElementCollectionMapper implements ValueMapper<SubmodelElementCollection, SubmodelElementCollectionValue>{

	@Override
	public SubmodelElementCollectionValue getValueOnly(SubmodelElementCollection modelElement) {
		SubmodelElementCollectionValue value = new SubmodelElementCollectionValue();
		// TODO Auto-generated method stub
		for ( SubmodelElement element : modelElement.getValues()) {
			value.getValues().put(element.getIdShort(), ValueHelper.toValue(element));
		}
		return value;
	}

}
