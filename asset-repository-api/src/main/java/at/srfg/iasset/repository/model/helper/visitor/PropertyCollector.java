package at.srfg.iasset.repository.model.helper.visitor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.aas4j.v3.dataformat.core.visitor.AssetAdministrationShellElementWalkerVisitor;
import org.eclipse.aas4j.v3.model.Property;
import org.eclipse.aas4j.v3.model.Referable;

public class PropertyCollector {
	public PropertyCollector() {
	}
	
    public <T extends Referable> Set<Property> collect(T root) {
        Visitor visitor = new Visitor();
        visitor.visit(root);
        return visitor.element;
    }

    private class Visitor implements AssetAdministrationShellElementWalkerVisitor {

        Set<Property> element = new HashSet<>();

		@Override
		public void visit(Property property) {
			element.add(property);
			AssetAdministrationShellElementWalkerVisitor.super.visit(property);
		}
        
    }

}
