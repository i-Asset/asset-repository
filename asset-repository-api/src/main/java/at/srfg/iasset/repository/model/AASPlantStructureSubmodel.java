package at.srfg.iasset.repository.model;

import org.eclipse.digitaltwin.aas4j.v3.model.AasSubmodelElements;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXsd;
import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeIec61360;
import org.eclipse.digitaltwin.aas4j.v3.model.EmbeddedDataSpecification;
import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.ModellingKind;
import org.eclipse.digitaltwin.aas4j.v3.model.QualifierKind;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultDataSpecificationIec61360;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEmbeddedDataSpecification;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultExternalReference;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangString;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultModelReference;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultOperation;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultOperationVariable;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultQualifier;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultReferenceElement;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodelElementCollection;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodelElementList;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultValueList;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultValueReferencePair;

import at.srfg.iasset.repository.utils.ReferenceUtils;




public class AASPlantStructureSubmodel {

	public static final String LANGUAGE = "de";

	public static final Submodel SUBMODEL_PLANT_STRUCTURE = createSubmodelForPlantStructure();
	public static final Submodel SUBMODEL_PLANT_STRUCTURE_REQUEST_OPERATION = createSubmodelForPlantStructureRequestOperation();
	public static final ConceptDescription CONCEPT_DESCRIPTION_PLANT_STRUCTURE = plantStructureConcept();
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
				.kind(ModellingKind.TEMPLATE)
				.build();

	}
	private static ConceptDescription plantStructureConcept() {
		return new DefaultConceptDescription.Builder()
				.id("http://iasset.salzburgresarch.at/common/plantStructure")
				.idShort("plantStructure")
				//
				.isCaseOf(new DefaultExternalReference.Builder()
						.key(new DefaultKey.Builder()
								.type(KeyTypes.GLOBAL_REFERENCE)
								.value("http://plantStrucutreFromSemanticLookup")
								.build())
						.build())
				.embeddedDataSpecification(createDataSpecificationPlantIdentifier())
				.embeddedDataSpecification(createDataSpecificationPlantName())
				.embeddedDataSpecification(createDataSpecificationPlantDescription())
				.build();
	}
	private static EmbeddedDataSpecification createDataSpecificationPlantIdentifier() {
		return new DefaultEmbeddedDataSpecification.Builder()
//				.id("http://iasset.salzburgresearch.at/common/dataSpecification/plantIdenifier")				
				.dataSpecificationContent(new DefaultDataSpecificationIec61360.Builder()
						.preferredName(new DefaultLangString.Builder()
								.language(LANGUAGE)
								.text("Plant Identifier")
								.build())
						.dataType(DataTypeIec61360.STRING)
						.valueList(new DefaultValueList.Builder()
								.valueReferencePair(new DefaultValueReferencePair.Builder()
										.value("identifier1")
										.valueId(new DefaultExternalReference.Builder()
//												.type(ReferenceTypes.GLOBAL_REFERENCE)
												.key(new DefaultKey.Builder()
														.type(KeyTypes.GLOBAL_REFERENCE)
														.value("http://somewhere.definition.org/identifier1")
														.build())
												.build())
										.build())
								.build())
						.shortName(new DefaultLangString.Builder()
								.language(LANGUAGE)
								.text("identifier")
								.build())
						.build())
				.build();
	}
	private static EmbeddedDataSpecification createDataSpecificationPlantName() {
		return new DefaultEmbeddedDataSpecification.Builder()
				.dataSpecificationContent(new DefaultDataSpecificationIec61360.Builder()
						.preferredName(new DefaultLangString.Builder()
								.language(LANGUAGE)
								.text("Plant Name")
								.build())
						.dataType(DataTypeIec61360.STRING_TRANSLATABLE)
						.shortName(new DefaultLangString.Builder()
								.language(LANGUAGE)
								.text("name")
								.build())
						.build())
				.build();
	}
	private static EmbeddedDataSpecification createDataSpecificationPlantDescription() {
		return new DefaultEmbeddedDataSpecification.Builder()
				.dataSpecificationContent(new DefaultDataSpecificationIec61360.Builder()
						.preferredName(new DefaultLangString.Builder()
								.language(LANGUAGE)
								.text("Plant Description")
								.build())
						.dataType(DataTypeIec61360.STRING_TRANSLATABLE)
						.shortName(new DefaultLangString.Builder()
								.language(LANGUAGE)
								.text("description")
								.build())
						.build())
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
				.kind(ModellingKind.TEMPLATE)
				.semanticId(ReferenceUtils.asGlobalReference("http://iassset.salzburgresearch.at/common/plantStructureRequest"))
				.submodelElement(new DefaultOperation.Builder()
						.idShort("getPlantStructure")
						.displayName(new DefaultLangString.Builder()
								.language(LANGUAGE)
								.text("Anlagenstruktur abrufen").build())
						.semanticId(new DefaultExternalReference.Builder()
								.key(new DefaultKey.Builder()
										.type(KeyTypes.GLOBAL_REFERENCE)
										.value("http://iasset.salzburgresarch.at/common/plantStructure")
										.build())
								.build())
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
						.inputVariable(new DefaultOperationVariable.Builder()
								.value(new DefaultProperty.Builder()
										.idShort("doubleValue")
										.valueType(DataTypeDefXsd.DOUBLE)
										.displayName(new DefaultLangString.Builder()
												.language(LANGUAGE)
												.text("Demo Double-Value")
												.build())
										.build())
								.build())
						.inputVariable(new DefaultOperationVariable.Builder()
								.value(new DefaultSubmodelElementCollection.Builder()
										.idShort("plantElement")
										.displayName(new DefaultLangString.Builder()
												.language(LANGUAGE)
												.text("AnlagenStrukturElement").build()
										)
										.semanticId(new DefaultExternalReference.Builder()
												.key(new DefaultKey.Builder()
														.type(KeyTypes.GLOBAL_REFERENCE)
														.value("http://iasset.salzburgresearch.at/common/plantElement")
														.build())
												.build())
										.value(new DefaultProperty.Builder()
												.idShort("name")
												.qualifier(new DefaultQualifier.Builder()
														.kind(QualifierKind.TEMPLATE_QUALIFIER)
														.build())
												.valueType(DataTypeDefXsd.STRING)
												.embeddedDataSpecification(createDataSpecificationPlantName())
												.displayName(new DefaultLangString.Builder()
														.language(LANGUAGE)
														.text("Name").build()
												)
												.build())
										.value(new DefaultProperty.Builder()
												.idShort("description")
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
												.valueTypeListElement(DataTypeDefXsd.STRING)
												.description(new DefaultLangString.Builder()
														.language(LANGUAGE)
														.text("Liste von (externen) Bezeichnern")
														.build())
												.build())
										// Add self reference (parent PlantElement)
										.value(new DefaultReferenceElement.Builder()
												.idShort("parent")
												.displayName(new DefaultLangString.Builder()
														.language(LANGUAGE)
														.text("Parent Structure Element")
														.build())
												.description(new DefaultLangString.Builder()
														.language(LANGUAGE)
														.text("Liste von (externen) Bezeichnern")
														.build())
												.build())
										.build())
								.build())
						.outputVariable(new DefaultOperationVariable.Builder()
								.value(new DefaultProperty.Builder()
										.idShort("doubleValue")
										.valueType(DataTypeDefXsd.DOUBLE)
										.displayName(new DefaultLangString.Builder()
												.language(LANGUAGE)
												.text("Demo Double-Value")
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
										.typeValueListElement(AasSubmodelElements.SUBMODEL_ELEMENT_COLLECTION)
										.semanticIdListElement(new DefaultModelReference.Builder()
												.key(new DefaultKey.Builder()
														.type(KeyTypes.SUBMODEL)
														.value("http://iasset.salzburgresearch.at/common/plantStuctureRequestOperation")
														.build())
												.key(new DefaultKey.Builder()
														.type(KeyTypes.SUBMODEL_ELEMENT_COLLECTION)
														.value("plantElement")
														.build())
												.build())
										.value(new DefaultSubmodelElementCollection.Builder()
												.idShort("plantElement")
												.displayName(new DefaultLangString.Builder()
														.language(LANGUAGE)
														.text("AnlagenStrukturElement").build()
												)
												.semanticId(new DefaultExternalReference.Builder()
														.key(new DefaultKey.Builder()
																.type(KeyTypes.GLOBAL_REFERENCE)
																.value("http://iasset.salzburgresearch.at/common/plantElement")
																.build())
														.build())
												.embeddedDataSpecification(new DefaultEmbeddedDataSpecification.Builder()
														// 
														.dataSpecificationContent(new DefaultDataSpecificationIec61360.Builder()
																.build()
														)	
														.build())
													
												.value(new DefaultProperty.Builder()
														.idShort("name")
														.valueType(DataTypeDefXsd.STRING)
														.displayName(new DefaultLangString.Builder()
																.language(LANGUAGE)
																.text("Name").build()
														)
														.build())
												.value(new DefaultProperty.Builder()
														.idShort("description")
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
														.valueTypeListElement(DataTypeDefXsd.STRING)
														.description(new DefaultLangString.Builder()
																.language(LANGUAGE)
																.text("Liste von (externen) Bezeichnern")
																.build())
														.build())
												// Add self reference (parent PlantElement)
												.value(new DefaultReferenceElement.Builder()
														.idShort("parent")
														.displayName(new DefaultLangString.Builder()
																.language(LANGUAGE)
																.text("Parent Structure Element")
																.build())
														.description(new DefaultLangString.Builder()
																.language(LANGUAGE)
																.text("Liste von (externen) Bezeichnern")
																.build())
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
						.semanticId(new DefaultExternalReference.Builder()
								.key(new DefaultKey.Builder()
										.type(KeyTypes.GLOBAL_REFERENCE)
										.value("http://iasset.salzburgresearch.at/common/plantElement")
										.build())
								.build())
						.value(new DefaultProperty.Builder()
								.idShort("name")
								.valueType(DataTypeDefXsd.STRING)
								.qualifier(new DefaultQualifier.Builder()
										.kind(QualifierKind.VALUE_QUALIFIER)
										.type("mandatory")
										.valueType(DataTypeDefXsd.BOOLEAN)
										.value("true")
										.build())
								.displayName(new DefaultLangString.Builder()
										.language(LANGUAGE)
										.text("Name").build()
								)
								.build())
						.value(new DefaultProperty.Builder()
								.idShort("description")
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
								.valueTypeListElement(DataTypeDefXsd.STRING)
								.embeddedDataSpecification(createDataSpecificationPlantIdentifier())
								.description(new DefaultLangString.Builder()
										.language(LANGUAGE)
										.text("Liste von (externen) Bezeichnern")
										.build())
								.build())
						// Add self reference (parent PlantElement)
						.value(new DefaultReferenceElement.Builder()
								.idShort("parent")
								.displayName(new DefaultLangString.Builder()
										.language(LANGUAGE)
										.text("Parent Structure Element")
										.build())
								.description(new DefaultLangString.Builder()
										.language(LANGUAGE)
										.text("Liste von (externen) Bezeichnern")
										.build())
								.build())
						.build())
				.build();
	}
}
