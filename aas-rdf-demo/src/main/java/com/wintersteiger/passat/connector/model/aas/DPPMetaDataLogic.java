package com.wintersteiger.passat.connector.model.aas;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.eclipse.digitaltwin.aas4j.v3.model.AasSubmodelElements;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXsd;
import org.eclipse.digitaltwin.aas4j.v3.model.MultiLanguageProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementList;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringNameType;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringTextType;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodelElementCollection;

import at.srfg.iasset.connector.environment.LocalEnvironment;
import at.srfg.iasset.repository.utils.ReferenceUtils;
import at.srfg.iasset.repository.utils.SubmodelUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped 
public class DPPMetaDataLogic implements  AASModelLogic {
    public static final String aasIdentifier = "urn:samm:io.admin-shell.idta.dpp.dpp_metadata:1.0.0#DppMetadata";
    public static final String submodelIdentifier = "urn:samm:io.admin-shell.idta.dpp.dpp_metadata:1.0.0#DppMetadata/submodel";
    public static final String nameplateAasIdentifier = "urn:samm:io.admin-shell.idta.digital_nameplate:3.0.0#Nameplate";
    public static final String nameplateSubmodelIdentifier = "urn:samm:io.admin-shell.idta.digital_nameplate:3.0.0#Nameplate/submodel";
    public static final String productModelAas = "urn:samm:com.copadata.dpp.AtomicSki:2.0.0#ProductModel";
    public static final String productModelSubmodel = "urn:samm:com.copadata.dpp.AtomicSki:2.0.0#ProductModel/submodel";
    public static final String itemDataAas = "urn:samm:com.copadata.dpp.AtomicSki:2.0.0#ItemData";
    public static final String itemDataSubmodel = "urn:samm:com.copadata.dpp.AtomicSki:2.0.0#ItemData/submodel";

    @Inject
    private LocalEnvironment environment;

