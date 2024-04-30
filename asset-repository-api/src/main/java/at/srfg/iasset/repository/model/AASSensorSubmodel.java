package at.srfg.iasset.repository.model;

import java.time.Instant;

import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXsd;
import org.eclipse.digitaltwin.aas4j.v3.model.Direction;
import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.ModellingKind;
import org.eclipse.digitaltwin.aas4j.v3.model.StateOfEvent;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultBasicEventElement;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultExternalReference;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangString;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultModelReference;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodelElementCollection;

// TODO import org.eclipse.aas4j.v3.rc02.model.impl.DefaultEmbeddedDataSpecification;


public class AASSensorSubmodel {

	public static final String LANGUAGE = "de";

    public static final Submodel SUBMODEL_SENSOR = createSubmodelForSensor();
    
    public static Submodel createSubmodelForSensor() {
    	return new DefaultSubmodel.Builder()
    			.idShort("properties")
    			.id("http://iasset.salzburgresearch.at/common/sensor")
    			.displayName(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset Sensor Submodel").build()
    					)
    			.description(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset OEE Sensor Submodel")
    					.build())
    			.kind(ModellingKind.TEMPLATE)
    			.submodelElement(new DefaultBasicEventElement.Builder()
    					.idShort("createSensor")
    					.direction(Direction.OUTPUT)
    					.state(StateOfEvent.ON)
    					.messageTopic("sensorTopic")
    					.observed(new DefaultModelReference.Builder()
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.SUBMODEL)
    									.value("http://iasset.salzburgresearch.at/common/sensor")
    									.build())
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION)
    									.value("sensor")
    									.build())
    							.build())
    					.semanticId(new DefaultExternalReference.Builder()
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.GLOBAL_REFERENCE)
    									.value("http://iasset.salzburgresearch.at/semantic/sensor")
    									.build())
    			
    							.build())
    					.build())
    			.submodelElement(new DefaultBasicEventElement.Builder()
    					.idShort("updateSensor")
    					.direction(Direction.INPUT)
    					.state(StateOfEvent.ON)
    					.messageTopic("sensorTopic")
    					.observed(new DefaultModelReference.Builder()
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.SUBMODEL)
    									.value("http://iasset.salzburgresearch.at/common/sensor")
    									.build())
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION)
    									.value("sensor")
    									.build())
    							.build())
    					.semanticId(new DefaultExternalReference.Builder()
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.GLOBAL_REFERENCE)
    									.value("http://iasset.salzburgresearch.at/semantic/sensor")
    									.build())
    			
    							.build())
    					.build())
    			.submodelElement(new DefaultSubmodelElementCollection.Builder()
    					.idShort("sensor")
    	    			.displayName(new DefaultLangString.Builder()
    	    					.language(LANGUAGE)
    	    					.text("i-Asset Sensormessung Daten").build()
    	    					)
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("assetId")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Asset Identifier").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("subElementPath")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Path Subelement").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("sensorId")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Sensor Identifier").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    	    			.value("Default Value")
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("timestampCreated")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Timestamp Created").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    	    			.value(Instant.now().toString())
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("measurement")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Sensor Measurement").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("measurementId")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Sensor Measurement ID").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    					.build())
    			.build();
    }
}