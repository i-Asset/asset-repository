package at.srfg.iasset.repository.model;

import org.eclipse.aas4j.v3.model.*;
import org.eclipse.aas4j.v3.model.impl.*;

// TODO import org.eclipse.aas4j.v3.rc02.model.impl.DefaultEmbeddedDataSpecification;


public class AASPlantStructureSubmodel {

	public static final String LANGUAGE = "de";

	public static final Submodel SUBMODEL_PLANT_STRUCTURE = createSubmodelForPlantStructure();
	public static final Submodel SUBMODEL_PLANT_STRUCTURE_REQUEST_OPERATION = createSubmodelForPlantStructureRequestOperation();

	private static Submodel createSubmodelForPlantStructure() {
		return new DefaultSubmodel.Builder()
				.idShort("properties")
				.id("http://iasset.salzburgresearch.at/common/plantStucture")
				.displayName(new DefaultLangString.Builder()
						.language(LANGUAGE)
						.text("i-Asset AnlagenstrukturElement Teilmodell")
						.build())
				.description(new DefaultLangString.Builder()
						.language(LANGUAGE)
						.text("Element einer Anlagenstruktur Teilmodell")
						.build())
				.kind(ModelingKind.TEMPLATE)
				.build();

	}

	private static Submodel createSubmodelForPlantStructureRequestOperation() {
		return new DefaultSubmodel.Builder()
				.idShort("operations")
				.id("http://iasset.salzburgresearch.at/common/plantStuctureRequestOperation")
				.displayName(new DefaultLangString.Builder()
						.language(LANGUAGE)
						.text("i-Asset Anlagenstruktur Abfrage Operation")
						.build()
				)
				.description(new DefaultLangString.Builder()
						.language(LANGUAGE)
						.text("Abfrage Operation für eine i-Asset Anlagenstruktur")
						.build())
				.kind(ModelingKind.TEMPLATE)
				.submodelElement(new DefaultOperation.Builder()
						.idShort("getPlantStructure")
						.displayName(new DefaultLangString.Builder()
								.language(LANGUAGE)
								.text("Anlagenstruktur abrufen").build())
						.inputVariable(new DefaultOperationVariable.Builder()
								.value(new DefaultProperty.Builder()
										.idShort("lastChange")
										.valueType(DataTypeDefXsd.DATE_TIME)
										.displayName(new DefaultLangString.Builder()
												.language(LANGUAGE)
												.text("Datum der letzten Änderung")
												.build())
										.build())
								.build())
						.outputVariable(new DefaultOperationVariable.Builder()
								.value(new DefaultSubmodelElementList.Builder()
										.idShort("plantStructure")
										.displayName(new DefaultLangString.Builder()
												.language(LANGUAGE)
												.text("Liste von Anlagen")
												.build())
										.semanticIdListElement(new DefaultReference.Builder()
												.type(ReferenceTypes.MODEL_REFERENCE)
												.key(new DefaultKey.Builder()
														.type(KeyTypes.SUBMODEL)
														.value("http://iasset.salzburgresearch.at/common/plantStuctureRequestOperation")
														.build())
												.key(new DefaultKey.Builder()
														.type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION)
														.value("plantElement")
														.build())
												.build())
										.build())
								.build())
						.build())
				.submodelElement(new DefaultSubmodelElementCollection.Builder()
						.idShort("plantElement")
						.displayName(new DefaultLangString.Builder()
								.language(LANGUAGE)
								.text("AnlagenStrukturElement").build()
						)
						.value(new DefaultProperty.Builder()
								.idShort("name")
								.kind(ModelingKind.TEMPLATE)
								.valueType(DataTypeDefXsd.STRING)
								.displayName(new DefaultLangString.Builder()
										.language(LANGUAGE)
										.text("Name").build()
								)
								.build())
						.value(new DefaultProperty.Builder()
								.idShort("description")
								.kind(ModelingKind.TEMPLATE)
								.valueType(DataTypeDefXsd.STRING)
								.displayName(new DefaultLangString.Builder()
										.language(LANGUAGE)
										.text("Description").build()
								)
								.build())
						.value(new DefaultSubmodelElementList.Builder()
								.idShort("identifiers")
								.displayName(new DefaultLangString.Builder()
										.language(LANGUAGE)
										.text("ID-Liste")
										.build())
								.description(new DefaultLangString.Builder()
										.language(LANGUAGE)
										.text("Liste von (externen) Bezeichnern")
										.build())
								.build())
						// TODO: Add self reference (parent PlantElement)
						.build())
				.build();
	}
}
