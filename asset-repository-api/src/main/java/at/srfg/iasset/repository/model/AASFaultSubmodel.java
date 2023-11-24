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
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultOperation;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultOperationVariable;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodelElementCollection;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodelElementList;

import at.srfg.iasset.repository.utils.ReferenceUtils;

// TODO import org.eclipse.aas4j.v3.rc02.model.impl.DefaultEmbeddedDataSpecification;


public class AASFaultSubmodel {

	public static final String LANGUAGE = "de";

    public static final Submodel SUBMODEL_FAULT1 = createSubmodelForFault();
    public static final Submodel SUBMODEL_FAULT_OPERATIONS = createSubmodelForFaultOperations();
    
    public static Submodel createSubmodelForFault() {
    	return new DefaultSubmodel.Builder()
    			.idShort("properties")
    			.id("http://iasset.salzburgresearch.at/common/fault")
    			.displayName(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset Fault Submodel").build()
    					)
    			.description(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset OEE Fault Submodel")
    					.build())
    			.kind(ModellingKind.TEMPLATE)
    			.submodelElement(new DefaultBasicEventElement.Builder()
    					.idShort("createFault")
    					.direction(Direction.OUTPUT)
    					.state(StateOfEvent.ON)
    					.messageTopic("faultTopic")
    					.observed(new DefaultModelReference.Builder()
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.SUBMODEL)
    									.value("http://iasset.salzburgresearch.at/common/fault")
    									.build())
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION)
    									.value("fault")
    									.build())
    							.build())
    					.semanticId(new DefaultExternalReference.Builder()
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.GLOBAL_REFERENCE)
    									.value("http://iasset.salzburgresearch.at/semantic/fault")
    									.build())
    			
    							.build())
    					.build())
    			.submodelElement(new DefaultBasicEventElement.Builder()
    					.idShort("updateFault")
    					.direction(Direction.INPUT)
    					.state(StateOfEvent.ON)
    					.messageTopic("faultTopic")
    					.observed(new DefaultModelReference.Builder()
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.SUBMODEL)
    									.value("http://iasset.salzburgresearch.at/common/fault")
    									.build())
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION)
    									.value("fault")
    									.build())
    							.build())
    					.semanticId(new DefaultExternalReference.Builder()
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.GLOBAL_REFERENCE)
    									.value("http://iasset.salzburgresearch.at/semantic/fault")
    									.build())
    			
    							.build())
    					.build())
    			.submodelElement(new DefaultSubmodelElementCollection.Builder()
    					.idShort("fault")
    	    			.displayName(new DefaultLangString.Builder()
    	    					.language(LANGUAGE)
    	    					.text("i-Asset Störmeldung Daten").build()
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
    	    					.idShort("faultId")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Fault Identifier").build()
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
    	    	    			.valueType(DataTypeDefXsd.DATE_TIME)
    	    	    			.value(Instant.now().toString())
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("timestampFinished")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Timestamp Finished").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.DATE_TIME)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("faultCode")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Fault Code").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("shortText")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Messagetext").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("longText")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Message Description").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("priority")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Priority").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.INTEGER)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("senderUserId")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Benutzer ID Melder").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("maintencanceUserId")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Benutzer ID Sender").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("status")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Fault status").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    	    			
    					.build())
    			.build();
    }
   
    public static Submodel createSubmodelForFaultOperations() {
    	return new DefaultSubmodel.Builder()
    			.idShort("operations")
    			.id("http://iasset.salzburgresearch.at/common/faultOperation")
    			.displayName(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset Fault Operation Submodel").build()
    					)
    			.description(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset Fault Operation Submodel")
    					.build())
    			.kind(ModellingKind.TEMPLATE)
    			.submodelElement(new DefaultOperation.Builder()
    					.idShort("reportFault")
    	    			.displayName(new DefaultLangString.Builder()
    	    					.language(LANGUAGE)
    	    					.text("Report Fault").build()
    	    			)
    	    			.inputVariable(new DefaultOperationVariable.Builder()
    	    					.value(new DefaultSubmodelElementCollection.Builder()
    	    							.idShort("fault")
    	    							.semanticId(new DefaultModelReference.Builder()
    	    									.key(new DefaultKey.Builder()
    	    											.type(KeyTypes.SUBMODEL)
    	    											.value("http://iasset.salzburgresearch.at/common/faultOperation")
    	    											.build())
    	    									.key(new DefaultKey.Builder()
    	    											.type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION)
    	    											.value("fault")
    	    											.build())
    	    									.build())
    	    							.build())
    	    					.build())
    	    			.outputVariable(new DefaultOperationVariable.Builder()
    	    					.value(new DefaultProperty.Builder()
    	    							.idShort("faultId")
    	    							.semanticId(ReferenceUtils.asGlobalReference("http://cmms.org/fault/id"))
    	    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    	    					.language(LANGUAGE)
    	    	    	    					.text("Fault-Identifier from CMMS").build()
    	    	    	    			)
    	    							.valueType(DataTypeDefXsd.STRING)
    	    							.build())
    	    					.build())
    					.build())
    			.submodelElement(new DefaultSubmodelElementCollection.Builder()
    					.idShort("fault")
    					.semanticId(null)
    	    			.displayName(new DefaultLangString.Builder()
    	    					.language(LANGUAGE)
    	    					.text("i-Asset Störmeldung Daten").build()
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
    	    					.idShort("faultId")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Fault Identifier").build()
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
    	    	    			.valueType(DataTypeDefXsd.DATE_TIME)
    	    	    			.value(Instant.now().toString())
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("timestampFinished")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Timestamp Finished").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.DATE_TIME)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("faultCode")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Fault Code").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("shortText")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Messagetext").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("longText")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Message Description").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("priority")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Priority").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.INTEGER)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("senderUserId")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Benutzer ID Melder").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("maintencanceUserId")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Benutzer ID Sender").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("status")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Fault status").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    	    			.value(new DefaultSubmodelElementCollection.Builder()
    	    					// this collection does not specify the properties, instead, the semanticId
    	    					// refers to the model element. 
    	    					// note: a GlobalReference would not work!!
    	    					// the
    	    					.idShort("errorCode")
    							.semanticId(new DefaultModelReference.Builder()
    									.key(new DefaultKey.Builder()
    											.type(KeyTypes.SUBMODEL)
    											.value("http://iasset.salzburgresearch.at/common/faultOperation")
    											.build())
    									.key(new DefaultKey.Builder()
    											.type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION)
    											.value("errorCode")
    											.build())
    									.build())
    	    					.build())
    	    				
    					.build())

    			.submodelElement(new DefaultOperation.Builder()
    					.idShort("getErrorCause")
    	    			.displayName(new DefaultLangString.Builder()
    	    					.language(LANGUAGE)
    	    					.text("Obtain Error Codes for Asset").build()
    	    			)
    	    			.inputVariable(new DefaultOperationVariable.Builder()
    	    					.value(new DefaultProperty.Builder()
    	    							.idShort("assetIdentifier")
    	    	    					.valueType(DataTypeDefXsd.STRING)
    	    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    	    					.language(LANGUAGE)
    	    	    	    					.text("Asset Identifier").build()
    	    	    	    			)
    	    							.build())
    	    					.build())
    	    			.outputVariable(new DefaultOperationVariable.Builder()
    	    					.value(new DefaultSubmodelElementList.Builder()
    	    							.category("result")
    	    							.idShort("errorCodes")
    	    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    	    					.language(LANGUAGE)
    	    	    	    					.text("Error Codes for Asset Result").build()
    	    	    	    			)
    	    	    	    			//
    	    	    	    			.semanticIdListElement(new DefaultModelReference.Builder()
    	    	    	    					.key(new DefaultKey.Builder()
    	    	    	    							.type(KeyTypes.SUBMODEL)
    	    	    	    							.value("http://iasset.salzburgresearch.at/common/faultOperation")
    	    	    	    							.build())
    	    	    	    					.key(new DefaultKey.Builder()
    	    	    	    							.type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION)
    	    	    	    							.value("errorCode")
    	    	    	    							.build())
    	    	    	    					.build())
    	    							.build())
    	    					.build())
    					.build())
    			.submodelElement(new DefaultSubmodelElementCollection.Builder()
    					.idShort("errorCode")
    	    			.displayName(new DefaultLangString.Builder()
    	    					.language(LANGUAGE)
    	    					.text("Error Code Record").build()
    	    			)
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("code")
    	    					.valueType(DataTypeDefXsd.STRING)
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Code").build()
    	    	    			)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("label")
    	    					.valueType(DataTypeDefXsd.STRING)
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Error Label").build()
    	    	    			)
    	    					.build())
    					.build())
    			.build();
    }

}
