package at.srfg.iasset.repository.model;

import org.eclipse.aas4j.v3.model.AasSubmodelElements;
import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.ConceptDescription;
import org.eclipse.aas4j.v3.model.DataSpecification;
import org.eclipse.aas4j.v3.model.DataTypeDefXsd;
import org.eclipse.aas4j.v3.model.DataTypeIEC61360;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.ModelingKind;
import org.eclipse.aas4j.v3.model.ReferenceTypes;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.impl.DefaultAdministrativeInformation;
import org.eclipse.aas4j.v3.model.impl.DefaultAssetAdministrationShell;
import org.eclipse.aas4j.v3.model.impl.DefaultConceptDescription;
import org.eclipse.aas4j.v3.model.impl.DefaultDataSpecification;
import org.eclipse.aas4j.v3.model.impl.DefaultDataSpecificationIEC61360;
import org.eclipse.aas4j.v3.model.impl.DefaultExtension;
import org.eclipse.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.aas4j.v3.model.impl.DefaultLangString;
import org.eclipse.aas4j.v3.model.impl.DefaultOperation;
import org.eclipse.aas4j.v3.model.impl.DefaultOperationVariable;
import org.eclipse.aas4j.v3.model.impl.DefaultProperty;
import org.eclipse.aas4j.v3.model.impl.DefaultReference;
import org.eclipse.aas4j.v3.model.impl.DefaultReferenceElement;
import org.eclipse.aas4j.v3.model.impl.DefaultSubmodel;
import org.eclipse.aas4j.v3.model.impl.DefaultSubmodelElementCollection;
import org.eclipse.aas4j.v3.model.impl.DefaultSubmodelElementList;
import org.eclipse.aas4j.v3.model.impl.DefaultValueList;
import org.eclipse.aas4j.v3.model.impl.DefaultValueReferencePair;




public class AASZenonAlarm {

	public static final String LANGUAGE = "de";

public static final AssetAdministrationShell ZENON_AAS = createAASforZenonAlarm();
	public static final Submodel ZENON_SUBMODEL = createSubmodelForZenonAlarmOperation();

	private static AssetAdministrationShell createAASforZenonAlarm() {
		return new DefaultAssetAdministrationShell.Builder()
				.id("https://iasset.salzburgresearch.at/zenon/jonas")
				.displayName(new DefaultLangString.Builder()
						.language(LANGUAGE)
						.text("Zenon AAS")
						.build()
				)
				.description(new DefaultLangString.Builder()
						.language(LANGUAGE)
						.text("Zenon Alarme")
						.build())
				.build();
	}
	private static Submodel createSubmodelForZenonAlarmOperation() {
		return new DefaultSubmodel.Builder()
				.idShort("zenonAlarm")
				.id("https://iasset.salzburgresearch.at/zenon/alarm")
				.displayName(new DefaultLangString.Builder()
						.language(LANGUAGE)
						.text("Zenon Alarm")
						.build()
				)
				.description(new DefaultLangString.Builder()
						.language(LANGUAGE)
						.text("Teilmodell für Zenon Alarme")
						.build())
				.kind(ModelingKind.TEMPLATE)
				.submodelElement(new DefaultSubmodelElementCollection.Builder()
						.idShort("alarm")
						.kind(ModelingKind.TEMPLATE)
						.displayName(new DefaultLangString.Builder()
								.language(LANGUAGE)
								.text("zenon Alarm Structure")
								.build()
						)
						.description(new DefaultLangString.Builder()
								.language(LANGUAGE)
								.text("Complex structure for a single zenon Alarm")
								.build())
						.semanticId(new DefaultReference.Builder()
								.type(ReferenceTypes.GLOBAL_REFERENCE)
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
								.kind(ModelingKind.TEMPLATE)
								.valueType(DataTypeDefXsd.STRING)
								.build()) 
						.value(new DefaultProperty.Builder()
								.idShort("alarmText")
								.displayName(new DefaultLangString.Builder()
										.language(LANGUAGE)
										.text("Wert aus alarmText")
										.build()
								)
								.kind(ModelingKind.TEMPLATE)
								.valueType(DataTypeDefXsd.STRING)
								.build()) 
						.value(new DefaultProperty.Builder()
								.idShort("alarmClass")
								.displayName(new DefaultLangString.Builder()
										.language(LANGUAGE)
										.text("Wert aus alarmClass.name")
										.build()
								)
								.kind(ModelingKind.TEMPLATE)
								.valueType(DataTypeDefXsd.STRING)
								.build()) 
						.value(new DefaultProperty.Builder()
								.idShort("alarmGroup")
								.displayName(new DefaultLangString.Builder()
										.language(LANGUAGE)
										.text("Wert aus alarmGroup.name")
										.build()
								)
								.kind(ModelingKind.TEMPLATE)
								.valueType(DataTypeDefXsd.STRING)
								.build()) 
						.value(new DefaultProperty.Builder()
								.idShort("timeComes")
								.displayName(new DefaultLangString.Builder()
										.language(LANGUAGE)
										.text("Datum des Auftretens")
										.build()
								)
								.kind(ModelingKind.TEMPLATE)
								.valueType(DataTypeDefXsd.DATE_TIME)
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
						.idShort("zenonAlarm")
						.displayName(new DefaultLangString.Builder()
								.language(LANGUAGE)
								.text("Datum des Auftretens")
								.build()
								)
						.semanticId(new DefaultReference.Builder()
								.type(ReferenceTypes.GLOBAL_REFERENCE)
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
												.text("Zeitstempel, ab dem die Alarme abgeholt werden")
												.build()
												)
										.valueType(DataTypeDefXsd.DATE_TIME)
										.build())
								.value(new DefaultProperty.Builder()
										.idShort("timeTo")
										.displayName(new DefaultLangString.Builder()
												.language(LANGUAGE)
												.text("Zeitstempel, bis zu dem die Alarme abgeholt werden")
												.build()
												)
										.valueType(DataTypeDefXsd.DATE_TIME)
										.build())
								.build())
						.outputVariable(new DefaultOperationVariable.Builder()
								.value(new DefaultSubmodelElementList.Builder()
										.idShort("result")
										.kind(ModelingKind.TEMPLATE)
										.typeValueListElement(AasSubmodelElements.SUBMODEL_ELEMENT_COLLECTION)
										.semanticIdListElement(new DefaultReference.Builder()
												.type(ReferenceTypes.MODEL_REFERENCE)
												.key(new DefaultKey.Builder()
														.type(KeyTypes.SUBMODEL)
														.value("https://iasset.salzburgresearch.at/zenon/alarm")
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
