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
package at.srfg.iasset.repository.persistence.startup;

import java.util.Arrays;
import java.util.Base64;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.AssetKind;
import org.eclipse.aas4j.v3.model.ConceptDescription;
import org.eclipse.aas4j.v3.model.DataTypeDefXsd;
import org.eclipse.aas4j.v3.model.DataTypeIEC61360;
import org.eclipse.aas4j.v3.model.Direction;
import org.eclipse.aas4j.v3.model.EntityType;
import org.eclipse.aas4j.v3.model.Environment;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.LevelType;
import org.eclipse.aas4j.v3.model.ModelingKind;
import org.eclipse.aas4j.v3.model.ReferenceTypes;
import org.eclipse.aas4j.v3.model.StateOfEvent;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.impl.DefaultAdministrativeInformation;
import org.eclipse.aas4j.v3.model.impl.DefaultAnnotatedRelationshipElement;
import org.eclipse.aas4j.v3.model.impl.DefaultAssetAdministrationShell;
import org.eclipse.aas4j.v3.model.impl.DefaultAssetInformation;
import org.eclipse.aas4j.v3.model.impl.DefaultBasicEventElement;
import org.eclipse.aas4j.v3.model.impl.DefaultBlob;
import org.eclipse.aas4j.v3.model.impl.DefaultCapability;
import org.eclipse.aas4j.v3.model.impl.DefaultConceptDescription;
import org.eclipse.aas4j.v3.model.impl.DefaultDataSpecification;
import org.eclipse.aas4j.v3.model.impl.DefaultDataSpecificationIEC61360;
import org.eclipse.aas4j.v3.model.impl.DefaultEntity;
import org.eclipse.aas4j.v3.model.impl.DefaultEnvironment;
import org.eclipse.aas4j.v3.model.impl.DefaultFile;
import org.eclipse.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.aas4j.v3.model.impl.DefaultLangString;
import org.eclipse.aas4j.v3.model.impl.DefaultMultiLanguageProperty;
import org.eclipse.aas4j.v3.model.impl.DefaultOperation;
import org.eclipse.aas4j.v3.model.impl.DefaultOperationVariable;
import org.eclipse.aas4j.v3.model.impl.DefaultProperty;
import org.eclipse.aas4j.v3.model.impl.DefaultQualifier;
import org.eclipse.aas4j.v3.model.impl.DefaultRange;
import org.eclipse.aas4j.v3.model.impl.DefaultReference;
import org.eclipse.aas4j.v3.model.impl.DefaultReferenceElement;
import org.eclipse.aas4j.v3.model.impl.DefaultRelationshipElement;
import org.eclipse.aas4j.v3.model.impl.DefaultSpecificAssetId;
import org.eclipse.aas4j.v3.model.impl.DefaultSubmodel;
import org.eclipse.aas4j.v3.model.impl.DefaultSubmodelElementCollection;
import org.eclipse.aas4j.v3.model.impl.DefaultValueList;
import org.eclipse.aas4j.v3.model.impl.DefaultValueReferencePair;

import at.srfg.iasset.repository.utils.ReferenceUtils;

public class ApplicationTypes {
	public static final String LANGUAGE = "de";

    public static final AssetAdministrationShell AAS_ROOT = createRootApplication();

    public static final Environment ENVIRONMENT = createEnvironment();

    public static AssetAdministrationShell createRootApplication() {
    	return new DefaultAssetAdministrationShell.Builder()
    			.idShort("iAsset-ApplicationType")
    			.id("http://iasset.salzburgresearch.at/application")
    			.displayName(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset Root Application ").build()
    				)
    			.administration(new DefaultAdministrativeInformation.Builder()
    					.version("V0.01")
    					.revision("001")
    					.build()
    				)
    			.category("application")
    			.description(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset Root Application beschreibt den Einstiegspunkt bzw. das Top-Level-Element für Anwendungen")
    					.build())
    			.submodel(ReferenceUtils.toReference(createSubmodelForInfoModel()))
    			.submodel(ReferenceUtils.toReference(createSubmodelForRootEventConfiguration()))
    			.build();

    }
    
