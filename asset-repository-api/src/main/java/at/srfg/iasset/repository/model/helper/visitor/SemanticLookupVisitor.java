package at.srfg.iasset.repository.model.helper.visitor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.eclipse.digitaltwin.aas4j.v3.dataformat.core.internal.visitor.AssetAdministrationShellElementWalkerVisitor;
import org.eclipse.digitaltwin.aas4j.v3.model.Entity;
import org.eclipse.digitaltwin.aas4j.v3.model.HasSemantics;
import org.eclipse.digitaltwin.aas4j.v3.model.Referable;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementList;
import org.springframework.util.Assert;


public class SemanticLookupVisitor {
	private final Referable root;
	public SemanticLookupVisitor(Referable root) {
		this.root = root;
		if (! Submodel.class.isInstance(root) 
				&& !(SubmodelElementCollection.class.isInstance(root))
				&& !(SubmodelElementList.class.isInstance(root)) 
				&& !(Entity.class.isInstance(root))) {
			throw new IllegalArgumentException("Provided referable is not a submodel element container");
		}
	}
	public Optional<SubmodelElement> findElement(Reference reference) {
		Assert.notNull(reference, "The Reference to search for must be provided!");
        Visitor visitor = new Visitor(reference);
        visitor.visit(root);
        if ( ! visitor.element.isEmpty()) {
        	return visitor.element.stream().findFirst();
        }
        return null;
	}
	public <T extends SubmodelElement> Optional<T> findElement(Reference reference, Class<T>  clazz) {
		Assert.notNull(reference, "The Reference to search for must be provided!");
		Visitor2<T> v2 = new Visitor2<>(reference, clazz);
		if (HasSemantics.class.isInstance(root)) {
			v2.visit(HasSemantics.class.cast(root));
		}
		else {
			v2.visit(root);
		}
		if (!v2.element.isEmpty() ) {
			return v2.element.stream().findFirst();
		}
        return Optional.empty();
	}

    private class Visitor implements AssetAdministrationShellElementWalkerVisitor {

        Set<SubmodelElement> element = new HashSet<>();
        private final Reference searchForRef;
        
        public Visitor(Reference refToSearch) {
        	this.searchForRef = refToSearch;
        }
        
        

		@Override
		public void visit(SubmodelElement hasSemantics) {
			if ( searchForRef.equals(hasSemantics.getSemanticId())) {
				element.add(hasSemantics);
			}
			AssetAdministrationShellElementWalkerVisitor.super.visit(hasSemantics);
		}
        
    }
	
    private class Visitor2<T extends HasSemantics> implements AssetAdministrationShellElementWalkerVisitor {

        Set<T> element = new HashSet<>();
        private final Reference searchForRef;
        private final Class<T> clazz;
        
        public Visitor2(Reference refToSearch, Class<T> clazz) {
        	this.searchForRef = refToSearch;
        	this.clazz = clazz;
        }
              

		@Override
		public void visit(SubmodelElement submodelElement) {
			if ( clazz.isInstance(submodelElement)) {
				if ( searchForRef.equals(submodelElement.getSemanticId())) {
					element.add(clazz.cast(submodelElement));
				}
				else {
					if ( submodelElement.getSupplementalSemanticIds()!= null ) {
						if ( submodelElement.getSupplementalSemanticIds().stream()
								.anyMatch(new Predicate<Reference>() {
									
									@Override
									public boolean test(Reference t) {
										return searchForRef.equals(t);
									}}) ) {
							// supplementalSemanticId matches, so add to the list
							element.add(clazz.cast(submodelElement));
						}
					}
				}
			}
			AssetAdministrationShellElementWalkerVisitor.super.visit(submodelElement);
		}
        
    }

}
