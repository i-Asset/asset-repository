package at.srfg.iasset.repository.model.helper.visitor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.digitaltwin.aas4j.v3.dataformat.core.internal.visitor.AssetAdministrationShellElementWalkerVisitor;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.eclipse.digitaltwin.aas4j.v3.model.ReferenceTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;

public class SemanticIdCollector {

	private final Submodel root;
	public SemanticIdCollector(Submodel root) {
		this.root = root;
	}
	public List<Reference> findSemanticIdentifier(Class<?> ...clazz ) {
		Visitor2 v2 = new Visitor2(clazz);
		v2.visit(root);
		//
		return v2.semanticReference.stream().collect(Collectors.toList());
	}

	

	
    private class Visitor2 implements AssetAdministrationShellElementWalkerVisitor {
    	Set<Reference> semanticReference = new HashSet<>();
        private final List<Class<?>> classes;
        
        public Visitor2(Class<?> ...classes) {
        	this.classes = Arrays.asList(classes);
        }
        private boolean isRelevantClass(SubmodelElement element) {
        	return this.classes.stream().anyMatch(new Predicate<Class<?>>() {
	
					@Override
					public boolean test(Class<?> t) {
						return t.isInstance(element);
					}
				});
        }
        @Override
        public void visit(SubmodelElement submodelElement) {
        	if ( isRelevantClass(submodelElement) ) {
        		if ( submodelElement.getSemanticId() != null ) {
        			Reference semanticId = submodelElement.getSemanticId();
        			if ( semanticId.getType() == ReferenceTypes.MODEL_REFERENCE) {
        				if ( semanticId.getReferredSemanticId() != null ) {
        					semanticReference.add(semanticId.getReferredSemanticId());
        				}
        				
        			}
        			else {
        				semanticReference.add(submodelElement.getSemanticId());
        			}
        		}
				semanticReference.addAll(submodelElement.getSupplementalSemanticIds());
        	}
			AssetAdministrationShellElementWalkerVisitor.super.visit(submodelElement);
        }
        
        
    }
    

}
