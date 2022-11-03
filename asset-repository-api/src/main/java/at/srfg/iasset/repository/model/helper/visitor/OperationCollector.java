package at.srfg.iasset.repository.model.helper.visitor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.aas4j.v3.dataformat.core.visitor.AssetAdministrationShellElementWalkerVisitor;
import org.eclipse.aas4j.v3.model.Operation;
import org.eclipse.aas4j.v3.model.Referable;

public class OperationCollector {
	public OperationCollector() {
	}
	
    public <T extends Referable> Set<Operation> collect(T root) {
        Visitor visitor = new Visitor();
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

}
