package at.srfg.iasset.repository.model.helper.visitor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.eclipse.aas4j.v3.dataformat.core.visitor.AssetAdministrationShellElementWalkerVisitor;
import org.eclipse.aas4j.v3.model.HasSemantics;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;
import org.eclipse.aas4j.v3.model.SubmodelElementCollection;
import org.springframework.util.Assert;

public class SemanticLookupVisitor {
	private final Referable root;
	public SemanticLookupVisitor(Referable root) {
		this.root = root;
		if (! Submodel.class.isInstance(root) && !(SubmodelElementCollection.class.isInstance(root))) {
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
        Visitor visitor = new Visitor(reference);
        visitor.visit(root);
        if ( ! visitor.element.isEmpty()) {
        	Optional<SubmodelElement> first = visitor.element.stream().findFirst();
        	if (first.isPresent()) {
        		return Optional.of(clazz.cast(first.getClass()));
        	}
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

}
