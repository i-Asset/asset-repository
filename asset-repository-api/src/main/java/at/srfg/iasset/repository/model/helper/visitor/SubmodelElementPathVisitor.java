package at.srfg.iasset.repository.model.helper.visitor;


import org.eclipse.aas4j.v3.model.*;

/**
 * Model Visitor collecting 
 * @author dglachs
 *
 */

public interface SubmodelElementPathVisitor {


    public default void visit(String pathToElement, DataElement dataElement) {
        if (dataElement == null) {
            return;
        }
        Class<?> type = dataElement.getClass();
        if (Property.class.isAssignableFrom(type)) {
            visit(pathToElement, (Property) dataElement);
        } else if (MultiLanguageProperty.class.isAssignableFrom(type)) {
            visit(pathToElement, (MultiLanguageProperty) dataElement);
        } else if (Range.class.isAssignableFrom(type)) {
            visit(pathToElement, (Range) dataElement);
        } else if (ReferenceElement.class.isAssignableFrom(type)) {
            visit(pathToElement,(ReferenceElement) dataElement);
        } else if (File.class.isAssignableFrom(type)) {
            visit(pathToElement, (File) dataElement);
        } else if (Blob.class.isAssignableFrom(type)) {
            visit(pathToElement, (Blob) dataElement);
        }
    }
//
//    public default void visit(DataSpecificationContent dataSpecificationContent) {
//        if (dataSpecificationContent == null) {
//            return;
//        }
//        Class<?> type = dataSpecificationContent.getClass();
//        // if (DataSpecificationIEC61360.class.isAssignableFrom(type)) {
//        //    visit((DataSpecificationIEC61360) dataSpecificationContent);
//        //}
//    }

    public default void visit(String pathToElement, EventElement event) {
        if (event == null) {
            return;
        }
        Class<?> type = event.getClass();
        if (BasicEventElement.class.isAssignableFrom(type)) {
            visit(pathToElement, (BasicEventElement) event);
        }
    }

//    public default void visit(String parentPath, HasDataSpecification hasDataSpecification) {
//        if (hasDataSpecification == null) {
//            return;
//        }
//        Class<?> type = hasDataSpecification.getClass();
//        if (AssetAdministrationShell.class.isAssignableFrom(type)) {
//            visit((AssetAdministrationShell) hasDataSpecification);
//        } else if (Submodel.class.isAssignableFrom(type)) {
//            visit((Submodel) hasDataSpecification);
//        } else if (SubmodelElement.class.isAssignableFrom(type)) {
//            visit((SubmodelElement) hasDataSpecification);
//        }
//
//    }
//
//    public default void visit(HasExtensions hasExtensions) {
//        if (hasExtensions == null) {
//            return;
//        }
//        Class<?> type = hasExtensions.getClass();
//        if (Referable.class.isAssignableFrom(type)) {
//            visit((Referable) hasExtensions);
//        }
//    }
//
//    public default void visit(HasKind hasKind) {
//        if (hasKind == null) {
//            return;
//        }
//        Class<?> type = hasKind.getClass();
//        if (Submodel.class.isAssignableFrom(type)) {
//            visit((Submodel) hasKind);
//        } else if (SubmodelElement.class.isAssignableFrom(type)) {
//            visit((SubmodelElement) hasKind);
//        }
//    }
//
//    public default void visit(HasSemantics hasSemantics) {
//        if (hasSemantics == null) {
//            return;
//        }
//        Class<?> type = hasSemantics.getClass();
//        if (Extension.class.isAssignableFrom(type)) {
//            visit((Extension) hasSemantics);
//        } else if (SpecificAssetId.class.isAssignableFrom(type)) {
//            visit((SpecificAssetId) hasSemantics);
//        } else if (Submodel.class.isAssignableFrom(type)) {
//            visit((Submodel) hasSemantics);
//        } else if (SubmodelElement.class.isAssignableFrom(type)) {
//            visit((SubmodelElement) hasSemantics);
//        } else if (Qualifier.class.isAssignableFrom(type)) {
//            visit((Qualifier) hasSemantics);
//        }
//    }
//
    public default void visit(String pathToElement, Identifiable identifiable) {
        if (identifiable == null) {
            return;
        }
        Class<?> type = identifiable.getClass();
//        if (AssetAdministrationShell.class.isAssignableFrom(type)) {
//            visit((AssetAdministrationShell) identifiable);
//        } else 
        if (Submodel.class.isAssignableFrom(type)) {
            visit(pathToElement, (Submodel) identifiable);
//        } else if (ConceptDescription.class.isAssignableFrom(type)) {
//            visit((ConceptDescription) identifiable);
        }
    }

