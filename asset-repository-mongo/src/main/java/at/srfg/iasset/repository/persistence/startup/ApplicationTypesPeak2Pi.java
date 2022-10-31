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

public class ApplicationTypesPeak2Pi {
	public static final String LANGUAGE = "de";

    //public static final AssetAdministrationShell AAS_ROOT = createRootApplication();

    public static final Environment ENVIRONMENT = createEnvironment();

    
    public static AssetAdministrationShell createP2PiRootApplication() {
    	return new DefaultAssetAdministrationShell.Builder()
    			.idShort("iAsset-ApplicationType")
    			.id("http://peak2pi.info/application")
    			.displayName(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("Peak2Pi i-Asset Root Application ").build()
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
    			.submodel(ReferenceUtils.fromIdentifiable(createSubmodelForInfoModel()))
    			//.submodel(ReferenceUtils.fromIdentifiable(createSubmodelForRootEventConfiguration()))
    			.build();

    }
    
    public static Submodel createSubmodelForInfoModel() {
    	return new DefaultSubmodel.Builder()
    			.idShort("rootInfoModel")
    			.id("http://peak2pi.info/application/application#info")
    			.displayName(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("Peak2Pi Information Submodel").build()
    					)
    			.description(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("Peak2Pi Information Submodel")
    					.build())
    			.kind(ModelingKind.TEMPLATE)
    			.semanticId(ReferenceUtils.asGlobalReference(KeyTypes.CONCEPT_DESCRIPTION, "http://iasset.salzburgresearch.at/concept/nameplate"))
    			.submodelElement(new DefaultProperty.Builder()
    					.idShort("owner")
    					.value("Peak2Pi @ IcoSense GmbH")
    					.valueType(DataTypeDefXsd.STRING)
    					.build())
    			.build();
    }
    
    public static AssetAdministrationShell createTypeAasForOEE() {
    	return new DefaultAssetAdministrationShell.Builder()
    			.id("http://peak2pi.info/labor/oee#aas")
    			.idShort("oee")
    			.assetInformation(new DefaultAssetInformation.Builder()
    					.assetKind(AssetKind.TYPE)
    					.globalAssetId(ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, "http://peak2pi.info/labor/oee"))
    					.specificAssetId(new DefaultSpecificAssetId.Builder()
    							.semanticId(ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, "http://peak2pi.info"))
    							.build() )
    					.build())
    			.displayName(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset Type for OEE").build()
    				)
    			.administration(new DefaultAdministrativeInformation.Builder()
    					.version("V0.01")
    					.version("001")
    					.build() )
    			.build();
    }
    
    public static Submodel createSubmodelForOEEInfo() {
    	return new DefaultSubmodel.Builder()
    			.idShort("oeeInformation")
    			.id("http://peak2pi.info/labor/oee#aas")
    			.displayName(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset OEE-Info Submodel").build()
    					)
    			.description(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset Information Submodel")
    					.build())
    			.kind(ModelingKind.TEMPLATE)
    			// the belt-info-Type refers to it's parent type
    			.semanticId(ReferenceUtils.fromIdentifiable(createSubmodelForInfoModel()))
//    			.submodelElement(new DefaultProperty.Builder()
//    					.idShort("manufacturer")
//    					.category("constant")
//    					.kind(ModelingKind.INSTANCE)
//    					.value("Hersteller Anlage")
//    					.valueType(DataTypeDefXsd.STRING)
//    					.semanticId(ReferenceUtils.fromIdentifiable(createConceptDescriptionForManufacturerName()))
//    					.build())
//    			.submodelElement(new DefaultBlob.Builder()
//    					.idShort("logo")
//    					.kind(ModelingKind.INSTANCE)
//    					.contentType("text/plain")
//    					.value("An dieser Stelle kommt ein Logo".getBytes())
//    					.build())
    			.build();
    }
    
    public static Submodel createSubmodelForOEEProperties() {
    	return new DefaultSubmodel.Builder()
    			.idShort("properties")
    			.id("http://peak2pi.info/labor/oee#properties")
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
    			.semanticId(ReferenceUtils.fromIdentifiable(createSubmodelForInfoModel()))
    			.submodelElement(new DefaultSubmodelElementCollection.Builder()
    					.idShort("oeeData")
    	    			.displayName(new DefaultLangString.Builder()
    	    					.language(LANGUAGE)
    	    					.text("i-Asset OEE Daten").build()
    	    					)
    	    			.kind(ModelingKind.TEMPLATE)
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("oee")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("OEE Anlageneffizienz").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.DECIMAL)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("capacity")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("OEE Kapazität").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.DECIMAL)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("quality")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("OEE Qualität").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.DECIMAL)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("efficiency")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("OEE Effizienz").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.DECIMAL)
    	    					.build())
    					.build())
    			.build();
    }
    
//    public static Submodel createSubmodelForOEEEvents() {
//    	return new DefaultSubmodel.Builder()
//    			.id("http://peak2pi.info/labor/oee#events")
//    			.idShort("events")
//    			.kind(ModelingKind.TEMPLATE)
//    			.displayName(new DefaultLangString.Builder()
//    					.language(LANGUAGE)
//    					.text("OEE Ereignisse").build()
//    					)
//    			.submodelElement(new DefaultBasicEventElement.Builder()
//    					.idShort("oeeEvent")
//    					.kind(ModelingKind.TEMPLATE)
//    					.observed(new DefaultReference.Builder()
//    							.type(ReferenceTypes.MODEL_REFERENCE)
//    							.key(new DefaultKey.Builder()
//    									.type(KeyTypes.SUBMODEL)
//    									.value(createSubmodelForOEEProperties().getId())
//    									.build())
//    							.key(new DefaultKey.Builder()
//    									.type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION)
//    									.value("oeeData")
//    									.build())
//    							.build())
//    					.messageBroker(ReferenceUtils.fromIdentifiable(createConceptDescriptionForMessageBroker()))
//    					.build())
//    			.build();
//    }
    

    

    public static Environment createEnvironment() {
        return new DefaultEnvironment.Builder()
                .assetAdministrationShells(Arrays.asList(
                        	createP2PiRootApplication()
                		)
                	)
                .submodels(Arrays.asList(
            			createSubmodelForInfoModel(),
            			createSubmodelForOEEProperties()
            		)
            	)
                .build();
    }

}
