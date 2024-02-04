package at.srfg.iasset.repository.model;

import java.util.UUID;

import org.eclipse.digitaltwin.aas4j.v3.model.AasSubmodelElements;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXsd;
import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.ModellingKind;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetInformation;
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
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodelElementList;




public class AASZenonVariable {

    public static final String LANGUAGE = "de";

    public static final AssetAdministrationShell ZENON_AAS_VARS = createAASforZenonVariable();
    public static final Submodel ZENON_SUBMODEL_VARS = createSubmodelForZenonVariableOperation();

    private static AssetAdministrationShell createAASforZenonVariable() {
        return new DefaultAssetAdministrationShell.Builder()
                .id("https://iasset.salzburgresearch.at/zenon/demo")
                .displayName(new DefaultLangString.Builder()
                        .language(LANGUAGE)
                        .text("Zenon AAS")
                        .build()
                )
                .assetInformation(new DefaultAssetInformation.Builder()
                        .globalAssetId("urn:zenon:demo")
                        .specificAssetId(new DefaultSpecificAssetId.Builder()
                                .name("zenonIdentifier")
                                .value(UUID.randomUUID().toString())
                                .build())
                        .build())
                .description(new DefaultLangString.Builder()
                        .language(LANGUAGE)
                        .text("Zenon Variablen")
                        .build())
                .build();
    }
    private static Submodel createSubmodelForZenonVariableOperation() {
        return new DefaultSubmodel.Builder()
                .idShort("zenonVariable")
                .id("https://iasset.salzburgresearch.at/zenon/variable")
                .displayName(new DefaultLangString.Builder()
                        .language(LANGUAGE)
                        .text("Zenon Variable")
                        .build()
                )
                .description(new DefaultLangString.Builder()
                        .language(LANGUAGE)
                        .text("Teilmodell für Zenon Variablen")
                        .build())
                .kind(ModellingKind.TEMPLATE)
                .submodelElement(new DefaultSubmodelElementCollection.Builder()
                        .idShort("variable")
                        .displayName(new DefaultLangString.Builder()
                                .language(LANGUAGE)
                                .text("zenon Variable Structure")
                                .build()
                        )
                        .description(new DefaultLangString.Builder()
                                .language(LANGUAGE)
                                .text("Complex structure for a single zenon variable")
                                .build())
                        .semanticId(new DefaultExternalReference.Builder()
                                .key(new DefaultKey.Builder()
                                        .type(KeyTypes.GLOBAL_REFERENCE)
                                        .value("http://iasset.salzburgresearch.at/zenon/variable")
                                        .build())
                                .build())
                        // ID PROPERTY
                        .value(new DefaultProperty.Builder()
                                .idShort("variableName")
                                .displayName(new DefaultLangString.Builder()
                                        .language(LANGUAGE)
                                        .text("Wert aus variableName")
                                        .build()
                                )
                                .valueType(DataTypeDefXsd.STRING)
                                .build())
                        .value(new DefaultProperty.Builder()
                                .idShort("displayName")
                                .displayName(new DefaultLangString.Builder()
                                        .language(LANGUAGE)
                                        .text("Wert aus displayName")
                                        .build()
                                )
                                .valueType(DataTypeDefXsd.STRING)
                                .build())
                        .value(new DefaultProperty.Builder()
                                .idShort("identification")
                                .displayName(new DefaultLangString.Builder()
                                        .language(LANGUAGE)
                                        .text("Wert aus identification")
                                        .build()
                                )
                                .valueType(DataTypeDefXsd.STRING)
                                .build())
                        .value(new DefaultProperty.Builder()
                                .idShort("description")
                                .displayName(new DefaultLangString.Builder()
                                        .language(LANGUAGE)
                                        .text("Wert aus description")
                                        .build()
                                )
                                .valueType(DataTypeDefXsd.STRING)
                                .build())
                        .value(new DefaultProperty.Builder()
                                .idShort("dataType")
                                .displayName(new DefaultLangString.Builder()
                                        .language(LANGUAGE)
                                        .text("Wert aus dataType")
                                        .build()
                                )
                                .valueType(DataTypeDefXsd.STRING)
                                .build())
                        .value(new DefaultProperty.Builder()
                                .idShort("resourcesLabel")
                                .displayName(new DefaultLangString.Builder()
                                        .language(LANGUAGE)
                                        .text("Wert aus resourcesLabel")
                                        .build()
                                )
                                .valueType(DataTypeDefXsd.STRING)
                                .build())
                        .value(new DefaultProperty.Builder()
                                .idShort("measuringUnit")
                                .displayName(new DefaultLangString.Builder()
                                        .language(LANGUAGE)
                                        .text("Wert aus measuringUnit")
                                        .build()
                                )
                                .valueType(DataTypeDefXsd.STRING)
                                .build())
//						.value(new DefaultProperty.Builder()
//								.idShort("timeGoes")
//								.displayName(new DefaultLangString.Builder()
//										.language(LANGUAGE)
//										.text("Datum des Auftretens")
//										.build()
//								)
//								.kind(ModelingKind.TEMPLATE)
//								.valueType(DataTypeDefXsd.DATE_TIME)
//								.build())
                        .build())
                .submodelElement(new DefaultOperation.Builder()
                        .idShort("zenonVariable")
                        .displayName(new DefaultLangString.Builder()
                                .language(LANGUAGE)
                                .text("Zenon Variable")
                                .build()
                        )
                        .semanticId(new DefaultExternalReference.Builder()
                                .key(new DefaultKey.Builder()
                                        .type(KeyTypes.GLOBAL_REFERENCE)
                                        .value("http://iasset.salzburgresearch.at/zenon/variable")
                                        .build())
                                .build())
                        .outputVariable(new DefaultOperationVariable.Builder()
                                .value(new DefaultSubmodelElementList.Builder()
                                        .idShort("result")
                                        .typeValueListElement(AasSubmodelElements.SUBMODEL_ELEMENT_COLLECTION)
                                        .semanticIdListElement(new DefaultModelReference.Builder()
                                                .key(new DefaultKey.Builder()
                                                        .type(KeyTypes.SUBMODEL)
                                                        .value("https://iasset.salzburgresearch.at/zenon/variable")
                                                        .build())
                                                .key(new DefaultKey.Builder()
                                                        .type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION)
                                                        .value("variable")
                                                        .build())
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();
    }
}