    public static AssetAdministrationShell createTypeAasForBelt() {
    	return new DefaultAssetAdministrationShell.Builder()
    			.id("http://iasset.salzburgresearch.at/labor/belt#aas")
    			.idShort("belt")
    			.assetInformation(new DefaultAssetInformation.Builder()
    					.assetKind(AssetKind.TYPE)
    					.globalAssetId(ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, "http://iasset.salzburgresearch.at/labor/belt"))
    					.specificAssetId(new DefaultSpecificAssetId.Builder()
    							.semanticId(ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, "http://specificAssetId.belt.com"))
    							.build() )
    					.build())
    			.displayName(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset Type for Belt").build()
    				)
    			.administration(new DefaultAdministrativeInformation.Builder()
    					.version("V0.01")
    					.version("001")
    					.build() )
    			.build();
    }
    public static Submodel createSubmodelForBeltInfo() {
    	return new DefaultSubmodel.Builder()
    			.idShort("beltInformation")
    			.id("http://iasset.salzburgresearch.at/labor/belt#info")
    			.displayName(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset Belt-Info Submodel").build()
    					)
    			.description(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset Information Submodel")
    					.build())
    			.kind(ModelingKind.TEMPLATE)
    			// the belt-info-Type refers to it's parent type
    			.semanticId(ReferenceUtils.toReference(createSubmodelForInfoModel()))
    			.submodelElement(new DefaultProperty.Builder()
    					.idShort("manufacturer")
    					.category("constant")
    					.kind(ModelingKind.INSTANCE)
    					.value("Hersteller Förderband")
    					.valueType(DataTypeDefXsd.STRING)
    					.semanticId(ReferenceUtils.toReference(createConceptDescriptionForManufacturerName()))
    					.build())
    			.submodelElement(new DefaultBlob.Builder()
    					.idShort("logo")
    					.kind(ModelingKind.INSTANCE)
    					.contentType("text/plain")
    					.value("An dieser Stelle kommt ein Logo".getBytes())
    					.build()
    					)
    			.build();
    }
    public static Submodel createSubmodelForBeltProperties() {
    	return new DefaultSubmodel.Builder()
    			.idShort("properties")
    			.id("http://iasset.salzburgresearch.at/labor/belt#properties")
    			.displayName(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset Belt-Propeties Submodel").build()
    					)
    			.description(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset Belt Properties Submodel")
    					.build())
    			.kind(ModelingKind.TEMPLATE)
    			// the belt-info-Type refers to it's parent type
    			.semanticId(ReferenceUtils.toReference(createSubmodelForInfoModel()))
    			.submodelElement(new DefaultSubmodelElementCollection.Builder()
    					.idShort("beltData")
    	    			.displayName(new DefaultLangString.Builder()
    	    					.language(LANGUAGE)
    	    					.text("i-Asset Belt-Data Contaoner").build()
    	    					)
    	    			.kind(ModelingKind.TEMPLATE)
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("state")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Status Förderband (on/off)").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.BOOLEAN)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("moving")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Förderband in Bewegung(on/off)").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.BOOLEAN)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("direction")
    	    					.category("variable")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Bewegungsrichtung").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("distance")
    	    					.category("variable")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("zurückgelegte Distanz").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.DECIMAL)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("timeElapsed")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Bewegungsdauer gesamt").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.DURATION)
    	    					.build())
    					.build())
    			.build();
    }
    public static Submodel createSubmodelForBeltOperation() {
    	return new DefaultSubmodel.Builder()
    			.id("http://iasset.salzburgresearch.at/labor/belt#operations")
    			.idShort("operations")
    			.kind(ModelingKind.TEMPLATE)
    			.displayName(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("Förderband Funktionen").build()
    					)
    			.submodelElement(new DefaultOperation.Builder()
    					.idShort("switchBusyLight")
    					.category("function")
    	    			.displayName(new DefaultLangString.Builder()
    	    					.language(LANGUAGE)
    	    					.text("Kontroll-Lampe ein/ausschalten").build()
    	    					)
    	    			.kind(ModelingKind.TEMPLATE)
    	    			.inoutputVariable(new DefaultOperationVariable.Builder()
    	    					.value(new DefaultProperty.Builder()
    	    							.idShort("state")
    	    							.category("constant")
    	    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    	    					.language(LANGUAGE)
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
    	    					.language(LANGUAGE)
    	    					.text("Band vor/zurück bewegen").build()
    	    					)
    	    			.kind(ModelingKind.TEMPLATE)
    	    			.inputVariable(new DefaultOperationVariable.Builder()
    	    					.value(new DefaultProperty.Builder()
    	    	    					.idShort("direction")
    	    	    					.category("constant")
    	    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    	    					.language(LANGUAGE)
    	    	    	    					.text("Bewegungsrichtung").build()
    	    	    	    					)
    	    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    	    					.build())
    	    					.value(new DefaultProperty.Builder()
    	    	    					.idShort("distance")
    	    	    					.category("constant")
    	    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    	    					.language(LANGUAGE)
    	    	    	    					.text("Zurückzulegende Entfernung").build()
    	    	    	    					)
    	    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    	    					.build())
    	    					.build())
    					.build())
    			.build();
    }
    public static Submodel createSubmodelForBeltEvents() {
    	return new DefaultSubmodel.Builder()
    			.id("http://iasset.salzburgresearch.at/labor/belt#events")
    			.idShort("events")
    			.kind(ModelingKind.TEMPLATE)
    			.displayName(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("Förderband Ereignisse").build()
    					)
    			.submodelElement(new DefaultBasicEventElement.Builder()
    					.idShort("beltEvent")
    					.kind(ModelingKind.TEMPLATE)
    					.observed(new DefaultReference.Builder()
    							.type(ReferenceTypes.MODEL_REFERENCE)
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.SUBMODEL)
    									.value(createSubmodelForBeltProperties().getId())
    									.build())
    							.key(new DefaultKey.Builder()
    									.type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION)
    									.value("beltData")
    									.build())
    							.build())
    					.messageBroker(ReferenceUtils.toReference(createConceptDescriptionForMessageBroker()))
    					.build())
    			.build();
    }
    public static ConceptDescription createConceptDescriptionForManufacturerName() {
    	return new DefaultConceptDescription.Builder()
    			.id("0173-1#02-AAO677#002")
    			.idShort("manufacturer")
    			.displayName(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("Hersteller").build()
    					)
    			.embeddedDataSpecification(new DefaultDataSpecification.Builder()
    					.dataSpecificationContent(new DefaultDataSpecificationIEC61360.Builder()
    							.levelType(LevelType.NOM)
    							.preferredName(new DefaultLangString.Builder()
			    					.language(LANGUAGE)
			    					.text("Hersteller").build()
			    					)
    							.shortName(new DefaultLangString.Builder()
			    					.language(LANGUAGE)
			    					.text("manufactuerer").build())
    							.dataType(DataTypeIEC61360.STRING)
    							.build())
    					.id("0173-1#02-AAO677#002")
    					.build())
    			.isCaseOf(ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, "0173-1#02-AAO677#002"))
    			.build();
    }
    public static Submodel createSubmodelForInfoModel() {
    	return new DefaultSubmodel.Builder()
    			.idShort("rootInfoModel")
    			.id("http://iasset.salzburgresearch.at/application#info")
    			.displayName(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset Information Submodel").build()
    					)
    			.description(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset Information Submodel")
    					.build())
    			.kind(ModelingKind.TEMPLATE)
    			.semanticId(ReferenceUtils.asGlobalReference(KeyTypes.CONCEPT_DESCRIPTION, "http://iasset.salzburgresearch.at/concept/nameplate"))
    			.submodelElement(new DefaultProperty.Builder()
    					.idShort("owner")
    					.value("i-Twin Project @ SalzburgResearch")
    					.valueType(DataTypeDefXsd.STRING)
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
    
    public static ConceptDescription createConceptDescriptionForMessageBrokerHosts() {
        return new DefaultConceptDescription.Builder()
        		.id("http://iasset.salzburgresearch.at/data/messageBroker/hosts")
                .idShort("hosts")
                .category("ConceptProperty")
                .descriptions(Arrays.asList(
                        new DefaultLangString.Builder().text("Concept referencing the messageBroker's host definition").language("en").build(),
                        new DefaultLangString.Builder().text("Concept zum MessageBroker ").language("de").build()
                        ))
                .administration(new DefaultAdministrativeInformation.Builder()
                        .version("0.9")
                        .revision("0")
                        .build())
                .isCaseOf(ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, "http://iasset.salzburgresearch.at/data/messageBroker/hosts"))
                .build();
    }
    
    public static ConceptDescription createConceptDescriptionForMessageBrokerBrokerType() {
        return new DefaultConceptDescription.Builder()
        		.id("http://iasset.salzburgresearch.at/data/messageBroker/brokerType")
                .idShort("brokerType")
                .category("ConceptProperty")
                .descriptions(Arrays.asList(
                        new DefaultLangString.Builder().text("Concept referencing the messageBroker's type definiton").language("en").build(),
                        new DefaultLangString.Builder().text("Concept zum MessageBroker ").language("de").build()
                        ))
                .administration(new DefaultAdministrativeInformation.Builder()
                        .version("0.9")
                        .revision("0")
                        .build())
                .isCaseOf(ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, "http://iasset.salzburgresearch.at/data/messageBroker/brokerType"))
                .build();
    }
    public static ConceptDescription createConceptDescriptionForSensorEventType() {
        return new DefaultConceptDescription.Builder()
        		.id("http://iasset.salzburgresearch.at/data/sensor/eventType")
                .idShort("sensorEventType")
                .category("ConceptClass")
                .descriptions(Arrays.asList(
                        new DefaultLangString.Builder().text("Concept referencing the messageBroker's type definiton").language("en").build(),
                        new DefaultLangString.Builder().text("Concept zum MessageBroker ").language("de").build()
                        ))
                .administration(new DefaultAdministrativeInformation.Builder()
                        .version("0.9")
                        .revision("0")
                        .build())
                .isCaseOf(ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, "http://iasset.salzburgresearch.at/data/sensor/eventType"))
                .build();
    }
    public static Submodel createSubmodelForRootEventConfiguration() {
    	return new DefaultSubmodel.Builder()
    			.idShort("rootInfoModel")
    			.id("http://iasset.salzburgresearch.at/application#eventConfig")
    			.displayName(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset Event Configuration Submodel")
    					.build()
    				)
    			.description(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset Event Configuration Submodel defining Message Broker config")
    					.build()
    				)
    			.kind(ModelingKind.TEMPLATE)
    			.semanticId(ReferenceUtils.asGlobalReference(KeyTypes.CONCEPT_DESCRIPTION, "http://iasset.salzburgresearch.at/concept/eventConfig"))
    			.submodelElement(
    					// SubmodelElementCollection holding hosts & broker type
    					new DefaultSubmodelElementCollection.Builder()
    					.idShort("messageBroker")
    					.kind(ModelingKind.TEMPLATE)
    	    			.displayName(new DefaultLangString.Builder()
    	    					.language(LANGUAGE)
    	    					.text("i-Asset Message Broker Config").build()
    	    				)
    	    			.semanticId(ReferenceUtils.asGlobalReference(KeyTypes.CONCEPT_DESCRIPTION, "http://iasset.salzburgresearch.at/data/messageBroker"))
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("hosts")
    	    					.kind(ModelingKind.TEMPLATE)
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("i-Asset Message Broker Hosts").build()
    	    	    				)
    	    	    			.value("tcp://localhost:1883")
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    	    			.semanticId(ReferenceUtils.asGlobalReference(KeyTypes.CONCEPT_DESCRIPTION, "http://iasset.salzburgresearch.at/data/messageBroker/hosts"))
    	    					.build()
    	    				)
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("brokerType")
    	    					.kind(ModelingKind.TEMPLATE)
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("i-Asset Message Broker Type").build()
    	    	    				)
    	    	    			.value("MQTT")
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    	    			.semanticId(ReferenceUtils.asGlobalReference(KeyTypes.CONCEPT_DESCRIPTION, "http://iasset.salzburgresearch.at/data/messageBroker/brokerType"))
    	    					.build()
    	    				)
    					.build()
    				)
    			.submodelElement(new DefaultBasicEventElement.Builder()
    					.idShort("sensorData")
    					.kind(ModelingKind.TEMPLATE)
    					.direction(Direction.OUTPUT)
    					.messageBroker(ReferenceUtils.toReference(createConceptDescriptionForMessageBroker()))
    					.messageTopic("sensorData")
    					.semanticId(ReferenceUtils.toReference(createConceptDescriptionForSensorEventType()))
    					.build()
    				)
    			.build();
    }

    public static Environment createEnvironment() {
        return new DefaultEnvironment.Builder()
                .assetAdministrationShells(Arrays.asList(
                        	createRootApplication()
                		)
                	)
                .submodels(Arrays.asList(
                			createSubmodelForInfoModel(),
                			createSubmodelForRootEventConfiguration()
                		)
                	)
                .conceptDescriptions(Arrays.asList(
                        createConceptDescriptionForMessageBroker(),
                        createConceptDescriptionForMessageBrokerBrokerType(),
                        createConceptDescriptionForMessageBrokerHosts(),
                        createConceptDescriptionForSensorEventType()))
                .build();
    }

}
