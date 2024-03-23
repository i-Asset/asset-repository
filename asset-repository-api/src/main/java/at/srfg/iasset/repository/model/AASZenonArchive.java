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




public class AASZenonArchive {

    public static final String LANGUAGE = "de";

    public static final AssetAdministrationShell ZENON_AAS_ARCHIVES = createAASforZenonArchive();
    public static final Submodel ZENON_SUBMODEL_ARCHIVES = createSubmodelForZenonArchiveOperation();

    private static AssetAdministrationShell createAASforZenonArchive() {
        return new DefaultAssetAdministrationShell.Builder()
                .id("https://iasset.salzburgresearch.at/zenon/archive")
                .displayName(new DefaultLangString.Builder()
                        .language(LANGUAGE)
                        .text("Zenon AAS")
                        .build()
                )
                .assetInformation(new DefaultAssetInformation.Builder()
                        .globalAssetId("urn:zenon:archive")
                        .specificAssetId(new DefaultSpecificAssetId.Builder()
                                .name("zenonIdentifier")
                                .value(UUID.randomUUID().toString())
                                .build())
                        .build())
                .description(new DefaultLangString.Builder()
                        .language(LANGUAGE)
                        .text("Zenon Archive")
                        .build())
                .build();
    }
    private static Submodel createSubmodelForZenonArchiveOperation() {
        return new DefaultSubmodel.Builder()
                .idShort("zenonArchive")
                .id("https://iasset.salzburgresearch.at/zenon/archiveDemo")
                .displayName(new DefaultLangString.Builder()
                        .language(LANGUAGE)
                        .text("Zenon Archive")
                        .build()
                )
                .description(new DefaultLangString.Builder()
                        .language(LANGUAGE)
                        .text("Teilmodell für Zenon Archive")
                        .build())
                .kind(ModellingKind.TEMPLATE)
                .submodelElement(new DefaultSubmodelElementCollection.Builder()
                        .idShort("archive")
                        .displayName(new DefaultLangString.Builder()
                                .language(LANGUAGE)
                                .text("zenon Archive Structure")
                                .build()
                        )
                        .description(new DefaultLangString.Builder()
                                .language(LANGUAGE)
                                .text("Complex structure for a single zenon Archive")
                                .build())
                        .semanticId(new DefaultExternalReference.Builder()
                                .key(new DefaultKey.Builder()
                                        .type(KeyTypes.GLOBAL_REFERENCE)
                                        .value("http://iasset.salzburgresearch.at/zenon/archive")
                                        .build())
                                .build())
                        // ID PROPERTY
                        .value(new DefaultProperty.Builder()
                                .idShort("variableName")
                                .displayName(new DefaultLangString.Builder()
                                        .language(LANGUAGE)
                                        .text("Name der Variable des Archives")
                                        .build()
                                )
                                .valueType(DataTypeDefXsd.STRING)
                                .build())
                        .value(new DefaultProperty.Builder()
                                .idShort("variableDataType")
                                .displayName(new DefaultLangString.Builder()
                                        .language(LANGUAGE)
                                        .text("Datentyp der Variable des Archives")
                                        .build()
                                )
                                .valueType(DataTypeDefXsd.STRING)
                                .build())
                        .value(new DefaultProperty.Builder()
                                .idShort("stringValue")
                                .displayName(new DefaultLangString.Builder()
                                        .language(LANGUAGE)
                                        .text("String-Wert")
                                        .build()
                                )
                                .valueType(DataTypeDefXsd.STRING)
                                .build())
                        .value(new DefaultProperty.Builder()
                                .idShort("numericValue")
                                .displayName(new DefaultLangString.Builder()
                                        .language(LANGUAGE)
                                        .text("Numerischer Wert")
                                        .build()
                                )
                                .valueType(DataTypeDefXsd.DOUBLE)
                                .build())
                        .value(new DefaultProperty.Builder()
                                .idShort("calculation")
                                .displayName(new DefaultLangString.Builder()
                                        .language(LANGUAGE)
                                        .text("Berechnung")
                                        .build()
                                )
                                .valueType(DataTypeDefXsd.STRING)
                                .build())
                        .value(new DefaultProperty.Builder()
                                .idShort("timestamp")
                                .displayName(new DefaultLangString.Builder()
                                        .language(LANGUAGE)
                                        .text("Zeitstempel")
                                        .build()
                                )
                                .valueType(DataTypeDefXsd.DATE_TIME)
                                .build())
                        .build())
                .submodelElement(new DefaultOperation.Builder()
                        .idShort("zenonArchive")
                        .displayName(new DefaultLangString.Builder()
                                .language(LANGUAGE)
                                .text("Zenon Archive abrufen")
                                .build()
                        )
                        .semanticId(new DefaultExternalReference.Builder()
                                .key(new DefaultKey.Builder()
                                        .type(KeyTypes.GLOBAL_REFERENCE)
                                        .value("http://iasset.salzburgresearch.at/zenon/archive")
                                        .build())
                                .build())
                        .inputVariable(new DefaultOperationVariable.Builder()
                                .value(new DefaultProperty.Builder()
                                        .idShort("archive")
                                        .displayName(new DefaultLangString.Builder()
                                                .language(LANGUAGE)
                                                .text("Name des Archives, das abgefragt werden soll")
                                                .build()
                                        )
                                        .valueType(DataTypeDefXsd.STRING)
                                        .build())
                                .build())
                        .inputVariable(new DefaultOperationVariable.Builder()
                                .value(new DefaultProperty.Builder()
                                        .idShort("startTime")
                                        .displayName(new DefaultLangString.Builder()
                                                .language(LANGUAGE)
                                                .text("Zeitstempel - Von")
                                                .build()
                                        )
                                        .valueType(DataTypeDefXsd.DATE_TIME)
                                        .build())
                                .build())
                        .inputVariable(new DefaultOperationVariable.Builder()
                                .value(new DefaultProperty.Builder()
                                        .idShort("endTime")
                                        .displayName(new DefaultLangString.Builder()
                                                .language(LANGUAGE)
                                                .text("Zeitstempel - Bis")
                                                .build()
                                        )
                                        .valueType(DataTypeDefXsd.DATE_TIME)
                                        .build())
                                .build())
                        .outputVariable(new DefaultOperationVariable.Builder()
                                .value(new DefaultSubmodelElementList.Builder()
                                        .idShort("result")
                                        .typeValueListElement(AasSubmodelElements.SUBMODEL_ELEMENT_COLLECTION)
                                        .semanticIdListElement(new DefaultModelReference.Builder()
                                                .key(new DefaultKey.Builder()
                                                        .type(KeyTypes.SUBMODEL)
                                                        .value("https://iasset.salzburgresearch.at/zenon/archiveDemo")
                                                        .build())
                                                .key(new DefaultKey.Builder()
                                                        .type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION)
                                                        .value("archive")
                                                        .build())
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();
    }
}