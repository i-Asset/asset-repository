package org.eclipse.digitaltwin.aas4j.v3.model.builder;

import java.util.List;

import org.eclipse.digitaltwin.aas4j.v3.model.LangStringNameType;
import org.eclipse.digitaltwin.aas4j.v3.model.LangStringTextType;

public abstract class DataSpecificationRDFBuilder<T extends org.eclipse.digitaltwin.aas4j.v3.model.DataSpecificationRDF, B extends DataSpecificationRDFBuilder<T, B>>
    extends ExtendableBuilder<T, B> {

  /**
   * This function allows setting a value for iri
   *
   * @param iri desired value to be set
   * @return Builder object with new value for iri
   */
  public B iri(String iri) {
    getBuildingInstance().setIri(iri);
    return getSelf();
  }

  /**
   * This function allows setting a value for localName
   *
   * @param localName desired value to be set
   * @return Builder object with new value for localName
   */
  public B localName(String localName) {
    getBuildingInstance().setLocalName(localName);
    return getSelf();
  }

  /**
   * This function allows setting a value for namespace
   *
   * @param namespace desired value to be set
   * @return Builder object with new value for namespace
   */
  public B namespace(String namespace) {
    getBuildingInstance().setNamespace(namespace);
    return getSelf();
  }

  /**
   * This function allows setting a value for rdfType
   *
   * @param rdfType desired value to be set
   * @return Builder object with new value for rdfType
   */
  public B rdfType(String rdfType) {
    getBuildingInstance().setRdfType(rdfType);
    return getSelf();
  }

  /**
   * This function allows setting a value for dataType
   *
   * @param dataType desired value to be set
   * @return Builder object with new value for dataType
   */
  public B dataType(String dataType) {
    getBuildingInstance().setDataType(dataType);
    return getSelf();
  }

    /**
   * This function allows setting a value for inputArguments
   *
   * @param inputArguments desired value to be set
   * @return Builder object with new value for inputArguments
   */
  public B preferredName(List<LangStringNameType> preferredNames) {
    getBuildingInstance().setPreferredName(preferredNames);
    return getSelf();
  }
  public B preferredName(LangStringNameType preferredName) {
	  getBuildingInstance().getPreferredName().add(preferredName);
	  return getSelf();
  }

  /**
   * This function allows adding a value to the List inputArguments
   *
   * @param inputArguments desired value to be added
   * @return Builder object with new value for inputArguments
   */
  public B definition(List<LangStringTextType> definitions) {
    getBuildingInstance().setDefinition(definitions);
    return getSelf();
  }
  public B definition(LangStringTextType definition) {
	    getBuildingInstance().getDefinition().add(definition);
	    return getSelf();
	  }

}
