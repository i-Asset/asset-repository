package com.wintersteiger.passat.connector.model.aas;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringNameType;
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
    public static final String aasIdentifier = "https://admin-shell.io/idta/aas/DppMetadata/1/0";
    public static final String submodelIdentifier = "https://admin-shell.io/idta/SubmodelTemplate/dppMetadata/1/0";

    @Inject
    private LocalEnvironment environment;

    @Override
    public void injectLogic(LocalEnvironment environment) {
        Optional<AssetAdministrationShell> shellTemplate = environment.getAssetAdministrationShell(aasIdentifier);

        Optional<Submodel> submodelTemplate = environment.getSubmodel(aasIdentifier, submodelIdentifier);
        if ( submodelTemplate.isPresent()) {

            Submodel template = submodelTemplate.get();
            
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

            Submodel submodel = new DefaultSubmodel.Builder()
                // TODO: Choose proper DPP-ID
                .id("https://www.copadata.com/dpp/123456789")
                .administration(template.getAdministration())
                .displayName(template.getDisplayName())
                .submodelElements(new DefaultSubmodelElementCollection.Builder()
                    .idShort("dppData")
                    // 
                    .semanticId(ReferenceUtils.asGlobalReference(ReferenceUtils.lastKeyValue(template.getSemanticId())))
                    .build())
                .build();

                SubmodelUtils.getSubmodelElementAt(submodel, "dppData", SubmodelElementCollection.class).ifPresent(dppContainer -> {
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
                    environment.createInstance(template, "economicOperatiorId", Property.class).ifPresent(instance -> {
                        instance.setValue("economicOperator-123456789");
                        dppContainer.getValue().add(instance);
                    });
                    environment.createInstance(template, "facilityId", Property.class).ifPresent(instance -> {
                        instance.setValue("facilityId-123456789");
                        dppContainer.getValue().add(instance);
                    });
                });

            

            
            try {
                environment.addSubmodel("http://example.org/aas2rdf", submodel);
            } catch (ShellNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }
}
