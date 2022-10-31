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
import java.util.Base64;

import org.eclipse.aas4j.v3.model.*;
import org.eclipse.aas4j.v3.model.impl.*;

// TODO import org.eclipse.aas4j.v3.rc02.model.impl.DefaultEmbeddedDataSpecification;


public class AASPeak2Pi {

	public static final String LANGUAGE = "de";

    public static final AssetAdministrationShell AAS_Peak = createAAS1();
    public static final Submodel SUBMODEL_PEAK1 = createSubmodelForOEEProperties();
    public static final Submodel SUBMODEL_PEAK2 = createSubmodelForProduct();
    public static final Environment ENVIRONMENT = createEnvironment();

    public static AssetAdministrationShell createAAS1() {
        return new DefaultAssetAdministrationShell.Builder()
                .idShort("Peak2PiAssetAdministrationShell")
                .descriptions(Arrays.asList(
                        new DefaultLangString.Builder().text("An Example Asset Administration Shell for Peak2Pi").language("en-us").build(),
                        new DefaultLangString.Builder().text("Ein Beispiel-Verwaltungsschale für Peak2Pi").language("de").build()
                        ))
                .id("https://peak2pi.info/Peak2Pi_AssetAdministrationShell")
                .administration(new DefaultAdministrativeInformation.Builder()
                        .version("0.9")
                        .revision("0")
                        .build())
                .assetInformation(new DefaultAssetInformation.Builder()
                        .assetKind(AssetKind.INSTANCE)
                        .globalAssetId(new DefaultReference.Builder()
                                .key(new DefaultKey.Builder()
                                        .type(KeyTypes.ASSET_ADMINISTRATION_SHELL)
                                        .value("http://peak2pi.info/labor/oee")
                                        .build())
                                .type(ReferenceTypes.GLOBAL_REFERENCE)
                                .build())
                        //.billOfMaterial((new DefaultReference.Builder()
                        //        .key(new DefaultKey.Builder()
                        //                .type(KeyTypes.SUBMODEL)
                        //                .value("http://acplt.org/Submodels/Assets/TestAsset/BillOfMaterial")
                        //                .build()))
                        //        .build())
                        .build())
                .submodel(new DefaultReference.Builder()
                        .key(new DefaultKey.Builder()
                                .type(KeyTypes.SUBMODEL)
                                .value("http://peak2pi.info/labor/oee#properties")
                                .build())
                        .type(ReferenceTypes.GLOBAL_REFERENCE)
                        .build())
                .submodel(new DefaultReference.Builder()
                        .key(new DefaultKey.Builder()
                                .type(KeyTypes.SUBMODEL)
                                .value("http://peak2pi.info/labor/oee#product")
                                .build())
                        .type(ReferenceTypes.GLOBAL_REFERENCE)
                        .build())
                .submodel(new DefaultReference.Builder()
                        .key(new DefaultKey.Builder()
                                .type(KeyTypes.SUBMODEL)
                                .value("http://acplt.org/Submodels/Assets/TestAsset/BillOfMaterial")
                                .build())
                        .type(ReferenceTypes.GLOBAL_REFERENCE)
                        .build())
                .submodel(new DefaultReference.Builder()
                        .key(new DefaultKey.Builder()
                                .type(KeyTypes.SUBMODEL)
                                .value("http://acplt.org/Submodels/Assets/TestAsset/Identification")
                                .build())
                        .type(ReferenceTypes.GLOBAL_REFERENCE)
                        .build())
                .submodel(new DefaultReference.Builder()
                        .key(new DefaultKey.Builder()
                                .type(KeyTypes.SUBMODEL)
                                .value(" https://acplt.org/Test_Submodel_Template")
                                .build())
                        .type(ReferenceTypes.GLOBAL_REFERENCE)
                        .build())
                .build();
    }
    
    public static Submodel createSubmodelForOEEProperties() {
    	return new DefaultSubmodel.Builder()
    			.idShort("properties")
    			.id("http://peak2pi.info/labor/oee#properties")
    			.displayName(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset OEE-Propeties Submodel").build()
    					)
    			.description(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset OEE Properties Submodel")
    					.build())
    			.kind(ModelingKind.INSTANCE)
    			// the belt-info-Type refers to it's parent type
    			.submodelElement(new DefaultSubmodelElementCollection.Builder()
    					.idShort("oeeData")
    	    			.displayName(new DefaultLangString.Builder()
    	    					.language(LANGUAGE)
    	    					.text("i-Asset OEE Daten").build()
    	    					)
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
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("quantity")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Stückzahl").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.DECIMAL)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("setvalue")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Sollwert").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.DECIMAL)
    	    					.build())
    					.build())
    			.build();
    }
    
    public static Submodel createSubmodelForProduct() {
    	return new DefaultSubmodel.Builder()
    			.idShort("product")
    			.id("http://peak2pi.info/labor/oee#product")
    			.displayName(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset Product-Propeties Submodel").build()
    					)
    			.description(new DefaultLangString.Builder()
    					.language(LANGUAGE)
    					.text("i-Asset Product Properties Submodel")
    					.build())
    			.kind(ModelingKind.INSTANCE)
    			// the belt-info-Type refers to it's parent type
    			.submodelElement(new DefaultSubmodelElementCollection.Builder()
    					.idShort("product")
    	    			.displayName(new DefaultLangString.Builder()
    	    					.language(LANGUAGE)
    	    					.text("i-Asset Product Daten").build()
    	    					)
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("productId")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Produkt ID").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("validFrom")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Gültig seit").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.DATE)
    	    					.build())
    	    			.value(new DefaultProperty.Builder()
    	    					.idShort("quantity")
    	    	    			.displayName(new DefaultLangString.Builder()
    	    	    					.language(LANGUAGE)
    	    	    					.text("Vorgabe").build()
    	    	    					)
    	    	    			.valueType(DataTypeDefXsd.STRING)
    	    					.build())
    					.build())
    			.build();
    }
    

    
    


    public static Environment createEnvironment() {
        return new DefaultEnvironment.Builder()
                .assetAdministrationShells(Arrays.asList(
                        createAAS1()))
                .submodels(Arrays.asList(
                		createSubmodelForOEEProperties(),
                		createSubmodelForProduct()))
                .build();
    }

}
