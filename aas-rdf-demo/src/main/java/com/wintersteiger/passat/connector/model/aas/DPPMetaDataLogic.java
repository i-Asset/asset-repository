package com.wintersteiger.passat.connector.model.aas;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.eclipse.digitaltwin.aas4j.v3.model.AasSubmodelElements;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXsd;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementList;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringNameType;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodelElementCollection;

import at.srfg.iasset.connector.environment.LocalEnvironment;
import at.srfg.iasset.repository.exception.ShellNotFoundException;
import at.srfg.iasset.repository.utils.ReferenceUtils;
import at.srfg.iasset.repository.utils.SubmodelUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped 
public class DPPMetaDataLogic implements  AASModelLogic {
    public static final String aasIdentifier = "urn:samm:io.admin-shell.idta.dpp.dpp_metadata:1.0.0#DppMetadata";
    public static final String submodelIdentifier = "urn:samm:io.admin-shell.idta.dpp.dpp_metadata:1.0.0#DppMetadata/submodel";

    @Inject
    private LocalEnvironment environment;

    @Override
    public void injectLogic(LocalEnvironment environment) {
        Optional<AssetAdministrationShell> shellTemplate = environment.getAssetAdministrationShell(aasIdentifier);

        environment.getSubmodel("http://example.org/aas2rdf/submodel").ifPresent((smInstance)-> {
        	Optional<Submodel> submodelTemplate = environment.getSubmodel(aasIdentifier, submodelIdentifier);
        	if ( submodelTemplate.isPresent()) {
        		
        		Submodel template = submodelTemplate.get();
        		
        		smInstance.getSubmodelElements().add(new DefaultSubmodelElementCollection.Builder()
        				.idShort("dppData")
        				// 
        				.semanticId(ReferenceUtils.asGlobalReference(ReferenceUtils.lastKeyValue(template.getSemanticId())))
        				.build());
        		
        		ConceptDescription cDesc = new DefaultConceptDescription.Builder()
        				.id(ReferenceUtils.lastKeyValue(template.getSemanticId()))
        				.displayName(new DefaultLangStringNameType.Builder()
        						.language("en")
        						.text("DPP Metadata according to EN 18223")
        						.build())
        				.isCaseOf(ReferenceUtils.asGlobalReference("urn:samm:io.admin-shell.idta.dpp.dpp_metadata:1.0.0#DppMetadata"))
        				.build();
        		//
        		environment.setConceptDescription(cDesc.getId(), cDesc);
        		
      
        		
        		SubmodelUtils.getSubmodelElementAt(smInstance, "dppData", SubmodelElementCollection.class).ifPresent(dppContainer -> {
        			environment.createInstance(template, "digitalProductPassportId", Property.class).ifPresent(instance -> {
        				instance.setValue("dppPass-1234456789");
        				dppContainer.getValue().add(instance);
        			});
        			environment.createInstance(template, "uniqueProductIdentifier", Property.class).ifPresent(instance -> {
        				instance.setValue("dppPass-1234456789");
        				dppContainer.getValue().add(instance);
        			});
        			environment.createInstance(template, "granularity", Property.class).ifPresent(instance -> {
        				instance.setValue("Batch");
        				dppContainer.getValue().add(instance);
        			});
        			environment.createInstance(template, "dppSchemaVersion", Property.class).ifPresent(instance -> {
        				instance.setValue("0.1");
        				dppContainer.getValue().add(instance);
        			});
        			environment.createInstance(template, "dppStatus", Property.class).ifPresent(instance -> {
        				instance.setValue("Active");
        				dppContainer.getValue().add(instance);
        			});
        			environment.createInstance(template, "lastUpdate", Property.class).ifPresent(instance -> {
        				instance.setValue(LocalDateTime.of(LocalDate.now(), LocalTime.of(12,00)).toString());
        				dppContainer.getValue().add(instance);
        			});
        			environment.createInstance(template, "economicOperatorId", Property.class).ifPresent(instance -> {
        				instance.setValue("economicOperator-123456789");
        				dppContainer.getValue().add(instance);
        			});
        			environment.createInstance(template, "facilityId", Property.class).ifPresent(instance -> {
        				instance.setValue("facilityId-123456789");
        				dppContainer.getValue().add(instance);
        			});
        			environment.createInstance(template, "contentSpecificationIds", SubmodelElementList.class).ifPresent((instance) ->{
        				instance.setTypeValueListElement(AasSubmodelElements.PROPERTY);
        				instance.setValueTypeListElement(DataTypeDefXsd.STRING);
        				instance.getValue().add(new DefaultProperty.Builder()
        						.idShort("0")
        						.value("contenSpecificationId-0")
        						.valueType(DataTypeDefXsd.STRING)
        						.build());
        				instance.getValue().add(new DefaultProperty.Builder()
        						.idShort("1")
        						.value("contenSpecificationId-1")
        						.valueType(DataTypeDefXsd.STRING)
        						.build());
        				dppContainer.getValue().add(instance);
        			});
        		});
        		
        		
        		
        		
        	}
        	
        	
        });

    }
}
