/*
 * Copyright (c) 2021 Fraunhofer-Gesellschaft zur Foerderung der angewandten Forschung e. V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.srfg.iasset.connector.component.impl;

import java.util.Arrays;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetKind;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXsd;
import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeIec61360;
import org.eclipse.digitaltwin.aas4j.v3.model.Direction;
import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.ModellingKind;
import org.eclipse.digitaltwin.aas4j.v3.model.ReferenceTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.StateOfEvent;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAdministrativeInformation;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetInformation;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultBasicEventElement;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultDataSpecificationIec61360;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEmbeddedDataSpecification;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEnvironment;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultExternalReference;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangString;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultModelReference;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultOperation;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultOperationVariable;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSpecificAssetId;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodelElementCollection;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultValueList;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultValueReferencePair;

import at.srfg.iasset.repository.utils.ReferenceUtils;

// TODO import org.eclipse.aas4j.v3.rc02.model.impl.DefaultEmbeddedDataSpecification;


public class AASFull {

	public static final AssetAdministrationShell AAS_BELT_TEMPLATE = createTypeAasForBelt();
	public static final AssetAdministrationShell AAS_BELT_INSTANCE = createInstanceAasForBelt();
	public static final Submodel SUBMODEL_BELT_PROPERTIES_TEMPLATE = createSubmodelTemplateForBeltProperties();
	public static final Submodel SUBMODEL_BELT_PROPERTIES_INSTANCE = createSubmodelInstanceForBeltProperties();
	public static final Submodel SUBMODEL_BELT_EVENT_TEMPLATE = createSubmodelTemplateForBeltEvents();
	public static final Submodel SUBMODEL_BELT_EVENT_INSTANCE = createSubmodelInstanceForBeltEvents();
	public static final Submodel SUBMODEL_BELT_OPERATIONS_TEMPLATE = createSubmodelTemplateForBeltOperation();
	public static final Submodel SUBMODEL_BELT_OPERATIONS_INSTANCE = createSubmodelInstanceForBeltOperation();
	
	public static final ConceptDescription CONCEPT_DESCRIPTION_MESSAGE_BROKER = createConceptDescriptionForMessageBroker();
	public static final ConceptDescription CONCEPT_DESCRIPTION_BELT_STATE = createConceptDescriptionForBeltState();
	
    public static final Environment ENVIRONMENT = createEnvironment();
    
    public static AssetAdministrationShell createTypeAasForBelt() {
    	return new DefaultAssetAdministrationShell.Builder()
    			.id("http://iasset.salzburgresearch.at/labor/belt#aas")
    			.idShort("belt")
    			.assetInformation(new DefaultAssetInformation.Builder()
    					.assetKind(AssetKind.TYPE)
    					.globalAssetId("http://iasset.salzburgresearch.at/labor/belt")
    					.specificAssetId(new DefaultSpecificAssetId.Builder()
    							.semanticId(ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, "http://specificAssetId.belt.com"))
    							.build() )
    					.build())
    			.displayName(new DefaultLangString.Builder()
    					.language("de")
    					.text("i-Asset Type for Belt").build()
    				)
    			.administration(new DefaultAdministrativeInformation.Builder()
    					.version("V0.01")
    					.version("001")
    					.build() )
    			.build();
    }
    public static AssetAdministrationShell createInstanceAasForBelt() {
    	return new DefaultAssetAdministrationShell.Builder()
    			.id("http://iasset.salzburgresearch.at/labor/beltInstance")
    			.idShort("beltInstance")
    			.assetInformation(new DefaultAssetInformation.Builder()
    					.assetKind(AssetKind.TYPE)
    					.globalAssetId("http://iasset.salzburgresearch.at/labor/belt")
    					.specificAssetId(new DefaultSpecificAssetId.Builder()
    							.semanticId(ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, "http://specificAssetId.belt.com"))
    							.build() )
    					.build())
    			.displayName(new DefaultLangString.Builder()
    					.language("de")
    					.text("i-Asset Type for Belt").build()
    				)
    			.administration(new DefaultAdministrativeInformation.Builder()
    					.version("V0.01")
    					.version("001")
    					.build() )
    			// point to the type aas
    			.derivedFrom(ReferenceUtils.toReference(createTypeAasForBelt()))
    			.build();
    }

    public static Submodel createSubmodelTemplateForBeltProperties() {
    	return new DefaultSubmodel.Builder()
    			.idShort("properties")
    			.id("http://iasset.salzburgresearch.at/labor/belt#properties")
    			.displayName(new DefaultLangString.Builder()
    					.language("de")
    					.text("i-Asset Belt-Propeties Submodel").build()
    					)
    			.description(new DefaultLangString.Builder()
    					.language("de")
    					.text("i-Asset Belt Properties Submodel")
    					.build())
    			.kind(ModellingKind.TEMPLATE)
    			// the belt-info-Type refers to it's parent type
//    			.semanticId(ReferenceUtils.toReference(createSubmodelForInfoModel()))
    			.submodelElement(new DefaultSubmodelElementCollection.Builder()
    					.idShort("beltData")
    	    			.displayName(new DefaultLangString.Builder()
    	    					.language("de")
    	    					.text("i-Asset Belt-Data Contaoner").build()
    	    					)
    	    			// TODO create concept description for belt data
    	    			.semanticId(new DefaultExternalReference.Builder()
	    					.key(new DefaultKey.Builder()
	    							.type(KeyTypes.GLOBAL_REFERENCE)
	    							.value("http://iasset.salzburgresearch.at/beltDataEvent")
	    							.build())
	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("state")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language("de")
    	    	    					.text("Status Förderband (on/off)").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.BOOLEAN)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("moving")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language("de")
    	    	    					.text("Förderband in Bewegung(on/off)").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.BOOLEAN)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("direction")
    	    					.category("variable")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language("de")
    	    	    					.text("Bewegungsrichtung").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("distance")
    	    					.category("variable")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language("de")
    	    	    					.text("zurückgelegte Distanz").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.DECIMAL)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("timeElapsed")
    	    					.category("variable")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language("de")
    	    	    					.text("Bewegungsdauer gesamt").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.DURATION)
    	    					.build())
    					.build())
    			.build();
    }
    public static Submodel createSubmodelInstanceForBeltProperties() {
    	return new DefaultSubmodel.Builder()
    			.idShort("properties")
    			.id("http://iasset.salzburgresearch.at/labor/beltInstance/properties")
    			.displayName(new DefaultLangString.Builder()
    					.language("de")
    					.text("i-Asset Belt-Propeties Submodel").build())
    			.description(new DefaultLangString.Builder()
    					.language("de")
    					.text("i-Asset Belt Properties Submodel")
    					.build())
    			.kind(ModellingKind.INSTANCE)
    			// the belt-info-Type refers to its parent type
    			.semanticId(ReferenceUtils.toReference(createSubmodelTemplateForBeltProperties()))
    			.submodelElement(new DefaultSubmodelElementCollection.Builder()
    					.idShort("beltData")
    	    			.displayName(new DefaultLangString.Builder()
    	    					.language("de")
    	    					.text("i-Asset Belt-Data Container").build()
    	    					)
    	    			.semanticId(new DefaultModelReference.Builder()
    	    					.key(new DefaultKey.Builder()
    	    							.type(KeyTypes.SUBMODEL)
    	    							.value("http://iasset.salzburgresearch.at/labor/belt#properties")
    	    							.build())
    	    					.key(new DefaultKey.Builder()
    	    							.type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION)
    	    							.value("beltData")
    	    							.build())
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("state")
    	    	    			.semanticId(new DefaultModelReference.Builder()
    	    	    					.key(new DefaultKey.Builder()
    	    	    							.type(KeyTypes.SUBMODEL)
    	    	    							.value("http://iasset.salzburgresearch.at/labor/belt#properties")
    	    	    							.build())
    	    	    					.key(new DefaultKey.Builder()
    	    	    							.type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION)
    	    	    							.value("beltData")
    	    	    							.build())
    	    	    					.key(new DefaultKey.Builder()
    	    	    							.type(KeyTypes.PROPERTY)
    	    	    							.value("state")
    	    	    							.build())
    	    	    					.build())
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language("de")
    	    	    					.text("Status Förderband (init/halt/left/right)").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("moving")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language("de")
    	    	    					.text("Förderband in Bewegung (on/off)").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.BOOLEAN)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("direction")
    	    					.category("variable")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language("de")
    	    	    					.text("Bewegungsrichtung (left/right)").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("distance")
    	    					.category("variable")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language("de")
    	    	    					.text("zurückgelegte Distanz (relativ)").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.DECIMAL)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("totalDistance")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language("de")
										.text("zurückgelegte Distanz (gesamt, absolut)").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.DECIMAL)
    	    					.build())
    					.build())
    			.build();
    }
    /**
     * Create the {@link ConceptDescription} defining the property for the belt state
     * @return
     */
    public static ConceptDescription createConceptDescriptionForBeltState() {
    	return new DefaultConceptDescription.Builder()
    			.id("http://iasset.salzburgresearch.at/labor/belt/data/state")
    			.idShort("state")
    			.category("ConceptProperty")
    			.isCaseOf(ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, "http://iasset.salzburgresearch.at/labor/belt/data/state"))
    			// Abbild aus Semantic Lookup!!
    			.embeddedDataSpecification(new DefaultEmbeddedDataSpecification.Builder()
    					.dataSpecificationContent(new DefaultDataSpecificationIec61360.Builder()
    							.dataType(DataTypeIec61360.STRING)
    							.preferredName(new DefaultLangString.Builder()
    									.language("de")
    									.text("On/Off Zustand des Förderbandes")
    									.build())
    							.valueList(new DefaultValueList.Builder()
    									.valueReferencePair(new DefaultValueReferencePair.Builder()
    											.value("ON")
    											.valueId(ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, "http://iasset.salzburgresearch.at/labor/belt/data/state/on"))
    											.build())
    									.valueReferencePair(new DefaultValueReferencePair.Builder()
    											.value("OFF")
    											.valueId(ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, "http://iasset.salzburgresearch.at/labor/belt/data/state/off"))
    											.build())
    									.build())
    							.build())
    					.build())
    			.build();
    }

    public static Submodel createSubmodelTemplateForBeltOperation() {
    	return new DefaultSubmodel.Builder()
    			.id("http://iasset.salzburgresearch.at/labor/belt#operations")
    			.idShort("operations")
    			.kind(ModellingKind.TEMPLATE)
    			.displayName(new DefaultLangString.Builder()
    					.language("de")
    					.text("Förderband Funktionen").build()
    					)
    			.submodelElement(new DefaultOperation.Builder()
    					.idShort("switchBusyLight")
    					.category("function")
    	    			.displayName(new DefaultLangString.Builder()
    	    					.language("de")
    	    					.text("Kontroll-Lampe ein/ausschalten").build()
    	    					)
    	    			.inoutputVariable(new DefaultOperationVariable.Builder()
    	    					.value(new DefaultProperty.Builder()
    	    							.idShort("state")
    	    							.category("constant")
    	    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    	    					.language("de")
    	    	    	    					.text("Neuer Status für Kontroll-Lampe").build()
    	    	    	    					)
    	    	    	    			.valueType(DataTypeDefXsd.BOOLEAN)
    	    							.build())
    	    					.build())
    					.build())
    			.submodelElement(new DefaultOperation.Builder()
    					.idShort("moveBelt")
    					.category("function")
    	    			.displayName(new DefaultLangString.Builder()
    	    					.language("de")
    	    					.text("Band vor/zurück bewegen").build()
    	    					)
    	    			.inputVariable(new DefaultOperationVariable.Builder()
    	    					.value(new DefaultProperty.Builder()
    	    	    					.idShort("direction")
    	    	    					.category("constant")
    	    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    	    					.language("de")
    	    	    	    					.text("Bewegungsrichtung").build()
    	    	    	    					)
    	    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    	    					.build())
    	    					.value(new DefaultProperty.Builder()
    	    	    					.idShort("distance")
    	    	    					.category("constant")
    	    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    	    					.language("de")
    	    	    	    					.text("Zurückzulegende Entfernung").build()
    	    	    	    					)
    	    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    	    					.build())
    	    					.build())
    					.build())
    			.build();
    }
    public static Submodel createSubmodelInstanceForBeltOperation() {
    	return new DefaultSubmodel.Builder()
    			.id("http://iasset.salzburgresearch.at/labor/beltInstance/operations")
    			.idShort("operations")
    			.kind(ModellingKind.INSTANCE)
    			.displayName(new DefaultLangString.Builder()
    					.language("de")
    					.text("Förderband Funktionen").build()
    					)
    			.semanticId(ReferenceUtils.toReference(createSubmodelTemplateForBeltOperation()))
    			.submodelElement(new DefaultOperation.Builder()
    					.idShort("switchBusyLight")
    					.category("function")
    	    			.displayName(new DefaultLangString.Builder()
    	    					.language("de")
    	    					.text("Kontroll-Lampe ein/ausschalten").build()
    	    					)
    	    			.inoutputVariable(new DefaultOperationVariable.Builder()
    	    					.value(new DefaultProperty.Builder()
    	    							.idShort("state")
    	    							.category("constant")
    	    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    	    					.language("de")
    	    	    	    					.text("Neuer Status für Kontroll-Lampe").build()
    	    	    	    					)
    	    	    	    			.valueType(DataTypeDefXsd.BOOLEAN)
    	    							.build())
    	    					.build())
    					.build())
    			.submodelElement(new DefaultOperation.Builder()
    					.idShort("moveBelt")
    					.category("function")
    	    			.displayName(new DefaultLangString.Builder()
    	    					.language("de")
    	    					.text("Band vor/zurück bewegen").build()
    	    					)
    	    			.inputVariable(new DefaultOperationVariable.Builder()
    	    					.value(new DefaultProperty.Builder()
    	    	    					.idShort("direction")
    	    	    					.category("constant")
    	    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    	    					.language("de")
    	    	    	    					.text("Bewegungsrichtung").build()
    	    	    	    					)
    	    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    	    					.build())
    	    					.value(new DefaultProperty.Builder()
    	    	    					.idShort("distance")
    	    	    					.category("constant")
    	    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    	    					.language("de")
    	    	    	    					.text("Zurückzulegende Entfernung").build()
    	    	    	    					)
    	    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    	    					.build())
    	    					.build())
    					.build())
    			.build();
    }

    public static Submodel createSubmodelTemplateForBeltEvents() {
    	return new DefaultSubmodel.Builder()
    			.id("http://iasset.salzburgresearch.at/labor/belt#events")
    			.idShort("events")
    			.kind(ModellingKind.TEMPLATE)
    			.displayName(new DefaultLangString.Builder()
    					.language("de")
    					.text("Förderband Ereignisse").build()
    					)
    			.semanticId(new DefaultExternalReference.Builder()
    					.key(new DefaultKey.Builder()
    							.type(KeyTypes.GLOBAL_REFERENCE)
    							.value("http://iasset.salzburgresearch.at/beltEvent")
    							.build())
    					.build())
    			.submodelElement(new DefaultBasicEventElement.Builder()
    					.idShort("beltEvent")
//    					.observed(new DefaultReference.Builder()
//    							.type(ReferenceTypes.MODEL_REFERENCE)
//    							.key(new DefaultKey.Builder()
//    									.type(KeyTypes.SUBMODEL)
//    									.value(createSubmodelTemplateForBeltProperties().getId())
//    									.build())
//    							.key(new DefaultKey.Builder()
//    									.type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION)
//    									.value("beltData")
//    									.build())
//    							.build())
    					
    					// message broker acts as default information
    					.messageBroker(new DefaultModelReference.Builder()
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.SUBMODEL)
    									.value("http://iasset.salzburgresearch.at/application#eventConfig")
    									.build())
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION)
    									.value("messageBroker")
    									.build())
    							.build())
    					.build())
    			.build();
    }
    public static Submodel createSubmodelInstanceForBeltEvents() {
    	return new DefaultSubmodel.Builder()
    			.id("http://iasset.salzburgresearch.at/labor/beltInstance/events")
    			.idShort("events")
    			.kind(ModellingKind.INSTANCE)
    			.displayName(new DefaultLangString.Builder()
    					.language("de")
    					.text("Förderband Ereignisse").build()
    					)
    			.semanticId(new DefaultModelReference.Builder()
    					.key(new DefaultKey.Builder()
    							.type(KeyTypes.SUBMODEL)
    							.value("http://iasset.salzburgresearch.at/labor/belt#events")
    							.build())
    					.build())
    			.submodelElement(new DefaultBasicEventElement.Builder()
    					.idShort("beltEvent")
    					.direction(Direction.OUTPUT)
    					.state(StateOfEvent.ON)
    					.semanticId(new DefaultModelReference.Builder()
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.SUBMODEL)
    									.value("http://iasset.salzburgresearch.at/labor/belt#events")
    									.build())
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.EVENT_ELEMENT)
    									.value("beltEvent")
    									.build())
    							.referredSemanticId(new DefaultExternalReference.Builder()
    									.key(new DefaultKey.Builder()
    											.type(KeyTypes.GLOBAL_REFERENCE)
    											.value("http://iasset.salzburgresearch.at/beltEvent")
    											.build())
    									.build())
    							.build())
    					.messageTopic("messageTopic")
    					.observed(new DefaultModelReference.Builder()
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.SUBMODEL)
    									.value(createSubmodelInstanceForBeltProperties().getId())
    									.build())
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION)
    									.value("beltData")
    									.build())
    							.build())
    					// Referenz auf das Broker-Objekt!
    					.messageBroker(new DefaultModelReference.Builder()
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.SUBMODEL)
    									.value("http://iasset.salzburgresearch.at/application#eventConfig")
    									.build())
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION)
    									.value("messageBroker")
    									.build())
    							// referredSemantic verweist auf den "Typ" des referenzierten Elements!!
    							// 
    							.referredSemanticId(new DefaultExternalReference.Builder()
    	    							.key(new DefaultKey.Builder()
    	    									.type(KeyTypes.CONCEPT_DESCRIPTION)
    	    									.value("http://iasset.salzburgresearch.at/data/messageBroker")
    	    									.build())
    									.build())
    							.build())
    					.build())
    			.build();
    }
    public static ConceptDescription createConceptDescriptionForMessageBroker() {
        return new DefaultConceptDescription.Builder()
        		.id("http://iasset.salzburgresearch.at/data/messageBroker")
                .idShort("messageBroker")
                .category("ConceptClass")
                .descriptions(Arrays.asList(
                        new DefaultLangString.Builder().text("Concept referencing the messageBroker").language("en").build(),
                        new DefaultLangString.Builder().text("Concept zum MessageBroker ").language("de").build()
                        ))
                .administration(new DefaultAdministrativeInformation.Builder()
                        .version("0.9")
                        .revision("0")
                        .build())
                .isCaseOf(ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, "http://iasset.salzburgresearch.at/data/messageBroker"))
                .build();
    }
    

    public static Environment createEnvironment() {
        return new DefaultEnvironment.Builder()
//                .assetAdministrationShells(Arrays.asList(
//                        createAAS1(),
//                        createAAS2(),
//                        createAAS3(),
//                        createAAS4()))
//                .submodels(Arrays.asList(
//                        createSubmodel1(),
//                        createSubmodel2(),
//                        createSubmodel3(),
//                        createSubmodel4(),
//                        createSubmodel5(),
//                        createSubmodel6(),
//                        createSubmodel7()))
//                .conceptDescriptions(Arrays.asList(
//                        createConceptDescription1(),
//                        createConceptDescription2(),
//                        createConceptDescription3(),
//                        createConceptDescription4()))
                .build();
    }

}
