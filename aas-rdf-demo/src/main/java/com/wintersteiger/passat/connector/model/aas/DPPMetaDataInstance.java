package com.wintersteiger.passat.connector.model.aas;

import java.time.LocalDateTime;
import java.util.Locale;

import org.eclipse.digitaltwin.aas4j.v3.model.AasSubmodelElements;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetKind;
import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXsd;
import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.ReferenceTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetInformation;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEnvironment;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringNameType;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringTextType;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultMultiLanguageProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultReference;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodelElementCollection;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodelElementList;
import org.slf4j.Logger;

import at.srfg.iasset.connector.environment.AASEnvironment;
import at.srfg.iasset.connector.environment.LocalEnvironment;
import at.srfg.iasset.repository.utils.ReferenceUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DPPMetaDataInstance implements AASEnvironment {

	@Inject
	private Logger logger;
	
	@Inject 
	LocalEnvironment environment;
	/**
	 * 
	 * Settings for Atomic Connector
	 */
	private AssetAdministrationShell shell;
	private Submodel operationSubmodel;
	
	private final String SUBMODEL_ID = "https://admin-shell.io/idta/SubmodelTemplate/dppMetadata/1/0";
	

	private AssetAdministrationShell createShell() {
		
		AssetAdministrationShell shell = new DefaultAssetAdministrationShell.Builder()
				.id("http://dppmeta.org/exampleAAS")
				.idShort("dppMetaAAS")
				.assetInformation(new DefaultAssetInformation.Builder()
						.assetKind(AssetKind.INSTANCE)
						.build())
				.displayName(new DefaultLangStringNameType.Builder()
						.language(Locale.getDefault().getLanguage())
						.text("Demo Instance showing AAS to RDF transformation")
						.build())
				.submodels(new DefaultReference.Builder()
						.type(ReferenceTypes.MODEL_REFERENCE)
						.keys(new DefaultKey.Builder()
								.type(KeyTypes.SUBMODEL)
								.value("http://dppmeta.org/exampleAAS/submodel")
								.build())
						.build())
				.build();
		
		return shell;
	}
	private Submodel createSubmodel() {
		Submodel submodel = new DefaultSubmodel.Builder()
				.id("http://dppmeta.org/exampleAAS/submodel")
				.idShort("dppMeta")
				.submodelElements(new DefaultSubmodelElementCollection.Builder()
						.idShort("data")
						.displayName(new DefaultLangStringNameType.Builder()
								.language("de")
								.text("Container for RDF-Data")
								.build())
						.value(new DefaultProperty.Builder()
								.semanticId(new DefaultReference.Builder()
										.type(ReferenceTypes.MODEL_REFERENCE)
										.keys(new DefaultKey.Builder()
												.type(KeyTypes.SUBMODEL)
												.value(SUBMODEL_ID)
												.build())
										.keys(new DefaultKey.Builder()
												.type(KeyTypes.PROPERTY)
												.value("digitalProductPassportId")
												.build())
										.build())
								.valueType(DataTypeDefXsd.STRING)
								.value("http://wseqr.amersports.com/dpp/1234567")
								.build())
						.value(new DefaultProperty.Builder()
								.semanticId(new DefaultReference.Builder()
										.type(ReferenceTypes.MODEL_REFERENCE)
										.keys(new DefaultKey.Builder()
												.type(KeyTypes.SUBMODEL)
												.value(SUBMODEL_ID)
												.build())
										.keys(new DefaultKey.Builder()
												.type(KeyTypes.PROPERTY)
												.value("uniqueProductIdentifier")
												.build())
										.build())
								.valueType(DataTypeDefXsd.STRING)
								.value("http://wseqr.amersports.com/?sn=1234567")
								.build())
						.value(new DefaultProperty.Builder()
								.semanticId(new DefaultReference.Builder()
										.type(ReferenceTypes.MODEL_REFERENCE)
										.keys(new DefaultKey.Builder()
												.type(KeyTypes.SUBMODEL)
												.value(SUBMODEL_ID)
												.build())
										.keys(new DefaultKey.Builder()
												.type(KeyTypes.PROPERTY)
												.value("granularity")
												.build())
										.build())
								.valueType(DataTypeDefXsd.STRING)
								.value("Item")
								.build())
						.value(new DefaultProperty.Builder()
								.semanticId(new DefaultReference.Builder()
										.type(ReferenceTypes.MODEL_REFERENCE)
										.keys(new DefaultKey.Builder()
												.type(KeyTypes.SUBMODEL)
												.value(SUBMODEL_ID)
												.build())
										.keys(new DefaultKey.Builder()
												.type(KeyTypes.PROPERTY)
												.value("dppSchemaVersion")
												.build())
										.build())
								.valueType(DataTypeDefXsd.STRING)
								.value("https://admin-shell.io/idta/SubmodelTemplate/dppMetadata/1/0")
								.build())
						.value(new DefaultProperty.Builder()
								.semanticId(new DefaultReference.Builder()
										.type(ReferenceTypes.MODEL_REFERENCE)
										.keys(new DefaultKey.Builder()
												.type(KeyTypes.SUBMODEL)
												.value(SUBMODEL_ID)
												.build())
										.keys(new DefaultKey.Builder()
												.type(KeyTypes.PROPERTY)
												.value("dppStatus")
												.build())
										.build())
								.valueType(DataTypeDefXsd.STRING)
								.value("Active")
								.build())
						.value(new DefaultProperty.Builder()
								.semanticId(new DefaultReference.Builder()
										.type(ReferenceTypes.MODEL_REFERENCE)
										.keys(new DefaultKey.Builder()
												.type(KeyTypes.SUBMODEL)
												.value(SUBMODEL_ID)
												.build())
										.keys(new DefaultKey.Builder()
												.type(KeyTypes.PROPERTY)
												.value("lastUpdate")
												.build())
										.build())
								.valueType(DataTypeDefXsd.DATE_TIME)
								.value(LocalDateTime.now().toString())
								.build())
						.value(new DefaultProperty.Builder()
								.semanticId(new DefaultReference.Builder()
										.type(ReferenceTypes.MODEL_REFERENCE)
										.keys(new DefaultKey.Builder()
												.type(KeyTypes.SUBMODEL)
												.value(SUBMODEL_ID)
												.build())
										.keys(new DefaultKey.Builder()
												.type(KeyTypes.PROPERTY)
												.value("economicOperatorId")
												.build())
										.build())
								.valueType(DataTypeDefXsd.STRING)
								.value("urn:dpp:amersports.com")
								.build())

					.build())
				.build();
		return submodel;
	}
	private SubmodelElementCollection createClassification(String version, String classId, String system) {
		return new DefaultSubmodelElementCollection.Builder()
				.idShort("productClassifications")
				.semanticId(new DefaultReference.Builder()
						.type(ReferenceTypes.MODEL_REFERENCE)
						.keys(new DefaultKey.Builder()
								.type(KeyTypes.SUBMODEL)
								.value(SUBMODEL_ID)
								.build())
						.keys(new DefaultKey.Builder()
								.type(KeyTypes.PROPERTY)
								.value("productClassifications")
								.build())
						.build())
				.value(new DefaultProperty.Builder()
						.idShort("productClassificationVersion")
						.semanticId(new DefaultReference.Builder()
								.type(ReferenceTypes.MODEL_REFERENCE)
								.referredSemanticId(new DefaultReference.Builder()
										.type(ReferenceTypes.EXTERNAL_REFERENCE)
										.keys(new DefaultKey.Builder()
												.type(KeyTypes.GLOBAL_REFERENCE)
												.value("urn:samm:com.examples:1.0.0#productClassificationVersion")
												.build())
										.build())
								.keys(new DefaultKey.Builder()
										.type(KeyTypes.SUBMODEL)
										.value(SUBMODEL_ID)
										.build())
								.keys(new DefaultKey.Builder()
										.type(KeyTypes.PROPERTY)
										.value("productClassifications(0)")
										.build())
								.keys(new DefaultKey.Builder()
										.type(KeyTypes.PROPERTY)
										.value("productClassificationVersion")
										.build())
								.build())		
						.value(version)
						.build())
				.value(new DefaultProperty.Builder()
						.idShort("productClassId")
						.semanticId(new DefaultReference.Builder()
								.type(ReferenceTypes.MODEL_REFERENCE)
								.referredSemanticId(new DefaultReference.Builder()
										.type(ReferenceTypes.EXTERNAL_REFERENCE)
										.keys(new DefaultKey.Builder()
												.type(KeyTypes.GLOBAL_REFERENCE)
												.value("urn:samm:com.examples:1.0.0#productClassId")
												.build())
										.build())
								.keys(new DefaultKey.Builder()
										.type(KeyTypes.SUBMODEL)
										.value(SUBMODEL_ID)
										.build())
								.keys(new DefaultKey.Builder()
										.type(KeyTypes.PROPERTY)
										.value("productClassifications(0)")
										.build())
								.keys(new DefaultKey.Builder()
										.type(KeyTypes.PROPERTY)
										.value("productClassId")
										.build())
								.build())		
						.value(classId)
						.build())
				.value(new DefaultProperty.Builder()
						.idShort("productClassificationSystem")
						.semanticId(new DefaultReference.Builder()
								.type(ReferenceTypes.MODEL_REFERENCE)
								.referredSemanticId(new DefaultReference.Builder()
										.type(ReferenceTypes.EXTERNAL_REFERENCE)
										.keys(new DefaultKey.Builder()
												.type(KeyTypes.GLOBAL_REFERENCE)
												.value("urn:samm:com.examples:1.0.0#productClassificationSystem")
												.build())
										.build())
								.keys(new DefaultKey.Builder()
										.type(KeyTypes.SUBMODEL)
										.value(SUBMODEL_ID)
										.build())
								.keys(new DefaultKey.Builder()
										.type(KeyTypes.PROPERTY)
										.value("productClassifications(0)")
										.build())
								.keys(new DefaultKey.Builder()
										.type(KeyTypes.PROPERTY)
										.value("productClassificationSystem")
										.build())
								.build())		
						.value(system)
						.build())
				.build();
	}
	@Override
	public Environment getAASData() {
		return new DefaultEnvironment.Builder()
				.assetAdministrationShells(createShell())
				.submodels(createSubmodel())
				.build();
	}

}
