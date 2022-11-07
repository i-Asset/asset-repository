package at.srfg.iasset.repository.model.helper.visitor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.aas4j.v3.dataformat.core.visitor.AssetAdministrationShellElementWalkerVisitor;
import org.eclipse.aas4j.v3.model.DataElement;
import org.eclipse.aas4j.v3.model.Referable;

public class DataElementCollector {
	public DataElementCollector() {
	}
	
    public <T extends Referable> Set<DataElement> collect(T root) {
        Visitor visitor = new Visitor();
        visitor.visit(root);
        return visitor.element;
    }

    private class Visitor implements AssetAdministrationShellElementWalkerVisitor {

        Set<DataElement> element = new HashSet<>();

		@Override
		public void visit(DataElement property) {
			element.add(property);
			AssetAdministrationShellElementWalkerVisitor.super.visit(property);
		}
        
    }

}
