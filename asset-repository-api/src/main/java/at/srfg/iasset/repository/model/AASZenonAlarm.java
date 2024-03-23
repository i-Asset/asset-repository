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




public class AASZenonAlarm {

	public static final String LANGUAGE = "de";

public static final AssetAdministrationShell ZENON_AAS_ALARMS = createAASforZenonAlarm();
	public static final Submodel ZENON_SUBMODEL_ALARMS = createSubmodelForZenonAlarmOperation();

	private static AssetAdministrationShell createAASforZenonAlarm() {
		return new DefaultAssetAdministrationShell.Builder()
				.id("https://iasset.salzburgresearch.at/zenon/alarm")
				.displayName(new DefaultLangString.Builder()
						.language(LANGUAGE)
						.text("Zenon AAS")
						.build()
				)
				.assetInformation(new DefaultAssetInformation.Builder()
						.globalAssetId("urn:zenon:alarm")
						.specificAssetId(new DefaultSpecificAssetId.Builder()
								.name("zenonIdentifier")
								.value(UUID.randomUUID().toString())
								.build())
						.build())
				.description(new DefaultLangString.Builder()
						.language(LANGUAGE)
						.text("Zenon Alarme")
						.build())
				.build();
	}
	private static Submodel createSubmodelForZenonAlarmOperation() {
		return new DefaultSubmodel.Builder()
				.idShort("zenonAlarm")
				.id("https://iasset.salzburgresearch.at/zenon/alarmDemo")
				.displayName(new DefaultLangString.Builder()
						.language(LANGUAGE)
						.text("Zenon Alarm")
						.build()
				)
				.description(new DefaultLangString.Builder()
						.language(LANGUAGE)
						.text("Teilmodell für Zenon Alarme")
						.build())
				.kind(ModellingKind.TEMPLATE)
				.submodelElement(new DefaultSubmodelElementCollection.Builder()
						.idShort("alarm")
						.displayName(new DefaultLangString.Builder()
								.language(LANGUAGE)
								.text("zenon Alarm Structure")
								.build()
						)
						.description(new DefaultLangString.Builder()
								.language(LANGUAGE)
								.text("Complex structure for a single zenon Alarm")
								.build())
						.semanticId(new DefaultExternalReference.Builder()
								.key(new DefaultKey.Builder()
										.type(KeyTypes.GLOBAL_REFERENCE)
										.value("http://iasset.salzburgresearch.at/zenon/alarm")
										.build())
								.build())
						// ID PROPERTY
						.value(new DefaultProperty.Builder()
								.idShort("variable")
								.displayName(new DefaultLangString.Builder()
										.language(LANGUAGE)
										.text("Wert aus variable.variableName")
										.build()
								)
								.valueType(DataTypeDefXsd.STRING)
								.build())
						.value(new DefaultProperty.Builder()
								.idShort("alarmText")
								.displayName(new DefaultLangString.Builder()
										.language(LANGUAGE)
										.text("Text der Alarmmeldung")
										.build()
								)
								.valueType(DataTypeDefXsd.STRING)
								.build()) 
						.value(new DefaultProperty.Builder()
								.idShort("alarmClass")
								.displayName(new DefaultLangString.Builder()
										.language(LANGUAGE)
										.text("Name der Alarmklasse")
										.build()
								)
								.valueType(DataTypeDefXsd.STRING)
								.build()) 
						.value(new DefaultProperty.Builder()
								.idShort("alarmGroup")
								.displayName(new DefaultLangString.Builder()
										.language(LANGUAGE)
										.text("Name der Alarmgruppe")
										.build()
								)
								.valueType(DataTypeDefXsd.STRING)
								.build()) 
						.value(new DefaultProperty.Builder()
								.idShort("timeComes")
								.displayName(new DefaultLangString.Builder()
										.language(LANGUAGE)
										.text("Zeitstempel des Auftretens")
										.build()
								)
								.valueType(DataTypeDefXsd.DATE_TIME)
								.build()) 
						.value(new DefaultProperty.Builder()
								.idShort("timeGoes")
								.displayName(new DefaultLangString.Builder()
										.language(LANGUAGE)
										.text("Zeitstempel der Quittierung")
										.build()
								)
								.valueType(DataTypeDefXsd.DATE_TIME)
								.build())
						.build())
				.submodelElement(new DefaultOperation.Builder()
						.idShort("zenonAlarm")
						.displayName(new DefaultLangString.Builder()
								.language(LANGUAGE)
								.text("Zenon Alarme abrufen")
								.build()
								)
						.semanticId(new DefaultExternalReference.Builder()
								.key(new DefaultKey.Builder()
										.type(KeyTypes.GLOBAL_REFERENCE)
										.value("http://iasset.salzburgresearch.at/zenon/alarm")
										.build())
								.build())
						.inputVariable(new DefaultOperationVariable.Builder()
								.value(new DefaultProperty.Builder()
										.idShort("timeFrom")
										.displayName(new DefaultLangString.Builder()
												.language(LANGUAGE)
												.text("Zeitstempel, ab dem die Alarme abgefragt werden")
												.build()
												)
										.valueType(DataTypeDefXsd.DATE_TIME)
										.build())
								.build())
						.inputVariable(new DefaultOperationVariable.Builder()
								.value(new DefaultProperty.Builder()
										.idShort("timeTo")
										.displayName(new DefaultLangString.Builder()
												.language(LANGUAGE)
												.text("Zeitstempel, bis zu dem die Alarme abgefragt werden")
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
														.value("https://iasset.salzburgresearch.at/zenon/alarmDemo")
														.build())
												.key(new DefaultKey.Builder()
														.type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION)
														.value("alarm")
														.build())
												.build())
										.build())
								.build())
						.build())
				.build();
	}
}