    public default void visit(String pathToElement, SubmodelElement submodelElement) {
        if (submodelElement == null) {
            return;
        }
        Class<?> type = submodelElement.getClass();
        if (RelationshipElement.class.isAssignableFrom(type)) {
            visit(pathToElement, (RelationshipElement) submodelElement);
        } else if (DataElement.class.isAssignableFrom(type)) {
            visit(pathToElement, (DataElement) submodelElement);
        } else if (Capability.class.isAssignableFrom(type)) {
            visit(pathToElement, (Capability) submodelElement);
        } else if (SubmodelElementCollection.class.isAssignableFrom(type)) {
            visit(pathToElement, (SubmodelElementCollection) submodelElement);
        } else if (SubmodelElementList.class.isAssignableFrom(type)) {
            visit(pathToElement, (SubmodelElementList) submodelElement);
        } else if (Operation.class.isAssignableFrom(type)) {
            visit(pathToElement, (Operation) submodelElement);
        } else if (EventElement.class.isAssignableFrom(type)) {
            visit(pathToElement, (EventElement) submodelElement);
        } else if (Entity.class.isAssignableFrom(type)) {
            visit(pathToElement, (Entity) submodelElement);
        }
    }

//    public default void visit(Qualifiable qualifiable) {
//        if (qualifiable == null) {
//            return;
//        }
//        Class<?> type = qualifiable.getClass();
//        if (Submodel.class.isAssignableFrom(type)) {
//            visit((Submodel) qualifiable);
//        } else if (SubmodelElement.class.isAssignableFrom(type)) {
//            visit((SubmodelElement) qualifiable);
//        }
//    }
//
    public default void visit(String pathToElement, Referable referable) {
        if (referable == null) {
            return;
        }
        Class<?> type = referable.getClass();
        if (Identifiable.class.isAssignableFrom(type)) {
            visit(pathToElement, (Identifiable) referable);
        } else if (SubmodelElement.class.isAssignableFrom(type)) {
            visit(pathToElement, (SubmodelElement) referable);
        }
    }
//
//    public default void visit(Environment assetAdministrationShellEnvironment) {
//    }
//
//    public default void visit(AdministrativeInformation administrativeInformation) {
//    }

    public default void visit(String pathToElement, AnnotatedRelationshipElement annotatedRelationshipElement) {
    }

//    public default void visit(AssetAdministrationShell assetAdministrationShell) {
//    }
//
//    public default void visit(AssetInformation assetInformation) {
//    }

    public default void visit(String pathToElement, BasicEventElement basicEvent) {
    }

    public default void visit(String pathToElement, Blob blob) {
    }

    public default void visit(String pathToElement, Capability capability) {
    }

//    public default void visit(ConceptDescription conceptDescription) {
//    }
//
//    public default void visit(EmbeddedDataSpecification embeddedDataSpecification) {
//    }

    public default void visit(String pathToElement, Entity entity) {
    }
//    public default void visit(EventPayload eventMessage) {
//    }
//
//    public default void visit(Extension extension) {
//    }

    public default void visit(String pathToElement, File file) {
    }

//    public default void visit(SpecificAssetId identifierKeyValuePair) {
//    }
//
//    public default void visit(Key key) {
//    }
//
//    public default void visit(LangString langString) {
//    }

    public default void visit(String pathToElement, MultiLanguageProperty multiLanguageProperty) {
    }

    public default void visit(String pathToElement, Operation operation) {
    }

//    public default void visit(OperationVariable operationVariable) {
//    }

    public default void visit(String pathToElement, Property property) {
    }

//    public default void visit(Qualifier qualifier) {
//    }

    public default void visit(String pathToElement, Range range) {
    }

//    public default void visit(Reference reference) {
//    }

    public default void visit(String pathToElement, ReferenceElement referenceElement) {
    }

    public default void visit(String pathToElement, RelationshipElement relationshipElement) {
    }

    public default void visit(String pathToElement, Submodel submodel) {
    }

    public default void visit(String pathToElement, SubmodelElementCollection submodelElementCollection) {
    }

    public default void visit(String pathToElement, SubmodelElementList submodelElementList) {
    }

    
//    public default void visit(Resource resource) {
//        
//    }
}
