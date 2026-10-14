package com.wintersteiger.passat.connector.model.aas;

import java.util.Optional;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.ModellingKind;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringNameType;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel;

import at.srfg.iasset.connector.environment.LocalEnvironment;
import at.srfg.iasset.repository.exception.ShellNotFoundException;
import at.srfg.iasset.repository.utils.ReferenceUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped 
public class DPPRepairInstance implements  AASModelLogic {
    public static final String aasIdentifier = "urn:samm:com.copadata.dpp:1.0.0#DPPRepair";
    public static final String submodelIdentifier = "urn:samm:com.copadata.dpp:1.0.0#DPPRepair/submodel";


    @Inject
    private LocalEnvironment environment;

    @Override
    public void injectLogic(LocalEnvironment environment) {
        Optional<AssetAdministrationShell> shellTemplate = environment.getAssetAdministrationShell(aasIdentifier);
        if ( shellTemplate.isPresent() ) {
        	AssetAdministrationShell shell = shellTemplate.get();
        	Optional<Submodel> submodelTemplate = environment.getSubmodel(aasIdentifier, submodelIdentifier);

        	
        	if ( submodelTemplate.isPresent()) {
        		Submodel sub = new DefaultSubmodel.Builder()
        				.id(aasIdentifier+"/instance")
        				.semanticId(ReferenceUtils.asGlobalReference(KeyTypes.SUBMODEL, submodelIdentifier))
        				.displayName(new DefaultLangStringNameType.Builder()
        						.language("en")
        						.text("DPP Repair API (My Semantic Integration Pattern")
        						.build())
        				.kind(ModellingKind.INSTANCE)
        				.build();
        		// template for the entire DPP - will contain all content specific details 
        		Submodel template = submodelTemplate.get();
        		
        		ConceptDescription cDesc = new DefaultConceptDescription.Builder()
        				.id(ReferenceUtils.lastKeyValue(template.getSemanticId()))
        				.displayName(new DefaultLangStringNameType.Builder()
        						.language("en")
        						.text("DPP Repair API (My Semantic Integration Pattern")
        						.build())
        				.isCaseOf(ReferenceUtils.asGlobalReference("urn:samm:com.copadata.dpp:1.0.0#DPPRepair"))
        				.build();
        		//
        		try {
					environment.addSubmodel(shell.getId(), sub);
				} catch (ShellNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		environment.setConceptDescription(cDesc.getId(), cDesc);
        		environment.createInstance(template, "repairInstruction", SubmodelElementCollection.class).ifPresent(repairInstruction ->{
        			environment.createInstance(template, "repairInstruction.edgePerformance", SubmodelElementCollection.class).ifPresent(edgePerformance -> {
        				environment.createInstance(template, "repairInstruction.edgePerformance.performanceClass", Property.class).ifPresent(performanceClass -> {
        					performanceClass.setValue("E");
        					edgePerformance.getValue().add(performanceClass);
        				});
        				repairInstruction.getValue().add(edgePerformance);
        				environment.createInstance(template, "repairInstruction.edgePerformance.vEdge", Property.class).ifPresent(vEdge -> {
        					vEdge.setValue("true");
        					edgePerformance.getValue().add(vEdge);
        				});
        			});
        			environment.createInstance(template, "repairInstruction.baseEdgeParameter", SubmodelElementCollection.class).ifPresent(baseEdge->{
        				environment.createInstance(template, "repairInstruction.baseEdgeParameter.angleTip", Property.class).ifPresent(angleTip -> {
        					angleTip.setValue("0.9");
        					baseEdge.getValue().add(angleTip);
        				});
        				environment.createInstance(template, "repairInstruction.baseEdgeParameter.angleWaist", Property.class).ifPresent(angleWaist -> {
        					angleWaist.setValue("0.9");
        					baseEdge.getValue().add(angleWaist);
        				});
        				environment.createInstance(template, "repairInstruction.baseEdgeParameter.angleTail", Property.class).ifPresent(angleTail -> {
        					angleTail.setValue("0.9");
        					baseEdge.getValue().add(angleTail);
        				});
        				repairInstruction.getValue().add(baseEdge);
        			});
        			environment.createInstance(template, "repairInstruction.sideEdgeParameter", SubmodelElementCollection.class).ifPresent(baseEdge->{
        				environment.createInstance(template, "repairInstruction.baseEdgeParameter.angleTip", Property.class).ifPresent(angleTip -> {
        					angleTip.setValue("87.5");
        					baseEdge.getValue().add(angleTip);
        				});
        				environment.createInstance(template, "repairInstruction.sideEdgeParameter.angleWaist", Property.class).ifPresent(angleWaist -> {
        					angleWaist.setValue("87.0");
        					baseEdge.getValue().add(angleWaist);
        				});
        				environment.createInstance(template, "repairInstruction.sideEdgeParameter.angleTail", Property.class).ifPresent(angleTail -> {
        					angleTail.setValue("87.1");
        					baseEdge.getValue().add(angleTail);
        				});
        				repairInstruction.getValue().add(baseEdge);
        			});
        			environment.createInstance(template, "repairInstruction.productData", SubmodelElementCollection.class).ifPresent(productData -> {
        				environment.createInstance(template, "repairInstruction.productData.skiWidthTail", Property.class).ifPresent(skiWidthTail -> {
        					skiWidthTail.setValue("94.5");
        					productData.getValue().add(skiWidthTail);
        				});
        				environment.createInstance(template, "repairInstruction.productData.skiWidthTip", Property.class).ifPresent(skiWidthTip -> {
        					skiWidthTip.setValue("108.0");
        					productData.getValue().add(skiWidthTip);
        				});
        				environment.createInstance(template, "repairInstruction.productData.skiWidthWaist", Property.class).ifPresent(skiWidthWaist -> {
        					skiWidthWaist.setValue("68.0");
        					productData.getValue().add(skiWidthWaist);
        				});
        				environment.createInstance(template, "repairInstruction.productData.skiBrand", Property.class).ifPresent(brand -> {
        					brand.setValue("ATOMIC");
        					productData.getValue().add(brand);
        				});
        				environment.createInstance(template, "repairInstruction.productData.skiProductName", Property.class).ifPresent(productName -> {
        					productName.setValue("REDSTER G9");
        					productData.getValue().add(productName);
        				});
        				environment.createInstance(template, "repairInstruction.productData.skiLength", Property.class).ifPresent(length -> {
        					length.setValue("172.0");
        					productData.getValue().add(length);
        				});
        				repairInstruction.getValue().add(productData);
        			});
        			sub.getSubmodelElements().add(repairInstruction);
        		}); 
        		
        	}
        }
        	
    }

}
