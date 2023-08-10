package at.srfg.iasset.repository.model.helper.visitor;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.eclipse.aas4j.v3.dataformat.core.visitor.AssetAdministrationShellElementWalkerVisitor;
import org.eclipse.aas4j.v3.model.Operation;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;

public class OperationCollector {
	public OperationCollector() {
	}
	
    public <T extends Referable> Set<Operation> collect(T root) {
        Visitor visitor = new Visitor();
        visitor.visit(root);
        return visitor.element;
    }
    public <T extends Referable> Set<Operation> collect(T root, Reference semanticRef) {
        Visitor2 visitor = new Visitor2(semanticRef);
        visitor.visit(root);
        return visitor.element;
    }
    private class Visitor implements AssetAdministrationShellElementWalkerVisitor {

        Set<Operation> element = new HashSet<>();

		@Override
		public void visit(Operation operation) {
			element.add(operation);
			AssetAdministrationShellElementWalkerVisitor.super.visit(operation);
		}
        
    }
    private class Visitor2 implements AssetAdministrationShellElementWalkerVisitor {
    	private Reference semanticId;
    	public Visitor2(Reference semanticRef) {
    		this.semanticId = semanticRef;
    	}
        Set<Operation> element = new HashSet<>();

		@Override
		public void visit(Operation operation) {
			if ( semanticId.equals(operation.getSemanticId())) {
				element.add(operation);
			}
			else {
				if ( operation.getSupplementalSemanticIds()!= null ) {
					if ( operation.getSupplementalSemanticIds().stream()
						.anyMatch(new Predicate<Reference>() {

							@Override
							public boolean test(Reference t) {
								return semanticId.equals(t);
							}}) ) {
						// supplementalSemanticId matches, so add to the list
						element.add(operation);
					}
				}
			}
			AssetAdministrationShellElementWalkerVisitor.super.visit(operation);
		}
        
    }

}
