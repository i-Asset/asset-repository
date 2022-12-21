package at.srfg.iasset.repository.model.helper.visitor;

import org.eclipse.aas4j.v3.model.Entity;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.aas4j.v3.model.SubmodelElementList;

public interface SubmodelElementPathWalkerVisitor extends SubmodelElementPathVisitor {

	@Override
	default void visit(String pathToElement, Entity entity) {
		if ( entity == null) {
			return;
		}
		entity.getStatements().forEach(x -> visit(String.format("%s.%s", pathToElement, x.getIdShort()), x));
		SubmodelElementPathVisitor.super.visit(pathToElement, entity);
	}

	@Override
	default void visit(String pathToElement, Submodel submodel) {
		if ( submodel == null) {
			return ;
		}
		submodel.getSubmodelElements().forEach(x -> visit(x.getIdShort(), x));
		//
		SubmodelElementPathVisitor.super.visit(pathToElement, submodel);
	}

	@Override
	default void visit(String pathToElement, SubmodelElementCollection submodelElementCollection) {
        if (submodelElementCollection == null) {
            return;
        }
        submodelElementCollection.getValues().forEach(x -> visit(String.format("%s.%s", pathToElement, x.getIdShort()), x));
        //
		SubmodelElementPathVisitor.super.visit(pathToElement, submodelElementCollection);
	}

	@Override
	default void visit(String pathToElement, SubmodelElementList submodelElementList) {
        if (submodelElementList == null) {
            return;
        }
        for (int i = 0; i < submodelElementList.getValues().size(); i++) {
        	visit(String.format("%s[%s]", pathToElement,i ), submodelElementList.getValues().get(i));
        }
        //
        SubmodelElementPathVisitor.super.visit(pathToElement, submodelElementList);
	}

}