    @Override
    public void injectLogic(LocalEnvironment environment) {
        Optional<AssetAdministrationShell> shellTemplate = environment.getAssetAdministrationShell(aasIdentifier);

        Optional<Submodel> submodelTemplate = environment.getSubmodel(aasIdentifier, submodelIdentifier);
        Optional<Submodel> nameplateTemplate = environment.getSubmodel(nameplateAasIdentifier, nameplateSubmodelIdentifier);
        Optional<Submodel> productModelTemplate = environment.getSubmodel(productModelAas, productModelSubmodel);
        Optional<Submodel> itemDataTemplate = environment.getSubmodel(itemDataAas, itemDataSubmodel);



        if ( submodelTemplate.isPresent()) {
            // template for the entire DPP - will contain all content specific details 
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
            environment.getSubmodel("http://example.org/aas2rdf/submodel").ifPresent(submodel -> {
                submodel.setSemanticId(ReferenceUtils.asGlobalReference(cDesc.getId()));
                submodel.getSubmodelElements().add(new DefaultSubmodelElementCollection.Builder()
                    .idShort("dppData")
                    // 
                    .semanticId(ReferenceUtils.asGlobalReference(ReferenceUtils.lastKeyValue(template.getSemanticId())))
                    .build());

                SubmodelUtils.getSubmodelElementAt(submodel, "dppData", SubmodelElementCollection.class).ifPresent(dppContainer -> {
                    environment.createInstance(template, "digitalProductPassportId", Property.class).ifPresent(instance -> {
                        instance.setValue("https://atomic-ski.passat.srfg.at/aas/ski/SN7777777");
                        dppContainer.getValue().add(instance);
                    });
                    environment.createInstance(template, "uniqueProductIdentifier", Property.class).ifPresent(instance -> {
                        instance.setValue("https://atomic-ski.passat.srfg.at/aas/ski/PMAA7777777");
                        dppContainer.getValue().add(instance);
                    });
                    environment.createInstance(template, "granularity", Property.class).ifPresent(instance -> {
                        instance.setValue("Item");
                        dppContainer.getValue().add(instance);
                    });
                    environment.createInstance(template, "dppSchemaVersion", Property.class).ifPresent(instance -> {
                        instance.setValue("en 18223:2026");
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
                        instance.setValue("https://atomic-ski.passat.srfg.at/aas/ski/altenmarkt");
                        dppContainer.getValue().add(instance);
                    });
                    environment.createInstance(template, "facilityId", Property.class).ifPresent(instance -> {
                        instance.setValue("https://atomic-ski.passat.srfg.at/aas/ski/altenmarkt");
                        dppContainer.getValue().add(instance);
                    });
                    environment.createInstance(template, "contentSpecificationIds", SubmodelElementList.class).ifPresent(instance -> {
                        instance.setOrderRelevant(true);
                        instance.setTypeValueListElement(AasSubmodelElements.PROPERTY);
                        instance.setValueTypeListElement(DataTypeDefXsd.STRING);
                        /*
                        instance.getValue().add(new DefaultProperty.Builder()
                        .idShort("0")
                        .value("Nameplate")
                        .valueType(DataTypeDefXsd.STRING)
                        .semanticId(instance.getSemanticId())
                        .build());
                        instance.getValue().add(new DefaultProperty.Builder()
                        .idShort("1")
                        .value("contentSpecificatonId-1")
                        .valueType(DataTypeDefXsd.STRING)
                        .semanticId(instance.getSemanticId())
                        .build());
                        */
                        dppContainer.getValue().add(instance);
                    });
                    nameplateTemplate.ifPresent(nameplate -> {
                        SubmodelUtils.getSubmodelElementAt(submodel,"dppData.contentSpecificationIds", SubmodelElementList.class).ifPresent(contentSpecIds->{
                            contentSpecIds.getValue().add((new DefaultProperty.Builder()
                            .idShort(String.format("%s",contentSpecIds.getValue().size()))
                            .value(ReferenceUtils.lastKeyValue(nameplate.getSemanticId()))
                            .valueType(DataTypeDefXsd.STRING)
                            .semanticId(contentSpecIds.getSemanticId())
                            .build()));
                        });
                        environment.createInstance(nameplate, "", SubmodelElementCollection.class).ifPresent(nameplateContainer -> {
                            dppContainer.getValue().add(nameplateContainer);

                            environment.createInstance(nameplate, "UriOfTheProduct", Property.class).ifPresent(element -> {
                                element.setValue("https://www.atomic.com/de-at/shop-emea/product/redster-g7-revoshock-l-mi-12-gw-aa7554.html");
                                nameplateContainer.getValue().add(element);
                            });
                            environment.createInstance(nameplate, "ManufacturerName", MultiLanguageProperty.class).ifPresent(element -> {
                                element.getValue().add(new DefaultLangStringTextType.Builder()
                                    .language("de")
                                    .text("ATOMIC")
                                    .build());
                                nameplateContainer.getValue().add(element);
                            });
                            environment.createInstance(nameplate, "ManufacturerProductDesignation", MultiLanguageProperty.class).ifPresent(element -> {
                                element.getValue().add(new DefaultLangStringTextType.Builder()
                                    .language("de")
                                    .text("REDSTER G7 REVOSHOCK L + MI 12 GW")
                                    .build());
                                nameplateContainer.getValue().add(element);
                            });
                            environment.createInstance(nameplate, "OrderCodeOfManufacturer", Property.class).ifPresent(element -> {
                                element.setValue("AA7777777+");
                                nameplateContainer.getValue().add(element);
                            });
                            environment.createInstance(nameplate, "SerialNumber", Property.class).ifPresent(element -> {
                                element.setValue("7777777");
                                nameplateContainer.getValue().add(element);
                            });
                        });


                        });

                    productModelTemplate.ifPresent(productModel -> {
                        SubmodelUtils.getSubmodelElementAt(submodel,"dppData.contentSpecificationIds", SubmodelElementList.class).ifPresent(contentSpecIds->{
                            contentSpecIds.getValue().add((new DefaultProperty.Builder()
                            .idShort(String.format("%s",contentSpecIds.getValue().size()))
                            .value(ReferenceUtils.lastKeyValue(productModel.getSemanticId()))
                            .valueType(DataTypeDefXsd.STRING)
                            .semanticId(contentSpecIds.getSemanticId())
                            .build()));
                        });
                        environment.createInstance(productModel, "", SubmodelElementCollection.class).ifPresent(productContainer -> {
                            dppContainer.getValue().add(productContainer);

                            environment.createInstance(productModel, "set", MultiLanguageProperty.class).ifPresent(element -> {
                                element.getValue().add(new DefaultLangStringTextType.Builder()
                                    .language("en")
                                    .text("Commercial Set - SYS")
                                    .build());
                                productContainer.getValue().add(element);
                            });
                            environment.createInstance(productModel, "productName", MultiLanguageProperty.class).ifPresent(element -> {
                                element.getValue().add(new DefaultLangStringTextType.Builder()
                                    .language("en")
                                    .text("REDSTER G7 REVOSHOCK L + MI 12 GW")
                                    .build());
                                productContainer.getValue().add(element);
                            });
                            environment.createInstance(productModel, "season", MultiLanguageProperty.class).ifPresent(element -> {
                                element.getValue().add(new DefaultLangStringTextType.Builder()
                                    .language("en")
                                    .text("WS26 - Winter sport 2026")
                                    .build());
                                productContainer.getValue().add(element);
                            });
                            environment.createInstance(productModel, "articleNumber", Property.class).ifPresent(element -> {
                                element.setValue("AASS03858+");
                                productContainer.getValue().add(element);
                            });
                            environment.createInstance(productModel, "baseEdgeAngle", Property.class).ifPresent(element -> {
                                element.setValue("RACE 0,8 +/- 0,3 GS/JR");
                                productContainer.getValue().add(element);
                            });
                            environment.createInstance(productModel, "steelEdgeSidewallAngle", Property.class).ifPresent(element -> {
                                element.setValue("87,0 +/- 0,5°");
                                productContainer.getValue().add(element);
                            });
                            environment.createInstance(productModel, "sideCutTip", Property.class).ifPresent(element -> {
                                element.setValue("113.0");
                                productContainer.getValue().add(element);
                            });
                            environment.createInstance(productModel, "sideCutWaist", Property.class).ifPresent(element -> {
                                element.setValue("69.0");
                                productContainer.getValue().add(element);
                            });
                            environment.createInstance(productModel, "sideCutTail", Property.class).ifPresent(element -> {
                                element.setValue("100.5");
                                productContainer.getValue().add(element);
                            });
                        });
                    });
                    
                    itemDataTemplate.ifPresent(itemModel -> {
                        SubmodelUtils.getSubmodelElementAt(submodel,"dppData.contentSpecificationIds", SubmodelElementList.class).ifPresent(contentSpecIds->{
                            contentSpecIds.getValue().add((new DefaultProperty.Builder()
                            .idShort(String.format("%s",contentSpecIds.getValue().size()))
                            .value(ReferenceUtils.lastKeyValue(itemModel.getSemanticId()))
                            .valueType(DataTypeDefXsd.STRING)
                            .semanticId(contentSpecIds.getSemanticId())
                            .build()));
                        });
                        environment.createInstance(itemModel, "", SubmodelElementCollection.class).ifPresent(productContainer -> {
                            dppContainer.getValue().add(productContainer);

                            environment.createInstance(itemModel, "ean", Property.class).ifPresent(element -> {
                                element.setValue("0190694134041");
                                productContainer.getValue().add(element);
                            });
                            environment.createInstance(itemModel, "deliveryDate", Property.class).ifPresent(element -> {
                                element.setValue(LocalDateTime.now().toString());
                                productContainer.getValue().add(element);
                            });
                            environment.createInstance(itemModel, "manufactureDate", Property.class).ifPresent(element -> {
                                element.setValue(LocalDateTime.now().toString());
                                productContainer.getValue().add(element);
                            });
                            environment.createInstance(itemModel, "url", Property.class).ifPresent(element -> {
                                element.setValue("https://www.atomic.com/de-at/shop-emea/product/redster-g7-revoshock-l-mi-12-gw-aa7554.html#color=58003");
                                productContainer.getValue().add(element);
                            });

                            environment.createInstance(itemModel, "maintenanceHistory", SubmodelElementList.class).ifPresent(instance -> {
                                instance.setOrderRelevant(true);
                                instance.setTypeValueListElement(AasSubmodelElements.SUBMODEL_ELEMENT_COLLECTION);
                                environment.createInstance(itemModel, "maintenanceHistory.MaintenanceActivity(0)", SubmodelElementCollection.class).ifPresent(activity -> {
                                    activity.setIdShort(String.format("%s", instance.getValue().size()));
                                    environment.createInstance(itemModel, "maintenanceHistory.MaintenanceAcitivity(0).occurredAt", Property.class).ifPresent(occurredAt-> {
                                        occurredAt.setValue(LocalDateTime.now().toString());
                                    });
                                    
                                });
                                /*
                                instance.getValue().add(new DefaultProperty.Builder()
                                .idShort("0")
                                .value("Nameplate")
                                .valueType(DataTypeDefXsd.STRING)
                                .semanticId(instance.getSemanticId())
                                .build());
                                instance.getValue().add(new DefaultProperty.Builder()
                                .idShort("1")
                                .value("contentSpecificatonId-1")
                                .valueType(DataTypeDefXsd.STRING)
                                .semanticId(instance.getSemanticId())
                                .build());
                                */
                                dppContainer.getValue().add(instance);
                            });
                        });
                    });

                });
                // add the nameplate data as well
            });

        }
    }

}
