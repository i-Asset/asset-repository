package at.srfg.iasset.repository.model.helper.visitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.aas4j.v3.dataformat.core.visitor.AssetAdministrationShellElementWalkerVisitor;
import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.eclipse.aas4j.v3.model.Blob;
import org.eclipse.aas4j.v3.model.DataElement;
import org.eclipse.aas4j.v3.model.MultiLanguageProperty;
import org.eclipse.aas4j.v3.model.Operation;
import org.eclipse.aas4j.v3.model.Property;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.SubmodelElement;

public class SubmodelElementCollector {
	public SubmodelElementCollector() {
	}
	public <T extends Referable> Map<String, SubmodelElement> collectMap(String rootPath, T root) {
		MapVisitor visitor = new MapVisitor();
		visitor.visit(rootPath, root);
		return visitor.map;
	}
    public <T extends Referable> Set<SubmodelElement> collect(T root) {
        Visitor visitor = new Visitor();
        visitor.visit(root);
        return visitor.element;
    }
    private class MapVisitor implements SubmodelElementPathWalkerVisitor {
    	Map<String, SubmodelElement> map = new HashMap<String, SubmodelElement>();

		@Override
		public void visit(String pathToElement, BasicEventElement basicEvent) {
			map.put(pathToElement, basicEvent);
		}

		@Override
		public void visit(String pathToElement, Blob blob) {
			map.put(pathToElement, blob);
		}

		@Override
		public void visit(String pathToElement, MultiLanguageProperty multiLanguageProperty) {
			map.put(pathToElement, multiLanguageProperty);
		}

		@Override
		public void visit(String pathToElement, Operation operation) {
			map.put(pathToElement, operation);
		}

		@Override
		public void visit(String pathToElement, Property property) {
			map.put(pathToElement, property);
		}
    	
    }

    private class Visitor implements AssetAdministrationShellElementWalkerVisitor {

        Set<SubmodelElement> element = new HashSet<>();

		@Override
		public void visit(DataElement property) {
			element.add(property);
			AssetAdministrationShellElementWalkerVisitor.super.visit(property);
		}
        
    }

}
