package at.srfg.iasset.repository.model.helper.visitor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.aas4j.v3.dataformat.core.visitor.AssetAdministrationShellElementWalkerVisitor;
import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.eclipse.aas4j.v3.model.Referable;

public class EventElementCollector {
	public EventElementCollector() {
	}
	
    public <T extends Referable> Set<BasicEventElement> collect(T root) {
        Visitor visitor = new Visitor();
        visitor.visit(root);
        return visitor.element;
    }

    private class Visitor implements AssetAdministrationShellElementWalkerVisitor {

        Set<BasicEventElement> element = new HashSet<>();

		@Override
		public void visit(BasicEventElement basicEvent) {
			element.add(basicEvent);
			AssetAdministrationShellElementWalkerVisitor.super.visit(basicEvent);
		}
        
    }

}
